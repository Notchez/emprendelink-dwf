package emprendelink.spring.rest;

import emprendelink.spring.config.SpringRuntime;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;

/** Handles status-only REST responses without changing successful bodies or business DTOs. */
@Provider
public class ApiResponseErrors implements ContainerResponseFilter {
    @Context private ServletContext servlet;
    @Override public void filter(ContainerRequestContext request, ContainerResponseContext response) {
        int status = response.getStatus();
        if (status >= 400 && !response.hasEntity() && !"HEAD".equals(request.getMethod())) {
            var errors = SpringRuntime.bean(servlet, ApiErrorPolicy.class);
            response.setEntity(errors.describe(status), new java.lang.annotation.Annotation[0], MediaType.APPLICATION_JSON_TYPE);
        }
        if (status >= 400) response.getHeaders().putSingle("Cache-Control", "no-store");
        response.getHeaders().putSingle("X-Content-Type-Options", "nosniff");
    }
}
