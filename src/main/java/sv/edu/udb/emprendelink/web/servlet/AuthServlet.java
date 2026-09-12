package sv.edu.udb.emprendelink.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.AutenticacionService;

import java.io.IOException;

@WebServlet({"/auth", "/logout"})
public class AuthServlet extends HttpServlet {

    private static final String SESION_USUARIO = "usuarioAutenticado";
    private static final String SERVICIO = "autenticacionService";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if ("/logout".equals(request.getServletPath())) {
            cerrarSesion(request, response);
            return;
        }

        Usuario usuario = obtenerUsuarioSesion(request);

        if (usuario != null) {
            redirigirSegunRol(request, response, usuario);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if ("/logout".equals(request.getServletPath())) {
            cerrarSesion(request, response);
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String correo = limpiar(request.getParameter("correo"));
        String contrasena = request.getParameter("contrasena");

        if (correo == null || correo.isBlank()
                || contrasena == null || contrasena.isBlank()) {

            request.setAttribute(
                    "error",
                    "Correo y contraseña son obligatorios."
            );

            request.setAttribute("correo", correo);

            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
                    .forward(request, response);

            return;
        }

        AutenticacionService service = obtenerServicio();

        Usuario usuario = service.autenticar(correo, contrasena);

        if (usuario == null) {

            request.setAttribute(
                    "error",
                    "Correo o contraseña incorrectos."
            );

            request.setAttribute("correo", correo);

            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
                    .forward(request, response);

            return;
        }

        HttpSession sesionAnterior = request.getSession(false);

        if (sesionAnterior != null) {
            sesionAnterior.invalidate();
        }

        HttpSession sesion = request.getSession(true);

        sesion.setAttribute(
                SESION_USUARIO,
                usuario
        );

        redirigirSegunRol(
                request,
                response,
                usuario
        );
    }

    private void cerrarSesion(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(
                request.getContextPath() + "/auth"
        );
    }

    private void redirigirSegunRol(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Usuario usuario)
            throws IOException {

        TipoRol rol = usuario.getRol() != null
                ? usuario.getRol().getNombre()
                : null;

        String destino = "/catalogo";

        if (rol == TipoRol.ROLE_ADMIN) {

            destino = "/admin/usuarios";

        } else if (rol == TipoRol.ROLE_EMPRENDEDOR) {

            destino = "/emprendimientos/gestion";
        }

        response.sendRedirect(
                request.getContextPath() + destino
        );
    }

    private Usuario obtenerUsuarioSesion(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object usuario =
                session.getAttribute(SESION_USUARIO);

        return usuario instanceof Usuario
                ? (Usuario) usuario
                : null;
    }

    private AutenticacionService obtenerServicio()
            throws ServletException {

        Object servicio =
                getServletContext().getAttribute(SERVICIO);

        if (!(servicio instanceof AutenticacionService autenticacionService)) {

            throw new ServletException(
                    "AutenticacionService no está configurado."
            );
        }

        return autenticacionService;
    }

    private String limpiar(String valor) {

        return valor == null
                ? null
                : valor.trim();
    }
}