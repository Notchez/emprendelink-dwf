package sv.edu.udb.emprendelink.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sv.edu.udb.emprendelink.model.enums.EstadoPedido;

public class Pedido {

    private Integer idPedido;
    private Usuario cliente;
    private Emprendimiento emprendimiento;
    private EstadoPedido estado;
    private BigDecimal total;
    private String observaciones;
    private LocalDateTime fechaPedido;
    private final List<DetallePedido> detalles;

    public Pedido() {
        this.estado = EstadoPedido.PENDIENTE;
        this.total = BigDecimal.ZERO;
        this.fechaPedido = LocalDateTime.now();
        this.detalles = new ArrayList<>();
    }

    public Pedido(Integer idPedido, Usuario cliente, Emprendimiento emprendimiento,
                  EstadoPedido estado, BigDecimal total, String observaciones,
                  LocalDateTime fechaPedido) {
        this();
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.emprendimiento = emprendimiento;
        this.estado = estado == null ? EstadoPedido.PENDIENTE : estado;
        setTotal(total);
        this.observaciones = observaciones;
        this.fechaPedido = fechaPedido;
    }

    public Pedido(Usuario cliente, Emprendimiento emprendimiento, String observaciones) {
        this();
        this.cliente = cliente;
        this.emprendimiento = emprendimiento;
        this.observaciones = observaciones;
    }

    // Mantiene la lista y el total del pedido sincronizados.
    public void agregarDetalle(DetallePedido detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle es obligatorio");
        }
        detalle.setPedido(this);
        this.detalles.add(detalle);
        recalcularTotal();
    }

    public void recalcularTotal() {
        BigDecimal nuevoTotal = BigDecimal.ZERO;
        for (DetallePedido detalle : detalles) {
            nuevoTotal = nuevoTotal.add(detalle.getSubtotal());
        }
        this.total = nuevoTotal;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Emprendimiento getEmprendimiento() {
        return emprendimiento;
    }

    public void setEmprendimiento(Emprendimiento emprendimiento) {
        this.emprendimiento = emprendimiento;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del pedido es obligatorio");
        }
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (total != null && total.signum() < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        this.total = total == null ? BigDecimal.ZERO : total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public List<DetallePedido> getDetalles() {
        return new ArrayList<>(detalles);
    }
}
