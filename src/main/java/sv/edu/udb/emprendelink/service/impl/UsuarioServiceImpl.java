package sv.edu.udb.emprendelink.service.impl;

import sv.edu.udb.emprendelink.dao.RolDAO;
import sv.edu.udb.emprendelink.dao.UsuarioDAO;
import sv.edu.udb.emprendelink.model.Rol;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.security.Contrasenas;
import sv.edu.udb.emprendelink.service.UsuarioService;

import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final RolDAO rolDAO;

    public UsuarioServiceImpl(
            UsuarioDAO usuarioDAO,
            RolDAO rolDAO) {

        this.usuarioDAO = usuarioDAO;
        this.rolDAO = rolDAO;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = usuarioDAO.listarTodos();

        for (Usuario usuario : usuarios) {
            usuario.setContrasenaHash(null);
        }

        return usuarios;
    }

    @Override
    public void registrar(
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            TipoRol rol) {

        if (esVacio(nombre)
                || esVacio(apellido)
                || esVacio(correo)
                || esVacio(contrasena)) {

            throw new IllegalArgumentException(
                    "Complete los campos obligatorios."
            );
        }

        if (rol != TipoRol.ROLE_CLIENTE
                && rol != TipoRol.ROLE_EMPRENDEDOR) {

            throw new IllegalArgumentException(
                    "El rol solicitado no está permitido."
            );
        }

        String correoNormalizado =
                correo.trim().toLowerCase();

        if (usuarioDAO.buscarPorCorreo(
                correoNormalizado).isPresent()) {

            throw new IllegalArgumentException(
                    "El correo ya está registrado."
            );
        }

        Rol rolEncontrado = rolDAO.listarTodos()
                .stream()
                .filter(r -> r.getNombre() == rol)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "El rol no existe en la base de datos."
                        )
                );

        Usuario usuario = new Usuario(
                rolEncontrado,
                nombre.trim(),
                apellido.trim(),
                correoNormalizado,
                Contrasenas.generarHash(contrasena),
                telefono == null ? null : telefono.trim()
        );

        usuarioDAO.crear(usuario);
    }

    @Override
    public void cambiarEstado(
            Integer idUsuario,
            boolean activo) {

        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El identificador es obligatorio."
            );
        }

        Usuario usuario = usuarioDAO.buscarPorId(idUsuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario no existe."
                        )
                );

        usuario.setActivo(activo);

        if (!usuarioDAO.actualizar(usuario)) {
            throw new IllegalStateException(
                    "No se pudo actualizar el usuario."
            );
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}