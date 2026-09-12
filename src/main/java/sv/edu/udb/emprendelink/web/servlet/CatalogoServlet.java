package sv.edu.udb.emprendelink.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sv.edu.udb.emprendelink.model.Publicacion;
import sv.edu.udb.emprendelink.service.PublicacionService;

import java.io.IOException;

@WebServlet({"/catalogo", "/catalogo/publicacion"})
public class CatalogoServlet extends HttpServlet {

    private static final String SERVICIO = "publicacionService";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        PublicacionService service = obtenerServicio();

        String ruta = request.getServletPath();

        if ("/catalogo".equals(ruta)) {

            request.setAttribute(
                    "publicaciones",
                    service.listarActivas()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/catalogo/lista.jsp"
            ).forward(request, response);

            return;
        }

        if ("/catalogo/publicacion".equals(ruta)) {

            Integer id = obtenerId(request);

            if (id == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "El identificador de la publicación es inválido."
                );

                return;
            }

            Publicacion publicacion =
                    service.buscarActivaPorId(id);

            if (publicacion == null) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );

                return;
            }

            request.setAttribute(
                    "publicacion",
                    publicacion
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/catalogo/detalle.jsp"
            ).forward(request, response);

            return;
        }

        response.sendError(
                HttpServletResponse.SC_NOT_FOUND
        );
    }

    private Integer obtenerId(HttpServletRequest request) {

        try {

            return Integer.valueOf(
                    request.getParameter("id")
            );

        } catch (NumberFormatException | NullPointerException e) {

            return null;
        }
    }

    private PublicacionService obtenerServicio()
            throws ServletException {

        Object servicio =
                getServletContext().getAttribute(SERVICIO);

        if (!(servicio instanceof PublicacionService publicacionService)) {

            throw new ServletException(
                    "PublicacionService no está configurado."
            );
        }

        return publicacionService;
    }
}