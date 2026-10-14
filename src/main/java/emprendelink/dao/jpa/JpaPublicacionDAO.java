package emprendelink.dao.jpa;

import emprendelink.dao.PublicacionDAO;
import emprendelink.model.Publicacion;
import emprendelink.persistence.jpa.entity.CategoriaEntity;
import emprendelink.persistence.jpa.entity.EmprendimientoEntity;
import emprendelink.persistence.jpa.entity.PublicacionEntity;
import emprendelink.persistence.jpa.mapper.PublicacionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaPublicacionDAO implements PublicacionDAO {

    @PersistenceContext(unitName = "emprendelinkPU")
    private EntityManager entityManager;

    @Override
    public Publicacion crear(Publicacion publicacion) {

        validarRelaciones(publicacion);

        PublicacionEntity entidad =
                PublicacionMapper.aEntidad(publicacion);

        entidad.setEmprendimiento(
                entityManager.getReference(
                        EmprendimientoEntity.class,
                        publicacion.getEmprendimiento()
                                .getIdEmprendimiento()
                )
        );

        entidad.setCategoria(
                entityManager.getReference(
                        CategoriaEntity.class,
                        publicacion.getCategoria()
                                .getIdCategoria()
                )
        );

        entityManager.persist(entidad);
        entityManager.flush();

        return buscarPorId(entidad.getIdPublicacion())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No se pudo recuperar la publicación creada."
                        )
                );
    }

    @Override
    public Optional<Publicacion> buscarPorId(
            Integer idPublicacion) {

        if (idPublicacion == null) {
            return Optional.empty();
        }

        return entityManager.createQuery(
                        """
                        SELECT p
                        FROM PublicacionEntity p
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.propietario u
                        JOIN FETCH u.rol
                        JOIN FETCH p.categoria
                        WHERE p.idPublicacion = :id
                        """,
                        PublicacionEntity.class
                )
                .setParameter("id", idPublicacion)
                .getResultStream()
                .findFirst()
                .map(PublicacionMapper::aDominio);
    }

    @Override
    public List<Publicacion> listarTodos() {

        return entityManager.createQuery(
                        """
                        SELECT p
                        FROM PublicacionEntity p
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.propietario u
                        JOIN FETCH u.rol
                        JOIN FETCH p.categoria
                        ORDER BY p.idPublicacion DESC
                        """,
                        PublicacionEntity.class
                )
                .getResultStream()
                .map(PublicacionMapper::aDominio)
                .toList();
    }

    @Override
    public List<Publicacion> listarPorEmprendimiento(
            Integer idEmprendimiento) {

        if (idEmprendimiento == null) {
            return List.of();
        }

        return entityManager.createQuery(
                        """
                        SELECT p
                        FROM PublicacionEntity p
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.propietario u
                        JOIN FETCH u.rol
                        JOIN FETCH p.categoria
                        WHERE e.idEmprendimiento = :id
                        ORDER BY p.idPublicacion DESC
                        """,
                        PublicacionEntity.class
                )
                .setParameter("id", idEmprendimiento)
                .getResultStream()
                .map(PublicacionMapper::aDominio)
                .toList();
    }

    @Override
    public List<Publicacion> listarPorCategoria(
            Integer idCategoria) {

        if (idCategoria == null) {
            return List.of();
        }

        return entityManager.createQuery(
                        """
                        SELECT p
                        FROM PublicacionEntity p
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.propietario u
                        JOIN FETCH u.rol
                        JOIN FETCH p.categoria c
                        WHERE c.idCategoria = :id
                        ORDER BY p.idPublicacion DESC
                        """,
                        PublicacionEntity.class
                )
                .setParameter("id", idCategoria)
                .getResultStream()
                .map(PublicacionMapper::aDominio)
                .toList();
    }

    @Override
    public boolean actualizar(
            Publicacion publicacion) {

        if (publicacion == null
                || publicacion.getIdPublicacion() == null) {

            return false;
        }

        validarRelaciones(publicacion);

        PublicacionEntity entidad =
                entityManager.find(
                        PublicacionEntity.class,
                        publicacion.getIdPublicacion()
                );

        if (entidad == null) {
            return false;
        }

        entidad.setEmprendimiento(
                entityManager.getReference(
                        EmprendimientoEntity.class,
                        publicacion.getEmprendimiento()
                                .getIdEmprendimiento()
                )
        );

        entidad.setCategoria(
                entityManager.getReference(
                        CategoriaEntity.class,
                        publicacion.getCategoria()
                                .getIdCategoria()
                )
        );

        entidad.setTipo(publicacion.getTipo());
        entidad.setNombre(publicacion.getNombre());
        entidad.setDescripcion(publicacion.getDescripcion());
        entidad.setPrecio(publicacion.getPrecio());
        entidad.setStock(publicacion.getStock());
        entidad.setActivo(publicacion.isActivo());
        entidad.setFechaPublicacion(publicacion.getFechaPublicacion());

        return true;
    }

    @Override
    public boolean eliminar(
            Integer idPublicacion) {

        if (idPublicacion == null) {
            return false;
        }

        PublicacionEntity entidad =
                entityManager.find(
                        PublicacionEntity.class,
                        idPublicacion
                );

        if (entidad == null) {
            return false;
        }

        entityManager.remove(entidad);

        return true;
    }

    private void validarRelaciones(
            Publicacion publicacion) {

        if (publicacion == null
                || publicacion.getEmprendimiento() == null
                || publicacion.getEmprendimiento()
                .getIdEmprendimiento() == null
                || publicacion.getCategoria() == null
                || publicacion.getCategoria()
                .getIdCategoria() == null) {

            throw new IllegalArgumentException(
                    "La publicación requiere emprendimiento y categoría válidos."
            );
        }
    }
}
