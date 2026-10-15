package emprendelink.web.jsf;

import emprendelink.model.Pedido;
import emprendelink.model.Publicacion;
import emprendelink.model.Usuario;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.service.PedidoService;
import emprendelink.service.PublicacionService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

@Named("pedidoBean")
@ViewScoped
public class PedidoBean
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private PedidoService pedidoService;

    @Inject
    private PublicacionService publicacionService;

    private Integer idPublicacion;
    private Publicacion publicacion;

    private Integer cantidad = 1;
    private String observaciones;

    private Integer idPedido;
    private Pedido detalle;

    private Integer mesSeleccionado =
            YearMonth.now()
                    .getMonthValue();

    private Integer anioSeleccionado =
            YearMonth.now()
                    .getYear();

    public void cargarNuevaOrden() {

        if (idPublicacion == null) {

            enviarError(
                    404,
                    "Publicación no encontrada"
            );

            return;
        }

        publicacion =
                publicacionService
                        .buscarActivaPorId(
                                idPublicacion
                        );

        if (publicacion == null) {

            enviarError(
                    404,
                    "Publicación no encontrada"
            );
        }
    }

    public void crear() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {

            mensajeError(
                    "Sesión inválida",
                    "Debes iniciar sesión nuevamente."
            );

            return;
        }

        if (publicacion == null
                || publicacion
                .getEmprendimiento()
                == null) {

            mensajeError(
                    "Publicación inválida",
                    "No se pudo identificar la publicación."
            );

            return;
        }

        try {

            pedidoService.crear(
                    usuario.getIdUsuario(),
                    publicacion
                            .getEmprendimiento()
                            .getIdEmprendimiento(),
                    List.of(
                            publicacion
                                    .getIdPublicacion()
                    ),
                    List.of(
                            cantidad
                    ),
                    observaciones
            );

            FacesContext contexto =
                    FacesContext
                            .getCurrentInstance();

            contexto
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(
                            true
                    );

            contexto.addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Pedido creado",
                            "Tu pedido fue registrado correctamente."
                    )
            );

            redirigir(
                    "/pedidos/mis-pedidos.xhtml"
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    "No se pudo crear el pedido",
                    e.getMessage()
            );
        }
    }

    public List<Pedido> getMisPedidos() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        try {

            return pedidoService
                    .listarPorCliente(
                            usuario.getIdUsuario()
                    );

        } catch (IllegalArgumentException e) {

            return List.of();
        }
    }

    public List<Pedido> getPedidosRecibidos() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        try {

            return pedidoService
                    .listarRecibidos(
                            usuario.getIdUsuario()
                    );

        } catch (IllegalArgumentException e) {

            return List.of();
        }
    }

    public List<Pedido>
    getPedidosActivosRecibidos() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        try {

            return pedidoService
                    .listarActivosRecibidos(
                            usuario.getIdUsuario()
                    );

        } catch (IllegalArgumentException e) {

            return List.of();
        }
    }

    public List<Pedido>
    getVentasCompletadas() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        try {

            return pedidoService
                    .listarCompletados(
                            usuario.getIdUsuario()
                    );

        } catch (IllegalArgumentException e) {

            return List.of();
        }
    }

    public List<Pedido>
    getVentasPeriodoSeleccionado() {

        YearMonth periodo =
                obtenerPeriodoSeleccionado();

        return getVentasCompletadas()
                .stream()
                .filter(
                        pedido ->
                                perteneceAlPeriodo(
                                        pedido,
                                        periodo
                                )
                )
                .toList();
    }

    public BigDecimal
    getTotalVentasPeriodoSeleccionado() {

        return sumarVentas(
                getVentasPeriodoSeleccionado()
        );
    }

    public int
    getCantidadVentasPeriodoSeleccionado() {

        return getVentasPeriodoSeleccionado()
                .size();
    }

    public String getPeriodoSeleccionado() {

        return formatearPeriodo(
                obtenerPeriodoSeleccionado()
        );
    }

    public List<Integer>
    getAniosDisponibles() {

        Set<Integer> anios =
                new TreeSet<>(
                        Comparator.reverseOrder()
                );

        anios.add(
                YearMonth.now()
                        .getYear()
        );

        for (Pedido pedido
                : getVentasCompletadas()) {

            if (pedido.getFechaCompletado()
                    != null) {

                anios.add(
                        pedido
                                .getFechaCompletado()
                                .getYear()
                );
            }
        }

        return new ArrayList<>(
                anios
        );
    }

    /*
     * Métodos de mes actual conservados para
     * la pantalla de Comisiones.
     */

    public List<Pedido>
    getVentasMesActual() {

        YearMonth periodo =
                YearMonth.now();

        return getVentasCompletadas()
                .stream()
                .filter(
                        pedido ->
                                perteneceAlPeriodo(
                                        pedido,
                                        periodo
                                )
                )
                .toList();
    }

    public BigDecimal
    getTotalVentasHistoricas() {

        return sumarVentas(
                getVentasCompletadas()
        );
    }

    public BigDecimal
    getTotalVentasMesActual() {

        return sumarVentas(
                getVentasMesActual()
        );
    }

    public BigDecimal
    getComisionMesActual() {

        return getVentasMesActual()
                .stream()
                .map(
                        Pedido::getMontoComision
                )
                .filter(
                        valor ->
                                valor != null
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public BigDecimal
    getNetoMesActual() {

        return getTotalVentasMesActual()
                .subtract(
                        getComisionMesActual()
                );
    }

    public int
    getCantidadVentasMesActual() {

        return getVentasMesActual()
                .size();
    }

    public String getPeriodoActual() {

        return formatearPeriodo(
                YearMonth.now()
        );
    }

    public BigDecimal porcentajeComision(
            Pedido pedido) {

        if (pedido == null
                || pedido.getTasaComision()
                == null) {

            return BigDecimal.ZERO;
        }

        return pedido
                .getTasaComision()
                .multiply(
                        BigDecimal.valueOf(
                                100
                        )
                )
                .stripTrailingZeros();
    }

    public String formatearFecha(
            LocalDateTime fecha) {

        if (fecha == null) {
            return "—";
        }

        return fecha.format(
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                )
        );
    }

    public void cargarDetalle() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {

            enviarError(
                    403,
                    "Acceso denegado"
            );

            return;
        }

        if (idPedido == null) {

            enviarError(
                    404,
                    "Pedido no encontrado"
            );

            return;
        }

        detalle =
                pedidoService
                        .buscarVisibleParaUsuario(
                                idPedido,
                                usuario.getIdUsuario()
                        );

        if (detalle == null) {

            enviarError(
                    404,
                    "Pedido no encontrado"
            );
        }
    }

    public void confirmar(
            Integer idPedido) {

        cambiarEstado(
                idPedido,
                EstadoPedido.CONFIRMADO
        );
    }

    public void iniciarProceso(
            Integer idPedido) {

        cambiarEstado(
                idPedido,
                EstadoPedido.EN_PROCESO
        );
    }

    public void completar(
            Integer idPedido) {

        cambiarEstado(
                idPedido,
                EstadoPedido.COMPLETADO
        );
    }

    public void cancelar(
            Integer idPedido) {

        cambiarEstado(
                idPedido,
                EstadoPedido.CANCELADO
        );
    }

    private void cambiarEstado(
            Integer idPedido,
            EstadoPedido nuevoEstado) {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {
            return;
        }

        try {

            pedidoService.actualizarEstado(
                    usuario.getIdUsuario(),
                    idPedido,
                    nuevoEstado
            );

            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "Estado actualizado",
                                    "El pedido fue actualizado correctamente."
                            )
                    );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    "No se pudo actualizar",
                    e.getMessage()
            );
        }
    }

    public String etiquetaEstado(
            EstadoPedido estado) {

        if (estado == null) {
            return "";
        }

        return switch (estado) {

            case PENDIENTE ->
                    "Pendiente";

            case CONFIRMADO ->
                    "Confirmado";

            case EN_PROCESO ->
                    "En camino";

            case COMPLETADO ->
                    "Entregado";

            case CANCELADO ->
                    "Cancelado";
        };
    }

    public boolean puedeConfirmar(
            Pedido pedido) {

        return pedido != null
                && pedido.getEstado()
                == EstadoPedido.PENDIENTE;
    }

    public boolean puedeIniciarProceso(
            Pedido pedido) {

        return pedido != null
                && pedido.getEstado()
                == EstadoPedido.CONFIRMADO;
    }

    public boolean puedeCompletar(
            Pedido pedido) {

        return pedido != null
                && pedido.getEstado()
                == EstadoPedido.EN_PROCESO;
    }

    public boolean puedeCancelar(
            Pedido pedido) {

        if (pedido == null
                || pedido.getEstado() == null) {

            return false;
        }

        return pedido.getEstado()
                == EstadoPedido.PENDIENTE
                || pedido.getEstado()
                == EstadoPedido.CONFIRMADO
                || pedido.getEstado()
                == EstadoPedido.EN_PROCESO;
    }

    private YearMonth
    obtenerPeriodoSeleccionado() {

        int mes =
                mesSeleccionado == null
                        || mesSeleccionado < 1
                        || mesSeleccionado > 12
                        ? YearMonth.now()
                        .getMonthValue()
                        : mesSeleccionado;

        int anio =
                anioSeleccionado == null
                        ? YearMonth.now()
                        .getYear()
                        : anioSeleccionado;

        return YearMonth.of(
                anio,
                mes
        );
    }

    private boolean perteneceAlPeriodo(
            Pedido pedido,
            YearMonth periodo) {

        return pedido != null
                && pedido.getFechaCompletado()
                != null
                && YearMonth.from(
                pedido.getFechaCompletado()
        ).equals(
                periodo
        );
    }

    private BigDecimal sumarVentas(
            List<Pedido> pedidos) {

        return pedidos.stream()
                .map(
                        Pedido::getTotal
                )
                .filter(
                        valor ->
                                valor != null
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private String formatearPeriodo(
            YearMonth periodo) {

        String valor =
                periodo.format(
                        DateTimeFormatter.ofPattern(
                                "MMMM yyyy",
                                Locale.forLanguageTag(
                                        "es-SV"
                                )
                        )
                );

        return Character
                .toUpperCase(
                        valor.charAt(0)
                )
                + valor.substring(1);
    }

    private Usuario obtenerUsuarioSesion() {

        Object valor =
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get(
                                "usuarioAutenticado"
                        );

        return valor instanceof Usuario
                ? (Usuario) valor
                : null;
    }

    private void mensajeError(
            String resumen,
            String detalleMensaje) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                resumen,
                                detalleMensaje
                        )
                );
    }

    private void redirigir(
            String destino) {

        FacesContext contexto =
                FacesContext
                        .getCurrentInstance();

        ExternalContext externo =
                contexto
                        .getExternalContext();

        try {

            externo.redirect(
                    externo.getRequestContextPath()
                            + destino
            );

            contexto.responseComplete();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "No se pudo realizar la redirección.",
                    e
            );
        }
    }

    private void enviarError(
            int codigo,
            String mensaje) {

        FacesContext contexto =
                FacesContext
                        .getCurrentInstance();

        try {

            contexto
                    .getExternalContext()
                    .responseSendError(
                            codigo,
                            mensaje
                    );

            contexto.responseComplete();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "No se pudo enviar la respuesta de error.",
                    e
            );
        }
    }

    public Integer getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(
            Integer mesSeleccionado) {

        this.mesSeleccionado =
                mesSeleccionado;
    }

    public Integer getAnioSeleccionado() {
        return anioSeleccionado;
    }

    public void setAnioSeleccionado(
            Integer anioSeleccionado) {

        this.anioSeleccionado =
                anioSeleccionado;
    }

    public Integer getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(
            Integer idPublicacion) {

        this.idPublicacion =
                idPublicacion;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(
            Integer cantidad) {

        this.cantidad =
                cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(
            String observaciones) {

        this.observaciones =
                observaciones;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(
            Integer idPedido) {

        this.idPedido =
                idPedido;
    }

    public Pedido getDetalle() {
        return detalle;
    }
}