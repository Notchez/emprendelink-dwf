
package emprendelink.web.rest.security;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class ApiSessionSecurity {

    public static final String USUARIO_SESION =
            "usuarioAutenticado";

    public static final String CSRF_SESION =
            "apiCsrfToken";

    public static final String CSRF_HEADER =
            "X-CSRF-Token";

    public static final String USUARIO_ACTUAL =
            "emprendelink.api.usuarioActual";

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private ApiSessionSecurity() {
    }

    public static Usuario requerirUsuario(
            HttpServletRequest request,
            UsuarioService usuarioService) {

        HttpSession sesion = request.getSession(false);

        if (sesion == null) {
            throw noAutenticado();
        }

        Object valor = sesion.getAttribute(
                USUARIO_SESION
        );

        if (!(valor instanceof Usuario usuario)
                || usuario.getIdUsuario() == null) {

            throw noAutenticado();
        }

        // No confiar en el estado o rol guardados
        // durante un login anterior.
        Usuario actual = usuarioService.buscarPorId(
                usuario.getIdUsuario()
        );

        if (actual == null
                || !actual.isActivo()
                || actual.getRol() == null
                || actual.getRol().getNombre() == null) {

            sesion.invalidate();
            throw new ForbiddenException();
        }

        return actual;
    }

    public static Usuario usuarioVerificado(
            HttpServletRequest request) {

        Object valor = request.getAttribute(
                USUARIO_ACTUAL
        );

        if (!(valor instanceof Usuario usuario)
                || usuario.getIdUsuario() == null
                || !usuario.isActivo()) {

            throw noAutenticado();
        }

        return usuario;
    }

    public static Integer idUsuarioVerificado(
            HttpServletRequest request) {

        return usuarioVerificado(
                request
        ).getIdUsuario();
    }

    public static void requerirRol(
            Usuario usuario,
            TipoRol... rolesPermitidos) {

        if (usuario == null
                || !usuario.isActivo()
                || usuario.getRol() == null
                || usuario.getRol().getNombre() == null) {

            throw new ForbiddenException();
        }

        TipoRol actual = usuario
                .getRol()
                .getNombre();

        if (rolesPermitidos != null) {
            for (TipoRol permitido : rolesPermitidos) {
                if (actual == permitido) {
                    return;
                }
            }
        }

        throw new ForbiddenException();
    }

    public static String generarTokenCsrf(
            HttpSession sesion) {

        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        sesion.setAttribute(
                CSRF_SESION,
                token
        );

        return token;
    }

    public static String obtenerTokenCsrf(
            HttpSession sesion) {

        if (sesion == null) {
            throw noAutenticado();
        }

        synchronized (sesion) {

            Object valor = sesion.getAttribute(
                    CSRF_SESION
            );

            if (valor instanceof String token
                    && !token.isBlank()) {

                return token;
            }

            return generarTokenCsrf(sesion);
        }
    }

    public static void requerirTokenCsrf(
            HttpServletRequest request) {

        HttpSession sesion = request.getSession(false);

        if (sesion == null) {
            throw noAutenticado();
        }

        Object valor = sesion.getAttribute(
                CSRF_SESION
        );

        String recibido = request.getHeader(
                CSRF_HEADER
        );

        if (!(valor instanceof String esperado)
                || esperado.isBlank()
                || recibido == null
                || recibido.isBlank()) {

            throw new ForbiddenException();
        }

        boolean coincide = MessageDigest.isEqual(
                esperado.getBytes(StandardCharsets.UTF_8),
                recibido.getBytes(StandardCharsets.UTF_8)
        );

        if (!coincide) {
            throw new ForbiddenException();
        }
    }

    private static WebApplicationException noAutenticado() {

        return new WebApplicationException(
                Response.Status.UNAUTHORIZED
        );
    }
}
