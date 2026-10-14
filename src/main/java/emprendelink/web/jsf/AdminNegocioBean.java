package emprendelink.web.jsf;

import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.model.Plan;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.service.AdminConsultaService;
import emprendelink.service.EmprendimientoService;
import emprendelink.service.PlanService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class AdminNegocioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmprendimientoService emprendimientoService;

    @Inject
    private AdminConsultaService adminConsultaService;

    @Inject
    private PlanService planService;

    private Integer idEmprendimiento;

    private Integer idPlanSeleccionado;

    private Emprendimiento emprendimiento;

    private List<Pedido> pedidos =
            Collections.emptyList();

    private List<Plan> planes =
            Collections.emptyList();

    @PostConstruct
    public void inicializar() {

        cargarPlanes();

        String parametro =
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get("id");

        if (parametro == null
                || parametro.isBlank()) {

            return;
        }

        try {

            idEmprendimiento =
                    Integer.valueOf(
                            parametro
                    );

        } catch (NumberFormatException e) {

            return;
        }

        cargar();
    }

    private void cargar() {

        emprendimiento =
                emprendimientoService
                        .listarTodos()
                        .stream()
                        .filter(item ->
                                idEmprendimiento.equals(
                                        item.getIdEmprendimiento()
                                )
                        )
                        .findFirst()
                        .orElse(null);

        if (emprendimiento == null) {

            pedidos =
                    Collections.emptyList();

            idPlanSeleccionado =
                    null;

            return;
        }

        pedidos =
                adminConsultaService
                        .listarPedidosPorEmprendimiento(
                                idEmprendimiento
                        );

        if (emprendimiento.getPlan()
                != null) {

            idPlanSeleccionado =
                    emprendimiento
                            .getPlan()
                            .getIdPlan();

        } else {

            idPlanSeleccionado =
                    null;
        }
    }

    private void cargarPlanes() {

        planes =
                planService.listarActivos();
    }

    public void guardarPlan() {

        if (emprendimiento == null
                || idPlanSeleccionado == null) {

            mensajeError(
                    "Seleccione un plan válido."
            );

            return;
        }

        try {

            planService
                    .asignarAEmprendimiento(
                            emprendimiento
                                    .getIdEmprendimiento(),
                            idPlanSeleccionado
                    );

            cargar();

            mensajeExito(
                    "Plan actualizado correctamente."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException ex) {

            mensajeError(
                    ex.getMessage()
            );
        }
    }

    public BigDecimal getTotalVentas() {

        return pedidos.stream()
                .filter(pedido ->
                        pedido.getEstado()
                                == EstadoPedido.COMPLETADO
                )
                .map(Pedido::getTotal)
                .filter(total ->
                        total != null
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public String getPorcentajeComision() {

        if (emprendimiento == null
                || emprendimiento.getPlan()
                == null
                || emprendimiento
                .getPlan()
                .getTasaComision()
                == null) {

            return "—";
        }

        return emprendimiento
                .getPlan()
                .getTasaComision()
                .multiply(
                        BigDecimal.valueOf(100)
                )
                .stripTrailingZeros()
                .toPlainString();
    }

    public Integer getIdEmprendimiento() {
        return idEmprendimiento;
    }

    public Emprendimiento getEmprendimiento() {
        return emprendimiento;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public Integer getIdPlanSeleccionado() {
        return idPlanSeleccionado;
    }

    public void setIdPlanSeleccionado(
            Integer idPlanSeleccionado) {

        this.idPlanSeleccionado =
                idPlanSeleccionado;
    }

    public boolean isEncontrado() {
        return emprendimiento != null;
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
}