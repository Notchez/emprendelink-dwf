package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Plan;
import emprendelink.persistence.jpa.entity.PlanEntity;

public final class PlanMapper {

    private PlanMapper() {
    }

    public static PlanEntity aEntidad(
            Plan plan) {

        if (plan == null) {
            return null;
        }

        PlanEntity entidad =
                new PlanEntity();

        entidad.setIdPlan(
                plan.getIdPlan()
        );

        entidad.setNombre(
                plan.getNombre()
        );

        entidad.setMaxProductosActivos(
                plan.getMaxProductosActivos()
        );

        entidad.setTasaComision(
                plan.getTasaComision()
        );

        entidad.setActivo(
                plan.isActivo()
        );

        return entidad;
    }

    public static Plan aDominio(
            PlanEntity entidad) {

        if (entidad == null) {
            return null;
        }

        return new Plan(
                entidad.getIdPlan(),
                entidad.getNombre(),
                entidad.getMaxProductosActivos(),
                entidad.getTasaComision(),
                entidad.isActivo()
        );
    }
}