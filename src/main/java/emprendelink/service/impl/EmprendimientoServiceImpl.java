package emprendelink.service.impl;

import emprendelink.dao.CategoriaDAO;
import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.PublicacionDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Categoria;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Publicacion;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.EmprendimientoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class EmprendimientoServiceImpl
        implements EmprendimientoService {

    private static final int MAX_NOMBRE = 120;
    private static final int MAX_CONTACTO = 120;
    private static final int MAX_LOGO_URL = 1000;

    private final EmprendimientoDAO emprendimientoDAO;
    private final UsuarioDAO usuarioDAO;

    @Inject
    private CategoriaDAO categoriaDAO;

    @Inject
    private PublicacionDAO publicacionDAO;

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
                .filter(
                        negocio ->
                                negocio.getPropietario()
                                        != null
                )
                .filter(
                        negocio ->
                                negocio
                                        .getPropietario()
                                        .isActivo()
                )
                .toList();
    }

    @Override
    public List<Emprendimiento> listarTodos() {

        return emprendimientoDAO
                .listarTodos();
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

        Emprendimiento negocio =
                emprendimientoDAO
                        .buscarPorId(
                                idEmprendimiento
                        )
                        .orElse(null);

        if (negocio == null
                || !negocio.isActivo()
                || negocio.getPropietario() == null
                || !negocio
                .getPropietario()
                .isActivo()) {

            return null;
        }

        return negocio;
    }

    @Override
    public Emprendimiento buscarPropioPorId(
            Integer idEmprendimiento,
            Integer idPropietario) {

        if (idEmprendimiento == null
                || idPropietario == null) {

            return null;
        }

        Emprendimiento negocio =
                emprendimientoDAO
                        .buscarPorId(
                                idEmprendimiento
                        )
                        .orElse(null);

        if (negocio == null
                || negocio.getPropietario() == null
                || !Objects.equals(
                negocio.getPropietario()
                        .getIdUsuario(),
                idPropietario
        )) {

            return null;
        }

        return negocio;
    }

    /*
     * Se conserva por compatibilidad interna y pruebas antiguas.
     * La interfaz del emprendedor ya no expone creación de negocios.
     */
    @Override
    public void crear(
            Integer idPropietario,
            String nombre,
            String descripcion,
            String contacto) {

        Usuario propietario =
                obtenerEmprendedorActivo(
                        idPropietario
                );

        if (!emprendimientoDAO
                .listarPorPropietario(
                        idPropietario
                )
                .isEmpty()) {

            throw new IllegalArgumentException(
                    "El emprendedor ya tiene un negocio asociado."
            );
        }

        Emprendimiento negocio =
                new Emprendimiento(
                        propietario,
                        validarYNormalizarNombre(
                                nombre
                        ),
                        normalizarOpcional(
                                descripcion
                        ),
                        normalizarContacto(
                                contacto
                        )
                );

        if (categoriaDAO != null) {

            categoriaDAO.listarTodos()
                    .stream()
                    .filter(
                            Categoria::isActivo
                    )
                    .findFirst()
                    .ifPresent(
                            negocio::setCategoria
                    );
        }

        emprendimientoDAO.crear(
                negocio
        );
    }

    /*
     * Método histórico.
     * El nombre recibido se ignora intencionalmente:
     * el emprendedor ya no puede cambiar el nombre.
     */
    @Override
    public void actualizar(
            Integer idPropietario,
            Integer idEmprendimiento,
            String nombre,
            String descripcion,
            String contacto) {

        Emprendimiento negocio =
                buscarPropioPorId(
                        idEmprendimiento,
                        idPropietario
                );

        actualizarDatosEmprendedor(
                idPropietario,
                idEmprendimiento,
                descripcion,
                contacto,
                negocio == null
                        ? null
                        : negocio.getLogoUrl()
        );
    }

    @Override
    public void actualizarDatosEmprendedor(
            Integer idPropietario,
            Integer idEmprendimiento,
            String descripcion,
            String contacto,
            String logoUrl) {

        obtenerEmprendedorActivo(
                idPropietario
        );

        Emprendimiento negocio =
                buscarPropioPorId(
                        idEmprendimiento,
                        idPropietario
                );

        if (negocio == null) {

            throw new IllegalArgumentException(
                    "El negocio no existe o no te pertenece."
            );
        }

        negocio.setDescripcion(
                normalizarOpcional(
                        descripcion
                )
        );

        negocio.setContacto(
                normalizarContacto(
                        contacto
                )
        );

        negocio.setLogoUrl(
                normalizarUrlImagen(
                        logoUrl
                )
        );

        if (!emprendimientoDAO.actualizar(
                negocio
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar el negocio."
            );
        }
    }

    @Override
    public void actualizarDatosAdministrativos(
            Integer idAdministrador,
            Integer idEmprendimiento,
            String nombre,
            Integer idCategoria,
            String logoUrl) {

        obtenerAdministradorActivo(
                idAdministrador
        );

        Emprendimiento negocio =
                emprendimientoDAO
                        .buscarPorId(
                                idEmprendimiento
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "El negocio no existe."
                                        )
                        );

        Categoria categoria =
                categoriaDAO
                        .buscarPorId(
                                idCategoria
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "La categoría no existe."
                                        )
                        );

        if (!categoria.isActivo()) {

            throw new IllegalArgumentException(
                    "La categoría seleccionada está inactiva."
            );
        }

        negocio.setNombre(
                validarYNormalizarNombre(
                        nombre
                )
        );

        negocio.setCategoria(
                categoria
        );

        negocio.setLogoUrl(
                normalizarUrlImagen(
                        logoUrl
                )
        );

        if (!emprendimientoDAO.actualizar(
                negocio
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar el negocio."
            );
        }

        sincronizarCategoriaPublicaciones(
                negocio,
                categoria
        );
    }

    @Override
    public void cambiarEstado(
            Integer idUsuario,
            Integer idEmprendimiento,
            boolean activo) {

        obtenerAdministradorActivo(
                idUsuario
        );

        Emprendimiento negocio =
                emprendimientoDAO
                        .buscarPorId(
                                idEmprendimiento
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "El negocio no existe."
                                        )
                        );

        negocio.setActivo(
                activo
        );

        if (!emprendimientoDAO.actualizar(
                negocio
        )) {

            throw new IllegalStateException(
                    "No se pudo cambiar el estado."
            );
        }
    }

    private void sincronizarCategoriaPublicaciones(
            Emprendimiento negocio,
            Categoria categoria) {

        if (publicacionDAO == null
                || negocio == null
                || negocio.getIdEmprendimiento()
                == null) {

            return;
        }

        TipoPublicacion tipo =
                categoria.isManejaStock()
                        ? TipoPublicacion.PRODUCTO
                        : TipoPublicacion.SERVICIO;

        for (Publicacion publicacion
                : publicacionDAO
                .listarPorEmprendimiento(
                        negocio.getIdEmprendimiento()
                )) {

            publicacion.setCategoria(
                    categoria
            );

            publicacion.setTipo(
                    tipo
            );

            if (!categoria.isManejaStock()) {

                publicacion.setStock(
                        0
                );
            }

            publicacionDAO.actualizar(
                    publicacion
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
                        .orElseThrow(
                                () ->
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
                    "Se requiere una cuenta de emprendedor activa."
            );
        }

        return usuario;
    }

    private Usuario obtenerAdministradorActivo(
            Integer idUsuario) {

        if (idUsuario == null) {

            throw new IllegalArgumentException(
                    "No se pudo identificar al administrador."
            );
        }

        Usuario usuario =
                usuarioDAO
                        .buscarPorId(
                                idUsuario
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "El administrador no existe."
                                        )
                        );

        if (!usuario.isActivo()
                || usuario.getRol() == null
                || usuario
                .getRol()
                .getNombre()
                != TipoRol.ROLE_ADMIN) {

            throw new IllegalArgumentException(
                    "Se requieren permisos de administrador."
            );
        }

        return usuario;
    }

    private String validarYNormalizarNombre(
            String nombre) {

        if (nombre == null
                || nombre.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre del negocio es obligatorio."
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

    private String normalizarUrlImagen(
            String valor) {

        String normalizado =
                normalizarOpcional(
                        valor
                );

        if (normalizado == null) {
            return null;
        }

        if (normalizado.length()
                > MAX_LOGO_URL) {

            throw new IllegalArgumentException(
                    "La URL del logo no puede superar 1000 caracteres."
            );
        }

        try {

            URI uri =
                    URI.create(
                            normalizado
                    );

            String esquema =
                    uri.getScheme();

            if (esquema == null
                    || (
                    !esquema.equalsIgnoreCase(
                            "http"
                    )
                            && !esquema.equalsIgnoreCase(
                            "https"
                    )
            )) {

                throw new IllegalArgumentException();
            }

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "El logo debe utilizar una URL http o https."
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