package emprendelink.web.jsf;

import emprendelink.model.Emprendimiento;
import emprendelink.model.Plan;
import emprendelink.model.Publicacion;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.EmprendimientoService;
import emprendelink.service.PublicacionService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named("emprendimientoBean")
@ViewScoped
public class EmprendimientoBean
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private EmprendimientoService emprendimientoService;

    @Inject
    private PublicacionService publicacionService;

    private Emprendimiento negocioActual;

    private String descripcion;
    private String contacto;
    private String logoUrl;

    @PostConstruct
    public void inicializar() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null
                || usuario.getRol() == null
                || usuario
                .getRol()
                .getNombre()
                != TipoRol.ROLE_EMPRENDEDOR) {

            return;
        }

        cargarNegocioActual(
                usuario.getIdUsuario()
        );
    }

    public void guardarCambios() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null
                || negocioActual == null) {

            mensajeError(
                    "No se pudo identificar tu negocio."
            );

            return;
        }

        try {

            emprendimientoService
                    .actualizarDatosEmprendedor(
                            usuario.getIdUsuario(),
                            negocioActual
                                    .getIdEmprendimiento(),
                            descripcion,
                            contacto,
                            logoUrl
                    );

            cargarNegocioActual(
                    usuario.getIdUsuario()
            );

            mensajeInfo(
                    "Los datos del negocio fueron actualizados."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    e.getMessage()
            );
        }
    }

    public List<Emprendimiento>
    getEmprendimientosAdmin() {

        return emprendimientoService
                .listarTodos();
    }

    public List<Emprendimiento>
    getEmprendimientosPropios() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        return emprendimientoService
                .listarPorPropietario(
                        usuario.getIdUsuario()
                );
    }

    public int getPublicacionesActivas() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null
                || negocioActual == null) {

            return 0;
        }

        return (int) publicacionService
                .listarPorPropietario(
                        usuario.getIdUsuario()
                )
                .stream()
                .filter(
                        Publicacion::isActivo
                )
                .count();
    }

    public int getMaxPublicaciones() {

        Plan plan =
                getPlanActual();

        if (plan == null
                || plan.getMaxProductosActivos()
                == null) {

            return 0;
        }

        return plan.getMaxProductosActivos();
    }

    public int getEspaciosDisponibles() {

        return Math.max(
                0,
                getMaxPublicaciones()
                        - getPublicacionesActivas()
        );
    }

    public BigDecimal getPorcentajeComision() {

        Plan plan =
                getPlanActual();

        if (plan == null
                || plan.getTasaComision()
                == null) {

            return BigDecimal.ZERO;
        }

        return plan.getTasaComision()
                .multiply(
                        BigDecimal.valueOf(
                                100
                        )
                )
                .stripTrailingZeros();
    }

    public Plan getPlanActual() {

        return negocioActual == null
                ? null
                : negocioActual.getPlan();
    }

    private void cargarNegocioActual(
            Integer idUsuario) {

        negocioActual =
                emprendimientoService
                        .listarPorPropietario(
                                idUsuario
                        )
                        .stream()
                        .findFirst()
                        .orElse(null);

        if (negocioActual == null) {

            descripcion = null;
            contacto = null;
            logoUrl = null;

            return;
        }

        descripcion =
                negocioActual.getDescripcion();

        contacto =
                negocioActual.getContacto();

        logoUrl =
                negocioActual.getLogoUrl();
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

    private void mensajeInfo(
            String mensaje) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Actualizado",
                                mensaje
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
                                "No se pudo completar la operación",
                                mensaje
                        )
                );
    }

    public Emprendimiento getNegocioActual() {
        return negocioActual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion =
                descripcion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(
            String contacto) {

        this.contacto =
                contacto;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(
            String logoUrl) {

        this.logoUrl =
                logoUrl;
    }
}