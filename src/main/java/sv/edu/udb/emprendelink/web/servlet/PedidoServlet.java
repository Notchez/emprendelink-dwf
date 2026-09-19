package sv.edu.udb.emprendelink.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sv.edu.udb.emprendelink.model.Pedido;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.EstadoPedido;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.PedidoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({
        "/pedidos",
        "/pedidos/crear",
        "/pedidos/detalle",
        "/pedidos/recibidos",
        "/pedidos/estado"
})
public class PedidoServlet extends HttpServlet {

    private static final String SESION_USUARIO =
            "usuarioAutenticado";

    private static final String SERVICIO =
            "pedidoService";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String ruta =
                request.getServletPath();

        PedidoService service =
                obtenerServicio();

        if ("/pedidos".equals(ruta)) {

            Usuario cliente =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_CLIENTE
                    );

            if (cliente == null) {
                return;
            }

            request.setAttribute(
                    "pedidos",
                    service.listarPorCliente(
                            cliente.getIdUsuario()
                    )
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/pedidos/lista.jsp"
            ).forward(request, response);

            return;
        }

        if ("/pedidos/recibidos".equals(ruta)) {

            Usuario emprendedor =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_EMPRENDEDOR
                    );

            if (emprendedor == null) {
                return;
            }

            request.setAttribute(
                    "pedidos",
                    service.listarRecibidos(
                            emprendedor.getIdUsuario()
                    )
            );

            request.setAttribute(
                    "modoRecibidos",
                    true
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/pedidos/lista.jsp"
            ).forward(request, response);

            return;
        }

        if ("/pedidos/detalle".equals(ruta)) {

            Usuario usuario =
                    requerirAutenticacion(
                            request,
                            response
                    );

            if (usuario == null) {
                return;
            }

            Integer idPedido =
                    obtenerEntero(
                            request.getParameter("id")
                    );

            if (idPedido == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                return;
            }

            Pedido pedido =
                    service.buscarVisibleParaUsuario(
                            idPedido,
                            usuario.getIdUsuario()
                    );

            if (pedido == null) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );

                return;
            }

            request.setAttribute(
                    "pedido",
                    pedido
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/pedidos/detalle.jsp"
            ).forward(request, response);

            return;
        }

        if ("/pedidos/crear".equals(ruta)) {

            Usuario cliente =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_CLIENTE
                    );

            if (cliente == null) {
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/catalogo"
            );

            return;
        }

        response.sendError(
                HttpServletResponse.SC_NOT_FOUND
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String ruta =
                request.getServletPath();

        PedidoService service =
                obtenerServicio();

        if ("/pedidos/crear".equals(ruta)) {

            Usuario cliente =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_CLIENTE
                    );

            if (cliente == null) {
                return;
            }

            Integer idEmprendimiento =
                    obtenerEntero(
                            request.getParameter(
                                    "idEmprendimiento"
                            )
                    );

            String[] idsPublicacion =
                    request.getParameterValues(
                            "idPublicacion"
                    );

            String[] cantidades =
                    request.getParameterValues(
                            "cantidad"
                    );

            String observaciones =
                    limpiar(
                            request.getParameter(
                                    "observaciones"
                            )
                    );

            if (idEmprendimiento == null
                    || idsPublicacion == null
                    || cantidades == null
                    || idsPublicacion.length == 0
                    || idsPublicacion.length
                    != cantidades.length) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                return;
            }

            List<Integer> publicaciones =
                    new ArrayList<>();

            List<Integer> cantidadesPedido =
                    new ArrayList<>();

            for (int i = 0;
                 i < idsPublicacion.length;
                 i++) {

                Integer idPublicacion =
                        obtenerEntero(
                                idsPublicacion[i]
                        );

                Integer cantidad =
                        obtenerEntero(
                                cantidades[i]
                        );

                if (idPublicacion == null
                        || cantidad == null
                        || cantidad <= 0) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Las cantidades del pedido son inválidas."
                    );

                    return;
                }

                publicaciones.add(
                        idPublicacion
                );

                cantidadesPedido.add(
                        cantidad
                );
            }

            service.crear(
                    cliente.getIdUsuario(),
                    idEmprendimiento,
                    publicaciones,
                    cantidadesPedido,
                    observaciones
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/pedidos"
            );

            return;
        }

        if ("/pedidos/estado".equals(ruta)) {

            Usuario emprendedor =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_EMPRENDEDOR
                    );

            if (emprendedor == null) {
                return;
            }

            Integer idPedido =
                    obtenerEntero(
                            request.getParameter("id")
                    );

            EstadoPedido estado =
                    obtenerEstado(
                            request.getParameter("estado")
                    );

            if (idPedido == null
                    || estado == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                return;
            }

            service.actualizarEstado(
                    emprendedor.getIdUsuario(),
                    idPedido,
                    estado
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/pedidos/recibidos"
            );

            return;
        }

        response.sendError(
                HttpServletResponse.SC_NOT_FOUND
        );
    }

    private Usuario requerirAutenticacion(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath() + "/auth"
            );

            return null;
        }

        Object valor =
                session.getAttribute(SESION_USUARIO);

        if (!(valor instanceof Usuario usuario)) {

            response.sendRedirect(
                    request.getContextPath() + "/auth"
            );

            return null;
        }

        return usuario;
    }

    private Usuario requerirRol(
            HttpServletRequest request,
            HttpServletResponse response,
            TipoRol rol)
            throws IOException {

        Usuario usuario =
                requerirAutenticacion(
                        request,
                        response
                );

        if (usuario == null) {
            return null;
        }

        if (usuario.getRol() == null
                || usuario.getRol().getNombre()
                != rol) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return null;
        }

        return usuario;
    }

    private PedidoService obtenerServicio()
            throws ServletException {

        Object servicio =
                getServletContext().getAttribute(SERVICIO);

        if (!(servicio instanceof PedidoService pedidoService)) {

            throw new ServletException(
                    "PedidoService no está configurado."
            );
        }

        return pedidoService;
    }

    private Integer obtenerEntero(String valor) {

        try {

            return Integer.valueOf(valor);

        } catch (NumberFormatException | NullPointerException e) {

            return null;
        }
    }

    private EstadoPedido obtenerEstado(String valor) {

        try {

            return EstadoPedido.valueOf(valor);

        } catch (IllegalArgumentException | NullPointerException e) {

            return null;
        }
    }

    private String limpiar(String valor) {

        return valor == null
                ? null
                : valor.trim();
    }
}