package emprendelink.web.jsf;

import emprendelink.model.Emprendimiento;
import emprendelink.model.Publicacion;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.service.PedidoService;
import emprendelink.service.PublicacionService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named("carritoBean")
@SessionScoped
public class CarritoBean
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private PublicacionService publicacionService;

    @Inject
    private PedidoService pedidoService;

    private final Map<Integer, Integer> cantidades =
            new LinkedHashMap<>();

    private String direccionEntrega;
    private String observaciones;

    private boolean direccionInicializada;

    public void agregar(
            Integer idPublicacion) {

        modificarCantidad(
                idPublicacion,
                1
        );
    }

    public void incrementar(
            Integer idPublicacion) {

        modificarCantidad(
                idPublicacion,
                1
        );
    }

    public void disminuir(
            Integer idPublicacion) {

        if (idPublicacion == null) {
            return;
        }

        int actual =
                cantidades.getOrDefault(
                        idPublicacion,
                        0
                );

        if (actual <= 1) {

            cantidades.remove(
                    idPublicacion
            );

            return;
        }

        cantidades.put(
                idPublicacion,
                actual - 1
        );
    }

    public void eliminar(
            Integer idPublicacion) {

        cantidades.remove(
                idPublicacion
        );
    }

    public int cantidadDe(
            Integer idPublicacion) {

        if (idPublicacion == null) {
            return 0;
        }

        return cantidades.getOrDefault(
                idPublicacion,
                0
        );
    }

    public boolean puedeIncrementar(
            Integer idPublicacion) {

        Publicacion publicacion =
                buscarDisponible(
                        idPublicacion
                );

        if (publicacion == null) {
            return false;
        }

        if (publicacion.getTipo()
                != TipoPublicacion.PRODUCTO) {

            return true;
        }

        int stock =
                publicacion.getStock() == null
                        ? 0
                        : publicacion.getStock();

        return cantidadDe(
                idPublicacion
        ) < stock;
    }

    public boolean puedeAgregar(
            Integer idPublicacion) {

        Publicacion publicacion =
                buscarDisponible(
                        idPublicacion
                );

        if (publicacion == null) {
            return false;
        }

        if (publicacion.getTipo()
                != TipoPublicacion.PRODUCTO) {

            return true;
        }

        return publicacion.getStock() != null
                && publicacion.getStock() > 0;
    }

    public int getCantidadTotal() {

        return cantidades.values()
                .stream()
                .mapToInt(
                        Integer::intValue
                )
                .sum();
    }

    public boolean isVacio() {
        return cantidades.isEmpty();
    }

    public List<ItemCarrito> getItems() {

        List<ItemCarrito> items =
                new ArrayList<>();

        for (Map.Entry<Integer, Integer> entrada
                : cantidades.entrySet()) {

            Publicacion publicacion =
                    buscarDisponible(
                            entrada.getKey()
                    );

            if (publicacion == null) {
                continue;
            }

            items.add(
                    new ItemCarrito(
                            publicacion,
                            entrada.getValue()
                    )
            );
        }

        return items;
    }

    public List<GrupoCarrito> getGrupos() {

        Map<Integer, GrupoCarrito> grupos =
                new LinkedHashMap<>();

        for (ItemCarrito item
                : getItems()) {

            Emprendimiento negocio =
                    item.getPublicacion()
                            .getEmprendimiento();

            if (negocio == null
                    || negocio.getIdEmprendimiento()
                    == null) {

                continue;
            }

            GrupoCarrito grupo =
                    grupos.computeIfAbsent(
                            negocio.getIdEmprendimiento(),
                            clave ->
                                    new GrupoCarrito(
                                            negocio
                                    )
                    );

            grupo.agregar(
                    item
            );
        }

        return new ArrayList<>(
                grupos.values()
        );
    }

    public BigDecimal getTotal() {

        return getItems()
                .stream()
                .map(
                        ItemCarrito::getSubtotal
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public void confirmarPedido() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {

            mensajeError(
                    "Tu sesión ya no es válida."
            );

            return;
        }

        if (cantidades.isEmpty()) {

            mensajeError(
                    "Tu carrito está vacío."
            );

            return;
        }

        List<Integer> ids =
                new ArrayList<>();

        List<Integer> cantidadesPedido =
                new ArrayList<>();

        for (Map.Entry<Integer, Integer> entrada
                : cantidades.entrySet()) {

            ids.add(
                    entrada.getKey()
            );

            cantidadesPedido.add(
                    entrada.getValue()
            );
        }

        try {

            pedidoService.crearDesdeCarrito(
                    usuario.getIdUsuario(),
                    ids,
                    cantidadesPedido,
                    getDireccionEntrega(),
                    observaciones
            );

            limpiar();

            FacesContext contexto =
                    FacesContext
                            .getCurrentInstance();

            contexto.getExternalContext()
                    .getFlash()
                    .setKeepMessages(
                            true
                    );

            contexto.addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Pedido creado",
                            "Tus pedidos fueron registrados correctamente."
                    )
            );

            redirigir(
                    "/pedidos/mis-pedidos.xhtml"
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    e.getMessage()
            );
        }
    }

    public void limpiar() {

        cantidades.clear();

        observaciones = null;
        direccionEntrega = null;
        direccionInicializada = false;
    }

    private void modificarCantidad(
            Integer idPublicacion,
            int incremento) {

        Publicacion publicacion =
                buscarDisponible(
                        idPublicacion
                );

        if (publicacion == null) {

            mensajeError(
                    "La publicación ya no está disponible."
            );

            return;
        }

        int actual =
                cantidades.getOrDefault(
                        idPublicacion,
                        0
                );

        int nueva =
                actual + incremento;

        if (publicacion.getTipo()
                == TipoPublicacion.PRODUCTO) {

            int stock =
                    publicacion.getStock() == null
                            ? 0
                            : publicacion.getStock();

            if (nueva > stock) {

                mensajeError(
                        "No hay más existencias disponibles."
                );

                return;
            }
        }

        cantidades.put(
                idPublicacion,
                nueva
        );
    }

    private Publicacion buscarDisponible(
            Integer idPublicacion) {

        if (idPublicacion == null) {
            return null;
        }

        return publicacionService
                .buscarActivaPorId(
                        idPublicacion
                );
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
            String mensaje) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                "Carrito",
                                mensaje
                        )
                );
    }

    private void redirigir(
            String ruta) {

        FacesContext contexto =
                FacesContext
                        .getCurrentInstance();

        ExternalContext externo =
                contexto.getExternalContext();

        try {

            externo.redirect(
                    externo.getRequestContextPath()
                            + ruta
            );

            contexto.responseComplete();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "No se pudo realizar la redirección.",
                    e
            );
        }
    }

    public String getDireccionEntrega() {

        if (!direccionInicializada) {

            Usuario usuario =
                    obtenerUsuarioSesion();

            direccionEntrega =
                    usuario == null
                            ? null
                            : usuario.getDireccion();

            direccionInicializada =
                    true;
        }

        return direccionEntrega;
    }

    public void setDireccionEntrega(
            String direccionEntrega) {

        this.direccionEntrega =
                direccionEntrega;

        direccionInicializada =
                true;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(
            String observaciones) {

        this.observaciones =
                observaciones;
    }

    public static class ItemCarrito
            implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private final Publicacion publicacion;
        private final int cantidad;

        public ItemCarrito(
                Publicacion publicacion,
                int cantidad) {

            this.publicacion =
                    publicacion;

            this.cantidad =
                    cantidad;
        }

        public Publicacion getPublicacion() {
            return publicacion;
        }

        public int getCantidad() {
            return cantidad;
        }

        public BigDecimal getSubtotal() {

            return publicacion
                    .getPrecio()
                    .multiply(
                            BigDecimal.valueOf(
                                    cantidad
                            )
                    );
        }
    }

    public static class GrupoCarrito
            implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private final Emprendimiento emprendimiento;

        private final List<ItemCarrito> items =
                new ArrayList<>();

        public GrupoCarrito(
                Emprendimiento emprendimiento) {

            this.emprendimiento =
                    emprendimiento;
        }

        public void agregar(
                ItemCarrito item) {

            items.add(
                    item
            );
        }

        public Emprendimiento getEmprendimiento() {
            return emprendimiento;
        }

        public List<ItemCarrito> getItems() {
            return items;
        }

        public BigDecimal getSubtotal() {

            return items.stream()
                    .map(
                            ItemCarrito::getSubtotal
                    )
                    .reduce(
                            BigDecimal.ZERO,
                            BigDecimal::add
                    );
        }
    }
}