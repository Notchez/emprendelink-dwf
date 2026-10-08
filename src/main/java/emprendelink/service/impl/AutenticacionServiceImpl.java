package emprendelink.service.impl;

import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Usuario;
import emprendelink.security.Contrasenas;
import emprendelink.service.AutenticacionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AutenticacionServiceImpl
        implements AutenticacionService {

    private final UsuarioDAO usuarioDAO;

    @Inject
    public AutenticacionServiceImpl(
            UsuarioDAO usuarioDAO) {

        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Usuario autenticar(
            String correo,
            String contrasena) {

        if (correo == null
                || correo.isBlank()
                || contrasena == null
                || contrasena.isBlank()) {

            return null;
        }

        Usuario usuario =
                usuarioDAO.buscarPorCorreo(
                                correo.trim()
                                        .toLowerCase()
                        )
                        .orElse(null);

        if (usuario == null
                || !usuario.isActivo()) {

            return null;
        }

        if (!Contrasenas.verificar(
                contrasena,
                usuario.getContrasenaHash())) {

            return null;
        }

        usuario.setContrasenaHash(null);

        return usuario;
    }
}