package emprendelink.web.jsf;

import emprendelink.model.Emprendimiento;
import emprendelink.model.Usuario;
import emprendelink.service.EmprendimientoService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("emprendimientoBean")
@ViewScoped
public class EmprendimientoBean
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmprendimientoService emprendimientoService;

    private Integer idEmprendimiento;
    private String nombre;
    private String descripcion;
    private String contacto;

    public void guardar() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {

            mensajeError(
                    "Sesión inválida",
                    "Debes iniciar sesión nuevamente."
            );

            return;
        }

        try {

            if (idEmprendimiento == null) {

                emprendimientoService.crear(
                        usuario.getIdUsuario(),
                        nombre,
                        descripcion,
                        contacto
                );

                mensajeInfo(
                        "Emprendimiento creado",
                        "El emprendimiento fue registrado correctamente."
                );

            } else {

                emprendimientoService.actualizar(
                        usuario.getIdUsuario(),
                        idEmprendimiento,
                        nombre,
                        descripcion,
                        contacto
                );

                mensajeInfo(
                        "Emprendimiento actualizado",
                        "Los cambios fueron guardados correctamente."
                );
            }

            limpiarFormulario();

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    "No se pudo guardar",
                    e.getMessage()
            );
        }
    }

    public void editar(
            Integer idEmprendimiento) {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {

            mensajeError(
                    "Sesión inválida",
                    "Debes iniciar sesión nuevamente."
            );

            return;
        }

        Emprendimiento emprendimiento =
                emprendimientoService
                        .buscarPropioPorId(
                                idEmprendimiento,
                                usuario.getIdUsuario()
                        );

        if (emprendimiento == null) {

            mensajeError(
                    "No disponible",
                    "El emprendimiento no existe o no te pertenece."
            );

            return;
        }

        this.idEmprendimiento =
                emprendimiento
                        .getIdEmprendimiento();

        nombre =
                emprendimiento.getNombre();

        descripcion =
                emprendimiento.getDescripcion();

        contacto =
                emprendimiento.getContacto();
    }

    public void cancelarEdicion() {
        limpiarFormulario();
    }

    public void alternarEstado(
            Integer idEmprendimiento,
            boolean estadoActual) {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {

            mensajeError(
                    "Sesión inválida",
                    "Debes iniciar sesión nuevamente."
            );

            return;
        }

        try {

            emprendimientoService.cambiarEstado(
                    usuario.getIdUsuario(),
                    idEmprendimiento,
                    !estadoActual
            );

            mensajeInfo(
                    "Estado actualizado",
                    "El estado del emprendimiento fue actualizado."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    "No se pudo actualizar",
                    e.getMessage()
            );
        }
    }

    public List<Emprendimiento>
    getEmprendimientosPropios() {

        Usuario usuario =
                obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        try {

            return emprendimientoService
                    .listarPorPropietario(
                            usuario.getIdUsuario()
                    );

        } catch (IllegalArgumentException e) {

            return List.of();
        }
    }

    public List<Emprendimiento>
    getEmprendimientosAdmin() {

        return emprendimientoService
                .listarTodos();
    }

    public boolean isEditando() {
        return idEmprendimiento != null;
    }

    private Usuario obtenerUsuarioSesion() {

        Object valor =
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get("usuarioAutenticado");

        return valor instanceof Usuario
                ? (Usuario) valor
                : null;
    }

    private void limpiarFormulario() {

        idEmprendimiento = null;
        nombre = null;
        descripcion = null;
        contacto = null;
    }

    private void mensajeInfo(
            String resumen,
            String detalle) {

        FacesContext.getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                resumen,
                                detalle
                        )
                );
    }

    private void mensajeError(
            String resumen,
            String detalle) {

        FacesContext.getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                resumen,
                                detalle
                        )
                );
    }

    public Integer getIdEmprendimiento() {
        return idEmprendimiento;
    }

    public void setIdEmprendimiento(
            Integer idEmprendimiento) {

        this.idEmprendimiento =
                idEmprendimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre) {

        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion = descripcion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(
            String contacto) {

        this.contacto = contacto;
    }
}