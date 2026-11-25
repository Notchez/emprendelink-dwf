package emprendelink.spring.rest;

import emprendelink.spring.config.SpringRuntime;
import jakarta.json.Json;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Servlet-container fallback: preserve HTML for JSF and JSON for API errors. */
public class ErrorDispatchServlet extends HttpServlet {
    @Override protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Object raw = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = raw instanceof Integer value ? value : 404;
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        String api = request.getContextPath() + "/api/v1";
        response.setStatus(status);
        response.setHeader("Cache-Control", "no-store");
        if (uri instanceof String path && (path.equals(api) || path.startsWith(api + "/"))) {
            var error = SpringRuntime.bean(request.getServletContext(), ApiErrorPolicy.class).describe(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (!"HEAD".equals(request.getMethod())) response.getWriter().write(Json.createObjectBuilder()
                    .add("status", error.getStatus()).add("error", error.getError()).add("message", error.getMessage()).build().toString());
            return;
        }
        String page = switch (status) { case 403 -> "403"; case 404 -> "404"; default -> "500"; };
        request.getRequestDispatcher("/error/" + page + ".html").forward(request, response);
    }
}
