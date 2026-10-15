package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Emprendimiento;
import emprendelink.persistence.jpa.entity.EmprendimientoEntity;

public final class EmprendimientoMapper {

    private EmprendimientoMapper() {
    }

    public static EmprendimientoEntity aEntidad(
            Emprendimiento emprendimiento) {

        if (emprendimiento == null) {
            return null;
        }

        EmprendimientoEntity entidad =
                new EmprendimientoEntity();

        entidad.setIdEmprendimiento(
                emprendimiento
                        .getIdEmprendimiento()
        );

        entidad.setPropietario(
                UsuarioMapper.aEntidad(
                        emprendimiento
                                .getPropietario()
                )
        );

        entidad.setPlan(
                PlanMapper.aEntidad(
                        emprendimiento
                                .getPlan()
                )
        );

        entidad.setCategoria(
                CategoriaMapper.aEntidad(
                        emprendimiento
                                .getCategoria()
                )
        );

        entidad.setNombre(
                emprendimiento.getNombre()
        );

        entidad.setDescripcion(
                emprendimiento.getDescripcion()
        );

        entidad.setContacto(
                emprendimiento.getContacto()
        );

        entidad.setLogoUrl(
                emprendimiento.getLogoUrl()
        );

        entidad.setActivo(
                emprendimiento.isActivo()
        );

        entidad.setFechaRegistro(
                emprendimiento.getFechaRegistro()
        );

        return entidad;
    }

    public static Emprendimiento aDominio(
            EmprendimientoEntity entidad) {

        if (entidad == null) {
            return null;
        }

        return new Emprendimiento(
                entidad.getIdEmprendimiento(),
                UsuarioMapper.aDominio(
                        entidad.getPropietario()
                ),
                PlanMapper.aDominio(
                        entidad.getPlan()
                ),
                CategoriaMapper.aDominio(
                        entidad.getCategoria()
                ),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getContacto(),
                entidad.getLogoUrl(),
                entidad.isActivo(),
                entidad.getFechaRegistro()
        );
    }
}