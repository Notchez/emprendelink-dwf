package emprendelink.dao.jpa;

import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Usuario;
import emprendelink.persistence.jpa.entity.RolEntity;
import emprendelink.persistence.jpa.entity.UsuarioEntity;
import emprendelink.persistence.jpa.mapper.UsuarioMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaUsuarioDAO implements UsuarioDAO {

    @PersistenceContext(unitName = "emprendelinkPU")
    private EntityManager entityManager;

    @Override
    public Usuario crear(
            Usuario usuario) {

        validarRol(usuario);

        UsuarioEntity entidad =
                UsuarioMapper.aEntidad(usuario);

        RolEntity rol =
                entityManager.getReference(
                        RolEntity.class,
                        usuario.getRol()
                                .getIdRol()
                );

        entidad.setRol(rol);

        entityManager.persist(entidad);
        entityManager.flush();

        return UsuarioMapper.aDominio(
                entidad
        );
    }

    @Override
    public Optional<Usuario> buscarPorId(
            Integer idUsuario) {

        if (idUsuario == null) {
            return Optional.empty();
        }

        UsuarioEntity entidad =
                entityManager.find(
                        UsuarioEntity.class,
                        idUsuario
                );

        return Optional.ofNullable(
                UsuarioMapper.aDominio(
                        entidad
                )
        );
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(
            String correo) {

        if (correo == null
                || correo.isBlank()) {

            return Optional.empty();
        }

        return entityManager.createQuery(
                        """
                        SELECT u
                        FROM UsuarioEntity u
                        JOIN FETCH u.rol
                        WHERE LOWER(u.correo) = :correo
                        """,
                        UsuarioEntity.class
                )
                .setParameter(
                        "correo",
                        correo.trim()
                                .toLowerCase()
                )
                .getResultStream()
                .findFirst()
                .map(
                        UsuarioMapper::aDominio
                );
    }

    @Override
    public List<Usuario> listarTodos() {

        return entityManager.createQuery(
                        """
                        SELECT u
                        FROM UsuarioEntity u
                        JOIN FETCH u.rol
                        ORDER BY u.idUsuario
                        """,
                        UsuarioEntity.class
                )
                .getResultStream()
                .map(
                        UsuarioMapper::aDominio
                )
                .toList();
    }

    @Override
    public boolean actualizar(
            Usuario usuario) {

        if (usuario == null
                || usuario.getIdUsuario() == null) {

            return false;
        }

        validarRol(usuario);

        UsuarioEntity entidad =
                entityManager.find(
                        UsuarioEntity.class,
                        usuario.getIdUsuario()
                );

        if (entidad == null) {
            return false;
        }

        RolEntity rol =
                entityManager.getReference(
                        RolEntity.class,
                        usuario.getRol()
                                .getIdRol()
                );

        entidad.setRol(rol);

        entidad.setNombre(
                usuario.getNombre()
        );

        entidad.setApellido(
                usuario.getApellido()
        );

        entidad.setCorreo(
                usuario.getCorreo()
        );

        entidad.setContrasenaHash(
                usuario.getContrasenaHash()
        );

        entidad.setTelefono(
                usuario.getTelefono()
        );

        entidad.setActivo(
                usuario.isActivo()
        );

        entidad.setFechaRegistro(
                usuario.getFechaRegistro()
        );

        return true;
    }

    @Override
    public boolean eliminar(
            Integer idUsuario) {

        if (idUsuario == null) {
            return false;
        }

        UsuarioEntity entidad =
                entityManager.find(
                        UsuarioEntity.class,
                        idUsuario
                );

        if (entidad == null) {
            return false;
        }

        entityManager.remove(entidad);

        return true;
    }

    private void validarRol(
            Usuario usuario) {

        if (usuario == null
                || usuario.getRol() == null
                || usuario.getRol()
                .getIdRol() == null) {

            throw new IllegalArgumentException(
                    "El usuario debe tener un rol válido."
            );
        }
    }
}