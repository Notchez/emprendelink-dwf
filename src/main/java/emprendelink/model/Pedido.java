package emprendelink.model;

import emprendelink.model.enums.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private Integer idPedido;
    private Usuario cliente;
    private Emprendimiento emprendimiento;
    private EstadoPedido estado;
    private BigDecimal total;
    private BigDecimal tasaComision;
    private BigDecimal montoComision;
    private String observaciones;
    private LocalDateTime fechaPedido;

    private final List<DetallePedido> detalles;

    public Pedido() {

        this.estado =
                EstadoPedido.PENDIENTE;

        this.total =
                BigDecimal.ZERO;

        this.fechaPedido =
                LocalDateTime.now();

        this.detalles =
                new ArrayList<>();
    }

    public Pedido(
            Integer idPedido,
            Usuario cliente,
            Emprendimiento emprendimiento,
            EstadoPedido estado,
            BigDecimal total,
            String observaciones,
            LocalDateTime fechaPedido) {

        this();

        this.idPedido =
                idPedido;

        this.cliente =
                cliente;

        this.emprendimiento =
                emprendimiento;

        this.estado =
                estado == null
                        ? EstadoPedido.PENDIENTE
                        : estado;

        setTotal(
                total
        );

        this.observaciones =
                observaciones;

        this.fechaPedido =
                fechaPedido;
    }

    public Pedido(
            Usuario cliente,
            Emprendimiento emprendimiento,
            String observaciones) {

        this();

        this.cliente =
                cliente;

        this.emprendimiento =
                emprendimiento;

        this.observaciones =
                observaciones;
    }

    public void agregarDetalle(
            DetallePedido detalle) {

        if (detalle == null) {

            throw new IllegalArgumentException(
                    "El detalle es obligatorio"
            );
        }

        detalle.setPedido(
                this
        );

        this.detalles.add(
                detalle
        );

        recalcularTotal();
    }

    public void recalcularTotal() {

        BigDecimal nuevoTotal =
                BigDecimal.ZERO;

        for (DetallePedido detalle
                : detalles) {

            nuevoTotal =
                    nuevoTotal.add(
                            detalle.getSubtotal()
                    );
        }

        this.total =
                nuevoTotal;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(
            Integer idPedido) {

        this.idPedido =
                idPedido;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(
            Usuario cliente) {

        this.cliente =
                cliente;
    }

    public Emprendimiento getEmprendimiento() {
        return emprendimiento;
    }

    public void setEmprendimiento(
            Emprendimiento emprendimiento) {

        this.emprendimiento =
                emprendimiento;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(
            EstadoPedido estado) {

        if (estado == null) {

            throw new IllegalArgumentException(
                    "El estado del pedido es obligatorio"
            );
        }

        this.estado =
                estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(
            BigDecimal total) {

        if (total != null
                && total.signum() < 0) {

            throw new IllegalArgumentException(
                    "El total no puede ser negativo"
            );
        }

        this.total =
                total == null
                        ? BigDecimal.ZERO
                        : total;
    }

    public BigDecimal getTasaComision() {
        return tasaComision;
    }

    public void setTasaComision(
            BigDecimal tasaComision) {

        if (tasaComision != null
                && (tasaComision.signum() < 0
                || tasaComision.compareTo(
                BigDecimal.ONE
        ) > 0)) {

            throw new IllegalArgumentException(
                    "La tasa de comisión debe estar entre 0 y 1."
            );
        }

        this.tasaComision =
                tasaComision;
    }

    public BigDecimal getMontoComision() {
        return montoComision;
    }

    public void setMontoComision(
            BigDecimal montoComision) {

        if (montoComision != null
                && montoComision.signum() < 0) {

            throw new IllegalArgumentException(
                    "La comisión no puede ser negativa."
            );
        }

        this.montoComision =
                montoComision;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(
            String observaciones) {

        this.observaciones =
                observaciones;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(
            LocalDateTime fechaPedido) {

        this.fechaPedido =
                fechaPedido;
    }

    public List<DetallePedido> getDetalles() {

        return new ArrayList<>(
                detalles
        );
    }
}