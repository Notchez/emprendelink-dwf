
package emprendelink.web.rest.security;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.UsuarioService;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ApiAuthenticationFilter
        implements ContainerRequestFilter {

    @Context
    private HttpServletRequest solicitud;

    @Inject
    private UsuarioService usuarioService;

    @Override
    public void filter(
            ContainerRequestContext contexto) {

        String metodo = contexto.getMethod();

        String ruta = contexto.getUriInfo()
                .getPath(false);

        if (esRutaPublica(metodo, ruta)) {
            return;
        }

        Usuario usuario =
                ApiSessionSecurity.requerirUsuario(
                        solicitud,
                        usuarioService
                );

        solicitud.setAttribute(
                ApiSessionSecurity.USUARIO_ACTUAL,
                usuario
        );

        if (requiereCsrf(metodo)) {
            ApiSessionSecurity.requerirTokenCsrf(
                    solicitud
            );
        }

        validarPermisos(
                metodo,
                ruta,
                usuario
        );
    }

    private boolean esRutaPublica(
            String metodo,
            String ruta) {

        if ("health".equals(ruta)) {
            return true;
        }

        if ("POST".equals(metodo)
                && "auth/login".equals(ruta)) {
            return true;
        }

        boolean lectura = esLectura(metodo);

        if (!lectura) {
            return false;
        }

        if ("emprendimientos".equals(ruta)
                || "publicaciones".equals(ruta)) {
            return true;
        }

        return ruta.matches(
                "emprendimientos/-?[0-9]+"
        ) || ruta.matches(
                "emprendimientos/-?[0-9]+/publicaciones"
        ) || ruta.matches(
                "publicaciones/-?[0-9]+"
        );
    }

    private boolean esLectura(String metodo) {

        return "GET".equals(metodo)
                || "HEAD".equals(metodo);
    }

    private boolean requiereCsrf(String metodo) {

        return !esLectura(metodo)
                && !"OPTIONS".equals(metodo);
    }

    private void validarPermisos(
            String metodo,
            String ruta,
            Usuario usuario) {

        // Consultar la sesion propia.
        if (esLectura(metodo)
                && "auth/me".equals(ruta)) {
            return;
        }

        // Cerrar la sesion propia.
        if ("POST".equals(metodo)
                && "auth/logout".equals(ruta)) {
            return;
        }

        // Listado de pedidos.
        if (esLectura(metodo)
                && "pedidos".equals(ruta)) {

            ApiSessionSecurity.requerirRol(
                    usuario,
                    TipoRol.ROLE_CLIENTE,
                    TipoRol.ROLE_EMPRENDEDOR
            );

            return;
        }

        // Consultar un pedido.
        if (esLectura(metodo)
                && ruta.matches(
                "pedidos/[0-9]+"
        )) {

            ApiSessionSecurity.requerirRol(
                    usuario,
                    TipoRol.ROLE_CLIENTE,
                    TipoRol.ROLE_EMPRENDEDOR,
                    TipoRol.ROLE_ADMIN
            );

            return;
        }

        // Crear pedidos.
        if ("POST".equals(metodo)
                && "pedidos/checkout".equals(ruta)) {

            ApiSessionSecurity.requerirRol(
                    usuario,
                    TipoRol.ROLE_CLIENTE
            );

            return;
        }

        // Modificar estado de pedidos.
        if ("PUT".equals(metodo)
                && ruta.matches(
                "pedidos/[0-9]+/estado"
        )) {

            ApiSessionSecurity.requerirRol(
                    usuario,
                    TipoRol.ROLE_EMPRENDEDOR
            );

            return;
        }

        // Regla de seguridad:
        // cualquier operacion protegida
        // no autorizada expresamente se rechaza.
        throw new ForbiddenException();
    }
}
