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

@WebFilter("/admin/*")
public class AdminFilter
        implements Filter {

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

            respuesta.sendRedirect(
                    solicitud.getContextPath()
                            + "/login.xhtml"
            );

            return;
        }

        Object valor =
                sesion.getAttribute(
                        "usuarioAutenticado"
                );

        if (!(valor instanceof Usuario usuario)) {

            respuesta.sendRedirect(
                    solicitud.getContextPath()
                            + "/login.xhtml"
            );

            return;
        }

        if (usuario.getRol() == null
                || usuario.getRol().getNombre()
                != TipoRol.ROLE_ADMIN) {

            respuesta.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return;
        }

        chain.doFilter(
                request,
                response
        );
    }
}