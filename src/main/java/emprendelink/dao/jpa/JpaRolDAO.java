package emprendelink.dao.jpa;

import emprendelink.dao.RolDAO;
import emprendelink.model.Rol;
import emprendelink.persistence.jpa.entity.RolEntity;
import emprendelink.persistence.jpa.mapper.RolMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaRolDAO implements RolDAO {

    @PersistenceContext(unitName = "emprendelinkPU")
    private EntityManager entityManager;

    @Override
    public Rol crear(Rol rol) {

        RolEntity entidad =
                RolMapper.aEntidad(rol);

        entityManager.persist(entidad);
        entityManager.flush();

        return RolMapper.aDominio(entidad);
    }

    @Override
    public Optional<Rol> buscarPorId(
            Integer idRol) {

        if (idRol == null) {
            return Optional.empty();
        }

        RolEntity entidad =
                entityManager.find(
                        RolEntity.class,
                        idRol
                );

        return Optional.ofNullable(
                RolMapper.aDominio(entidad)
        );
    }

    @Override
    public List<Rol> listarTodos() {

        return entityManager.createQuery(
                        """
                        SELECT r
                        FROM RolEntity r
                        ORDER BY r.idRol
                        """,
                        RolEntity.class
                )
                .getResultStream()
                .map(RolMapper::aDominio)
                .toList();
    }

    @Override
    public boolean actualizar(Rol rol) {

        if (rol == null
                || rol.getIdRol() == null) {

            return false;
        }

        RolEntity entidad =
                entityManager.find(
                        RolEntity.class,
                        rol.getIdRol()
                );

        if (entidad == null) {
            return false;
        }

        entidad.setNombre(
                rol.getNombre()
        );

        return true;
    }

    @Override
    public boolean eliminar(
            Integer idRol) {

        if (idRol == null) {
            return false;
        }

        RolEntity entidad =
                entityManager.find(
                        RolEntity.class,
                        idRol
                );

        if (entidad == null) {
            return false;
        }

        entityManager.remove(entidad);

        return true;
    }
}