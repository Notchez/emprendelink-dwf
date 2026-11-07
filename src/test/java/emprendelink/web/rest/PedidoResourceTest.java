package emprendelink.web.rest;

import emprendelink.dto.CambiarEstadoPedidoDTO;
import emprendelink.dto.CrearPedidoDTO;
import emprendelink.dto.PedidoDTO;
import emprendelink.model.DetallePedido;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.PedidoService;
import emprendelink.service.UsuarioService;
import emprendelink.web.rest.security.ApiSessionSecurity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoResourceTest {

    private record Contexto(PedidoResource resource, PedidoServiceFalso servicio) {
    }

    @Test
    void listarSinSesionDevuelve401() throws Exception {
        Contexto ctx = contextoSinSesion();

        assertEquals(401, codigo(() -> ctx.resource().listar()));
    }

    @Test
    void listarComoClienteDevuelveResumenSinDetalles() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        ctx.servicio().listaCliente.add(pedido(100, EstadoPedido.PENDIENTE));

        Response respuesta = ctx.resource().listar();

        assertEquals(200, respuesta.getStatus());
        List<?> cuerpo = (List<?>) respuesta.getEntity();
        assertEquals(1, cuerpo.size());
        PedidoDTO dto = (PedidoDTO) cuerpo.get(0);
        assertEquals(100, dto.getIdPedido());
        assertEquals("PENDIENTE", dto.getEstado());
        assertEquals(10, dto.getIdEmprendimiento());
        assertNull(dto.getDetalles());
    }

    @Test
    void listarComoEmprendedorUsaPedidosRecibidos() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_EMPRENDEDOR, true);
        ctx.servicio().listaRecibidos.add(pedido(200, EstadoPedido.CONFIRMADO));

        Response respuesta = ctx.resource().listar();

        assertEquals(200, respuesta.getStatus());
        List<?> cuerpo = (List<?>) respuesta.getEntity();
        assertEquals(1, cuerpo.size());
        assertEquals(200, ((PedidoDTO) cuerpo.get(0)).getIdPedido());
    }

    @Test
    void listarComoAdministradorDevuelve403() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_ADMIN, true);

        assertEquals(403, codigo(() -> ctx.resource().listar()));
    }

    @Test
    void obtenerPedidoVisibleDevuelveDetalle() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        ctx.servicio().visible = pedido(100, EstadoPedido.PENDIENTE);

        Response respuesta = ctx.resource().obtener("100");

        assertEquals(200, respuesta.getStatus());
        PedidoDTO dto = (PedidoDTO) respuesta.getEntity();
        assertEquals("Direccion de prueba", dto.getDireccionEntrega());
        assertEquals(1, dto.getDetalles().size());
        assertEquals(2, dto.getDetalles().get(0).getCantidad());
        assertEquals(new BigDecimal("11.00"), dto.getDetalles().get(0).getSubtotal());
        assertEquals(7, ctx.servicio().idUsuarioConsultaRecibido);
    }

    @Test
    void obtenerPedidoNoVisibleDevuelve404() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        ctx.servicio().visible = null;

        assertEquals(404, codigo(() -> ctx.resource().obtener("999")));
    }

    @Test
    void obtenerConIdInvalidoDevuelve400() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);

        assertEquals(400, codigo(() -> ctx.resource().obtener("abc")));
        assertEquals(400, codigo(() -> ctx.resource().obtener("0")));
        assertEquals(400, codigo(() -> ctx.resource().obtener("-5")));
    }

    @Test
    void checkoutSinTokenCsrfDevuelve403YNoCreaPedidos() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, false);

        assertEquals(403, codigo(() -> ctx.resource().checkout(cuerpoCheckout())));
        assertEquals(0, ctx.servicio().llamadasCrear);
    }

    @Test
    void checkoutDeUnEmprendedorDevuelve403() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_EMPRENDEDOR, true);

        assertEquals(403, codigo(() -> ctx.resource().checkout(cuerpoCheckout())));
        assertEquals(0, ctx.servicio().llamadasCrear);
    }

    @Test
    void checkoutConCarritoVacioDevuelve400() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        CrearPedidoDTO cuerpo = cuerpoCheckout();
        cuerpo.setItems(new ArrayList<>());

        assertEquals(400, codigo(() -> ctx.resource().checkout(cuerpo)));
        assertEquals(0, ctx.servicio().llamadasCrear);
    }

    @Test
    void checkoutConArticuloIncompletoDevuelve400() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        CrearPedidoDTO.Item incompleto = new CrearPedidoDTO.Item();
        incompleto.setIdPublicacion(25);
        CrearPedidoDTO cuerpo = cuerpoCheckout();
        cuerpo.setItems(List.of(incompleto));

        assertEquals(400, codigo(() -> ctx.resource().checkout(cuerpo)));
        assertEquals(0, ctx.servicio().llamadasCrear);
    }

    @Test
    void checkoutValidoUsaLaIdentidadDeLaSesion() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);

        Response respuesta = ctx.resource().checkout(cuerpoCheckout());

        assertEquals(204, respuesta.getStatus());
        assertEquals(1, ctx.servicio().llamadasCrear);
        assertEquals(7, ctx.servicio().idClienteRecibido);
        assertEquals(List.of(25, 30), ctx.servicio().idsRecibidos);
        assertEquals(List.of(2, 1), ctx.servicio().cantidadesRecibidas);
        assertEquals("San Salvador", ctx.servicio().direccionRecibida);
        assertEquals("Entregar por la tarde", ctx.servicio().observacionesRecibidas);
    }

    @Test
    void checkoutConStockInsuficienteDevuelve409() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        ctx.servicio().errorCrear = new IllegalArgumentException("No hay existencias suficientes para Pan.");

        assertEquals(409, codigo(() -> ctx.resource().checkout(cuerpoCheckout())));
    }

    @Test
    void checkoutConPublicacionNoDisponibleDevuelve404() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        ctx.servicio().errorCrear = new IllegalArgumentException("Una publicación no está disponible.");

        assertEquals(404, codigo(() -> ctx.resource().checkout(cuerpoCheckout())));
    }

    @Test
    void checkoutSinDireccionDevuelve400() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        ctx.servicio().errorCrear = new IllegalArgumentException("La dirección de entrega es obligatoria.");

        assertEquals(400, codigo(() -> ctx.resource().checkout(cuerpoCheckout())));
    }

    @Test
    void errorInesperadoDelServiceNoSeConvierteEnErrorDeCliente() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);
        ctx.servicio().errorCrear = new IllegalStateException("El negocio no tiene un plan válido.");

        assertThrows(IllegalStateException.class, () -> ctx.resource().checkout(cuerpoCheckout()));
    }

    @Test
    void actualizarEstadoValidoDevuelve204() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_EMPRENDEDOR, true);

        Response respuesta = ctx.resource().actualizarEstado("5", cuerpoEstado("CONFIRMADO"));

        assertEquals(204, respuesta.getStatus());
        assertEquals(1, ctx.servicio().llamadasEstado);
        assertEquals(7, ctx.servicio().idEmprendedorRecibido);
        assertEquals(5, ctx.servicio().idPedidoRecibido);
        assertEquals(EstadoPedido.CONFIRMADO, ctx.servicio().estadoRecibido);
    }

    @Test
    void actualizarEstadoComoClienteDevuelve403() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_CLIENTE, true);

        assertEquals(403, codigo(() -> ctx.resource().actualizarEstado("5", cuerpoEstado("CONFIRMADO"))));
        assertEquals(0, ctx.servicio().llamadasEstado);
    }

    @Test
    void actualizarEstadoSinTokenCsrfDevuelve403() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_EMPRENDEDOR, false);

        assertEquals(403, codigo(() -> ctx.resource().actualizarEstado("5", cuerpoEstado("CONFIRMADO"))));
        assertEquals(0, ctx.servicio().llamadasEstado);
    }

    @Test
    void actualizarEstadoConValorInexistenteDevuelve400() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_EMPRENDEDOR, true);

        assertEquals(400, codigo(() -> ctx.resource().actualizarEstado("5", cuerpoEstado("VOLANDO"))));
        assertEquals(400, codigo(() -> ctx.resource().actualizarEstado("5", cuerpoEstado(" "))));
        assertEquals(400, codigo(() -> ctx.resource().actualizarEstado("5", null)));
        assertEquals(0, ctx.servicio().llamadasEstado);
    }

    @Test
    void actualizarEstadoConTransicionNoPermitidaDevuelve409() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_EMPRENDEDOR, true);
        ctx.servicio().errorEstado = new IllegalArgumentException("La transición de estado no está permitida.");

        assertEquals(409, codigo(() -> ctx.resource().actualizarEstado("5", cuerpoEstado("COMPLETADO"))));
    }

    @Test
    void actualizarEstadoDePedidoAjenoDevuelve404() throws Exception {
        Contexto ctx = contexto(TipoRol.ROLE_EMPRENDEDOR, true);
        ctx.servicio().errorEstado = new IllegalArgumentException("El pedido no pertenece a tu negocio.");

        assertEquals(404, codigo(() -> ctx.resource().actualizarEstado("8", cuerpoEstado("CONFIRMADO"))));
    }

    private static int codigo(Executable accion) {
        WebApplicationException error = assertThrows(WebApplicationException.class, accion);
        return error.getResponse().getStatus();
    }

    private static Contexto contexto(TipoRol rol, boolean tokenEnCabecera) throws Exception {
        Usuario usuario = usuario(7, rol);
        PedidoServiceFalso servicio = new PedidoServiceFalso();
        HttpServletRequest solicitud = solicitud(usuario, "token-123", tokenEnCabecera ? "token-123" : null);
        return new Contexto(crearResource(solicitud, usuarios(usuario), servicio), servicio);
    }

    private static Contexto contextoSinSesion() throws Exception {
        PedidoServiceFalso servicio = new PedidoServiceFalso();
        HttpServletRequest solicitud = solicitud(null, null, null);
        return new Contexto(crearResource(solicitud, usuarios(null), servicio), servicio);
    }

    private static PedidoResource crearResource(
            HttpServletRequest solicitud,
            UsuarioService usuarios,
            PedidoService pedidos) throws Exception {

        PedidoResource resource = new PedidoResource();
        asignar(resource, "pedidoService", pedidos);
        asignar(resource, "usuarioService", usuarios);
        asignar(resource, "request", solicitud);
        return resource;
    }

    private static void asignar(Object destino, String campo, Object valor) throws Exception {
        Field atributo = destino.getClass().getDeclaredField(campo);
        atributo.setAccessible(true);
        atributo.set(destino, valor);
    }

    private static Usuario usuario(int id, TipoRol rol) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setRol(new Rol(1, rol));
        usuario.setActivo(true);
        return usuario;
    }

    private static Pedido pedido(int id, EstadoPedido estado) {
        Emprendimiento negocio = new Emprendimiento();
        negocio.setIdEmprendimiento(10);
        negocio.setNombre("Tienda Ejemplo");

        Pedido pedido = new Pedido();
        pedido.setIdPedido(id);
        pedido.setEmprendimiento(negocio);
        pedido.setEstado(estado);
        pedido.setDireccionEntrega("Direccion de prueba");
        pedido.agregarDetalle(new DetallePedido(null, 2, new BigDecimal("5.50")));
        return pedido;
    }

    private static CrearPedidoDTO cuerpoCheckout() {
        CrearPedidoDTO.Item primero = new CrearPedidoDTO.Item();
        primero.setIdPublicacion(25);
        primero.setCantidad(2);

        CrearPedidoDTO.Item segundo = new CrearPedidoDTO.Item();
        segundo.setIdPublicacion(30);
        segundo.setCantidad(1);

        CrearPedidoDTO cuerpo = new CrearPedidoDTO();
        cuerpo.setItems(List.of(primero, segundo));
        cuerpo.setDireccionEntrega("San Salvador");
        cuerpo.setObservaciones("Entregar por la tarde");
        return cuerpo;
    }

    private static CambiarEstadoPedidoDTO cuerpoEstado(String estado) {
        CambiarEstadoPedidoDTO cuerpo = new CambiarEstadoPedidoDTO();
        cuerpo.setEstado(estado);
        return cuerpo;
    }

    private static HttpServletRequest solicitud(Usuario enSesion, String tokenSesion, String tokenCabecera) {
        HttpSession sesion = (HttpSession) Proxy.newProxyInstance(
                PedidoResourceTest.class.getClassLoader(),
                new Class<?>[]{HttpSession.class},
                (proxy, metodo, args) -> {
                    if (metodo.getName().equals("getAttribute")) {
                        if (ApiSessionSecurity.USUARIO_SESION.equals(args[0])) {
                            return enSesion;
                        }
                        if (ApiSessionSecurity.CSRF_SESION.equals(args[0])) {
                            return tokenSesion;
                        }
                        return null;
                    }
                    return valorPorDefecto(metodo.getReturnType());
                }
        );

        return (HttpServletRequest) Proxy.newProxyInstance(
                PedidoResourceTest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class},
                (proxy, metodo, args) -> {
                    if (metodo.getName().equals("getSession")) {
                        return enSesion == null ? null : sesion;
                    }
                    if (metodo.getName().equals("getHeader")) {
                        return ApiSessionSecurity.CSRF_HEADER.equals(args[0]) ? tokenCabecera : null;
                    }
                    return valorPorDefecto(metodo.getReturnType());
                }
        );
    }

    private static UsuarioService usuarios(Usuario actual) {
        return (UsuarioService) Proxy.newProxyInstance(
                PedidoResourceTest.class.getClassLoader(),
                new Class<?>[]{UsuarioService.class},
                (proxy, metodo, args) -> metodo.getName().equals("buscarPorId")
                        ? actual
                        : valorPorDefecto(metodo.getReturnType())
        );
    }

    private static Object valorPorDefecto(Class<?> tipo) {
        if (tipo == boolean.class) {
            return false;
        }
        if (tipo == int.class) {
            return 0;
        }
        if (tipo == long.class) {
            return 0L;
        }
        if (tipo == double.class) {
            return 0.0;
        }
        if (tipo == float.class) {
            return 0f;
        }
        if (tipo == short.class) {
            return (short) 0;
        }
        if (tipo == byte.class) {
            return (byte) 0;
        }
        if (tipo == char.class) {
            return '\0';
        }
        return null;
    }

    private static class PedidoServiceFalso implements PedidoService {

        List<Pedido> listaCliente = new ArrayList<>();
        List<Pedido> listaRecibidos = new ArrayList<>();
        Pedido visible;
        RuntimeException errorCrear;
        RuntimeException errorEstado;

        int llamadasCrear;
        int llamadasEstado;
        Integer idClienteRecibido;
        List<Integer> idsRecibidos;
        List<Integer> cantidadesRecibidas;
        String direccionRecibida;
        String observacionesRecibidas;
        Integer idEmprendedorRecibido;
        Integer idPedidoRecibido;
        EstadoPedido estadoRecibido;
        Integer idUsuarioConsultaRecibido;

        @Override
        public List<Pedido> listarPorCliente(Integer idCliente) {
            return listaCliente;
        }

        @Override
        public List<Pedido> listarRecibidos(Integer idEmprendedor) {
            return listaRecibidos;
        }

        @Override
        public List<Pedido> listarActivosRecibidos(Integer idEmprendedor) {
            return listaRecibidos;
        }

        @Override
        public List<Pedido> listarCompletados(Integer idEmprendedor) {
            return listaRecibidos;
        }

        @Override
        public Pedido buscarVisibleParaUsuario(Integer idPedido, Integer idUsuario) {
            idUsuarioConsultaRecibido = idUsuario;
            return visible;
        }

        @Override
        public void crear(
                Integer idCliente,
                Integer idEmprendimiento,
                List<Integer> idsPublicacion,
                List<Integer> cantidades,
                String observaciones) {
        }

        @Override
        public void crearDesdeCarrito(
                Integer idCliente,
                List<Integer> idsPublicacion,
                List<Integer> cantidades,
                String direccionEntrega,
                String observaciones) {

            llamadasCrear++;
            idClienteRecibido = idCliente;
            idsRecibidos = idsPublicacion;
            cantidadesRecibidas = cantidades;
            direccionRecibida = direccionEntrega;
            observacionesRecibidas = observaciones;

            if (errorCrear != null) {
                throw errorCrear;
            }
        }

        @Override
        public void actualizarEstado(Integer idEmprendedor, Integer idPedido, EstadoPedido estado) {
            llamadasEstado++;
            idEmprendedorRecibido = idEmprendedor;
            idPedidoRecibido = idPedido;
            estadoRecibido = estado;

            if (errorEstado != null) {
                throw errorEstado;
            }
        }
    }
}