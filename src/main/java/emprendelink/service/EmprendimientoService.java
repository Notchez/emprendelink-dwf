package emprendelink.service;

import emprendelink.model.Emprendimiento;

import java.util.List;

public interface EmprendimientoService {

    List<Emprendimiento> listarActivos();

    List<Emprendimiento> listarTodos();

    List<Emprendimiento> listarPorPropietario(
            Integer idPropietario
    );

    Emprendimiento buscarActivoPorId(
            Integer idEmprendimiento
    );

    Emprendimiento buscarPropioPorId(
            Integer idEmprendimiento,
            Integer idPropietario
    );

    void crear(
            Integer idPropietario,
            String nombre,
            String descripcion,
            String contacto
    );

    void actualizar(
            Integer idPropietario,
            Integer idEmprendimiento,
            String nombre,
            String descripcion,
            String contacto
    );

    void cambiarEstado(
            Integer idUsuario,
            Integer idEmprendimiento,
            boolean activo
    );
}