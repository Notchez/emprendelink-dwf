package emprendelink.security;

import emprendelink.model.Usuario;
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

@WebFilter("/inicio.xhtml")
public class AutenticacionFilter
        implements Filter {

    private static final String SESION_USUARIO =
            "usuarioAutenticado";

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

            redirigirLogin(
                    solicitud,
                    respuesta
            );

            return;
        }

        Object valor =
                sesion.getAttribute(
                        SESION_USUARIO
                );

        if (!(valor instanceof Usuario)) {

            redirigirLogin(
                    solicitud,
                    respuesta
            );

            return;
        }

        chain.doFilter(
                request,
                response
        );
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