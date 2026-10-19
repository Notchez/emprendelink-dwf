package emprendelink.web.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @GET
    public Response health() {
        return Response.ok(
                Map.of(
                        "status", "UP",
                        "application", "EmprendeLink",
                        "phase", "Fase 3",
                        "apiVersion", "v1"
                )
        ).build();
    }
}