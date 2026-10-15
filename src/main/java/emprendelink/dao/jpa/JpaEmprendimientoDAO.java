package emprendelink.dao.jpa;

import emprendelink.dao.EmprendimientoDAO;
import emprendelink.model.Emprendimiento;
import emprendelink.persistence.jpa.entity.CategoriaEntity;
import emprendelink.persistence.jpa.entity.EmprendimientoEntity;
import emprendelink.persistence.jpa.entity.PlanEntity;
import emprendelink.persistence.jpa.entity.UsuarioEntity;
import emprendelink.persistence.jpa.mapper.EmprendimientoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaEmprendimientoDAO
        implements EmprendimientoDAO {

    @PersistenceContext(
            unitName = "emprendelinkPU"
    )
    private EntityManager entityManager;

    @Override
    public Emprendimiento crear(
            Emprendimiento emprendimiento) {

        validarPropietario(
                emprendimiento
        );

        EmprendimientoEntity entidad =
                EmprendimientoMapper.aEntidad(
                        emprendimiento
                );

        entidad.setPropietario(
                entityManager.getReference(
                        UsuarioEntity.class,
                        emprendimiento
                                .getPropietario()
                                .getIdUsuario()
                )
        );

        entidad.setPlan(
                resolverPlan(
                        emprendimiento
                )
        );

        entidad.setCategoria(
                resolverCategoria(
                        emprendimiento
                )
        );

        entityManager.persist(
                entidad
        );

        entityManager.flush();

        return buscarPorId(
                entidad.getIdEmprendimiento()
        ).orElseThrow(
                () ->
                        new IllegalStateException(
                                "No se pudo recuperar el negocio creado."
                        )
        );
    }

    @Override
    public Optional<Emprendimiento> buscarPorId(
            Integer idEmprendimiento) {

        if (idEmprendimiento == null) {
            return Optional.empty();
        }

        return entityManager.createQuery(
                        """
                        SELECT e
                        FROM EmprendimientoEntity e
                        JOIN FETCH e.propietario u
                        JOIN FETCH u.rol
                        JOIN FETCH e.plan
                        JOIN FETCH e.categoria
                        WHERE e.idEmprendimiento = :id
                        """,
                        EmprendimientoEntity.class
                )
                .setParameter(
                        "id",
                        idEmprendimiento
                )
                .getResultStream()
                .findFirst()
                .map(
                        EmprendimientoMapper::aDominio
                );
    }

    @Override
    public List<Emprendimiento> listarTodos() {

        return entityManager.createQuery(
                        """
                        SELECT e
                        FROM EmprendimientoEntity e
                        JOIN FETCH e.propietario u
                        JOIN FETCH u.rol
                        JOIN FETCH e.plan
                        JOIN FETCH e.categoria
                        ORDER BY e.idEmprendimiento
                        """,
                        EmprendimientoEntity.class
                )
                .getResultStream()
                .map(
                        EmprendimientoMapper::aDominio
                )
                .toList();
    }

    @Override
    public List<Emprendimiento> listarPorPropietario(
            Integer idUsuario) {

        if (idUsuario == null) {
            return List.of();
        }

        return entityManager.createQuery(
                        """
                        SELECT e
                        FROM EmprendimientoEntity e
                        JOIN FETCH e.propietario u
                        JOIN FETCH u.rol
                        JOIN FETCH e.plan
                        JOIN FETCH e.categoria
                        WHERE u.idUsuario = :idUsuario
                        ORDER BY e.idEmprendimiento
                        """,
                        EmprendimientoEntity.class
                )
                .setParameter(
                        "idUsuario",
                        idUsuario
                )
                .getResultStream()
                .map(
                        EmprendimientoMapper::aDominio
                )
                .toList();
    }

    @Override
    public boolean actualizar(
            Emprendimiento emprendimiento) {

        if (emprendimiento == null
                || emprendimiento
                .getIdEmprendimiento() == null) {

            return false;
        }

        validarPropietario(
                emprendimiento
        );

        EmprendimientoEntity entidad =
                entityManager.find(
                        EmprendimientoEntity.class,
                        emprendimiento
                                .getIdEmprendimiento()
                );

        if (entidad == null) {
            return false;
        }

        entidad.setPropietario(
                entityManager.getReference(
                        UsuarioEntity.class,
                        emprendimiento
                                .getPropietario()
                                .getIdUsuario()
                )
        );

        if (emprendimiento.getPlan() != null
                && emprendimiento
                .getPlan()
                .getIdPlan() != null) {

            entidad.setPlan(
                    entityManager.getReference(
                            PlanEntity.class,
                            emprendimiento
                                    .getPlan()
                                    .getIdPlan()
                    )
            );
        }

        if (emprendimiento.getCategoria() != null
                && emprendimiento
                .getCategoria()
                .getIdCategoria() != null) {

            entidad.setCategoria(
                    entityManager.getReference(
                            CategoriaEntity.class,
                            emprendimiento
                                    .getCategoria()
                                    .getIdCategoria()
                    )
            );
        }

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

        return true;
    }

    @Override
    public boolean eliminar(
            Integer idEmprendimiento) {

        if (idEmprendimiento == null) {
            return false;
        }

        EmprendimientoEntity entidad =
                entityManager.find(
                        EmprendimientoEntity.class,
                        idEmprendimiento
                );

        if (entidad == null) {
            return false;
        }

        entityManager.remove(
                entidad
        );

        return true;
    }

    private PlanEntity resolverPlan(
            Emprendimiento emprendimiento) {

        if (emprendimiento.getPlan() != null
                && emprendimiento
                .getPlan()
                .getIdPlan() != null) {

            return entityManager.getReference(
                    PlanEntity.class,
                    emprendimiento
                            .getPlan()
                            .getIdPlan()
            );
        }

        return entityManager.createQuery(
                        """
                        SELECT p
                        FROM PlanEntity p
                        WHERE p.activo = true
                        ORDER BY p.idPlan
                        """,
                        PlanEntity.class
                )
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalStateException(
                                        "No existe un plan activo."
                                )
                );
    }

    private CategoriaEntity resolverCategoria(
            Emprendimiento emprendimiento) {

        if (emprendimiento.getCategoria() == null
                || emprendimiento
                .getCategoria()
                .getIdCategoria() == null) {

            throw new IllegalArgumentException(
                    "El negocio requiere una categoría."
            );
        }

        return entityManager.getReference(
                CategoriaEntity.class,
                emprendimiento
                        .getCategoria()
                        .getIdCategoria()
        );
    }

    private void validarPropietario(
            Emprendimiento emprendimiento) {

        if (emprendimiento == null
                || emprendimiento.getPropietario() == null
                || emprendimiento
                .getPropietario()
                .getIdUsuario() == null) {

            throw new IllegalArgumentException(
                    "El negocio requiere un propietario válido."
            );
        }
    }
}