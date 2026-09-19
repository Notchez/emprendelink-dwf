package sv.edu.udb.emprendelink.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.UsuarioService;

import java.io.IOException;

@WebServlet({"/registro", "/admin/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private static final String SESION_USUARIO =
            "usuarioAutenticado";

    private static final String SERVICIO =
            "usuarioService";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String ruta =
                request.getServletPath();

        if ("/registro".equals(ruta)) {

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/registro.jsp"
            ).forward(request, response);

            return;
        }

        if ("/admin/usuarios".equals(ruta)) {

            Usuario administrador =
                    requerirAdministrador(
                            request,
                            response
                    );

            if (administrador == null) {
                return;
            }

            UsuarioService service =
                    obtenerServicio();

            request.setAttribute(
                    "usuarios",
                    service.listarTodos()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/usuarios.jsp"
            ).forward(request, response);

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

        if ("/registro".equals(ruta)) {

            registrar(
                    request,
                    response
            );

            return;
        }

        if ("/admin/usuarios".equals(ruta)) {

            administrarUsuario(
                    request,
                    response
            );

            return;
        }

        response.sendError(
                HttpServletResponse.SC_NOT_FOUND
        );
    }

    private void registrar(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String nombre =
                limpiar(
                        request.getParameter("nombre")
                );

        String apellido =
                limpiar(
                        request.getParameter("apellido")
                );

        String correo =
                limpiar(
                        request.getParameter("correo")
                );

        String contrasena =
                request.getParameter("contrasena");

        String telefono =
                limpiar(
                        request.getParameter("telefono")
                );

        if (esVacio(nombre)
                || esVacio(apellido)
                || esVacio(correo)
                || esVacio(contrasena)) {

            request.setAttribute(
                    "error",
                    "Complete todos los campos obligatorios."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/auth/registro.jsp"
            ).forward(request, response);

            return;
        }

        TipoRol rol =
                TipoRol.ROLE_CLIENTE;

        String rolSolicitado =
                request.getParameter("rol");

        if (TipoRol.ROLE_EMPRENDEDOR.name()
                .equals(rolSolicitado)) {

            rol =
                    TipoRol.ROLE_EMPRENDEDOR;
        }

        UsuarioService service =
                obtenerServicio();

        service.registrar(
                nombre,
                apellido,
                correo,
                contrasena,
                telefono,
                rol
        );

        response.sendRedirect(
                request.getContextPath()
                        + "/auth?registro=exitoso"
        );
    }

    private void administrarUsuario(
            HttpServletRequest request,
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

        Integer idUsuario =
                obtenerEntero(
                        request.getParameter("id")
                );

        String activoParametro =
                request.getParameter("activo");

        if (idUsuario == null
                || activoParametro == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            return;
        }

        boolean activo =
                Boolean.parseBoolean(
                        activoParametro
                );

        UsuarioService service =
                obtenerServicio();

        service.cambiarEstado(
                idUsuario,
                activo
        );

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/usuarios"
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

    private UsuarioService obtenerServicio()
            throws ServletException {

        Object servicio =
                getServletContext().getAttribute(SERVICIO);

        if (!(servicio instanceof UsuarioService usuarioService)) {

            throw new ServletException(
                    "UsuarioService no está configurado."
            );
        }

        return usuarioService;
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