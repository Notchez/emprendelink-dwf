package emprendelink.service.impl;

import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.PlanDAO;
import emprendelink.dao.PublicacionDAO;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Plan;
import emprendelink.model.Publicacion;
import emprendelink.service.PlanService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class PlanServiceImpl
        implements PlanService {

    private final PlanDAO planDAO;

    private final EmprendimientoDAO emprendimientoDAO;

    private final PublicacionDAO publicacionDAO;

    @Inject
    public PlanServiceImpl(
            PlanDAO planDAO,
            EmprendimientoDAO emprendimientoDAO,
            PublicacionDAO publicacionDAO) {

        this.planDAO =
                planDAO;

        this.emprendimientoDAO =
                emprendimientoDAO;

        this.publicacionDAO =
                publicacionDAO;
    }

    @Override
    public List<Plan> listarTodos() {

        return planDAO.listarTodos();
    }

    @Override
    public List<Plan> listarActivos() {

        return planDAO.listarTodos()
                .stream()
                .filter(
                        Plan::isActivo
                )
                .toList();
    }

    @Override
    public Plan buscarPorId(
            Integer idPlan) {

        if (idPlan == null) {
            return null;
        }

        return planDAO.buscarPorId(
                idPlan
        ).orElse(null);
    }

    @Override
    public void crear(
            String nombre,
            Integer maxProductosActivos,
            BigDecimal tasaComision) {

        validarDatos(
                nombre,
                maxProductosActivos,
                tasaComision
        );

        if (planDAO.buscarPorNombre(
                nombre.trim()
        ).isPresent()) {

            throw new IllegalArgumentException(
                    "Ya existe un plan con ese nombre."
            );
        }

        Plan plan =
                new Plan(
                        nombre.trim(),
                        maxProductosActivos,
                        tasaComision
                );

        planDAO.crear(
                plan
        );
    }

    @Override
    public void actualizar(
            Integer idPlan,
            String nombre,
            Integer maxProductosActivos,
            BigDecimal tasaComision) {

        if (idPlan == null) {

            throw new IllegalArgumentException(
                    "El plan es obligatorio."
            );
        }

        validarDatos(
                nombre,
                maxProductosActivos,
                tasaComision
        );

        Plan plan =
                planDAO.buscarPorId(
                        idPlan
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "El plan no existe."
                        )
                );

        Plan otroConMismoNombre =
                planDAO.buscarPorNombre(
                        nombre.trim()
                ).orElse(null);

        if (otroConMismoNombre != null
                && !Objects.equals(
                otroConMismoNombre.getIdPlan(),
                idPlan
        )) {

            throw new IllegalArgumentException(
                    "Ya existe otro plan con ese nombre."
            );
        }

        validarLimiteEnNegociosAsignados(
                idPlan,
                maxProductosActivos
        );

        plan.setNombre(
                nombre.trim()
        );

        plan.setMaxProductosActivos(
                maxProductosActivos
        );

        plan.setTasaComision(
                tasaComision
        );

        if (!planDAO.actualizar(
                plan
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar el plan."
            );
        }
    }

    @Override
    public void cambiarEstado(
            Integer idPlan,
            boolean activo) {

        Plan plan =
                planDAO.buscarPorId(
                        idPlan
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "El plan no existe."
                        )
                );

        if (!activo
                && estaAsignado(
                idPlan
        )) {

            throw new IllegalArgumentException(
                    "No puedes desactivar un plan que está asignado a uno o más negocios."
            );
        }

        plan.setActivo(
                activo
        );

        if (!planDAO.actualizar(
                plan
        )) {

            throw new IllegalStateException(
                    "No se pudo cambiar el estado del plan."
            );
        }
    }

    @Override
    public void asignarAEmprendimiento(
            Integer idEmprendimiento,
            Integer idPlan) {

        if (idEmprendimiento == null
                || idPlan == null) {

            throw new IllegalArgumentException(
                    "El negocio y el plan son obligatorios."
            );
        }

        Emprendimiento emprendimiento =
                emprendimientoDAO.buscarPorId(
                        idEmprendimiento
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "El negocio no existe."
                        )
                );

        Plan plan =
                planDAO.buscarPorId(
                        idPlan
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "El plan no existe."
                        )
                );

        if (!plan.isActivo()) {

            throw new IllegalArgumentException(
                    "No puedes asignar un plan inactivo."
            );
        }

        int publicacionesActivas =
                contarPublicacionesActivas(
                        idEmprendimiento
                );

        if (publicacionesActivas
                > plan.getMaxProductosActivos()) {

            throw new IllegalArgumentException(
                    "El negocio tiene "
                            + publicacionesActivas
                            + " publicaciones activas y el plan "
                            + plan.getNombre()
                            + " permite un máximo de "
                            + plan.getMaxProductosActivos()
                            + "."
            );
        }

        emprendimiento.setPlan(
                plan
        );

        if (!emprendimientoDAO.actualizar(
                emprendimiento
        )) {

            throw new IllegalStateException(
                    "No se pudo asignar el plan al negocio."
            );
        }
    }

    private void validarDatos(
            String nombre,
            Integer maxProductosActivos,
            BigDecimal tasaComision) {

        if (nombre == null
                || nombre.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre del plan es obligatorio."
            );
        }

        if (maxProductosActivos == null
                || maxProductosActivos <= 0) {

            throw new IllegalArgumentException(
                    "El límite de productos debe ser mayor que cero."
            );
        }

        if (tasaComision == null
                || tasaComision.signum() < 0
                || tasaComision.compareTo(
                BigDecimal.ONE
        ) > 0) {

            throw new IllegalArgumentException(
                    "La comisión debe estar entre 0% y 100%."
            );
        }
    }

    private boolean estaAsignado(
            Integer idPlan) {

        return emprendimientoDAO
                .listarTodos()
                .stream()
                .anyMatch(
                        emprendimiento ->
                                emprendimiento.getPlan()
                                        != null
                                        && Objects.equals(
                                        emprendimiento
                                                .getPlan()
                                                .getIdPlan(),
                                        idPlan
                                )
                );
    }

    private void validarLimiteEnNegociosAsignados(
            Integer idPlan,
            Integer nuevoLimite) {

        for (Emprendimiento emprendimiento
                : emprendimientoDAO.listarTodos()) {

            if (emprendimiento.getPlan() == null
                    || !Objects.equals(
                    emprendimiento
                            .getPlan()
                            .getIdPlan(),
                    idPlan
            )) {

                continue;
            }

            int activas =
                    contarPublicacionesActivas(
                            emprendimiento
                                    .getIdEmprendimiento()
                    );

            if (activas > nuevoLimite) {

                throw new IllegalArgumentException(
                        "No puedes reducir el límite a "
                                + nuevoLimite
                                + " porque "
                                + emprendimiento.getNombre()
                                + " tiene "
                                + activas
                                + " publicaciones activas."
                );
            }
        }
    }

    private int contarPublicacionesActivas(
            Integer idEmprendimiento) {

        return (int) publicacionDAO
                .listarPorEmprendimiento(
                        idEmprendimiento
                )
                .stream()
                .filter(
                        Publicacion::isActivo
                )
                .count();
    }
}