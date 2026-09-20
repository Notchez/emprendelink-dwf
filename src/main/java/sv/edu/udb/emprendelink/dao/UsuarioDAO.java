package sv.edu.udb.emprendelink.dao;

import sv.edu.udb.emprendelink.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {

    Usuario crear(Usuario usuario);

    Optional<Usuario> buscarPorId(Integer idUsuario);

    Optional<Usuario> buscarPorCorreo(String correo);

    List<Usuario> listarTodos();

    boolean actualizar(Usuario usuario);

    boolean eliminar(Integer idUsuario);
}