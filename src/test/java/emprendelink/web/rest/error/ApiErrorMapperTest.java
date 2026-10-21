
package emprendelink.web.rest.error;

import emprendelink.dto.ApiErrorDTO;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorMapperTest {

    private final ApiHttpExceptionMapper httpMapper =
            new ApiHttpExceptionMapper();

    @Test
    void convierteError404AJson() {

        Response respuesta = httpMapper.toResponse(
                new NotFoundException()
        );

        assertEquals(404, respuesta.getStatus());

        ApiErrorDTO error =
                (ApiErrorDTO) respuesta.getEntity();

        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals(
                "Recurso no encontrado.",
                error.getMessage()
        );
    }

    @Test
    void ocultaMensajesInternosDeErroresHttp() {

        Response respuesta = httpMapper.toResponse(
                new BadRequestException(
                        "SQL interno: informacion sensible"
                )
        );

        ApiErrorDTO error =
                (ApiErrorDTO) respuesta.getEntity();

        assertEquals(400, respuesta.getStatus());

        assertFalse(
                error.getMessage().contains("SQL")
        );

        assertEquals(
                "La solicitud contiene datos invalidos.",
                error.getMessage()
        );
    }

    @Test
    void permiteMensajesDeNegocioControlados() {

        Response respuesta = httpMapper.toResponse(
                ApiBusinessException.conflict(
                        "No hay inventario suficiente."
                )
        );

        ApiErrorDTO error =
                (ApiErrorDTO) respuesta.getEntity();

        assertEquals(409, respuesta.getStatus());

        assertEquals(
                "Conflict",
                error.getError()
        );

        assertEquals(
                "No hay inventario suficiente.",
                error.getMessage()
        );
    }

    @Test
    void mantieneCodigo403ParaAccesosDenegados() {

        Response respuesta = httpMapper.toResponse(
                new ForbiddenException()
        );

        ApiErrorDTO error =
                (ApiErrorDTO) respuesta.getEntity();

        assertEquals(403, respuesta.getStatus());

        assertEquals(
                "No tienes permiso para realizar esta operacion.",
                error.getMessage()
        );
    }

    @Test
    void convierteExcepcionInesperadaEn500Seguro() {

        ApiUnexpectedExceptionMapper inesperado =
                new ApiUnexpectedExceptionMapper();

        Response respuesta = inesperado.toResponse(
                new IllegalStateException(
                        "Fallo SQL con informacion privada"
                )
        );

        ApiErrorDTO error =
                (ApiErrorDTO) respuesta.getEntity();

        assertEquals(500, respuesta.getStatus());

        assertEquals(
                "Internal Server Error",
                error.getError()
        );

        assertEquals(
                "Ocurrio un error interno al procesar la solicitud.",
                error.getMessage()
        );

        assertFalse(
                error.getMessage().contains("SQL")
        );
    }
}
