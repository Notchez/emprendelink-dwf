package emprendelink.web.jsf;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.AutenticacionService;
import jakarta.enterprise.inject.Model;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@Model
public class AuthBean {

    private static final String SESION_USUARIO =
            "usuarioAutenticado";

    @Inject
    private AutenticacionService autenticacionService;

    private String correo;
    private String contrasena;

    public void iniciarSesion() {

        FacesContext contexto =
                FacesContext.getCurrentInstance();

        Usuario usuario =
                autenticacionService.autenticar(
                        correo,
                        contrasena
                );

        if (usuario == null) {

            contexto.addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "No se pudo iniciar sesión",
                            "Correo o contraseña incorrectos."
                    )
            );

            return;
        }

        ExternalContext externo =
                contexto.getExternalContext();

        HttpServletRequest solicitud =
                (HttpServletRequest)
                        externo.getRequest();

        HttpSession sesionAnterior =
                solicitud.getSession(false);

        if (sesionAnterior != null) {
            sesionAnterior.invalidate();
        }

        HttpSession nuevaSesion =
                solicitud.getSession(true);

        nuevaSesion.setAttribute(
                SESION_USUARIO,
                usuario
        );

        correo = null;
        contrasena = null;

        redirigirSegunRol(
                usuario
        );
    }

    public void cerrarSesion() {

        FacesContext contexto =
                FacesContext.getCurrentInstance();

        ExternalContext externo =
                contexto.getExternalContext();

        String contextoAplicacion =
                externo.getRequestContextPath();

        externo.invalidateSession();

        try {

            externo.redirect(
                    contextoAplicacion
                            + "/login.xhtml"
            );

            contexto.responseComplete();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "No se pudo cerrar la sesión.",
                    e
            );
        }
    }

    public void redirigirInicio() {

        Usuario usuario =
                getUsuario();

        if (usuario == null) {

            redirigirA(
                    "/login.xhtml"
            );

            return;
        }

        redirigirSegunRol(
                usuario
        );
    }

    public Usuario getUsuario() {

        Object valor =
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get(
                                SESION_USUARIO
                        );

        return valor instanceof Usuario
                ? (Usuario) valor
                : null;
    }

    public boolean isAutenticado() {

        return getUsuario() != null;
    }

    private void redirigirSegunRol(
            Usuario usuario) {

        TipoRol rol =
                usuario != null
                        && usuario.getRol() != null
                        ? usuario
                        .getRol()
                        .getNombre()
                        : null;

        String destino;

        if (rol == TipoRol.ROLE_ADMIN) {

            destino =
                    "/admin/inicio.xhtml";

        } else if (rol
                == TipoRol.ROLE_EMPRENDEDOR) {

            destino =
                    "/emprendimientos/index.xhtml";

        } else if (rol
                == TipoRol.ROLE_CLIENTE) {

            destino =
                    "/catalogo/index.xhtml";

        } else {

            destino =
                    "/login.xhtml";
        }

        redirigirA(
                destino
        );
    }

    private void redirigirA(
            String destino) {

        FacesContext contexto =
                FacesContext.getCurrentInstance();

        ExternalContext externo =
                contexto.getExternalContext();

        try {

            externo.redirect(
                    externo.getRequestContextPath()
                            + destino
            );

            contexto.responseComplete();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "No se pudo realizar la redirección.",
                    e
            );
        }
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
}