package emprendelink.web.rest;

import emprendelink.dto.CambiarEstadoPedidoDTO;
import emprendelink.dto.CrearPedidoDTO;
import emprendelink.dto.DetallePedidoDTO;
import emprendelink.dto.PedidoDTO;
import emprendelink.model.DetallePedido;
import emprendelink.model.Pedido;
import emprendelink.model.Usuario;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.PedidoService;
import emprendelink.service.UsuarioService;
import emprendelink.web.rest.error.ApiBusinessException;
import emprendelink.web.rest.security.ApiSessionSecurity;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    private PedidoService pedidoService;

    @Inject
    private UsuarioService usuarioService;

    @Context
    private HttpServletRequest request;

    @GET
    public Response listar() {
        Usuario usuario = ApiSessionSecurity.requerirUsuario(request, usuarioService);
        TipoRol rol = usuario.getRol().getNombre();
        Integer idUsuario = usuario.getIdUsuario();

        List<Pedido> pedidos;

        if (rol == TipoRol.ROLE_CLIENTE) {
            pedidos = ejecutar(() -> pedidoService.listarPorCliente(idUsuario));
        } else if (rol == TipoRol.ROLE_EMPRENDEDOR) {
            pedidos = ejecutar(() -> pedidoService.listarRecibidos(idUsuario));
        } else {
            throw new ForbiddenException();
        }

        List<PedidoDTO> respuesta = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            respuesta.add(resumen(pedido));
        }

        return Response.ok(respuesta).build();
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") String idTexto) {
        Usuario usuario = ApiSessionSecurity.requerirUsuario(request, usuarioService);
        Integer id = parsearId(idTexto);
        Integer idUsuario = usuario.getIdUsuario();

        Pedido pedido = ejecutar(() -> pedidoService.buscarVisibleParaUsuario(id, idUsuario));

        if (pedido == null) {
            throw ApiBusinessException.notFound("Pedido no encontrado.");
        }

        return Response.ok(detalle(pedido)).build();
    }

    @POST
    @Path("/checkout")
    public Response checkout(CrearPedidoDTO cuerpo) {
        Usuario usuario = ApiSessionSecurity.requerirUsuario(request, usuarioService);
        ApiSessionSecurity.requerirTokenCsrf(request);
        ApiSessionSecurity.requerirRol(usuario, TipoRol.ROLE_CLIENTE);

        validarCuerpoCheckout(cuerpo);

        List<Integer> idsPublicacion = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();

        for (CrearPedidoDTO.Item item : cuerpo.getItems()) {
            idsPublicacion.add(item.getIdPublicacion());
            cantidades.add(item.getCantidad());
        }

        Integer idCliente = usuario.getIdUsuario();

        ejecutarSinResultado(() -> pedidoService.crearDesdeCarrito(
                idCliente,
                idsPublicacion,
                cantidades,
                cuerpo.getDireccionEntrega(),
                cuerpo.getObservaciones()
        ));

        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/estado")
    public Response actualizarEstado(
            @PathParam("id") String idTexto,
            CambiarEstadoPedidoDTO cuerpo) {

        Usuario usuario = ApiSessionSecurity.requerirUsuario(request, usuarioService);
        ApiSessionSecurity.requerirTokenCsrf(request);
        ApiSessionSecurity.requerirRol(usuario, TipoRol.ROLE_EMPRENDEDOR);

        Integer id = parsearId(idTexto);
        EstadoPedido nuevoEstado = parsearEstado(cuerpo);
        Integer idEmprendedor = usuario.getIdUsuario();

        ejecutarSinResultado(() -> pedidoService.actualizarEstado(idEmprendedor, id, nuevoEstado));

        return Response.noContent().build();
    }

    private void validarCuerpoCheckout(CrearPedidoDTO cuerpo) {
        if (cuerpo == null
                || cuerpo.getItems() == null
                || cuerpo.getItems().isEmpty()) {

            throw ApiBusinessException.badRequest("El carrito está vacío.");
        }

        for (CrearPedidoDTO.Item item : cuerpo.getItems()) {
            if (item == null
                    || item.getIdPublicacion() == null
                    || item.getCantidad() == null) {

                throw ApiBusinessException.badRequest(
                        "Cada artículo debe indicar idPublicacion y cantidad."
                );
            }
        }
    }

    private Integer parsearId(String valor) {
        int id;

        try {
            id = Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw ApiBusinessException.badRequest("El identificador no es válido.");
        }

        if (id <= 0) {
            throw ApiBusinessException.badRequest("El identificador no es válido.");
        }

        return id;
    }

    private EstadoPedido parsearEstado(CambiarEstadoPedidoDTO cuerpo) {
        if (cuerpo == null
                || cuerpo.getEstado() == null
                || cuerpo.getEstado().isBlank()) {

            throw ApiBusinessException.badRequest("El estado es obligatorio.");
        }

        try {
            return EstadoPedido.valueOf(cuerpo.getEstado().trim());
        } catch (IllegalArgumentException e) {
            throw ApiBusinessException.badRequest("El estado indicado no es válido.");
        }
    }

    private <T> T ejecutar(Supplier<T> accion) {
        try {
            return accion.get();
        } catch (WebApplicationException e) {
            throw e;
        } catch (RuntimeException e) {
            IllegalArgumentException invalido = buscarArgumentoInvalido(e);

            if (invalido == null) {
                throw e;
            }

            throw clasificar(invalido);
        }
    }

    private void ejecutarSinResultado(Runnable accion) {
        ejecutar(() -> {
            accion.run();
            return null;
        });
    }

    private IllegalArgumentException buscarArgumentoInvalido(Throwable error) {
        Throwable actual = error;

        while (actual != null) {
            if (actual instanceof IllegalArgumentException invalido) {
                return invalido;
            }

            actual = actual.getCause();
        }

        return null;
    }

    private WebApplicationException clasificar(IllegalArgumentException error) {
        String mensaje = error.getMessage();

        if (mensaje == null || mensaje.isBlank()) {
            return ApiBusinessException.badRequest("La solicitud contiene datos inválidos.");
        }

        String texto = mensaje.toLowerCase(Locale.ROOT);

        if (texto.contains("permisos")) {
            return new ForbiddenException();
        }

        if (texto.contains("existencias") || texto.contains("transición")) {
            return ApiBusinessException.conflict(mensaje);
        }

        if (texto.contains("no existe")
                || texto.contains("no está disponible")
                || texto.contains("no pertenece")) {

            return ApiBusinessException.notFound(mensaje);
        }

        return ApiBusinessException.badRequest(mensaje);
    }

    private PedidoDTO resumen(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();

        dto.setIdPedido(pedido.getIdPedido());

        if (pedido.getEmprendimiento() != null) {
            dto.setIdEmprendimiento(pedido.getEmprendimiento().getIdEmprendimiento());
            dto.setNombreEmprendimiento(pedido.getEmprendimiento().getNombre());
        }

        dto.setEstado(pedido.getEstado() == null ? null : pedido.getEstado().name());
        dto.setTotal(pedido.getTotal());
        dto.setFechaPedido(formatear(pedido.getFechaPedido()));

        return dto;
    }

    private PedidoDTO detalle(Pedido pedido) {
        PedidoDTO dto = resumen(pedido);

        dto.setDireccionEntrega(pedido.getDireccionEntrega());
        dto.setObservaciones(pedido.getObservaciones());
        dto.setFechaCompletado(formatear(pedido.getFechaCompletado()));

        List<DetallePedidoDTO> lineas = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetalles()) {
            DetallePedidoDTO linea = new DetallePedidoDTO();

            if (detalle.getPublicacion() != null) {
                linea.setIdPublicacion(detalle.getPublicacion().getIdPublicacion());
                linea.setNombrePublicacion(detalle.getPublicacion().getNombre());
            }

            linea.setCantidad(detalle.getCantidad());
            linea.setPrecioUnitario(detalle.getPrecioUnitario());
            linea.setSubtotal(detalle.getSubtotal());

            lineas.add(linea);
        }

        dto.setDetalles(lineas);

        return dto;
    }

    private String formatear(LocalDateTime fecha) {
        return fecha == null
                ? null
                : fecha.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}