package emprendelink.web.jsf;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.UsuarioService;
import jakarta.enterprise.inject.Model;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import java.util.List;

@Model
public class UsuarioBean {

    @Inject
    private UsuarioService usuarioService;

    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String telefono;

    private TipoRol rol =
            TipoRol.ROLE_CLIENTE;

    private List<Usuario> usuarios;

    public String registrar() {

        FacesContext contexto =
                FacesContext.getCurrentInstance();

        try {

            usuarioService.registrar(
                    nombre,
                    apellido,
                    correo,
                    contrasena,
                    telefono,
                    rol
            );

            return "/login.xhtml"
                    + "?faces-redirect=true"
                    + "&registro=exitoso";

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            contexto.addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "No se pudo registrar",
                            e.getMessage()
                    )
            );

            return null;
        }
    }

    public void alternarEstado(
            Integer idUsuario,
            boolean estadoActual) {

        FacesContext contexto =
                FacesContext.getCurrentInstance();

        Usuario actual =
                obtenerUsuarioSesion();

        if (actual != null
                && actual.getIdUsuario() != null
                && actual.getIdUsuario()
                .equals(idUsuario)
                && estadoActual) {

            contexto.addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_WARN,
                            "Acción no permitida",
                            "No puedes desactivar tu propia cuenta."
                    )
            );

            return;
        }

        try {

            usuarioService.cambiarEstado(
                    idUsuario,
                    !estadoActual
            );

            usuarios = null;

            contexto.addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Usuario actualizado",
                            "El estado del usuario fue actualizado."
                    )
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            contexto.addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "No se pudo actualizar",
                            e.getMessage()
                    )
            );
        }
    }

    public List<Usuario> getUsuarios() {

        if (usuarios == null) {

            usuarios =
                    usuarioService.listarTodos();
        }

        return usuarios;
    }

    public TipoRol[] getRolesRegistro() {

        return new TipoRol[]{
                TipoRol.ROLE_CLIENTE,
                TipoRol.ROLE_EMPRENDEDOR
        };
    }

    public String etiquetaRol(
            TipoRol rol) {

        if (rol == TipoRol.ROLE_CLIENTE) {
            return "Cliente";
        }

        if (rol == TipoRol.ROLE_EMPRENDEDOR) {
            return "Emprendedor";
        }

        return "Administrador";
    }

    public String getDescripcionRol() {

        if (rol == TipoRol.ROLE_EMPRENDEDOR) {

            return "Podrás registrar y administrar un emprendimiento.";
        }

        return "Podrás explorar el catálogo y realizar pedidos.";
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre) {

        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(
            String apellido) {

        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(
            String correo) {

        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(
            String contrasena) {

        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(
            String telefono) {

        this.telefono = telefono;
    }

    public TipoRol getRol() {
        return rol;
    }

    public void setRol(
            TipoRol rol) {

        this.rol = rol;
    }
}