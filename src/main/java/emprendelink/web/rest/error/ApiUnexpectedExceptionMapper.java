
package emprendelink.web.rest.error;

import emprendelink.dto.ApiErrorDTO;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ApiUnexpectedExceptionMapper
        implements ExceptionMapper<Exception> {

    private static final Logger LOGGER =
            Logger.getLogger(
                    ApiUnexpectedExceptionMapper.class.getName()
            );

    @Override
    public Response toResponse(Exception exception) {

        LOGGER.log(
                Level.SEVERE,
                "Error inesperado en la API REST.",
                exception
        );

        ApiErrorDTO respuesta = new ApiErrorDTO(
                500,
                "Internal Server Error",
                "Ocurrio un error interno al procesar la solicitud."
        );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(respuesta)
                .build();
    }
}
