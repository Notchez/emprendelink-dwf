package sv.edu.udb.emprendelink.dao;

import sv.edu.udb.emprendelink.model.Emprendimiento;

import java.util.List;
import java.util.Optional;

public interface EmprendimientoDAO {

    Emprendimiento crear(Emprendimiento emprendimiento);

    Optional<Emprendimiento> buscarPorId(Integer idEmprendimiento);

    List<Emprendimiento> listarTodos();

    List<Emprendimiento> listarPorPropietario(Integer idUsuario);

    boolean actualizar(Emprendimiento emprendimiento);

    boolean eliminar(Integer idEmprendimiento);
}