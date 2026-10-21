
package emprendelink.web.rest.security;

import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.UriInfo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ApiSecurityTest {

    @Test
    void permiteRutasPublicasSinSesion() {

        MockHttp http = new MockHttp();
        http.sinSesion = true;

        ApiAuthenticationFilter filtro = filtro(http, null);

        assertDoesNotThrow(() ->
                filtro.filter(contexto("GET", "health"))
        );

        assertDoesNotThrow(() ->
                filtro.filter(contexto("GET", "publicaciones"))
        );

        assertDoesNotThrow(() ->
                filtro.filter(contexto(
                        "GET",
                        "emprendimientos/1/publicaciones"
                ))
        );

        assertDoesNotThrow(() ->
                filtro.filter(contexto("POST", "auth/login"))
        );
    }

    @Test
    void rechazaAccesoPrivadoSinSesion() {

        MockHttp http = new MockHttp();
        http.sinSesion = true;

        ApiAuthenticationFilter filtro = filtro(http, null);

        assertEquals(401, estado(() ->
                filtro.filter(contexto("GET", "auth/me"))
        ));

        assertEquals(401, estado(() ->
                filtro.filter(contexto("GET", "pedidos"))
        ));
    }

    @Test
    void rechazaUsuarioDesactivadoEInvalidaSesion() {

        MockHttp http = new MockHttp();

        http.guardarUsuario(
                usuario(4, TipoRol.ROLE_CLIENTE, true)
        );

        Usuario actual = usuario(
                4, TipoRol.ROLE_CLIENTE, false
        );

        ApiAuthenticationFilter filtro =
                filtro(http, actual);

        assertEquals(403, estado(() ->
                filtro.filter(contexto("GET", "auth/me"))
        ));

        assertTrue(http.invalidada.get());
    }

    @Test
    void utilizaRolActualEnLugarDelRolDeSesion() {

        MockHttp http = new MockHttp();

        // La sesion conserva un rol anterior.
        http.guardarUsuario(
                usuario(4, TipoRol.ROLE_ADMIN, true)
        );

        // La base de datos ya indica CLIENTE.
        Usuario actual = usuario(
                4, TipoRol.ROLE_CLIENTE, true
        );

        ApiAuthenticationFilter filtro =
                filtro(http, actual);

        assertDoesNotThrow(() ->
                filtro.filter(contexto("GET", "pedidos"))
        );

        assertSame(
                actual,
                http.atributosSolicitud.get(
                        ApiSessionSecurity.USUARIO_ACTUAL
                )
        );

        String csrf =
                ApiSessionSecurity.generarTokenCsrf(
                        http.sesion
                );

        http.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                csrf
        );

        // El cliente no debe heredar permisos
        // de administrador o emprendedor.
        assertEquals(403, estado(() ->
                filtro.filter(contexto(
                        "PUT",
                        "pedidos/1/estado"
                ))
        ));
    }

    @Test
    void generaYVerificaTokenCsrf() {

        MockHttp http = new MockHttp();

        String token =
                ApiSessionSecurity.generarTokenCsrf(
                        http.sesion
                );

        assertNotNull(token);
        assertFalse(token.isBlank());

        assertEquals(
                token,
                ApiSessionSecurity.obtenerTokenCsrf(
                        http.sesion
                )
        );

        // Sin cabecera: prohibido.
        assertEquals(403, estado(() ->
                ApiSessionSecurity.requerirTokenCsrf(
                        http.solicitud
                )
        ));

        // Cabecera incorrecta: prohibido.
        http.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                "token-falso"
        );

        assertEquals(403, estado(() ->
                ApiSessionSecurity.requerirTokenCsrf(
                        http.solicitud
                )
        ));

        // Token correcto: permitido.
        http.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                token
        );

        assertDoesNotThrow(() ->
                ApiSessionSecurity.requerirTokenCsrf(
                        http.solicitud
                )
        );
    }

    @Test
    void protegeCheckoutPorRolYCsrf() {

        MockHttp clienteHttp = new MockHttp();

        Usuario cliente = usuario(
                4, TipoRol.ROLE_CLIENTE, true
        );

        clienteHttp.guardarUsuario(cliente);

        ApiAuthenticationFilter filtroCliente =
                filtro(clienteHttp, cliente);

        // Primero exige CSRF.
        assertEquals(403, estado(() ->
                filtroCliente.filter(contexto(
                        "POST",
                        "pedidos/checkout"
                ))
        ));

        String token =
                ApiSessionSecurity.generarTokenCsrf(
                        clienteHttp.sesion
                );

        clienteHttp.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                token
        );

        // Cliente con CSRF: permitido por el filtro.
        assertDoesNotThrow(() ->
                filtroCliente.filter(contexto(
                        "POST",
                        "pedidos/checkout"
                ))
        );

        MockHttp emprendedorHttp = new MockHttp();

        Usuario emprendedor = usuario(
                2, TipoRol.ROLE_EMPRENDEDOR, true
        );

        emprendedorHttp.guardarUsuario(emprendedor);

        String tokenEmprendedor =
                ApiSessionSecurity.generarTokenCsrf(
                        emprendedorHttp.sesion
                );

        emprendedorHttp.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                tokenEmprendedor
        );

        ApiAuthenticationFilter filtroEmprendedor =
                filtro(emprendedorHttp, emprendedor);

        // Emprendedor no puede hacer checkout.
        assertEquals(403, estado(() ->
                filtroEmprendedor.filter(contexto(
                        "POST",
                        "pedidos/checkout"
                ))
        ));
    }

    @Test
    void protegeCambioEstadoPorRol() {

        MockHttp clienteHttp = new MockHttp();

        Usuario cliente = usuario(
                4, TipoRol.ROLE_CLIENTE, true
        );

        clienteHttp.guardarUsuario(cliente);

        clienteHttp.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                ApiSessionSecurity.generarTokenCsrf(
                        clienteHttp.sesion
                )
        );

        assertEquals(403, estado(() ->
                filtro(clienteHttp, cliente).filter(
                        contexto("PUT", "pedidos/10/estado")
                )
        ));

        MockHttp emprendedorHttp = new MockHttp();

        Usuario emprendedor = usuario(
                2, TipoRol.ROLE_EMPRENDEDOR, true
        );

        emprendedorHttp.guardarUsuario(emprendedor);

        emprendedorHttp.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                ApiSessionSecurity.generarTokenCsrf(
                        emprendedorHttp.sesion
                )
        );

        assertDoesNotThrow(() ->
                filtro(emprendedorHttp, emprendedor)
                        .filter(contexto(
                                "PUT",
                                "pedidos/10/estado"
                        ))
        );
    }

    @Test
    void restringeListadoDePedidosPorRol() {

        MockHttp adminHttp = new MockHttp();

        Usuario admin = usuario(
                1, TipoRol.ROLE_ADMIN, true
        );

        adminHttp.guardarUsuario(admin);

        assertEquals(403, estado(() ->
                filtro(adminHttp, admin).filter(
                        contexto("GET", "pedidos")
                )
        ));

        MockHttp emprendedorHttp = new MockHttp();

        Usuario emprendedor = usuario(
                2, TipoRol.ROLE_EMPRENDEDOR, true
        );

        emprendedorHttp.guardarUsuario(emprendedor);

        assertDoesNotThrow(() ->
                filtro(emprendedorHttp, emprendedor)
                        .filter(contexto("GET", "pedidos"))
        );
    }

    @Test
    void rechazaRutasNoAutorizadasExpresamente() {

        MockHttp http = new MockHttp();

        Usuario cliente = usuario(
                4, TipoRol.ROLE_CLIENTE, true
        );

        http.guardarUsuario(cliente);

        ApiAuthenticationFilter filtro =
                filtro(http, cliente);

        assertEquals(403, estado(() ->
                filtro.filter(contexto(
                        "GET",
                        "pedidos/administracion"
                ))
        ));

        assertEquals(403, estado(() ->
                filtro.filter(contexto(
                        "GET",
                        "publicaciones/10/interno"
                ))
        ));
    }

    @Test
    void permiteConsultarSesionYCerrarConCsrf() {

        MockHttp http = new MockHttp();

        Usuario cliente = usuario(
                4, TipoRol.ROLE_CLIENTE, true
        );

        http.guardarUsuario(cliente);

        ApiAuthenticationFilter filtro =
                filtro(http, cliente);

        assertDoesNotThrow(() ->
                filtro.filter(contexto("GET", "auth/me"))
        );

        // Logout sin CSRF: prohibido.
        assertEquals(403, estado(() ->
                filtro.filter(contexto(
                        "POST",
                        "auth/logout"
                ))
        ));

        String token =
                ApiSessionSecurity.generarTokenCsrf(
                        http.sesion
                );

        http.headers.put(
                ApiSessionSecurity.CSRF_HEADER,
                token
        );

        // El filtro permite llegar al Resource.
        assertDoesNotThrow(() ->
                filtro.filter(contexto(
                        "POST",
                        "auth/logout"
                ))
        );
    }

    private static int estado(Executable accion) {

        WebApplicationException error = assertThrows(
                WebApplicationException.class,
                accion
        );

        return error.getResponse().getStatus();
    }

    private static Usuario usuario(
            int id,
            TipoRol rol,
            boolean activo) {

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(id);
        usuario.setRol(new Rol(rol));
        usuario.setActivo(activo);

        return usuario;
    }

    private static ApiAuthenticationFilter filtro(
            MockHttp http,
            Usuario usuarioBD) {

        ApiAuthenticationFilter filtro =
                new ApiAuthenticationFilter();

        UsuarioService servicio = proxy(
                UsuarioService.class,
                (objeto, metodo, argumentos) -> {

                    if ("buscarPorId".equals(
                            metodo.getName())) {
                        return usuarioBD;
                    }

                    throw new UnsupportedOperationException(
                            metodo.getName()
                    );
                }
        );

        try {
            Field campoSolicitud =
                    ApiAuthenticationFilter.class
                            .getDeclaredField("solicitud");

            campoSolicitud.setAccessible(true);
            campoSolicitud.set(filtro, http.solicitud);

            Field campoServicio =
                    ApiAuthenticationFilter.class
                            .getDeclaredField("usuarioService");

            campoServicio.setAccessible(true);
            campoServicio.set(filtro, servicio);

        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }

        return filtro;
    }

    private static ContainerRequestContext contexto(
            String metodo,
            String ruta) {

        UriInfo uriInfo = proxy(
                UriInfo.class,
                (objeto, funcion, argumentos) -> {

                    if ("getPath".equals(
                            funcion.getName())) {
                        return ruta;
                    }

                    throw new UnsupportedOperationException(
                            funcion.getName()
                    );
                }
        );

        return proxy(
                ContainerRequestContext.class,
                (objeto, funcion, argumentos) -> {

                    return switch (funcion.getName()) {

                        case "getMethod" -> metodo;

                        case "getUriInfo" -> uriInfo;

                        default ->
                                throw new UnsupportedOperationException(
                                        funcion.getName()
                                );
                    };
                }
        );
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(
            Class<T> interfaz,
            InvocationHandler manejador) {

        return (T) Proxy.newProxyInstance(
                interfaz.getClassLoader(),
                new Class<?>[]{interfaz},
                manejador
        );
    }

    private static final class MockHttp {

        private final Map<String, Object> atributosSesion =
                new HashMap<>();

        private final Map<String, Object> atributosSolicitud =
                new HashMap<>();

        private final Map<String, String> headers =
                new HashMap<>();

        private final AtomicBoolean invalidada =
                new AtomicBoolean(false);

        private boolean sinSesion = false;

        private final HttpSession sesion = proxy(
                HttpSession.class,
                (objeto, metodo, argumentos) -> {

                    return switch (metodo.getName()) {

                        case "getAttribute" ->
                                atributosSesion.get(
                                        argumentos[0]
                                );

                        case "setAttribute" -> {
                            atributosSesion.put(
                                    (String) argumentos[0],
                                    argumentos[1]
                            );
                            yield null;
                        }

                        case "invalidate" -> {
                            invalidada.set(true);
                            atributosSesion.clear();
                            yield null;
                        }

                        default ->
                                throw new UnsupportedOperationException(
                                        metodo.getName()
                                );
                    };
                }
        );

        private final HttpServletRequest solicitud = proxy(
                HttpServletRequest.class,
                (objeto, metodo, argumentos) -> {

                    return switch (metodo.getName()) {

                        case "getSession" ->
                                sinSesion ? null : sesion;

                        case "getHeader" ->
                                headers.get(
                                        (String) argumentos[0]
                                );

                        case "getAttribute" ->
                                atributosSolicitud.get(
                                        (String) argumentos[0]
                                );

                        case "setAttribute" -> {
                            atributosSolicitud.put(
                                    (String) argumentos[0],
                                    argumentos[1]
                            );
                            yield null;
                        }

                        default ->
                                throw new UnsupportedOperationException(
                                        metodo.getName()
                                );
                    };
                }
        );

        private void guardarUsuario(Usuario usuario) {

            sesion.setAttribute(
                    ApiSessionSecurity.USUARIO_SESION,
                    usuario
            );
        }
    }
}
