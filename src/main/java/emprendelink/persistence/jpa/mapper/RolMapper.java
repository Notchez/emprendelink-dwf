package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Rol;
import emprendelink.persistence.jpa.entity.RolEntity;

public final class RolMapper {

    private RolMapper() {
    }

    public static Rol aDominio(
            RolEntity entidad) {

        if (entidad == null) {
            return null;
        }

        return new Rol(
                entidad.getIdRol(),
                entidad.getNombre()
        );
    }

    public static RolEntity aEntidad(
            Rol dominio) {

        if (dominio == null) {
            return null;
        }

        RolEntity entidad =
                new RolEntity();

        entidad.setIdRol(
                dominio.getIdRol()
        );

        entidad.setNombre(
                dominio.getNombre()
        );

        return entidad;
    }
}