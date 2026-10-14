package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Categoria;
import emprendelink.persistence.jpa.entity.CategoriaEntity;

public final class CategoriaMapper {

    private CategoriaMapper() {
    }

    public static CategoriaEntity aEntidad(
            Categoria categoria) {

        if (categoria == null) {
            return null;
        }

        CategoriaEntity entidad =
                new CategoriaEntity();

        entidad.setIdCategoria(
                categoria.getIdCategoria()
        );

        entidad.setNombre(
                categoria.getNombre()
        );

        entidad.setDescripcion(
                categoria.getDescripcion()
        );

        entidad.setActivo(
                categoria.isActivo()
        );

        return entidad;
    }

    public static Categoria aDominio(
            CategoriaEntity entidad) {

        if (entidad == null) {
            return null;
        }

        return new Categoria(
                entidad.getIdCategoria(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.isActivo()
        );
    }
}