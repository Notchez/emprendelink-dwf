package emprendelink.dao.jpa;

import emprendelink.dao.PlanDAO;
import emprendelink.model.Plan;
import emprendelink.persistence.jpa.entity.PlanEntity;
import emprendelink.persistence.jpa.mapper.PlanMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaPlanDAO
        implements PlanDAO {

    @PersistenceContext(
            unitName = "emprendelinkPU"
    )
    private EntityManager entityManager;

    @Override
    public Plan crear(
            Plan plan) {

        PlanEntity entidad =
                PlanMapper.aEntidad(
                        plan
                );

        entityManager.persist(
                entidad
        );

        entityManager.flush();

        return PlanMapper.aDominio(
                entidad
        );
    }

    @Override
    public Optional<Plan> buscarPorId(
            Integer idPlan) {

        if (idPlan == null) {
            return Optional.empty();
        }

        PlanEntity entidad =
                entityManager.find(
                        PlanEntity.class,
                        idPlan
                );

        return Optional.ofNullable(
                        entidad
                )
                .map(
                        PlanMapper::aDominio
                );
    }

    @Override
    public Optional<Plan> buscarPorNombre(
            String nombre) {

        if (nombre == null
                || nombre.isBlank()) {

            return Optional.empty();
        }

        return entityManager.createQuery(
                        """
                        SELECT p
                        FROM PlanEntity p
                        WHERE LOWER(p.nombre) = LOWER(:nombre)
                        """,
                        PlanEntity.class
                )
                .setParameter(
                        "nombre",
                        nombre.trim()
                )
                .getResultStream()
                .findFirst()
                .map(
                        PlanMapper::aDominio
                );
    }

    @Override
    public List<Plan> listarTodos() {

        return entityManager.createQuery(
                        """
                        SELECT p
                        FROM PlanEntity p
                        ORDER BY p.idPlan
                        """,
                        PlanEntity.class
                )
                .getResultStream()
                .map(
                        PlanMapper::aDominio
                )
                .toList();
    }

    @Override
    public boolean actualizar(
            Plan plan) {

        if (plan == null
                || plan.getIdPlan() == null) {

            return false;
        }

        PlanEntity entidad =
                entityManager.find(
                        PlanEntity.class,
                        plan.getIdPlan()
                );

        if (entidad == null) {
            return false;
        }

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

        return true;
    }

    @Override
    public boolean eliminar(
            Integer idPlan) {

        if (idPlan == null) {
            return false;
        }

        PlanEntity entidad =
                entityManager.find(
                        PlanEntity.class,
                        idPlan
                );

        if (entidad == null) {
            return false;
        }

        entityManager.remove(
                entidad
        );

        return true;
    }
}