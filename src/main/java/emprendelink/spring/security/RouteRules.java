package emprendelink.spring.security;

import emprendelink.model.enums.TipoRol;
import static emprendelink.model.enums.TipoRol.*;

/** Stateless rules shared by JSF and REST; resource ownership remains in Services. */
public class RouteRules {
    public enum WebDecision { ALLOW, LOGIN, FORBIDDEN, ADMIN_HOME, ENTREPRENEUR_HOME }

    public boolean read(String method) { return "GET".equals(method) || "HEAD".equals(method); }
    public boolean publicApi(String method, String path) {
        if (read(method) && "health".equals(path)) return true;
        if ("POST".equals(method) && "auth/login".equals(path)) return true;
        return read(method) && ("emprendimientos".equals(path) || "publicaciones".equals(path)
                || path.matches("emprendimientos/-?[0-9]+(?:/publicaciones)?")
                || path.matches("publicaciones/-?[0-9]+"));
    }
    public boolean allowedApi(String method, String path, TipoRol role) {
        if (publicApi(method, path)) return true;
        if (role == null) return false;
        if (read(method) && "auth/me".equals(path) || "POST".equals(method) && "auth/logout".equals(path)) return true;
        if (read(method) && "pedidos".equals(path)) return role == ROLE_CLIENTE || role == ROLE_EMPRENDEDOR;
        if (read(method) && path.matches("pedidos/[0-9]+")) return true;
        if ("POST".equals(method) && "pedidos/checkout".equals(path)) return role == ROLE_CLIENTE;
        if ("PUT".equals(method) && path.matches("pedidos/[0-9]+/estado")) return role == ROLE_EMPRENDEDOR;
        return false;
    }
    public WebDecision web(String path, TipoRol role) {
        if (path.startsWith("/catalogo/")) {
            if (role == ROLE_ADMIN) return WebDecision.ADMIN_HOME;
            if (role == ROLE_EMPRENDEDOR) return WebDecision.ENTREPRENEUR_HOME;
            return WebDecision.ALLOW;
        }
        if (path.equals("/login.xhtml") || path.equals("/registro.xhtml")) return WebDecision.ALLOW;
        if (role == null) return WebDecision.LOGIN;
        if (path.equals("/inicio.xhtml")) return WebDecision.ALLOW;
        if (path.startsWith("/admin/")) return decision(role == ROLE_ADMIN);
        if (path.startsWith("/carrito/") || path.startsWith("/perfil/")) return decision(role == ROLE_CLIENTE);
        if (path.startsWith("/emprendimientos/") || path.startsWith("/publicaciones/")
                || path.startsWith("/ventas/") || path.startsWith("/comisiones/")) return decision(role == ROLE_EMPRENDEDOR);
        if (path.equals("/pedidos/detalle.xhtml")) return WebDecision.ALLOW;
        if (path.equals("/pedidos/nuevo.xhtml") || path.equals("/pedidos/mis-pedidos.xhtml")) return decision(role == ROLE_CLIENTE);
        if (path.equals("/pedidos/recibidos.xhtml")) return decision(role == ROLE_EMPRENDEDOR);
        return WebDecision.FORBIDDEN;
    }
    private WebDecision decision(boolean allowed) { return allowed ? WebDecision.ALLOW : WebDecision.FORBIDDEN; }
}
