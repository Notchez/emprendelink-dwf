
package emprendelink.web.rest.error;

import emprendelink.dto.ApiErrorDTO;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiHttpExceptionMapper
        implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(
            WebApplicationException exception) {

        Response original = exception.getResponse();

        int status = original.getStatus();

        String error = obtenerNombreHttp(status);

        String message = obtenerMensajeSeguro(
                exception,
                status
        );

        ApiErrorDTO respuesta = new ApiErrorDTO(
                status,
                error,
                message
        );

        return Response.fromResponse(original)
                .type(MediaType.APPLICATION_JSON)
                .entity(respuesta)
                .build();
    }

    private String obtenerNombreHttp(int status) {

        Response.Status conocido =
                Response.Status.fromStatusCode(status);

        if (conocido != null) {
            return conocido.getReasonPhrase();
        }

        return "HTTP Error";
    }

    private String obtenerMensajeSeguro(
            WebApplicationException exception,
            int status) {

        // Solo los errores explicitamente definidos
        // para la API pueden presentar un mensaje
        // de negocio personalizado.
        if (exception instanceof ApiBusinessException negocio
                && status >= 400
                && status < 500) {

            return negocio.getPublicMessage();
        }

        return switch (status) {

            case 400 ->
                    "La solicitud contiene datos invalidos.";

            case 401 ->
                    "Es necesario iniciar sesion.";

            case 403 ->
                    "No tienes permiso para realizar esta operacion.";

            case 404 ->
                    "Recurso no encontrado.";

            case 405 ->
                    "Metodo HTTP no permitido.";

            case 406 ->
                    "El formato solicitado no es compatible.";

            case 409 ->
                    "Conflicto al procesar la operacion.";

            case 415 ->
                    "El tipo de contenido no es compatible.";

            default ->
                    status >= 500
                            ? "No fue posible completar la solicitud."
                            : "No se pudo procesar la solicitud.";
        };
    }
}
