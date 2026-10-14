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
import emprendelink.service.PublicacionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class PublicacionServiceImpl
        implements PublicacionService {

    private static final int MAX_NOMBRE = 150;
    private static final BigDecimal PRECIO_MAXIMO =
            new BigDecimal("99999999.99");

    private final PublicacionDAO publicacionDAO;
    private final EmprendimientoDAO emprendimientoDAO;
    private final CategoriaDAO categoriaDAO;
    private final UsuarioDAO usuarioDAO;

    @Inject
    public PublicacionServiceImpl(
            PublicacionDAO publicacionDAO,
            EmprendimientoDAO emprendimientoDAO,
            CategoriaDAO categoriaDAO,
            UsuarioDAO usuarioDAO) {

        this.publicacionDAO = publicacionDAO;
        this.emprendimientoDAO = emprendimientoDAO;
        this.categoriaDAO = categoriaDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public List<Publicacion> listarActivas() {

        return publicacionDAO.listarTodos()
                .stream()
                .filter(this::esVisible)
                .toList();
    }

    @Override
    public List<Publicacion> listarTodas() {
        return publicacionDAO.listarTodos();
    }

    @Override
    public List<Publicacion> listarPorPropietario(
            Integer idPropietario) {

        if (idPropietario == null) {
            throw new IllegalArgumentException(
                    "El propietario es obligatorio."
            );
        }

        return publicacionDAO.listarTodos()
                .stream()
                .filter(p -> perteneceA(
                        p.getEmprendimiento(),
                        idPropietario
                ))
                .toList();
    }

    @Override
    public Publicacion buscarActivaPorId(
            Integer idPublicacion) {

        if (idPublicacion == null) {
            return null;
        }

        Publicacion publicacion =
                publicacionDAO.buscarPorId(
                        idPublicacion
                ).orElse(null);

        return esVisible(publicacion)
                ? publicacion
                : null;
    }

    @Override
    public Publicacion buscarPropiaPorId(
            Integer idPublicacion,
            Integer idPropietario) {

        if (idPublicacion == null
                || idPropietario == null) {
            return null;
        }

        Publicacion publicacion =
                publicacionDAO.buscarPorId(
                        idPublicacion
                ).orElse(null);

        if (publicacion == null
                || !perteneceA(
                publicacion.getEmprendimiento(),
                idPropietario)) {

            return null;
        }

        return publicacion;
    }

    @Override
    public void crear(
            Integer idPropietario,
            Integer idEmprendimiento,
            Integer idCategoria,
            TipoPublicacion tipo,
            String nombre,
            String descripcion,
            BigDecimal precio,
            Integer stock) {

        validarEmprendedorActivo(idPropietario);
        validarDatos(tipo, nombre, precio, stock);

        Emprendimiento emprendimiento =
                obtenerEmprendimientoPropio(
                        idEmprendimiento,
                        idPropietario
                );

        if (!emprendimiento.isActivo()) {
            throw new IllegalArgumentException(
                    "El emprendimiento está inactivo."
            );
        }

        Categoria categoria =
                obtenerCategoriaActiva(idCategoria);

        Publicacion publicacion = new Publicacion(
                emprendimiento,
                categoria,
                tipo,
                nombre.trim(),
                normalizarOpcional(descripcion),
                precio,
                normalizarStock(tipo, stock)
        );

        publicacionDAO.crear(publicacion);
    }

    @Override
    public void actualizar(
            Integer idPropietario,
            Integer idPublicacion,
            Integer idEmprendimiento,
            Integer idCategoria,
            TipoPublicacion tipo,
            String nombre,
            String descripcion,
            BigDecimal precio,
            Integer stock) {

        validarEmprendedorActivo(idPropietario);
        validarDatos(tipo, nombre, precio, stock);

        Publicacion publicacion =
                buscarPropiaPorId(
                        idPublicacion,
                        idPropietario
                );

        if (publicacion == null) {
            throw new IllegalArgumentException(
                    "La publicación no existe o no te pertenece."
            );
        }

        Emprendimiento emprendimiento =
                obtenerEmprendimientoPropio(
                        idEmprendimiento,
                        idPropietario
                );

        if (!Objects.equals(
                publicacion.getEmprendimiento()
                        .getIdEmprendimiento(),
                emprendimiento.getIdEmprendimiento())) {

            throw new IllegalArgumentException(
                    "No se permite trasladar la publicación a otro emprendimiento."
            );
        }

        Categoria categoria =
                obtenerCategoriaActiva(idCategoria);

        publicacion.setCategoria(categoria);
        publicacion.setTipo(tipo);
        publicacion.setNombre(nombre.trim());
        publicacion.setDescripcion(
                normalizarOpcional(descripcion)
        );
        publicacion.setPrecio(precio);
        publicacion.setStock(
                normalizarStock(tipo, stock)
        );

        if (!publicacionDAO.actualizar(publicacion)) {
            throw new IllegalStateException(
                    "No se pudo actualizar la publicación."
            );
        }
    }

    @Override
    public void cambiarEstado(
            Integer idUsuario,
            Integer idPublicacion,
            boolean activo) {

        Usuario usuario =
                obtenerUsuarioActivo(idUsuario);

        Publicacion publicacion =
                publicacionDAO.buscarPorId(
                        idPublicacion
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "La publicación no existe."
                        )
                );

        TipoRol rol = usuario.getRol() != null
                ? usuario.getRol().getNombre()
                : null;

        boolean esAdmin =
                rol == TipoRol.ROLE_ADMIN;

        boolean esPropietario =
                rol == TipoRol.ROLE_EMPRENDEDOR
                        && perteneceA(
                        publicacion.getEmprendimiento(),
                        usuario.getIdUsuario()
                );

        if (!esAdmin && !esPropietario) {
            throw new IllegalArgumentException(
                    "No tienes permiso para cambiar esta publicación."
            );
        }

        publicacion.setActivo(activo);

        if (!publicacionDAO.actualizar(publicacion)) {
            throw new IllegalStateException(
                    "No se pudo cambiar el estado."
            );
        }
    }

    private Usuario obtenerUsuarioActivo(
            Integer idUsuario) {

        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El usuario es obligatorio."
            );
        }

        Usuario usuario = usuarioDAO.buscarPorId(
                idUsuario
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "El usuario no existe."
                )
        );

        if (!usuario.isActivo()) {
            throw new IllegalArgumentException(
                    "El usuario está inactivo."
            );
        }

        return usuario;
    }

    private void validarEmprendedorActivo(
            Integer idUsuario) {

        Usuario usuario =
                obtenerUsuarioActivo(idUsuario);

        if (usuario.getRol() == null
                || usuario.getRol().getNombre()
                != TipoRol.ROLE_EMPRENDEDOR) {

            throw new IllegalArgumentException(
                    "Se requiere una cuenta de emprendedor."
            );
        }
    }

    private Emprendimiento obtenerEmprendimientoPropio(
            Integer idEmprendimiento,
            Integer idPropietario) {

        if (idEmprendimiento == null
                || idPropietario == null) {
            throw new IllegalArgumentException(
                    "Falta identificar el emprendimiento."
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

        if (!perteneceA(
                emprendimiento,
                idPropietario)) {

            throw new IllegalArgumentException(
                    "El emprendimiento no te pertenece."
            );
        }

        return emprendimiento;
    }

    private Categoria obtenerCategoriaActiva(
            Integer idCategoria) {

        if (idCategoria == null) {
            throw new IllegalArgumentException(
                    "Seleccione una categoría."
            );
        }

        Categoria categoria = categoriaDAO.buscarPorId(
                idCategoria
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "La categoría no existe."
                )
        );

        if (!categoria.isActivo()) {
            throw new IllegalArgumentException(
                    "La categoría está inactiva."
            );
        }

        return categoria;
    }

    private boolean perteneceA(
            Emprendimiento emprendimiento,
            Integer idPropietario) {

        return emprendimiento != null
                && emprendimiento.getPropietario() != null
                && Objects.equals(
                emprendimiento.getPropietario()
                        .getIdUsuario(),
                idPropietario
        );
    }

    private boolean esVisible(
            Publicacion publicacion) {

        if (publicacion == null
                || !publicacion.isActivo()
                || publicacion.getCategoria() == null
                || !publicacion.getCategoria().isActivo()) {

            return false;
        }

        Emprendimiento emprendimiento =
                publicacion.getEmprendimiento();

        return emprendimiento != null
                && emprendimiento.isActivo()
                && emprendimiento.getPropietario() != null
                && emprendimiento.getPropietario().isActivo();
    }

    private void validarDatos(
            TipoPublicacion tipo,
            String nombre,
            BigDecimal precio,
            Integer stock) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Seleccione el tipo de publicación."
            );
        }

        if (nombre == null
                || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (nombre.trim().length() > MAX_NOMBRE) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar 150 caracteres."
            );
        }

        if (precio == null
                || precio.signum() < 0
                || precio.compareTo(PRECIO_MAXIMO) > 0
                || precio.stripTrailingZeros().scale() > 2) {

            throw new IllegalArgumentException(
                    "El precio debe estar entre 0.00 y 99999999.99 con máximo 2 decimales."
            );
        }

        if (stock != null && stock < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }
    }

    private Integer normalizarStock(
            TipoPublicacion tipo,
            Integer stock) {

        if (tipo == TipoPublicacion.SERVICIO) {
            return 0;
        }

        return stock == null ? 0 : stock;
    }

    private String normalizarOpcional(
            String valor) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}
