package emprendelink.web.rest.security;

import emprendelink.model.Usuario;
import emprendelink.service.UsuarioService;
import emprendelink.spring.config.SpringRuntime;
import emprendelink.spring.security.AccessPolicy;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ApiAuthenticationFilter implements ContainerRequestFilter {
    @Context private HttpServletRequest solicitud;
    @Inject private UsuarioService usuarioService;
    private final AccessPolicy suppliedPolicy;

    public ApiAuthenticationFilter() { this(null); }
    ApiAuthenticationFilter(AccessPolicy policy) { this.suppliedPolicy = policy; }

    @Override public void filter(ContainerRequestContext contexto) {
        AccessPolicy policy = suppliedPolicy != null ? suppliedPolicy :
                SpringRuntime.bean(solicitud.getServletContext(), AccessPolicy.class);
        String metodo = contexto.getMethod();
        String ruta = contexto.getUriInfo().getPath(false);
        if (policy.publicApi(metodo, ruta)) return;
        Usuario usuario = ApiSessionSecurity.requerirUsuario(solicitud, usuarioService);
        solicitud.setAttribute(ApiSessionSecurity.USUARIO_ACTUAL, usuario);
        if (policy.needsCsrf(metodo)) ApiSessionSecurity.requerirTokenCsrf(solicitud);
        if (!policy.allowedApi(metodo, ruta, usuario)) throw new ForbiddenException();
    }
}
