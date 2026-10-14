package emprendelink.model;

import java.math.BigDecimal;

public class Plan {

    private Integer idPlan;
    private String nombre;
    private Integer maxProductosActivos;
    private BigDecimal tasaComision;
    private boolean activo;

    public Plan() {
        this.activo = true;
    }

    public Plan(
            Integer idPlan,
            String nombre,
            Integer maxProductosActivos,
            BigDecimal tasaComision,
            boolean activo) {

        this.idPlan = idPlan;
        this.nombre = nombre;
        this.maxProductosActivos = maxProductosActivos;
        this.tasaComision = tasaComision;
        this.activo = activo;
    }

    public Plan(
            String nombre,
            Integer maxProductosActivos,
            BigDecimal tasaComision) {

        this();

        this.nombre = nombre;
        this.maxProductosActivos = maxProductosActivos;
        this.tasaComision = tasaComision;
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