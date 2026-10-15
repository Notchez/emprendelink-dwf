package emprendelink.persistence.jpa.entity;

import emprendelink.model.enums.EstadoPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "id_pedido")
    private Integer idPedido;

    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(
            name = "id_cliente",
            nullable = false
    )
    private UsuarioEntity cliente;

    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(
            name = "id_emprendimiento",
            nullable = false
    )
    private EmprendimientoEntity emprendimiento;

    @NotNull
    @Column(
            name = "id_plan_aplicado",
            nullable = false
    )
    private Integer idPlanAplicado;

    @NotBlank
    @Size(max = 80)
    @Column(
            name = "nombre_plan_aplicado",
            nullable = false,
            length = 80
    )
    private String nombrePlanAplicado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            name = "estado",
            nullable = false,
            length = 20
    )
    private EstadoPedido estado;

    @NotNull
    @DecimalMin("0.00")
    @Digits(
            integer = 8,
            fraction = 2
    )
    @Column(
            name = "total",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal total;

    @NotNull
    @DecimalMin("0.0000")
    @DecimalMax("1.0000")
    @Column(
            name = "tasa_comision",
            nullable = false,
            precision = 5,
            scale = 4
    )
    private BigDecimal tasaComision;

    @NotNull
    @DecimalMin("0.00")
    @Column(
            name = "monto_comision",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal montoComision;

    @Column(
            name = "observaciones",
            columnDefinition = "TEXT"
    )
    private String observaciones;

    @NotNull
    @Column(
            name = "fecha_pedido",
            nullable = false
    )
    private LocalDateTime fechaPedido;

    @Column(
            name = "fecha_completado"
    )
    private LocalDateTime fechaCompletado;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetallePedidoEntity> detalles =
            new ArrayList<>();

    public PedidoEntity() {
    }

    public void agregarDetalle(
            DetallePedidoEntity detalle) {

        detalle.setPedido(
                this
        );

        detalles.add(
                detalle
        );
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(
            Integer idPedido) {

        this.idPedido =
                idPedido;
    }

    public UsuarioEntity getCliente() {
        return cliente;
    }

    public void setCliente(
            UsuarioEntity cliente) {

        this.cliente =
                cliente;
    }

    public EmprendimientoEntity getEmprendimiento() {
        return emprendimiento;
    }

    public void setEmprendimiento(
            EmprendimientoEntity emprendimiento) {

        this.emprendimiento =
                emprendimiento;
    }

    public Integer getIdPlanAplicado() {
        return idPlanAplicado;
    }

    public void setIdPlanAplicado(
            Integer idPlanAplicado) {

        this.idPlanAplicado =
                idPlanAplicado;
    }

    public String getNombrePlanAplicado() {
        return nombrePlanAplicado;
    }

    public void setNombrePlanAplicado(
            String nombrePlanAplicado) {

        this.nombrePlanAplicado =
                nombrePlanAplicado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(
            EstadoPedido estado) {

        this.estado =
                estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(
            BigDecimal total) {

        this.total =
                total;
    }

    public BigDecimal getTasaComision() {
        return tasaComision;
    }

    public void setTasaComision(
            BigDecimal tasaComision) {

        this.tasaComision =
                tasaComision;
    }

    public BigDecimal getMontoComision() {
        return montoComision;
    }

    public void setMontoComision(
            BigDecimal montoComision) {

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

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(
            LocalDateTime fechaCompletado) {

        this.fechaCompletado =
                fechaCompletado;
    }

    public List<DetallePedidoEntity> getDetalles() {
        return detalles;
    }

    public void setDetalles(
            List<DetallePedidoEntity> detalles) {

        this.detalles =
                detalles;
    }
}