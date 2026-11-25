package emprendelink.security;

import emprendelink.model.Usuario;
import emprendelink.service.UsuarioService;
import emprendelink.spring.config.SpringRuntime;
import emprendelink.spring.security.AccessPolicy;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/inicio.xhtml", "/login.xhtml", "/registro.xhtml", "/catalogo/*", "/admin/*",
        "/carrito/*", "/perfil/*", "/pedidos/*", "/emprendimientos/*", "/publicaciones/*", "/ventas/*", "/comisiones/*"})
public class WebAccessFilter implements Filter {
    @Inject private UsuarioService users;

    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        AccessPolicy policy = SpringRuntime.bean(request.getServletContext(), AccessPolicy.class);
        String path = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        if (!"GET".equals(request.getMethod()) && !"HEAD".equals(request.getMethod())
                && "cross-site".equalsIgnoreCase(request.getHeader("Sec-Fetch-Site"))) {
            response.sendError(403);
            return;
        }
        HttpSession session = request.getSession(false);
        Usuario user = null;
        if (session != null && session.getAttribute(AccessPolicy.USER_KEY) instanceof Usuario old) {
            user = AccessPolicy.current(session, users);
            if (user == null || AccessPolicy.role(old) != AccessPolicy.role(user)) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login.xhtml");
                return;
            }
            session.setAttribute(AccessPolicy.USER_KEY, user);
        }
        if (user != null) response.setHeader("Cache-Control", "no-store");
        switch (policy.web(path, user)) {
            case ALLOW -> chain.doFilter(req, res);
            case LOGIN -> response.sendRedirect(request.getContextPath() + "/login.xhtml");
            case FORBIDDEN -> response.sendError(403);
            case ADMIN_HOME -> response.sendRedirect(request.getContextPath() + "/admin/inicio.xhtml");
            case ENTREPRENEUR_HOME -> response.sendRedirect(request.getContextPath() + "/pedidos/recibidos.xhtml");
        }
    }
}
