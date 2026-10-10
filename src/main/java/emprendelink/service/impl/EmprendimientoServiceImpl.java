package emprendelink.service.impl;

import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.EmprendimientoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class EmprendimientoServiceImpl
        implements EmprendimientoService {

    private static final int MAX_NOMBRE = 120;
    private static final int MAX_CONTACTO = 120;

    private final EmprendimientoDAO emprendimientoDAO;
    private final UsuarioDAO usuarioDAO;

    @Inject
    public EmprendimientoServiceImpl(
            EmprendimientoDAO emprendimientoDAO,
            UsuarioDAO usuarioDAO) {

        this.emprendimientoDAO =
                emprendimientoDAO;

        this.usuarioDAO =
                usuarioDAO;
    }

    @Override
    public List<Emprendimiento> listarActivos() {

        return emprendimientoDAO
                .listarTodos()
                .stream()
                .filter(
                        Emprendimiento::isActivo
                )
                .filter(e ->
                        e.getPropietario() != null
                )
                .filter(e ->
                        e.getPropietario()
                                .isActivo()
                )
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

        return emprendimientoDAO
                .listarPorPropietario(
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
                emprendimientoDAO
                        .buscarPorId(
                                idEmprendimiento
                        )
                        .orElse(null);

        if (emprendimiento == null
                || !emprendimiento.isActivo()
                || emprendimiento
                .getPropietario() == null
                || !emprendimiento
                .getPropietario()
                .isActivo()) {

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
                emprendimientoDAO
                        .buscarPorId(
                                idEmprendimiento
                        )
                        .orElse(null);

        if (emprendimiento == null
                || emprendimiento
                .getPropietario() == null
                || !Objects.equals(
                emprendimiento
                        .getPropietario()
                        .getIdUsuario(),
                idPropietario
        )) {

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

        String nombreNormalizado =
                validarYNormalizarNombre(
                        nombre
                );

        Usuario propietario =
                obtenerEmprendedorActivo(
                        idPropietario
                );

        Emprendimiento emprendimiento =
                new Emprendimiento(
                        propietario,
                        nombreNormalizado,
                        normalizarOpcional(
                                descripcion
                        ),
                        normalizarContacto(
                                contacto
                        )
                );

        emprendimientoDAO.crear(
                emprendimiento
        );
    }

    @Override
    public void actualizar(
            Integer idPropietario,
            Integer idEmprendimiento,
            String nombre,
            String descripcion,
            String contacto) {

        obtenerEmprendedorActivo(
                idPropietario
        );

        String nombreNormalizado =
                validarYNormalizarNombre(
                        nombre
                );

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

        emprendimiento.setNombre(
                nombreNormalizado
        );

        emprendimiento.setDescripcion(
                normalizarOpcional(
                        descripcion
                )
        );

        emprendimiento.setContacto(
                normalizarContacto(
                        contacto
                )
        );

        if (!emprendimientoDAO.actualizar(
                emprendimiento
        )) {

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

        Usuario administrador =
                usuarioDAO
                        .buscarPorId(
                                idUsuario
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El usuario no existe."
                                )
                        );

        if (!administrador.isActivo()
                || administrador.getRol() == null
                || administrador
                .getRol()
                .getNombre()
                != TipoRol.ROLE_ADMIN) {

            throw new IllegalArgumentException(
                    "Se requieren permisos de administrador."
            );
        }

        Emprendimiento emprendimiento =
                emprendimientoDAO
                        .buscarPorId(
                                idEmprendimiento
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El emprendimiento no existe."
                                )
                        );

        emprendimiento.setActivo(
                activo
        );

        if (!emprendimientoDAO.actualizar(
                emprendimiento
        )) {

            throw new IllegalStateException(
                    "No se pudo cambiar el estado."
            );
        }
    }

    private Usuario obtenerEmprendedorActivo(
            Integer idUsuario) {

        if (idUsuario == null) {

            throw new IllegalArgumentException(
                    "El propietario es obligatorio."
            );
        }

        Usuario usuario =
                usuarioDAO
                        .buscarPorId(
                                idUsuario
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El propietario no existe."
                                )
                        );

        if (!usuario.isActivo()
                || usuario.getRol() == null
                || usuario
                .getRol()
                .getNombre()
                != TipoRol.ROLE_EMPRENDEDOR) {

            throw new IllegalArgumentException(
                    "El usuario no puede administrar emprendimientos."
            );
        }

        return usuario;
    }

    private String validarYNormalizarNombre(
            String nombre) {

        if (nombre == null
                || nombre.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre del emprendimiento es obligatorio."
            );
        }

        String normalizado =
                nombre.trim();

        if (normalizado.length()
                > MAX_NOMBRE) {

            throw new IllegalArgumentException(
                    "El nombre no puede superar 120 caracteres."
            );
        }

        return normalizado;
    }

    private String normalizarContacto(
            String contacto) {

        String normalizado =
                normalizarOpcional(
                        contacto
                );

        if (normalizado != null
                && normalizado.length()
                > MAX_CONTACTO) {

            throw new IllegalArgumentException(
                    "El contacto no puede superar 120 caracteres."
            );
        }

        return normalizado;
    }

    private String normalizarOpcional(
            String valor) {

        if (valor == null
                || valor.isBlank()) {

            return null;
        }

        return valor.trim();
    }
}