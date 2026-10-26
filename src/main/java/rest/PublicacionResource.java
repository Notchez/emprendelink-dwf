package emprendelink.rest;

import emprendelink.dto.PublicacionDTO;
import emprendelink.model.Publicacion;
import emprendelink.service.PublicacionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/publicaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PublicacionResource {

    @Inject
    private PublicacionService publicacionService;

    @GET
    public Response obtenerTodasLasPublicaciones() {
        List<Publicacion> activas = publicacionService.listarActivas();
        List<PublicacionDTO> lista = activas.stream()
                .map(this::convertirAPublicacionDTO)
                .collect(Collectors.toList());
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPublicacionPorId(@PathParam("id") Integer id) {
        Publicacion publicacion = publicacionService.buscarActivaPorId(id);
        if (publicacion == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertirAPublicacionDTO(publicacion)).build();
    }

    private PublicacionDTO convertirAPublicacionDTO(Publicacion p) {
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(Long.valueOf(p.getIdPublicacion()));
        dto.setTitulo(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        if (p.getEmprendimiento() != null) {
            dto.setEmprendimientoId(Long.valueOf(p.getEmprendimiento().getIdEmprendimiento()));
        }
        return dto;
    }
}