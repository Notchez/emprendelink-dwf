package emprendelink.web.rest.error;

import emprendelink.spring.config.SpringRuntime;
import emprendelink.spring.rest.ApiErrorPolicy;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ApiUnexpectedExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger LOGGER = Logger.getLogger(ApiUnexpectedExceptionMapper.class.getName());
    @Context private ServletContext servlet;
    private final ApiErrorPolicy suppliedPolicy;
    public ApiUnexpectedExceptionMapper() { this(null); }
    ApiUnexpectedExceptionMapper(ApiErrorPolicy policy) { this.suppliedPolicy = policy; }

    @Override public Response toResponse(Exception exception) {
        LOGGER.log(Level.SEVERE, "Error inesperado en la API REST.", exception);
        ApiErrorPolicy policy = suppliedPolicy != null ? suppliedPolicy : SpringRuntime.bean(servlet, ApiErrorPolicy.class);
        return Response.status(500).type(MediaType.APPLICATION_JSON).entity(policy.describe(500)).build();
    }
}
