package emprendelink.security;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/publicaciones/*", "/admin/publicaciones.xhtml"})
public class PublicacionFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest solicitud =
                (HttpServletRequest) request;

        HttpServletResponse respuesta =
                (HttpServletResponse) response;

        HttpSession sesion =
                solicitud.getSession(false);

        if (sesion == null) {
            redirigirLogin(solicitud, respuesta);
            return;
        }

        Object valor = sesion.getAttribute(
                "usuarioAutenticado"
        );

        if (!(valor instanceof Usuario usuario)) {
            redirigirLogin(solicitud, respuesta);
            return;
        }

        if (solicitud.getServletPath().equals("/admin/publicaciones.xhtml")) {
            if (!usuario.isActivo() || usuario.getRol() == null
                    || usuario.getRol().getNombre() != TipoRol.ROLE_ADMIN) {
                respuesta.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if (!usuario.isActivo()
                || usuario.getRol() == null
                || usuario.getRol().getNombre()
                != TipoRol.ROLE_EMPRENDEDOR) {

            respuesta.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return;
        }

        chain.doFilter(request, response);
    }

    private void redirigirLogin(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta)
            throws IOException {

        respuesta.sendRedirect(
                solicitud.getContextPath()
                        + "/login.xhtml"
        );
    }
}
