package sv.edu.udb.emprendelink.service;

import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listarTodos();

    void registrar(
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            TipoRol rol
    );

    void cambiarEstado(
            Integer idUsuario,
            boolean activo
    );
}