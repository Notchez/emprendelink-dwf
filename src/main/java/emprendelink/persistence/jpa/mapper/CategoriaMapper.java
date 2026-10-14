package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Categoria;
import emprendelink.persistence.jpa.entity.CategoriaEntity;

public class CategoriaMapper {

    public static Categoria toDomain(CategoriaEntity entity) {
        if (entity == null) return null;
        Categoria cat = new Categoria();
        cat.setIdCategoria(entity.getId());
        cat.setNombre(entity.getNombre());
        cat.setDescripcion(entity.getDescripcion());
        cat.setActivo(entity.isActivo());
        return cat;
    }

    public static CategoriaEntity toEntity(Categoria domain) {
        if (domain == null) return null;
        CategoriaEntity entity = new CategoriaEntity();
        entity.setId(domain.getIdCategoria());
        entity.setNombre(domain.getNombre());
        entity.setDescripcion(domain.getDescripcion());
        entity.setActivo(domain.isActivo());
        return entity;
    }
}