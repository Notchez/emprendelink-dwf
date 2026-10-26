package emprendelink.rest;

import emprendelink.dto.EmprendimientoDTO;
import emprendelink.dto.PublicacionDTO;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Publicacion;
import emprendelink.service.EmprendimientoService;
import emprendelink.service.PublicacionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/emprendimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmprendimientoResource {

    @Inject
    private EmprendimientoService emprendimientoService;

    @Inject
    private PublicacionService publicacionService;

    @GET
    public Response obtenerEmprendimientos() {
        List<Emprendimiento> activos = emprendimientoService.listarActivos();
        List<EmprendimientoDTO> lista = activos.stream()
                .map(this::convertirAEmprendimientoDTO)
                .collect(Collectors.toList());
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerEmprendimientoPorId(@PathParam("id") Integer id) {
        Emprendimiento emprendimiento = emprendimientoService.buscarActivoPorId(id);
        if (emprendimiento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertirAEmprendimientoDTO(emprendimiento)).build();
    }

    @GET
    @Path("/{id}/publicaciones")
    public Response obtenerPublicacionesDeEmprendimiento(@PathParam("id") Integer id) {
        List<PublicacionDTO> publicaciones = publicacionService.listarActivas().stream()
                .filter(p -> p.getEmprendimiento() != null && p.getEmprendimiento().getIdEmprendimiento().equals(id))
                .map(this::convertirAPublicacionDTO)
                .collect(Collectors.toList());
        return Response.ok(publicaciones).build();
    }

    private EmprendimientoDTO convertirAEmprendimientoDTO(Emprendimiento e) {
        EmprendimientoDTO dto = new EmprendimientoDTO();
        dto.setId(Long.valueOf(e.getIdEmprendimiento()));
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        return dto;
    }

    private PublicacionDTO convertirAPublicacionDTO(Publicacion p) {
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(Long.valueOf(p.getIdPublicacion()));
        dto.setTitulo(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setEmprendimientoId(Long.valueOf(p.getEmprendimiento().getIdEmprendimiento()));
        return dto;
    }
}