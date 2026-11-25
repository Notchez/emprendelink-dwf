package emprendelink.spring.security;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

/** Spring-managed application policy; the injected rules are shared by both interfaces. */
public class AccessPolicy {
    public static final String USER_KEY = "usuarioAutenticado";
    private final RouteRules rules;
    public AccessPolicy(RouteRules rules) { this.rules = rules; }
    public boolean publicApi(String method, String path) { return rules.publicApi(method, path); }
    public boolean allowedApi(String method, String path, Usuario user) { return rules.allowedApi(method, path, role(user)); }
    public boolean needsCsrf(String method) { return !rules.read(method) && !"OPTIONS".equals(method); }
    public RouteRules.WebDecision web(String path, Usuario user) { return rules.web(path, role(user)); }
    public static TipoRol role(Usuario user) {
        return user != null && user.isActivo() && user.getRol() != null ? user.getRol().getNombre() : null;
    }
    /** Refresh from CDI-managed service; never trust cached authorization or expose a hash. */
    public static Usuario current(HttpSession session, UsuarioService users) {
        if (session == null || !(session.getAttribute(USER_KEY) instanceof Usuario old) || old.getIdUsuario() == null) return null;
        Usuario current = users.buscarPorId(old.getIdUsuario());
        if (role(current) == null || !old.getIdUsuario().equals(current.getIdUsuario())) return null;
        current.setContrasenaHash(null);
        return current;
    }
}
