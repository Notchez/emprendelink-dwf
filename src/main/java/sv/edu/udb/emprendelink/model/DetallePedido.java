package sv.edu.udb.emprendelink.model;

import java.math.BigDecimal;

public class DetallePedido {

    private Integer idDetallePedido;
    private Pedido pedido;
    private Publicacion publicacion;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetallePedido() {
        this.subtotal = BigDecimal.ZERO;
    }

    public DetallePedido(Integer idDetallePedido, Pedido pedido, Publicacion publicacion,
                         int cantidad, BigDecimal precioUnitario) {
        this();
        this.idDetallePedido = idDetallePedido;
        this.pedido = pedido;
        this.publicacion = publicacion;
        setCantidad(cantidad);
        setPrecioUnitario(precioUnitario);
        recalcularSubtotal();
    }

    public DetallePedido(Publicacion publicacion, int cantidad, BigDecimal precioUnitario) {
        this(null, null, publicacion, cantidad, precioUnitario);
    }

    // Se llama cuando cambia la cantidad o el precio.
    public void recalcularSubtotal() {
        if (precioUnitario == null || cantidad <= 0) {
            this.subtotal = BigDecimal.ZERO;
            return;
        }
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Integer getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(Integer idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        if (precioUnitario == null || precioUnitario.signum() < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }
        this.precioUnitario = precioUnitario;
        recalcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
