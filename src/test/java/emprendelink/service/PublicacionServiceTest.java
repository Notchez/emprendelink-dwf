package emprendelink.service;

import emprendelink.dao.CategoriaDAO;
import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.PublicacionDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Categoria;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Publicacion;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.impl.PublicacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicacionServiceTest {

    private FakePublicacionDAO publicacionDAO;
    private FakeEmprendimientoDAO emprendimientoDAO;
    private FakeCategoriaDAO categoriaDAO;
    private FakeUsuarioDAO usuarioDAO;

    private PublicacionServiceImpl publicacionService;

    @BeforeEach
    void preparar() {

        publicacionDAO =
                new FakePublicacionDAO();

        emprendimientoDAO =
                new FakeEmprendimientoDAO();

        categoriaDAO =
                new FakeCategoriaDAO();

        usuarioDAO =
                new FakeUsuarioDAO();

        publicacionService =
                new PublicacionServiceImpl(
                        publicacionDAO,
                        emprendimientoDAO,
                        categoriaDAO,
                        usuarioDAO
                );
    }

    @Test
    void creaProductoNormalizado() {

        Usuario propietario =
                crearUsuario(
                        1,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        usuarioDAO.agregar(propietario);

        Emprendimiento emprendimiento =
                crearEmprendimiento(
                        10,
                        propietario,
                        true
                );

        emprendimientoDAO.agregar(
                emprendimiento
        );

        Categoria categoria =
                crearCategoria(
                        20,
                        true
                );

        categoriaDAO.agregar(
                categoria
        );

        publicacionService.crear(
                1,
                10,
                20,
                TipoPublicacion.PRODUCTO,
                "  Café Premium  ",
                "  Bolsa de café artesanal  ",
                new BigDecimal("8.50"),
                12
        );

        Publicacion publicacion =
                publicacionDAO
                        .listarTodos()
                        .get(0);

        assertEquals(
                "Café Premium",
                publicacion.getNombre()
        );

        assertEquals(
                "Bolsa de café artesanal",
                publicacion.getDescripcion()
        );

        assertEquals(
                new BigDecimal("8.50"),
                publicacion.getPrecio()
        );

        assertEquals(
                12,
                publicacion.getStock()
        );

        assertEquals(
                10,
                publicacion
                        .getEmprendimiento()
                        .getIdEmprendimiento()
        );

        assertEquals(
                20,
                publicacion
                        .getCategoria()
                        .getIdCategoria()
        );

        assertTrue(
                publicacion.isActivo()
        );
    }

    @Test
    void servicioNormalizaStockACero() {

        Usuario propietario =
                crearUsuario(
                        1,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        usuarioDAO.agregar(propietario);

        emprendimientoDAO.agregar(
                crearEmprendimiento(
                        10,
                        propietario,
                        true
                )
        );

        categoriaDAO.agregar(
                crearCategoria(
                        20,
                        true
                )
        );

        publicacionService.crear(
                1,
                10,
                20,
                TipoPublicacion.SERVICIO,
                "Diseño de logotipo",
                null,
                new BigDecimal("25.00"),
                50
        );

        Publicacion publicacion =
                publicacionDAO
                        .listarTodos()
                        .get(0);

        assertEquals(
                0,
                publicacion.getStock()
        );
    }

    @Test
    void rechazaCategoriaInactiva() {

        Usuario propietario =
                crearUsuario(
                        1,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        usuarioDAO.agregar(propietario);

        emprendimientoDAO.agregar(
                crearEmprendimiento(
                        10,
                        propietario,
                        true
                )
        );

        categoriaDAO.agregar(
                crearCategoria(
                        20,
                        false
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> publicacionService.crear(
                        1,
                        10,
                        20,
                        TipoPublicacion.PRODUCTO,
                        "Producto",
                        null,
                        new BigDecimal("10.00"),
                        5
                )
        );
    }

    @Test
    void rechazaEmprendimientoAjeno() {

        Usuario usuarioActual =
                crearUsuario(
                        1,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        Usuario otroPropietario =
                crearUsuario(
                        2,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        usuarioDAO.agregar(
                usuarioActual
        );

        usuarioDAO.agregar(
                otroPropietario
        );

        emprendimientoDAO.agregar(
                crearEmprendimiento(
                        10,
                        otroPropietario,
                        true
                )
        );

        categoriaDAO.agregar(
                crearCategoria(
                        20,
                        true
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> publicacionService.crear(
                        1,
                        10,
                        20,
                        TipoPublicacion.PRODUCTO,
                        "Producto ajeno",
                        null,
                        new BigDecimal("10.00"),
                        5
                )
        );
    }

    @Test
    void propietarioPuedeDesactivarPublicacion() {

        Usuario propietario =
                crearUsuario(
                        1,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        usuarioDAO.agregar(
                propietario
        );

        Emprendimiento emprendimiento =
                crearEmprendimiento(
                        10,
                        propietario,
                        true
                );

        Categoria categoria =
                crearCategoria(
                        20,
                        true
                );

        Publicacion publicacion =
                crearPublicacion(
                        30,
                        emprendimiento,
                        categoria
                );

        publicacionDAO.agregar(
                publicacion
        );

        publicacionService.cambiarEstado(
                1,
                30,
                false
        );

        assertFalse(
                publicacion.isActivo()
        );
    }

    @Test
    void clienteNoPuedeCambiarEstadoPublicacion() {

        Usuario propietario =
                crearUsuario(
                        1,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        Usuario cliente =
                crearUsuario(
                        2,
                        TipoRol.ROLE_CLIENTE,
                        true
                );

        usuarioDAO.agregar(
                propietario
        );

        usuarioDAO.agregar(
                cliente
        );

        Emprendimiento emprendimiento =
                crearEmprendimiento(
                        10,
                        propietario,
                        true
                );

        Categoria categoria =
                crearCategoria(
                        20,
                        true
                );

        publicacionDAO.agregar(
                crearPublicacion(
                        30,
                        emprendimiento,
                        categoria
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> publicacionService.cambiarEstado(
                        2,
                        30,
                        false
                )
        );
    }

    private Usuario crearUsuario(
            Integer id,
            TipoRol tipoRol,
            boolean activo) {

        Integer idRol =
                switch (tipoRol) {
                    case ROLE_ADMIN -> 1;
                    case ROLE_EMPRENDEDOR -> 2;
                    case ROLE_CLIENTE -> 3;
                };

        Rol rol =
                new Rol(
                        idRol,
                        tipoRol
                );

        return new Usuario(
                id,
                rol,
                "Usuario",
                "Prueba",
                "usuario" + id + "@test.com",
                "hash",
                "7000-0000",
                activo,
                LocalDateTime.now()
        );
    }

    private Emprendimiento crearEmprendimiento(
            Integer id,
            Usuario propietario,
            boolean activo) {

        return new Emprendimiento(
                id,
                propietario,
                "Emprendimiento " + id,
                "Descripción",
                "7000-0000",
                activo,
                LocalDateTime.now()
        );
    }

    private Categoria crearCategoria(
            Integer id,
            boolean activo) {

        return new Categoria(
                id,
                "Categoría " + id,
                "Descripción",
                activo
        );
    }

    private Publicacion crearPublicacion(
            Integer id,
            Emprendimiento emprendimiento,
            Categoria categoria) {

        return new Publicacion(
                id,
                emprendimiento,
                categoria,
                TipoPublicacion.PRODUCTO,
                "Producto",
                "Descripción",
                new BigDecimal("10.00"),
                5,
                true,
                LocalDateTime.now()
        );
    }

    private static class FakePublicacionDAO
            implements PublicacionDAO {

        private final List<Publicacion> publicaciones =
                new ArrayList<>();

        private int siguienteId = 1;

        void agregar(
                Publicacion publicacion) {

            publicaciones.add(
                    publicacion
            );
        }

        @Override
        public Publicacion crear(
                Publicacion publicacion) {

            publicacion.setIdPublicacion(
                    siguienteId++
            );

            publicaciones.add(
                    publicacion
            );

            return publicacion;
        }

        @Override
        public Optional<Publicacion> buscarPorId(
                Integer idPublicacion) {

            return publicaciones
                    .stream()
                    .filter(p ->
                            p.getIdPublicacion()
                                    .equals(idPublicacion)
                    )
                    .findFirst();
        }

        @Override
        public List<Publicacion> listarTodos() {

            return new ArrayList<>(
                    publicaciones
            );
        }

        @Override
        public List<Publicacion> listarPorEmprendimiento(
                Integer idEmprendimiento) {

            return publicaciones
                    .stream()
                    .filter(p ->
                            p.getEmprendimiento() != null
                                    && p.getEmprendimiento()
                                    .getIdEmprendimiento()
                                    .equals(idEmprendimiento)
                    )
                    .toList();
        }

        @Override
        public List<Publicacion> listarPorCategoria(
                Integer idCategoria) {

            return publicaciones
                    .stream()
                    .filter(p ->
                            p.getCategoria() != null
                                    && p.getCategoria()
                                    .getIdCategoria()
                                    .equals(idCategoria)
                    )
                    .toList();
        }

        @Override
        public boolean actualizar(
                Publicacion publicacion) {

            return buscarPorId(
                    publicacion.getIdPublicacion()
            ).isPresent();
        }

        @Override
        public boolean eliminar(
                Integer idPublicacion) {

            return publicaciones.removeIf(
                    p ->
                            p.getIdPublicacion()
                                    .equals(idPublicacion)
            );
        }
    }

    private static class FakeEmprendimientoDAO
            implements EmprendimientoDAO {

        private final List<Emprendimiento> emprendimientos =
                new ArrayList<>();

        void agregar(
                Emprendimiento emprendimiento) {

            emprendimientos.add(
                    emprendimiento
            );
        }

        @Override
        public Emprendimiento crear(
                Emprendimiento emprendimiento) {

            emprendimientos.add(
                    emprendimiento
            );

            return emprendimiento;
        }

        @Override
        public Optional<Emprendimiento> buscarPorId(
                Integer idEmprendimiento) {

            return emprendimientos
                    .stream()
                    .filter(e ->
                            e.getIdEmprendimiento()
                                    .equals(idEmprendimiento)
                    )
                    .findFirst();
        }

        @Override
        public List<Emprendimiento> listarTodos() {

            return new ArrayList<>(
                    emprendimientos
            );
        }

        @Override
        public List<Emprendimiento> listarPorPropietario(
                Integer idUsuario) {

            return emprendimientos
                    .stream()
                    .filter(e ->
                            e.getPropietario() != null
                                    && e.getPropietario()
                                    .getIdUsuario()
                                    .equals(idUsuario)
                    )
                    .toList();
        }

        @Override
        public boolean actualizar(
                Emprendimiento emprendimiento) {

            return buscarPorId(
                    emprendimiento.getIdEmprendimiento()
            ).isPresent();
        }

        @Override
        public boolean eliminar(
                Integer idEmprendimiento) {

            return emprendimientos.removeIf(
                    e ->
                            e.getIdEmprendimiento()
                                    .equals(idEmprendimiento)
            );
        }
    }

    private static class FakeCategoriaDAO
            implements CategoriaDAO {

        private final List<Categoria> categorias =
                new ArrayList<>();

        void agregar(
                Categoria categoria) {

            categorias.add(
                    categoria
            );
        }

        @Override
        public Categoria crear(
                Categoria categoria) {

            categorias.add(
                    categoria
            );

            return categoria;
        }

        @Override
        public Optional<Categoria> buscarPorId(
                Integer idCategoria) {

            return categorias
                    .stream()
                    .filter(c ->
                            c.getIdCategoria()
                                    .equals(idCategoria)
                    )
                    .findFirst();
        }

        @Override
        public List<Categoria> listarTodos() {

            return new ArrayList<>(
                    categorias
            );
        }

        @Override
        public boolean actualizar(
                Categoria categoria) {

            return buscarPorId(
                    categoria.getIdCategoria()
            ).isPresent();
        }

        @Override
        public boolean eliminar(
                Integer idCategoria) {

            return categorias.removeIf(
                    c ->
                            c.getIdCategoria()
                                    .equals(idCategoria)
            );
        }
    }

    private static class FakeUsuarioDAO
            implements UsuarioDAO {

        private final List<Usuario> usuarios =
                new ArrayList<>();

        void agregar(
                Usuario usuario) {

            usuarios.add(
                    usuario
            );
        }

        @Override
        public Usuario crear(
                Usuario usuario) {

            usuarios.add(
                    usuario
            );

            return usuario;
        }

        @Override
        public Optional<Usuario> buscarPorId(
                Integer idUsuario) {

            return usuarios
                    .stream()
                    .filter(u ->
                            u.getIdUsuario()
                                    .equals(idUsuario)
                    )
                    .findFirst();
        }

        @Override
        public Optional<Usuario> buscarPorCorreo(
                String correo) {

            return usuarios
                    .stream()
                    .filter(u ->
                            u.getCorreo()
                                    .equalsIgnoreCase(correo)
                    )
                    .findFirst();
        }

        @Override
        public List<Usuario> listarTodos() {

            return new ArrayList<>(
                    usuarios
            );
        }

        @Override
        public boolean actualizar(
                Usuario usuario) {

            return buscarPorId(
                    usuario.getIdUsuario()
            ).isPresent();
        }

        @Override
        public boolean eliminar(
                Integer idUsuario) {

            return usuarios.removeIf(
                    u ->
                            u.getIdUsuario()
                                    .equals(idUsuario)
            );
        }
    }
}