package sv.edu.udb.emprendelink.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sv.edu.udb.emprendelink.model.Publicacion;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoPublicacion;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.CategoriaService;
import sv.edu.udb.emprendelink.service.EmprendimientoService;
import sv.edu.udb.emprendelink.service.PublicacionService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet({
        "/publicaciones",
        "/publicaciones/nueva",
        "/publicaciones/editar",
        "/admin/publicaciones"
})
public class PublicacionServlet extends HttpServlet {

    private static final String SESION_USUARIO =
            "usuarioAutenticado";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String ruta =
                request.getServletPath();

        if ("/admin/publicaciones".equals(ruta)) {

            Usuario administrador =
                    requerirRol(
                            request,
                            response,
                            TipoRol.ROLE_ADMIN
                    );

            if (administrador == null) {
                return;
            }

            PublicacionService service =
                    publicacionService();

            request.setAttribute(
                    "publicaciones",
                    service.listarTodas()
            );

            request.setAttribute(
                    "modoAdmin",
                    true
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/publicaciones/lista.jsp"
            ).forward(request, response);

            return;
        }

        Usuario usuario =
                requerirRol(
                        request,
                        response,
                        TipoRol.ROLE_EMPRENDEDOR
                );

        if (usuario == null) {
            return;
        }

        PublicacionService service =
                publicacionService();

        if ("/publicaciones".equals(ruta)) {

            request.setAttribute(
                    "publicaciones",
                    service.listarPorPropietario(
                            usuario.getIdUsuario()
                    )
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/publicaciones/lista.jsp"
            ).forward(request, response);

            return;
        }

        if ("/publicaciones/nueva".equals(ruta)) {

            cargarDatosFormulario(
                    request,
                    usuario
            );

            request.setAttribute(
                    "modo",
                    "crear"
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/publicaciones/formulario.jsp"
            ).forward(request, response);

            return;
        }

        if ("/publicaciones/editar".equals(ruta)) {

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

            Publicacion publicacion =
                    service.buscarPropiaPorId(
                            id,
                            usuario.getIdUsuario()
                    );

            if (publicacion == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );
                return;
            }

            cargarDatosFormulario(
                    request,
                    usuario
            );

            request.setAttribute(
                    "publicacion",
                    publicacion
            );

            request.setAttribute(
                    "modo",
                    "editar"
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/publicaciones/formulario.jsp"
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

        if ("/admin/publicaciones".equals(ruta)) {

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

            publicacionService().cambiarEstado(
                    administrador.getIdUsuario(),
                    id,
                    Boolean.parseBoolean(
                            activoParametro
                    )
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/publicaciones"
            );

            return;
        }

        Usuario usuario =
                requerirRol(
                        request,
                        response,
                        TipoRol.ROLE_EMPRENDEDOR
                );

        if (usuario == null) {
            return;
        }

        Integer idEmprendimiento =
                obtenerEntero(
                        request.getParameter(
                                "idEmprendimiento"
                        )
                );

        Integer idCategoria =
                obtenerEntero(
                        request.getParameter(
                                "idCategoria"
                        )
                );

        TipoPublicacion tipo =
                obtenerTipo(
                        request.getParameter("tipo")
                );

        String nombre =
                limpiar(
                        request.getParameter("nombre")
                );

        String descripcion =
                limpiar(
                        request.getParameter("descripcion")
                );

        BigDecimal precio =
                obtenerDecimal(
                        request.getParameter("precio")
                );

        Integer stock =
                obtenerEnteroOpcional(
                        request.getParameter("stock")
                );

        if (idEmprendimiento == null
                || idCategoria == null
                || tipo == null
                || esVacio(nombre)
                || precio == null
                || precio.signum() < 0
                || (stock != null && stock < 0)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Los datos de la publicación son inválidos."
            );

            return;
        }

        PublicacionService service =
                publicacionService();

        if ("/publicaciones/nueva".equals(ruta)) {

            service.crear(
                    usuario.getIdUsuario(),
                    idEmprendimiento,
                    idCategoria,
                    tipo,
                    nombre,
                    descripcion,
                    precio,
                    stock
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/publicaciones"
            );

            return;
        }

        if ("/publicaciones/editar".equals(ruta)) {

            Integer idPublicacion =
                    obtenerEntero(
                            request.getParameter("id")
                    );

            if (idPublicacion == null) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                );
                return;
            }

            service.actualizar(
                    usuario.getIdUsuario(),
                    idPublicacion,
                    idEmprendimiento,
                    idCategoria,
                    tipo,
                    nombre,
                    descripcion,
                    precio,
                    stock
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/publicaciones"
            );

            return;
        }

        response.sendError(
                HttpServletResponse.SC_NOT_FOUND
        );
    }

    private void cargarDatosFormulario(
            HttpServletRequest request,
            Usuario usuario)
            throws ServletException {

        request.setAttribute(
                "emprendimientos",
                emprendimientoService()
                        .listarPorPropietario(
                                usuario.getIdUsuario()
                        )
        );

        request.setAttribute(
                "categorias",
                categoriaService()
                        .listarActivas()
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

    private PublicacionService publicacionService()
            throws ServletException {

        return obtenerServicio(
                "publicacionService",
                PublicacionService.class
        );
    }

    private EmprendimientoService emprendimientoService()
            throws ServletException {

        return obtenerServicio(
                "emprendimientoService",
                EmprendimientoService.class
        );
    }

    private CategoriaService categoriaService()
            throws ServletException {

        return obtenerServicio(
                "categoriaService",
                CategoriaService.class
        );
    }

    private <T> T obtenerServicio(
            String nombre,
            Class<T> tipo)
            throws ServletException {

        Object servicio =
                getServletContext().getAttribute(nombre);

        if (!tipo.isInstance(servicio)) {
            throw new ServletException(
                    nombre + " no está configurado."
            );
        }

        return tipo.cast(servicio);
    }

    private Integer obtenerEntero(String valor) {

        try {
            return Integer.valueOf(valor);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private Integer obtenerEnteroOpcional(String valor) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return obtenerEntero(valor);
    }

    private BigDecimal obtenerDecimal(String valor) {

        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private TipoPublicacion obtenerTipo(String valor) {

        try {
            return TipoPublicacion.valueOf(valor);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}