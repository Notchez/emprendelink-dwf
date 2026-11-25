package emprendelink.spring.rest;

import emprendelink.dto.ApiErrorDTO;
import jakarta.ws.rs.core.Response;

/** A single public error vocabulary; internal exceptions never become client messages. */
public class ApiErrorPolicy {
    public ApiErrorDTO describe(int status) {
        Response.Status known = Response.Status.fromStatusCode(status);
        String error = known == null ? "HTTP Error" : known.getReasonPhrase();
        String message = switch (status) {
            case 400 -> "La solicitud contiene datos invalidos.";
            case 401 -> "Es necesario iniciar sesion.";
            case 403 -> "No tienes permiso para realizar esta operacion.";
            case 404 -> "Recurso no encontrado.";
            case 405 -> "Metodo HTTP no permitido.";
            case 406 -> "El formato solicitado no es compatible.";
            case 409 -> "Conflicto al procesar la operacion.";
            case 415 -> "El tipo de contenido no es compatible.";
            case 500 -> "Ocurrio un error interno al procesar la solicitud.";
            default -> status >= 500 ? "No fue posible completar la solicitud." : "No se pudo procesar la solicitud.";
        };
        return new ApiErrorDTO(status, error, message);
    }
}
