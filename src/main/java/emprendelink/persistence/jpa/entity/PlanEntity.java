package emprendelink.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "planes")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Integer idPlan;

    @NotBlank
    @Size(max = 80)
    @Column(
            name = "nombre",
            nullable = false,
            unique = true,
            length = 80
    )
    private String nombre;

    @NotNull
    @Min(1)
    @Column(
            name = "max_productos_activos",
            nullable = false
    )
    private Integer maxProductosActivos;

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

    @Column(
            name = "activo",
            nullable = false
    )
    private boolean activo;

    public PlanEntity() {
    }

    public Integer getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Integer idPlan) {
        this.idPlan = idPlan;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMaxProductosActivos() {
        return maxProductosActivos;
    }

    public void setMaxProductosActivos(
            Integer maxProductosActivos) {

        this.maxProductosActivos =
                maxProductosActivos;
    }

    public BigDecimal getTasaComision() {
        return tasaComision;
    }

    public void setTasaComision(
            BigDecimal tasaComision) {

        this.tasaComision =
                tasaComision;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}