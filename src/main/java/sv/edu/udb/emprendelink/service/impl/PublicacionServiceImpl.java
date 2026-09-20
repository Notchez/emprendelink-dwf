package sv.edu.udb.emprendelink.service.impl;

import sv.edu.udb.emprendelink.dao.CategoriaDAO;
import sv.edu.udb.emprendelink.dao.EmprendimientoDAO;
import sv.edu.udb.emprendelink.dao.PublicacionDAO;
import sv.edu.udb.emprendelink.dao.UsuarioDAO;
import sv.edu.udb.emprendelink.model.Categoria;
import sv.edu.udb.emprendelink.model.Emprendimiento;
import sv.edu.udb.emprendelink.model.Publicacion;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoPublicacion;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.PublicacionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class PublicacionServiceImpl
        implements PublicacionService {

    private final PublicacionDAO publicacionDAO;
    private final EmprendimientoDAO emprendimientoDAO;
    private final CategoriaDAO categoriaDAO;
    private final UsuarioDAO usuarioDAO;

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
                descripcion,
                precio,
                stock == null ? 0 : stock
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
                    "No se permite trasladar la publicación."
            );
        }

        Categoria categoria =
                obtenerCategoriaActiva(idCategoria);

        publicacion.setCategoria(categoria);
        publicacion.setTipo(tipo);
        publicacion.setNombre(nombre.trim());
        publicacion.setDescripcion(descripcion);
        publicacion.setPrecio(precio);
        publicacion.setStock(stock == null ? 0 : stock);

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

        Publicacion publicacion =
                publicacionDAO.buscarPorId(
                        idPublicacion
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "La publicación no existe."
                        )
                );

        publicacion.setActivo(activo);

        if (!publicacionDAO.actualizar(publicacion)) {
            throw new IllegalStateException(
                    "No se pudo cambiar el estado."
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

        if (!perteneceA(emprendimiento, idPropietario)) {
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

    private boolean esVisible(Publicacion publicacion) {
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

        if (tipo == null
                || nombre == null
                || nombre.isBlank()
                || precio == null
                || precio.signum() < 0
                || (stock != null && stock < 0)) {

            throw new IllegalArgumentException(
                    "Los datos de la publicación son inválidos."
            );
        }
    }
}