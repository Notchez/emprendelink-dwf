package emprendelink.service;

import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listarTodos();

    Usuario buscarPorId(
            Integer idUsuario
    );

    void registrar(
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            TipoRol rol
    );

    Usuario actualizarPerfilCliente(
            Integer idUsuario,
            String telefono,
            String direccion
    );

    void cambiarEstado(
            Integer idUsuario,
            boolean activo
    );
}