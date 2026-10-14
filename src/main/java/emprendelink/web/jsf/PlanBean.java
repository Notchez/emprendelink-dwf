package emprendelink.web.jsf;

import emprendelink.model.Plan;
import emprendelink.service.PlanService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class PlanBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PlanService planService;

    private List<Plan> planes =
            Collections.emptyList();

    private Integer idPlanEditando;

    private String nombre;

    private Integer maxProductosActivos;

    private BigDecimal porcentajeComision;

    @PostConstruct
    public void inicializar() {

        cargarPlanes();
        limpiarFormulario();
    }

    public void guardar() {

        try {

            BigDecimal tasa =
                    porcentajeATasa(
                            porcentajeComision
                    );

            if (idPlanEditando == null) {

                planService.crear(
                        nombre,
                        maxProductosActivos,
                        tasa
                );

                mensajeExito(
                        "Plan creado correctamente."
                );

            } else {

                planService.actualizar(
                        idPlanEditando,
                        nombre,
                        maxProductosActivos,
                        tasa
                );

                mensajeExito(
                        "Plan actualizado correctamente."
                );
            }

            cargarPlanes();
            limpiarFormulario();

        } catch (IllegalArgumentException
                 | IllegalStateException ex) {

            mensajeError(
                    ex.getMessage()
            );
        }
    }

    public void editar(
            Plan plan) {

        if (plan == null) {
            return;
        }

        idPlanEditando =
                plan.getIdPlan();

        nombre =
                plan.getNombre();

        maxProductosActivos =
                plan.getMaxProductosActivos();

        porcentajeComision =
                tasaAPorcentaje(
                        plan.getTasaComision()
                );
    }

    public void cancelarEdicion() {

        limpiarFormulario();
    }

    public void cambiarEstado(
            Plan plan) {

        if (plan == null
                || plan.getIdPlan() == null) {

            return;
        }

        try {

            planService.cambiarEstado(
                    plan.getIdPlan(),
                    !plan.isActivo()
            );

            cargarPlanes();

            mensajeExito(
                    plan.isActivo()
                            ? "Plan desactivado correctamente."
                            : "Plan activado correctamente."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException ex) {

            mensajeError(
                    ex.getMessage()
            );
        }
    }

    private void cargarPlanes() {

        planes =
                planService.listarTodos();
    }

    private void limpiarFormulario() {

        idPlanEditando = null;
        nombre = null;
        maxProductosActivos = null;
        porcentajeComision = null;
    }

    private BigDecimal porcentajeATasa(
            BigDecimal porcentaje) {

        if (porcentaje == null) {
            return null;
        }

        return porcentaje
                .divide(
                        BigDecimal.valueOf(100),
                        4,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal tasaAPorcentaje(
            BigDecimal tasa) {

        if (tasa == null) {
            return null;
        }

        return tasa
                .multiply(
                        BigDecimal.valueOf(100)
                )
                .stripTrailingZeros();
    }

    public BigDecimal porcentaje(
            Plan plan) {

        if (plan == null) {
            return BigDecimal.ZERO;
        }

        return tasaAPorcentaje(
                plan.getTasaComision()
        );
    }

    private void mensajeExito(
            String mensaje) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                mensaje,
                                null
                        )
                );
    }

    private void mensajeError(
            String mensaje) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                mensaje,
                                null
                        )
                );
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public Integer getIdPlanEditando() {
        return idPlanEditando;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre) {

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

    public BigDecimal getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(
            BigDecimal porcentajeComision) {

        this.porcentajeComision =
                porcentajeComision;
    }

    public boolean isEditando() {
        return idPlanEditando != null;
    }
}