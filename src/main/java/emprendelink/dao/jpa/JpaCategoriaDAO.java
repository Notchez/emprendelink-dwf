package emprendelink.dao.jpa;

import emprendelink.dao.CategoriaDAO;
import emprendelink.model.Categoria;
import emprendelink.persistence.jpa.entity.CategoriaEntity;
import emprendelink.persistence.jpa.mapper.CategoriaMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
public class JpaCategoriaDAO implements CategoriaDAO {

    @PersistenceContext(unitName = "EmprendeLinkPU")
    private EntityManager em;

    @Override
    @Transactional
    public Categoria crear(Categoria categoria) {
        CategoriaEntity entity = CategoriaMapper.toEntity(categoria);
        em.persist(entity);
        return CategoriaMapper.toDomain(entity);
    }

    @Override
    public Optional<Categoria> buscarPorId(Integer idCategoria) {
        CategoriaEntity entity = em.find(CategoriaEntity.class, idCategoria);
        return Optional.ofNullable(CategoriaMapper.toDomain(entity));
    }

    @Override
    public List<Categoria> listarTodos() {
        List<CategoriaEntity> entities = em.createQuery("SELECT c FROM CategoriaEntity c", CategoriaEntity.class).getResultList();
        return entities.stream().map(CategoriaMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean actualizar(Categoria categoria) {
        CategoriaEntity entity = CategoriaMapper.toEntity(categoria);
        em.merge(entity);
        return true;
    }

    @Override
    @Transactional
    public boolean eliminar(Integer idCategoria) {
        CategoriaEntity entity = em.find(CategoriaEntity.class, idCategoria);
        if (entity != null) {
            em.remove(entity);
            return true;
        }
        return false;
    }
}