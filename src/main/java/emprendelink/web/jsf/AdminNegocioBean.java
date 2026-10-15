package emprendelink.web.jsf;

import emprendelink.model.Categoria;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.model.Plan;
import emprendelink.model.Usuario;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.service.AdminConsultaService;
import emprendelink.service.CategoriaService;
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
public class AdminNegocioBean
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmprendimientoService emprendimientoService;

    @Inject
    private AdminConsultaService adminConsultaService;

    @Inject
    private PlanService planService;

    @Inject
    private CategoriaService categoriaService;

    private Integer idEmprendimiento;

    private Integer idPlanSeleccionado;
    private Integer idCategoriaSeleccionada;

    private String nombre;
    private String logoUrl;

    private Emprendimiento emprendimiento;

    private List<Pedido> pedidos =
            Collections.emptyList();

    private List<Plan> planes =
            Collections.emptyList();

    private List<Categoria> categorias =
            Collections.emptyList();

    @PostConstruct
    public void inicializar() {

        planes =
                planService.listarActivos();

        categorias =
                categoriaService.listarActivas();

        String parametro =
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get(
                                "id"
                        );

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
                        .filter(
                                item ->
                                        idEmprendimiento.equals(
                                                item.getIdEmprendimiento()
                                        )
                        )
                        .findFirst()
                        .orElse(null);

        if (emprendimiento == null) {

            pedidos =
                    Collections.emptyList();

            return;
        }

        pedidos =
                adminConsultaService
                        .listarPedidosPorEmprendimiento(
                                idEmprendimiento
                        );

        nombre =
                emprendimiento.getNombre();

        logoUrl =
                emprendimiento.getLogoUrl();

        idPlanSeleccionado =
                emprendimiento.getPlan() == null
                        ? null
                        : emprendimiento
                        .getPlan()
                        .getIdPlan();

        idCategoriaSeleccionada =
                emprendimiento.getCategoria()
                        == null
                        ? null
                        : emprendimiento
                        .getCategoria()
                        .getIdCategoria();
    }

    public void guardarDatos() {

        Usuario administrador =
                obtenerUsuarioSesion();

        if (administrador == null) {

            mensajeError(
                    "No se pudo identificar al administrador."
            );

            return;
        }

        try {

            emprendimientoService
                    .actualizarDatosAdministrativos(
                            administrador
                                    .getIdUsuario(),
                            idEmprendimiento,
                            nombre,
                            idCategoriaSeleccionada,
                            logoUrl
                    );

            cargar();

            mensajeExito(
                    "Datos del negocio actualizados."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    e.getMessage()
            );
        }
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
                 | IllegalStateException e) {

            mensajeError(
                    e.getMessage()
            );
        }
    }

    public BigDecimal getTotalVentas() {

        return pedidos.stream()
                .filter(
                        pedido ->
                                pedido.getEstado()
                                        == EstadoPedido.COMPLETADO
                )
                .map(
                        Pedido::getTotal
                )
                .filter(
                        total ->
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
                        BigDecimal.valueOf(
                                100
                        )
                )
                .stripTrailingZeros()
                .toPlainString();
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

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Integer getIdPlanSeleccionado() {
        return idPlanSeleccionado;
    }

    public void setIdPlanSeleccionado(
            Integer idPlanSeleccionado) {

        this.idPlanSeleccionado =
                idPlanSeleccionado;
    }

    public Integer getIdCategoriaSeleccionada() {
        return idCategoriaSeleccionada;
    }

    public void setIdCategoriaSeleccionada(
            Integer idCategoriaSeleccionada) {

        this.idCategoriaSeleccionada =
                idCategoriaSeleccionada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre) {

        this.nombre =
                nombre;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(
            String logoUrl) {

        this.logoUrl =
                logoUrl;
    }

    public boolean isEncontrado() {
        return emprendimiento != null;
    }
}