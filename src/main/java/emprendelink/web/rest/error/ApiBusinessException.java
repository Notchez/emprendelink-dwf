
package emprendelink.web.rest.error;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public final class ApiBusinessException
        extends WebApplicationException {

    private final String publicMessage;

    private ApiBusinessException(
            Response.Status status,
            String publicMessage) {

        super(Response.status(status).build());

        if (publicMessage == null
                || publicMessage.isBlank()) {

            throw new IllegalArgumentException(
                    "El mensaje publico es obligatorio."
            );
        }

        this.publicMessage = publicMessage;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

    public static ApiBusinessException badRequest(
            String mensaje) {

        return new ApiBusinessException(
                Response.Status.BAD_REQUEST,
                mensaje
        );
    }

    public static ApiBusinessException notFound(
            String mensaje) {

        return new ApiBusinessException(
                Response.Status.NOT_FOUND,
                mensaje
        );
    }

    public static ApiBusinessException conflict(
            String mensaje) {

        return new ApiBusinessException(
                Response.Status.CONFLICT,
                mensaje
        );
    }
}
