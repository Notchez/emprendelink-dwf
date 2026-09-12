package sv.edu.udb.emprendelink.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sv.edu.udb.emprendelink.model.Emprendimiento;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.EmprendimientoService;

import java.io.IOException;

@WebServlet({
        "/emprendimientos",
        "/emprendimientos/detalle",
        "/emprendimientos/gestion",
        "/emprendimientos/nuevo",
        "/emprendimientos/editar",
        "/admin/emprendimientos"
})
public class EmprendimientoServlet extends HttpServlet {

    private static final String SESION_USUARIO =
            "usuarioAutenticado";

    private static final String SERVICIO =
            "emprendimientoService";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String ruta =
                request.getServletPath();

        EmprendimientoService service =
                obtenerServicio();

        switch (ruta) {

            case "/emprendimientos" -> {

                request.setAttribute(
                        "emprendimientos",
                        service.listarActivos()
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/emprendimientos/lista.jsp"
                ).forward(request, response);
            }

            case "/emprendimientos/detalle" -> {

                Integer id =
                        obtenerEntero(
                                request.getParameter("id")
                        );

                if (id == null) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST
                    );

                    return;
                }

                Emprendimiento emprendimiento =
                        service.buscarActivoPorId(id);

                if (emprendimiento == null) {

                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND
                    );

                    return;
                }

                request.setAttribute(
                        "emprendimiento",
                        emprendimiento
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/emprendimientos/detalle.jsp"
                ).forward(request, response);
            }

            case "/emprendimientos/gestion" -> {

                Usuario usuario =
                        requerirRol(
                                request,
                                response,
                                TipoRol.ROLE_EMPRENDEDOR
                        );

                if (usuario == null) {
                    return;
                }

                request.setAttribute(
                        "emprendimientos",
                        service.listarPorPropietario(
                                usuario.getIdUsuario()
                        )
                );

                request.setAttribute(
                        "modoGestion",
                        true
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/emprendimientos/lista.jsp"
                ).forward(request, response);
            }

            case "/emprendimientos/nuevo" -> {

                Usuario usuario =
                        requerirRol(
                                request,
                                response,
                                TipoRol.ROLE_EMPRENDEDOR
                        );

                if (usuario == null) {
                    return;
                }

                request.setAttribute(
                        "modo",
                        "crear"
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/emprendimientos/formulario.jsp"
                ).forward(request, response);
            }

            case "/emprendimientos/editar" -> {

                Usuario usuario =
                        requerirRol(
                                request,
                                response,
                                TipoRol.ROLE_EMPRENDEDOR
                        );

                if (usuario == null) {
                    return;
                }

                Integer id =
                        obtenerEntero(
                                request.getParameter("id")
                        );

                if (id == null) {

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST
                    );

                    return;
                }

                Emprendimiento emprendimiento =
                        service.buscarPropioPorId(
                                id,
                                usuario.getIdUsuario()
                        );

                if (emprendimiento == null) {

                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND
                    );

                    return;
                }

                request.setAttribute(
                        "emprendimiento",
                        emprendimiento
                );

                request.setAttribute(
                        "modo",
                        "editar"
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/emprendimientos/formulario.jsp"
                ).forward(request, response);
            }

            case "/admin/emprendimientos" -> {

                Usuario administrador =
                        requerirRol(
                                request,
                                response,
                                TipoRol.ROLE_ADMIN
                        );

                if (administrador == null) {
                    return;
                }

                request.setAttribute(
                        "emprendimientos",
                        service.listarTodos()
                );

                request.setAttribute(
                        "modoAdmin",
                        true
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/emprendimientos/lista.jsp"
                ).forward(request, response);
            }

            default -> response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String ruta =
                request.getServletPath();

        EmprendimientoService service =
                obtenerServicio();

        if ("/emprendimientos/nuevo".equals(ruta)) {

            Usuario usuario =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_EMPRENDEDOR
                    );

            if (usuario == null) {
                return;
            }

            String nombre =
                    limpiar(
                            request.getParameter("nombre")
                    );

            String descripcion =
                    limpiar(
                            request.getParameter("descripcion")
                    );

            String contacto =
                    limpiar(
                            request.getParameter("contacto")
                    );

            if (esVacio(nombre)) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "El nombre es obligatorio."
                );

                return;
            }

            service.crear(
                    usuario.getIdUsuario(),
                    nombre,
                    descripcion,
                    contacto
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/emprendimientos/gestion"
            );

            return;
        }

        if ("/emprendimientos/editar".equals(ruta)) {

            Usuario usuario =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_EMPRENDEDOR
                    );

            if (usuario == null) {
                return;
            }

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

            String contacto =
                    limpiar(
                            request.getParameter("contacto")
                    );

            if (id == null || esVacio(nombre)) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                );

                return;
            }

            service.actualizar(
                    usuario.getIdUsuario(),
                    id,
                    nombre,
                    descripcion,
                    contacto
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/emprendimientos/gestion"
            );

            return;
        }

        if ("/admin/emprendimientos".equals(ruta)) {

            Usuario administrador =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_ADMIN
                    );

            if (administrador == null) {
                return;
            }

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
                    administrador.getIdUsuario(),
                    id,
                    Boolean.parseBoolean(
                            activoParametro
                    )
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/emprendimientos"
            );

            return;
        }

        response.sendError(
                HttpServletResponse.SC_NOT_FOUND
        );
    }

    private Usuario requerirRol(
            HttpServletRequest request,
            HttpServletResponse response,
            TipoRol rol)
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
                || usuario.getRol().getNombre() != rol) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return null;
        }

        return usuario;
    }

    private EmprendimientoService obtenerServicio()
            throws ServletException {

        Object servicio =
                getServletContext().getAttribute(SERVICIO);

        if (!(servicio instanceof EmprendimientoService emprendimientoService)) {

            throw new ServletException(
                    "EmprendimientoService no está configurado."
            );
        }

        return emprendimientoService;
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