package emprendelink.service;

import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.PedidoDAO;
import emprendelink.dao.PublicacionDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Categoria;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.model.Plan;
import emprendelink.model.Publicacion;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoServiceTest {

    private FakePedidoDAO pedidoDAO;
    private FakeUsuarioDAO usuarioDAO;
    private FakeEmprendimientoDAO emprendimientoDAO;
    private FakePublicacionDAO publicacionDAO;

    private PedidoServiceImpl pedidoService;

    @BeforeEach
    void preparar() {

        pedidoDAO =
                new FakePedidoDAO();

        usuarioDAO =
                new FakeUsuarioDAO();

        emprendimientoDAO =
                new FakeEmprendimientoDAO();

        publicacionDAO =
                new FakePublicacionDAO();

        pedidoService =
                new PedidoServiceImpl(
                        pedidoDAO,
                        usuarioDAO,
                        emprendimientoDAO,
                        publicacionDAO
                );
    }

    @Test
    void creaPedidoCalculaTotalYDescuentaStock() {

        Escenario escenario =
                prepararEscenario();

        pedidoService.crear(
                escenario.cliente
                        .getIdUsuario(),
                escenario.emprendimiento
                        .getIdEmprendimiento(),
                List.of(
                        escenario.publicacion
                                .getIdPublicacion()
                ),
                List.of(2),
                "  Entregar por la tarde  "
        );

        Pedido pedido =
                pedidoDAO.listarTodos()
                        .get(0);

        assertEquals(
                new BigDecimal("10.00"),
                pedido.getTotal()
        );

        assertEquals(
                "Entregar por la tarde",
                pedido.getObservaciones()
        );

        assertEquals(
                8,
                escenario.publicacion
                        .getStock()
        );

        /*
         * También comprobamos el snapshot
         * del plan aplicado al pedido.
         */
        assertEquals(
                1,
                pedido.getIdPlanAplicado()
        );

        assertEquals(
                "Básico",
                pedido.getNombrePlanAplicado()
        );

        assertEquals(
                new BigDecimal("0.0700"),
                pedido.getTasaComision()
        );

        assertEquals(
                new BigDecimal("0.70"),
                pedido.getMontoComision()
        );
    }

    @Test
    void rechazaStockInsuficiente() {

        Escenario escenario =
                prepararEscenario();

        assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(
                        escenario.cliente
                                .getIdUsuario(),
                        escenario.emprendimiento
                                .getIdEmprendimiento(),
                        List.of(
                                escenario.publicacion
                                        .getIdPublicacion()
                        ),
                        List.of(50),
                        null
                )
        );
    }

    @Test
    void rechazaUsuarioQueNoEsCliente() {

        Escenario escenario =
                prepararEscenario();

        assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(
                        escenario.propietario
                                .getIdUsuario(),
                        escenario.emprendimiento
                                .getIdEmprendimiento(),
                        List.of(
                                escenario.publicacion
                                        .getIdPublicacion()
                        ),
                        List.of(1),
                        null
                )
        );
    }

    @Test
    void permiteConfirmarPedidoPendiente() {

        Escenario escenario =
                prepararEscenario();

        pedidoService.crear(
                escenario.cliente
                        .getIdUsuario(),
                escenario.emprendimiento
                        .getIdEmprendimiento(),
                List.of(
                        escenario.publicacion
                                .getIdPublicacion()
                ),
                List.of(1),
                null
        );

        pedidoService.actualizarEstado(
                escenario.propietario
                        .getIdUsuario(),
                1,
                EstadoPedido.CONFIRMADO
        );

        assertEquals(
                EstadoPedido.CONFIRMADO,
                pedidoDAO.buscarPorId(1)
                        .orElseThrow()
                        .getEstado()
        );
    }

    @Test
    void rechazaSaltoDirectoACompletado() {

        Escenario escenario =
                prepararEscenario();

        pedidoService.crear(
                escenario.cliente
                        .getIdUsuario(),
                escenario.emprendimiento
                        .getIdEmprendimiento(),
                List.of(
                        escenario.publicacion
                                .getIdPublicacion()
                ),
                List.of(1),
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        pedidoService.actualizarEstado(
                                escenario.propietario
                                        .getIdUsuario(),
                                1,
                                EstadoPedido.COMPLETADO
                        )
        );
    }

    @Test
    void cancelarPedidoRestauraStock() {

        Escenario escenario =
                prepararEscenario();

        pedidoService.crear(
                escenario.cliente
                        .getIdUsuario(),
                escenario.emprendimiento
                        .getIdEmprendimiento(),
                List.of(
                        escenario.publicacion
                                .getIdPublicacion()
                ),
                List.of(2),
                null
        );

        assertEquals(
                8,
                escenario.publicacion
                        .getStock()
        );

        pedidoService.actualizarEstado(
                escenario.propietario
                        .getIdUsuario(),
                1,
                EstadoPedido.CANCELADO
        );

        assertEquals(
                10,
                escenario.publicacion
                        .getStock()
        );
    }

    @Test
    void rechazaCambioDeEstadoPorOtroEmprendedor() {

        Escenario escenario =
                prepararEscenario();

        Usuario otro =
                crearUsuario(
                        99,
                        TipoRol.ROLE_EMPRENDEDOR
                );

        usuarioDAO.agregar(
                otro
        );

        pedidoService.crear(
                escenario.cliente
                        .getIdUsuario(),
                escenario.emprendimiento
                        .getIdEmprendimiento(),
                List.of(
                        escenario.publicacion
                                .getIdPublicacion()
                ),
                List.of(1),
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        pedidoService.actualizarEstado(
                                otro.getIdUsuario(),
                                1,
                                EstadoPedido.CONFIRMADO
                        )
        );
    }

    @Test
    void rechazaPublicacionDeOtroEmprendimiento() {

        Escenario escenario =
                prepararEscenario();

        Plan planOtro =
                new Plan(
                        2,
                        "Estándar",
                        30,
                        new BigDecimal("0.0500"),
                        true
                );

        Emprendimiento otro =
                new Emprendimiento(
                        999,
                        escenario.propietario,
                        planOtro,
                        "Otro negocio",
                        null,
                        null,
                        true,
                        LocalDateTime.now()
                );

        Publicacion ajena =
                crearPublicacion(
                        999,
                        otro,
                        10
                );

        publicacionDAO.agregar(
                ajena
        );

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        pedidoService.crear(
                                escenario.cliente
                                        .getIdUsuario(),
                                escenario.emprendimiento
                                        .getIdEmprendimiento(),
                                List.of(
                                        ajena.getIdPublicacion()
                                ),
                                List.of(1),
                                null
                        )
        );
    }

    private Escenario prepararEscenario() {

        Usuario cliente =
                crearUsuario(
                        1,
                        TipoRol.ROLE_CLIENTE
                );

        Usuario propietario =
                crearUsuario(
                        2,
                        TipoRol.ROLE_EMPRENDEDOR
                );

        usuarioDAO.agregar(
                cliente
        );

        usuarioDAO.agregar(
                propietario
        );

        /*
         * Todo emprendimiento real tiene plan.
         *
         * Antes este fixture creaba el negocio
         * con plan = null, cosa que ya no representa
         * el modelo actual de EmprendeLink.
         */
        Plan plan =
                new Plan(
                        1,
                        "Básico",
                        10,
                        new BigDecimal("0.0700"),
                        true
                );

        Emprendimiento emprendimiento =
                new Emprendimiento(
                        10,
                        propietario,
                        plan,
                        "Panadería",
                        null,
                        null,
                        true,
                        LocalDateTime.now()
                );

        emprendimientoDAO.agregar(
                emprendimiento
        );

        Publicacion publicacion =
                crearPublicacion(
                        20,
                        emprendimiento,
                        10
                );

        publicacionDAO.agregar(
                publicacion
        );

        return new Escenario(
                cliente,
                propietario,
                emprendimiento,
                publicacion
        );
    }

    private Publicacion crearPublicacion(
            Integer id,
            Emprendimiento emprendimiento,
            Integer stock) {

        Categoria categoria =
                new Categoria(
                        1,
                        "Alimentos",
                        null,
                        true
                );

        return new Publicacion(
                id,
                emprendimiento,
                categoria,
                TipoPublicacion.PRODUCTO,
                "Producto",
                null,
                new BigDecimal("5.00"),
                stock,
                true,
                LocalDateTime.now()
        );
    }

    private Usuario crearUsuario(
            Integer id,
            TipoRol tipoRol) {

        int idRol =
                switch (tipoRol) {

                    case ROLE_ADMIN ->
                            1;

                    case ROLE_EMPRENDEDOR ->
                            2;

                    case ROLE_CLIENTE ->
                            3;
                };

        return new Usuario(
                id,
                new Rol(
                        idRol,
                        tipoRol
                ),
                "Usuario",
                "Prueba",
                "usuario"
                        + id
                        + "@test.com",
                "hash",
                "7000-0000",
                true,
                LocalDateTime.now()
        );
    }

    private record Escenario(
            Usuario cliente,
            Usuario propietario,
            Emprendimiento emprendimiento,
            Publicacion publicacion) {
    }

    private static class FakePedidoDAO
            implements PedidoDAO {

        private final List<Pedido> pedidos =
                new ArrayList<>();

        private int siguienteId = 1;

        @Override
        public Pedido crear(
                Pedido pedido) {

            pedido.setIdPedido(
                    siguienteId++
            );

            pedidos.add(
                    pedido
            );

            return pedido;
        }

        @Override
        public Optional<Pedido> buscarPorId(
                Integer idPedido) {

            return pedidos.stream()
                    .filter(
                            p ->
                                    p.getIdPedido()
                                            .equals(
                                                    idPedido
                                            )
                    )
                    .findFirst();
        }

        @Override
        public List<Pedido> listarTodos() {

            return new ArrayList<>(
                    pedidos
            );
        }

        @Override
        public List<Pedido> listarPorCliente(
                Integer idCliente) {

            return pedidos.stream()
                    .filter(
                            p ->
                                    p.getCliente() != null
                                            && p.getCliente()
                                            .getIdUsuario()
                                            .equals(
                                                    idCliente
                                            )
                    )
                    .toList();
        }

        @Override
        public List<Pedido> listarPorEmprendimiento(
                Integer idEmprendimiento) {

            return pedidos.stream()
                    .filter(
                            p ->
                                    p.getEmprendimiento()
                                            != null
                                            && p.getEmprendimiento()
                                            .getIdEmprendimiento()
                                            .equals(
                                                    idEmprendimiento
                                            )
                    )
                    .toList();
        }

        @Override
        public boolean actualizarEstado(
                Integer idPedido,
                EstadoPedido nuevoEstado) {

            Optional<Pedido> pedido =
                    buscarPorId(
                            idPedido
                    );

            if (pedido.isEmpty()) {
                return false;
            }

            pedido.get()
                    .setEstado(
                            nuevoEstado
                    );

            return true;
        }

        @Override
        public boolean eliminar(
                Integer idPedido) {

            return pedidos.removeIf(
                    p ->
                            p.getIdPedido()
                                    .equals(
                                            idPedido
                                    )
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

            return usuarios.stream()
                    .filter(
                            u ->
                                    u.getIdUsuario()
                                            .equals(
                                                    idUsuario
                                            )
                    )
                    .findFirst();
        }

        @Override
        public Optional<Usuario> buscarPorCorreo(
                String correo) {

            return usuarios.stream()
                    .filter(
                            u ->
                                    u.getCorreo()
                                            .equalsIgnoreCase(
                                                    correo
                                            )
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
                                    .equals(
                                            idUsuario
                                    )
            );
        }
    }

    private static class FakeEmprendimientoDAO
            implements EmprendimientoDAO {

        private final List<Emprendimiento>
                emprendimientos =
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

            return emprendimientos.stream()
                    .filter(
                            e ->
                                    e.getIdEmprendimiento()
                                            .equals(
                                                    idEmprendimiento
                                            )
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

            return emprendimientos.stream()
                    .filter(
                            e ->
                                    e.getPropietario()
                                            != null
                                            && e.getPropietario()
                                            .getIdUsuario()
                                            .equals(
                                                    idUsuario
                                            )
                    )
                    .toList();
        }

        @Override
        public boolean actualizar(
                Emprendimiento emprendimiento) {

            return buscarPorId(
                    emprendimiento
                            .getIdEmprendimiento()
            ).isPresent();
        }

        @Override
        public boolean eliminar(
                Integer idEmprendimiento) {

            return emprendimientos.removeIf(
                    e ->
                            e.getIdEmprendimiento()
                                    .equals(
                                            idEmprendimiento
                                    )
            );
        }
    }

    private static class FakePublicacionDAO
            implements PublicacionDAO {

        private final List<Publicacion>
                publicaciones =
                new ArrayList<>();

        void agregar(
                Publicacion publicacion) {

            publicaciones.add(
                    publicacion
            );
        }

        @Override
        public Publicacion crear(
                Publicacion publicacion) {

            publicaciones.add(
                    publicacion
            );

            return publicacion;
        }

        @Override
        public Optional<Publicacion> buscarPorId(
                Integer idPublicacion) {

            return publicaciones.stream()
                    .filter(
                            p ->
                                    p.getIdPublicacion()
                                            .equals(
                                                    idPublicacion
                                            )
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

            return publicaciones.stream()
                    .filter(
                            p ->
                                    p.getEmprendimiento()
                                            != null
                                            && p.getEmprendimiento()
                                            .getIdEmprendimiento()
                                            .equals(
                                                    idEmprendimiento
                                            )
                    )
                    .toList();
        }

        @Override
        public List<Publicacion> listarPorCategoria(
                Integer idCategoria) {

            return publicaciones.stream()
                    .filter(
                            p ->
                                    p.getCategoria()
                                            != null
                                            && p.getCategoria()
                                            .getIdCategoria()
                                            .equals(
                                                    idCategoria
                                            )
                    )
                    .toList();
        }

        @Override
        public boolean actualizar(
                Publicacion publicacion) {

            return buscarPorId(
                    publicacion
                            .getIdPublicacion()
            ).isPresent();
        }

        @Override
        public boolean eliminar(
                Integer idPublicacion) {

            return publicaciones.removeIf(
                    p ->
                            p.getIdPublicacion()
                                    .equals(
                                            idPublicacion
                                    )
            );
        }
    }
}