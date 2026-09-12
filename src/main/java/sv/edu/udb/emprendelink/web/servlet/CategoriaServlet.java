package sv.edu.udb.emprendelink.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.CategoriaService;

import java.io.IOException;

@WebServlet("/admin/categorias")
public class CategoriaServlet extends HttpServlet {

    private static final String SESION_USUARIO =
            "usuarioAutenticado";

    private static final String SERVICIO =
            "categoriaService";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Usuario administrador =
                requerirAdministrador(
                        request,
                        response
                );

        if (administrador == null) {
            return;
        }

        CategoriaService service =
                obtenerServicio();

        request.setAttribute(
                "categorias",
                service.listarTodas()
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/categorias.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Usuario administrador =
                requerirAdministrador(
                        request,
                        response
                );

        if (administrador == null) {
            return;
        }

        String accion =
                request.getParameter("accion");

        CategoriaService service =
                obtenerServicio();

        if (accion == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            return;
        }

        switch (accion) {

            case "crear" -> {

                String nombre =
                        limpiar(
                                request.getParameter("nombre")
                        );

                String descripcion =
                        limpiar(
                                request.getParameter("descripcion")
                        );

                if (esVacio(nombre)) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "El nombre es obligatorio."
                    );

                    return;
                }

                service.crear(
                        nombre,
                        descripcion
                );
            }

            case "editar" -> {

                Integer id =
                        obtenerEntero(
                                request.getParameter("id")
                        );

                String nombre =
                        limpiar(
                                request.getParameter("nombre")
                        );

                String descripcion =
                        limpiar(
                                request.getParameter("descripcion")
                        );

                if (id == null || esVacio(nombre)) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST
                    );

                    return;
                }

                service.actualizar(
                        id,
                        nombre,
                        descripcion
                );
            }

            case "estado" -> {

                Integer id =
                        obtenerEntero(
                                request.getParameter("id")
                        );

                String activoParametro =
                        request.getParameter("activo");

                if (id == null
                        || activoParametro == null) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST
                    );

                    return;
                }

                service.cambiarEstado(
                        id,
                        Boolean.parseBoolean(
                                activoParametro
                        )
                );
            }

            default -> {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                return;
            }
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/categorias"
        );
    }

    private Usuario requerirAdministrador(
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

        if (usuario.getRol() == null
                || usuario.getRol().getNombre()
                != TipoRol.ROLE_ADMIN) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return null;
        }

        return usuario;
    }

    private CategoriaService obtenerServicio()
            throws ServletException {

        Object servicio =
                getServletContext().getAttribute(SERVICIO);

        if (!(servicio instanceof CategoriaService categoriaService)) {

            throw new ServletException(
                    "CategoriaService no está configurado."
            );
        }

        return categoriaService;
    }

    private Integer obtenerEntero(String valor) {

        try {

            return Integer.valueOf(valor);

        } catch (NumberFormatException | NullPointerException e) {

            return null;
        }
    }

    private boolean esVacio(String valor) {

        return valor == null
                || valor.isBlank();
    }

    private String limpiar(String valor) {

        return valor == null
                ? null
                : valor.trim();
    }
}