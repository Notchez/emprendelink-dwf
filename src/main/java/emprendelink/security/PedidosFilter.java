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

@WebFilter("/pedidos/*")
public class PedidosFilter implements Filter {

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
                        "usuarioAutenticado"
                );

        if (!(valor instanceof Usuario usuario)
                || usuario.getRol() == null) {

            redirigirLogin(
                    solicitud,
                    respuesta
            );
            return;
        }

        String ruta =
                solicitud.getRequestURI()
                        .substring(
                                solicitud
                                        .getContextPath()
                                        .length()
                        );

        TipoRol rol =
                usuario.getRol()
                        .getNombre();

        boolean rutaCliente =
                ruta.endsWith(
                        "/nuevo.xhtml"
                )
                        || ruta.endsWith(
                        "/mis-pedidos.xhtml"
                );

        boolean rutaEmprendedor =
                ruta.endsWith(
                        "/recibidos.xhtml"
                );

        if (rutaCliente
                && rol != TipoRol.ROLE_CLIENTE) {

            respuesta.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return;
        }

        if (rutaEmprendedor
                && rol != TipoRol.ROLE_EMPRENDEDOR) {

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