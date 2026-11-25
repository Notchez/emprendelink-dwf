package emprendelink.web.rest.error;

import emprendelink.dto.ApiErrorDTO;
import emprendelink.spring.config.SpringRuntime;
import emprendelink.spring.rest.ApiErrorPolicy;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiHttpExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Context private ServletContext servlet;
    private final ApiErrorPolicy suppliedPolicy;
    public ApiHttpExceptionMapper() { this(null); }
    ApiHttpExceptionMapper(ApiErrorPolicy policy) { this.suppliedPolicy = policy; }

    @Override public Response toResponse(WebApplicationException exception) {
        Response original = exception.getResponse();
        int status = original.getStatus();
        ApiErrorPolicy policy = suppliedPolicy != null ? suppliedPolicy : SpringRuntime.bean(servlet, ApiErrorPolicy.class);
        ApiErrorDTO error = policy.describe(status);
        if (exception instanceof ApiBusinessException business && status >= 400 && status < 500) {
            error.setMessage(business.getPublicMessage());
        }
        return Response.fromResponse(original).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
}
