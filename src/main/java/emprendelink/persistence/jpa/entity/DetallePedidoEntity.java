package emprendelink.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Integer idDetallePedido;

    @NotNull
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "id_pedido",
            nullable = false
    )
    private PedidoEntity pedido;

    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(
            name = "id_publicacion",
            nullable = false
    )
    private PublicacionEntity publicacion;

    @Min(1)
    @Column(
            name = "cantidad",
            nullable = false
    )
    private int cantidad;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    @Column(
            name = "precio_unitario",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal precioUnitario;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    @Column(
            name = "subtotal",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal subtotal;

    public DetallePedidoEntity() {
    }

    public Integer getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(
            Integer idDetallePedido) {

        this.idDetallePedido =
                idDetallePedido;
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(
            PedidoEntity pedido) {

        this.pedido = pedido;
    }

    public PublicacionEntity getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(
            PublicacionEntity publicacion) {

        this.publicacion =
                publicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(
            int cantidad) {

        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(
            BigDecimal precioUnitario) {

        this.precioUnitario =
                precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(
            BigDecimal subtotal) {

        this.subtotal = subtotal;
    }
}