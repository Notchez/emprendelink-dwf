package emprendelink.web.jsf;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;

@Named("clientePerfilBean")
@ViewScoped
public class ClientePerfilBean
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    private Usuario usuario;

    private String telefono;
    private String direccion;

    @PostConstruct
    public void inicializar() {

        Usuario sesion =
                obtenerUsuarioSesion();

        if (sesion == null
                || sesion.getRol() == null
                || sesion
                .getRol()
                .getNombre()
                != TipoRol.ROLE_CLIENTE) {

            return;
        }

        usuario =
                usuarioService.buscarPorId(
                        sesion.getIdUsuario()
                );

        if (usuario == null) {
            return;
        }

        telefono =
                usuario.getTelefono();

        direccion =
                usuario.getDireccion();
    }

    public void guardar() {

        if (usuario == null) {
            return;
        }

        try {

            usuario =
                    usuarioService
                            .actualizarPerfilCliente(
                                    usuario.getIdUsuario(),
                                    telefono,
                                    direccion
                            );

            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put(
                            "usuarioAutenticado",
                            usuario
                    );

            telefono =
                    usuario.getTelefono();

            direccion =
                    usuario.getDireccion();

            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "Perfil actualizado",
                                    "Tus datos fueron guardados."
                            )
                    );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo actualizar",
                                    e.getMessage()
                            )
                    );
        }
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

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(
            String telefono) {

        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(
            String direccion) {

        this.direccion = direccion;
    }
}