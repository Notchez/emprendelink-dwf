
package emprendelink.web.rest;

import emprendelink.dto.AuthLoginDTO;
import emprendelink.dto.AuthSessionDTO;
import emprendelink.model.Usuario;
import emprendelink.service.AutenticacionService;
import emprendelink.web.rest.security.ApiSessionSecurity;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AutenticacionService autenticacionService;

    @Context
    private HttpServletRequest solicitud;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(AuthLoginDTO datos) {

        validarCredenciales(datos);

        Usuario usuario = autenticacionService.autenticar(
                datos.getCorreo(),
                datos.getContrasena()
        );

        if (usuario == null
                || !usuario.isActivo()
                || usuario.getIdUsuario() == null
                || usuario.getRol() == null
                || usuario.getRol().getNombre() == null) {

            throw new WebApplicationException(
                    Response.Status.UNAUTHORIZED
            );
        }

        // Elimina una posible sesion anterior para
        // impedir la fijacion del identificador.
        HttpSession anterior = solicitud.getSession(false);

        if (anterior != null) {
            anterior.invalidate();
        }

        HttpSession nueva = solicitud.getSession(true);

        nueva.setAttribute(
                ApiSessionSecurity.USUARIO_SESION,
                usuario
        );

        String token = ApiSessionSecurity.generarTokenCsrf(
                nueva
        );

        return Response.ok(
                respuesta(usuario, token)
        ).header(
                "Cache-Control",
                "no-store"
        ).build();
    }

    @GET
    @Path("/me")
    public Response me() {

        // El filtro ya comprobo el estado actual
        // del usuario en la base de datos.
        Usuario usuario = ApiSessionSecurity
                .usuarioVerificado(solicitud);

        HttpSession sesion = solicitud.getSession(false);

        String token = ApiSessionSecurity
                .obtenerTokenCsrf(sesion);

        return Response.ok(
                respuesta(usuario, token)
        ).header(
                "Cache-Control",
                "no-store"
        ).build();
    }

    @POST
    @Path("/logout")
    public Response logout() {

        // Exige identidad validada por el filtro.
        ApiSessionSecurity.usuarioVerificado(
                solicitud
        );

        // El filtro ya verifico el token CSRF.
        HttpSession sesion = solicitud.getSession(false);

        if (sesion != null) {
            sesion.invalidate();
        }

        return Response.noContent()
                .header("Cache-Control", "no-store")
                .build();
    }

    private void validarCredenciales(
            AuthLoginDTO datos) {

        if (datos == null
                || datos.getCorreo() == null
                || datos.getCorreo().isBlank()
                || datos.getCorreo().length() > 120
                || datos.getContrasena() == null
                || datos.getContrasena().isBlank()
                || datos.getContrasena().length() > 128) {

            throw new BadRequestException();
        }
    }

    private AuthSessionDTO respuesta(
            Usuario usuario,
            String token) {

        return new AuthSessionDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getRol().getNombre().name(),
                token
        );
    }
}
