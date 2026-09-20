package sv.edu.udb.emprendelink.dao;

import sv.edu.udb.emprendelink.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolDAO {

    Rol crear(Rol rol);

    Optional<Rol> buscarPorId(Integer idRol);

    List<Rol> listarTodos();

    boolean actualizar(Rol rol);

    boolean eliminar(Integer idRol);
}