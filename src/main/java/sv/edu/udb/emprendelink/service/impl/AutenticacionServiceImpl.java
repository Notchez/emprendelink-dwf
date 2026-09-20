package sv.edu.udb.emprendelink.service.impl;

import sv.edu.udb.emprendelink.dao.UsuarioDAO;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.security.Contrasenas;
import sv.edu.udb.emprendelink.service.AutenticacionService;

public class AutenticacionServiceImpl
        implements AutenticacionService {

    private final UsuarioDAO usuarioDAO;

    public AutenticacionServiceImpl(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Usuario autenticar(
            String correo,
            String contrasena) {

        if (correo == null || correo.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            return null;
        }

        Usuario usuario = usuarioDAO.buscarPorCorreo(
                correo.trim().toLowerCase()
        ).orElse(null);

        if (usuario == null || !usuario.isActivo()) {
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