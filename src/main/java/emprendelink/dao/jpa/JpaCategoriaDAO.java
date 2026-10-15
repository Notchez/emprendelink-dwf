package emprendelink.dao.jpa;

import emprendelink.dao.CategoriaDAO;
import emprendelink.model.Categoria;
import emprendelink.persistence.jpa.entity.CategoriaEntity;
import emprendelink.persistence.jpa.mapper.CategoriaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaCategoriaDAO
        implements CategoriaDAO {

    @PersistenceContext(
            unitName = "emprendelinkPU"
    )
    private EntityManager entityManager;

    @Override
    public Categoria crear(
            Categoria categoria) {

        CategoriaEntity entidad =
                CategoriaMapper.aEntidad(
                        categoria
                );

        entityManager.persist(
                entidad
        );

        entityManager.flush();

        return CategoriaMapper.aDominio(
                entidad
        );
    }

    @Override
    public Optional<Categoria> buscarPorId(
            Integer idCategoria) {

        if (idCategoria == null) {
            return Optional.empty();
        }

        CategoriaEntity entidad =
                entityManager.find(
                        CategoriaEntity.class,
                        idCategoria
                );

        return Optional.ofNullable(
                CategoriaMapper.aDominio(
                        entidad
                )
        );
    }

    @Override
    public List<Categoria> listarTodos() {

        return entityManager.createQuery(
                        """
                        SELECT c
                        FROM CategoriaEntity c
                        ORDER BY c.nombre
                        """,
                        CategoriaEntity.class
                )
                .getResultStream()
                .map(
                        CategoriaMapper::aDominio
                )
                .toList();
    }

    @Override
    public boolean actualizar(
            Categoria categoria) {

        if (categoria == null
                || categoria.getIdCategoria() == null) {

            return false;
        }

        CategoriaEntity entidad =
                entityManager.find(
                        CategoriaEntity.class,
                        categoria.getIdCategoria()
                );

        if (entidad == null) {
            return false;
        }

        entidad.setNombre(
                categoria.getNombre()
        );

        entidad.setDescripcion(
                categoria.getDescripcion()
        );

        entidad.setManejaStock(
                categoria.isManejaStock()
        );

        entidad.setActivo(
                categoria.isActivo()
        );

        return true;
    }

    @Override
    public boolean eliminar(
            Integer idCategoria) {

        if (idCategoria == null) {
            return false;
        }

        CategoriaEntity entidad =
                entityManager.find(
                        CategoriaEntity.class,
                        idCategoria
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