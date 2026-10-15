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

@WebFilter("/catalogo/*")
public class CatalogoFilter
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

        /*
         * El catálogo es público para visitantes.
         */
        if (sesion == null) {

            chain.doFilter(
                    request,
                    response
            );

            return;
        }

        Object valor =
                sesion.getAttribute(
                        "usuarioAutenticado"
                );

        /*
         * Sesión sin usuario válido:
         * se trata como visitante.
         */
        if (!(valor instanceof Usuario usuario)
                || usuario.getRol() == null) {

            chain.doFilter(
                    request,
                    response
            );

            return;
        }

        TipoRol rol =
                usuario.getRol()
                        .getNombre();

        if (rol == TipoRol.ROLE_CLIENTE) {

            chain.doFilter(
                    request,
                    response
            );

            return;
        }

        if (rol == TipoRol.ROLE_EMPRENDEDOR) {

            respuesta.sendRedirect(
                    solicitud.getContextPath()
                            + "/pedidos/recibidos.xhtml"
            );

            return;
        }

        if (rol == TipoRol.ROLE_ADMIN) {

            respuesta.sendRedirect(
                    solicitud.getContextPath()
                            + "/admin/inicio.xhtml"
            );

            return;
        }

        respuesta.sendError(
                HttpServletResponse.SC_FORBIDDEN
        );
    }
}