package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Publicacion;
import emprendelink.persistence.jpa.entity.PublicacionEntity;

public final class PublicacionMapper {

    private PublicacionMapper() {
    }

    public static PublicacionEntity aEntidad(Publicacion publicacion) {

        if (publicacion == null) {
            return null;
        }

        PublicacionEntity entidad = new PublicacionEntity();

        entidad.setIdPublicacion(publicacion.getIdPublicacion());
        entidad.setEmprendimiento(
                EmprendimientoMapper.aEntidad(
                        publicacion.getEmprendimiento()
                )
        );
        entidad.setCategoria(
                CategoriaMapper.aEntidad(
                        publicacion.getCategoria()
                )
        );
        entidad.setTipo(publicacion.getTipo());
        entidad.setNombre(publicacion.getNombre());
        entidad.setDescripcion(publicacion.getDescripcion());
        entidad.setPrecio(publicacion.getPrecio());
        entidad.setStock(publicacion.getStock());
        entidad.setActivo(publicacion.isActivo());
        entidad.setFechaPublicacion(publicacion.getFechaPublicacion());

        return entidad;
    }

    public static Publicacion aDominio(PublicacionEntity entidad) {

        if (entidad == null) {
            return null;
        }

        return new Publicacion(
                entidad.getIdPublicacion(),
                EmprendimientoMapper.aDominio(
                        entidad.getEmprendimiento()
                ),
                CategoriaMapper.aDominio(
                        entidad.getCategoria()
                ),
                entidad.getTipo(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getPrecio(),
                entidad.getStock(),
                entidad.isActivo(),
                entidad.getFechaPublicacion()
        );
    }
}
