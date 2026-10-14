package emprendelink.service;

import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.impl.EmprendimientoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmprendimientoServiceTest {

    private FakeEmprendimientoDAO emprendimientoDAO;
    private FakeUsuarioDAO usuarioDAO;
    private EmprendimientoServiceImpl emprendimientoService;

    @BeforeEach
    void preparar() {

        emprendimientoDAO =
                new FakeEmprendimientoDAO();

        usuarioDAO =
                new FakeUsuarioDAO();

        emprendimientoService =
                new EmprendimientoServiceImpl(
                        emprendimientoDAO,
                        usuarioDAO
                );
    }

    @Test
    void creaEmprendimientoNormalizado() {

        Usuario propietario =
                crearUsuario(
                        1,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        usuarioDAO.agregar(propietario);

        emprendimientoService.crear(
                1,
                "  Café Central  ",
                "  Café artesanal  ",
                "  7777-7777  "
        );

        Emprendimiento emprendimiento =
                emprendimientoDAO
                        .listarTodos()
                        .get(0);

        assertEquals(
                "Café Central",
                emprendimiento.getNombre()
        );

        assertEquals(
                "Café artesanal",
                emprendimiento.getDescripcion()
        );

        assertEquals(
                "7777-7777",
                emprendimiento.getContacto()
        );

        assertEquals(
                1,
                emprendimiento
                        .getPropietario()
                        .getIdUsuario()
        );

        assertTrue(
                emprendimiento.isActivo()
        );
    }

    @Test
    void rechazaUsuarioQueNoEsEmprendedor() {

        Usuario cliente =
                crearUsuario(
                        2,
                        TipoRol.ROLE_CLIENTE,
                        true
                );

        usuarioDAO.agregar(cliente);

        assertThrows(
                IllegalArgumentException.class,
                () -> emprendimientoService.crear(
                        2,
                        "Negocio inválido",
                        null,
                        null
                )
        );
    }

    @Test
    void rechazaActualizarEmprendimientoAjeno() {

        Usuario propietario =
                crearUsuario(
                        3,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        Usuario otroEmprendedor =
                crearUsuario(
                        4,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        usuarioDAO.agregar(propietario);
        usuarioDAO.agregar(otroEmprendedor);

        Emprendimiento emprendimiento =
                new Emprendimiento(
                        propietario,
                        "Negocio original",
                        null,
                        null
                );

        emprendimientoDAO.crear(
                emprendimiento
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> emprendimientoService.actualizar(
                        4,
                        emprendimiento.getIdEmprendimiento(),
                        "Intento de cambio",
                        null,
                        null
                )
        );
    }

    @Test
    void adminPuedeDesactivarEmprendimiento() {

        Usuario propietario =
                crearUsuario(
                        5,
                        TipoRol.ROLE_EMPRENDEDOR,
                        true
                );

        Usuario admin =
                crearUsuario(
                        6,
                        TipoRol.ROLE_ADMIN,
                        true
                );

        usuarioDAO.agregar(propietario);
        usuarioDAO.agregar(admin);

        Emprendimiento emprendimiento =
                new Emprendimiento(
                        propietario,
                        "Tienda Demo",
                        null,
                        null
                );

        emprendimientoDAO.crear(
                emprendimiento
        );

        emprendimientoService.cambiarEstado(
                6,
                emprendimiento.getIdEmprendimiento(),
                false
        );

        assertFalse(
                emprendimiento.isActivo()
        );
    }

    private Usuario crearUsuario(
            Integer id,
            TipoRol tipoRol,
            boolean activo) {

        Rol rol =
                new Rol(
                        tipoRol == TipoRol.ROLE_ADMIN
                                ? 1
                                : tipoRol == TipoRol.ROLE_EMPRENDEDOR
                                ? 2
                                : 3,
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

    private static class FakeEmprendimientoDAO
            implements EmprendimientoDAO {

        private final List<Emprendimiento> emprendimientos =
                new ArrayList<>();

        private int siguienteId = 1;

        @Override
        public Emprendimiento crear(
                Emprendimiento emprendimiento) {

            emprendimiento.setIdEmprendimiento(
                    siguienteId++
            );

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

    private static class FakeUsuarioDAO
            implements UsuarioDAO {

        private final List<Usuario> usuarios =
                new ArrayList<>();

        void agregar(
                Usuario usuario) {

            usuarios.add(usuario);
        }

        @Override
        public Usuario crear(
                Usuario usuario) {

            usuarios.add(usuario);
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