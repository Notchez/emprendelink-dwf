package sv.edu.udb.emprendelink.service.impl;

import sv.edu.udb.emprendelink.dao.EmprendimientoDAO;
import sv.edu.udb.emprendelink.dao.UsuarioDAO;
import sv.edu.udb.emprendelink.model.Emprendimiento;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.EmprendimientoService;

import java.util.List;
import java.util.Objects;

public class EmprendimientoServiceImpl
        implements EmprendimientoService {

    private final EmprendimientoDAO emprendimientoDAO;
    private final UsuarioDAO usuarioDAO;

    public EmprendimientoServiceImpl(
            EmprendimientoDAO emprendimientoDAO,
            UsuarioDAO usuarioDAO) {

        this.emprendimientoDAO = emprendimientoDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public List<Emprendimiento> listarActivos() {
        return emprendimientoDAO.listarTodos()
                .stream()
                .filter(Emprendimiento::isActivo)
                .filter(e -> e.getPropietario() != null)
                .filter(e -> e.getPropietario().isActivo())
                .toList();
    }

    @Override
    public List<Emprendimiento> listarTodos() {
        return emprendimientoDAO.listarTodos();
    }

    @Override
    public List<Emprendimiento> listarPorPropietario(
            Integer idPropietario) {

        if (idPropietario == null) {
            throw new IllegalArgumentException(
                    "El propietario es obligatorio."
            );
        }

        return emprendimientoDAO.listarPorPropietario(
                idPropietario
        );
    }

    @Override
    public Emprendimiento buscarActivoPorId(
            Integer idEmprendimiento) {

        if (idEmprendimiento == null) {
            return null;
        }

        Emprendimiento emprendimiento =
                emprendimientoDAO.buscarPorId(
                        idEmprendimiento
                ).orElse(null);

        if (emprendimiento == null
                || !emprendimiento.isActivo()
                || emprendimiento.getPropietario() == null
                || !emprendimiento.getPropietario().isActivo()) {

            return null;
        }

        return emprendimiento;
    }

    @Override
    public Emprendimiento buscarPropioPorId(
            Integer idEmprendimiento,
            Integer idPropietario) {

        if (idEmprendimiento == null
                || idPropietario == null) {
            return null;
        }

        Emprendimiento emprendimiento =
                emprendimientoDAO.buscarPorId(
                        idEmprendimiento
                ).orElse(null);

        if (emprendimiento == null
                || emprendimiento.getPropietario() == null
                || !Objects.equals(
                emprendimiento.getPropietario()
                        .getIdUsuario(),
                idPropietario)) {

            return null;
        }

        return emprendimiento;
    }

    @Override
    public void crear(
            Integer idPropietario,
            String nombre,
            String descripcion,
            String contacto) {

        validarNombre(nombre);

        Usuario propietario = usuarioDAO.buscarPorId(
                idPropietario
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "El propietario no existe."
                )
        );

        if (!propietario.isActivo()
                || propietario.getRol() == null
                || propietario.getRol().getNombre()
                != TipoRol.ROLE_EMPRENDEDOR) {

            throw new IllegalArgumentException(
                    "El usuario no puede crear emprendimientos."
            );
        }

        Emprendimiento emprendimiento =
                new Emprendimiento(
                        propietario,
                        nombre.trim(),
                        descripcion,
                        contacto
                );

        emprendimientoDAO.crear(emprendimiento);
    }

    @Override
    public void actualizar(
            Integer idPropietario,
            Integer idEmprendimiento,
            String nombre,
            String descripcion,
            String contacto) {

        validarNombre(nombre);

        Emprendimiento emprendimiento =
                buscarPropioPorId(
                        idEmprendimiento,
                        idPropietario
                );

        if (emprendimiento == null) {
            throw new IllegalArgumentException(
                    "El emprendimiento no existe o no te pertenece."
            );
        }

        emprendimiento.setNombre(nombre.trim());
        emprendimiento.setDescripcion(descripcion);
        emprendimiento.setContacto(contacto);

        if (!emprendimientoDAO.actualizar(emprendimiento)) {
            throw new IllegalStateException(
                    "No se pudo actualizar el emprendimiento."
            );
        }
    }

    @Override
    public void cambiarEstado(
            Integer idUsuario,
            Integer idEmprendimiento,
            boolean activo) {

        Usuario administrador = usuarioDAO.buscarPorId(
                idUsuario
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "El usuario no existe."
                )
        );

        if (!administrador.isActivo()
                || administrador.getRol() == null
                || administrador.getRol().getNombre()
                != TipoRol.ROLE_ADMIN) {

            throw new IllegalArgumentException(
                    "Se requieren permisos de administrador."
            );
        }

        Emprendimiento emprendimiento =
                emprendimientoDAO.buscarPorId(
                        idEmprendimiento
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "El emprendimiento no existe."
                        )
                );

        emprendimiento.setActivo(activo);

        if (!emprendimientoDAO.actualizar(emprendimiento)) {
            throw new IllegalStateException(
                    "No se pudo cambiar el estado."
            );
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del emprendimiento es obligatorio."
            );
        }
    }
}