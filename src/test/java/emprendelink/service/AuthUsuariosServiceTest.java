package emprendelink.service;

import emprendelink.dao.RolDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.security.Contrasenas;
import emprendelink.service.impl.AutenticacionServiceImpl;
import emprendelink.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthUsuariosServiceTest {

    private FakeUsuarioDAO usuarioDAO;
    private UsuarioServiceImpl usuarioService;
    private AutenticacionServiceImpl autenticacionService;

    @BeforeEach
    void preparar() {

        usuarioDAO =
                new FakeUsuarioDAO();

        FakeRolDAO rolDAO =
                new FakeRolDAO();

        usuarioService =
                new UsuarioServiceImpl(
                        usuarioDAO,
                        rolDAO
                );

        autenticacionService =
                new AutenticacionServiceImpl(
                        usuarioDAO
                );
    }

    @Test
    void registraClienteConCorreoNormalizadoYHash() {

        usuarioService.registrar(
                " Tito ",
                " Nochez ",
                " TITO@TEST.COM ",
                "Demo1234!",
                " 7000-0000 ",
                TipoRol.ROLE_CLIENTE
        );

        Usuario usuario =
                usuarioDAO
                        .buscarPorCorreo(
                                "tito@test.com"
                        )
                        .orElseThrow();

        assertEquals(
                "Tito",
                usuario.getNombre()
        );

        assertEquals(
                "Nochez",
                usuario.getApellido()
        );

        assertEquals(
                "tito@test.com",
                usuario.getCorreo()
        );

        assertEquals(
                "7000-0000",
                usuario.getTelefono()
        );

        assertNotNull(
                usuario.getContrasenaHash()
        );

        assertFalse(
                usuario.getContrasenaHash()
                        .equals("Demo1234!")
        );

        assertTrue(
                Contrasenas.verificar(
                        "Demo1234!",
                        usuario.getContrasenaHash()
                )
        );
    }

    @Test
    void rechazaRegistroComoAdministrador() {

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrar(
                        "Admin",
                        "Prueba",
                        "admin@test.com",
                        "Demo1234!",
                        null,
                        TipoRol.ROLE_ADMIN
                )
        );
    }

    @Test
    void rechazaCorreoDuplicado() {

        registrarCliente(
                "duplicado@test.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> registrarCliente(
                        " DUPLICADO@TEST.COM "
                )
        );
    }

    @Test
    void rechazaCorreoConFormatoInvalido() {

        assertThrows(
                IllegalArgumentException.class,
                () -> registrarCliente(
                        "correo-invalido"
                )
        );
    }

    @Test
    void rechazaCamposQueSuperanLongitudPermitida() {

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrar(
                        "A".repeat(81),
                        "Nochez",
                        "tito@test.com",
                        "Demo1234!",
                        null,
                        TipoRol.ROLE_CLIENTE
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrar(
                        "Tito",
                        "Nochez",
                        "tito@test.com",
                        "Demo1234!",
                        "7".repeat(21),
                        TipoRol.ROLE_CLIENTE
                )
        );
    }

    @Test
    void autenticaCredencialesValidas() {

        registrarCliente(
                "david@test.com"
        );

        Usuario autenticado =
                autenticacionService.autenticar(
                        "DAVID@TEST.COM",
                        "Demo1234!"
                );

        assertNotNull(autenticado);

        assertEquals(
                "david@test.com",
                autenticado.getCorreo()
        );

        assertNull(
                autenticado.getContrasenaHash()
        );
    }

    @Test
    void usuarioInactivoNoPuedeAutenticarse() {

        registrarCliente(
                "mariana@test.com"
        );

        Usuario usuario =
                usuarioDAO
                        .buscarPorCorreo(
                                "mariana@test.com"
                        )
                        .orElseThrow();

        usuarioService.cambiarEstado(
                usuario.getIdUsuario(),
                false
        );

        Usuario autenticado =
                autenticacionService.autenticar(
                        "mariana@test.com",
                        "Demo1234!"
                );

        assertNull(autenticado);
    }

    private void registrarCliente(
            String correo) {

        usuarioService.registrar(
                "Tito",
                "Nochez",
                correo,
                "Demo1234!",
                null,
                TipoRol.ROLE_CLIENTE
        );
    }

    private static class FakeRolDAO
            implements RolDAO {

        private final List<Rol> roles =
                new ArrayList<>();

        FakeRolDAO() {

            roles.add(
                    new Rol(
                            1,
                            TipoRol.ROLE_ADMIN
                    )
            );

            roles.add(
                    new Rol(
                            2,
                            TipoRol.ROLE_EMPRENDEDOR
                    )
            );

            roles.add(
                    new Rol(
                            3,
                            TipoRol.ROLE_CLIENTE
                    )
            );
        }

        @Override
        public Rol crear(Rol rol) {
            roles.add(rol);
            return rol;
        }

        @Override
        public Optional<Rol> buscarPorId(
                Integer idRol) {

            return roles.stream()
                    .filter(
                            rol ->
                                    rol.getIdRol()
                                            .equals(idRol)
                    )
                    .findFirst();
        }

        @Override
        public List<Rol> listarTodos() {
            return new ArrayList<>(roles);
        }

        @Override
        public boolean actualizar(Rol rol) {
            return true;
        }

        @Override
        public boolean eliminar(
                Integer idRol) {

            return roles.removeIf(
                    rol ->
                            rol.getIdRol()
                                    .equals(idRol)
            );
        }
    }

    private static class FakeUsuarioDAO
            implements UsuarioDAO {

        private final List<Usuario> usuarios =
                new ArrayList<>();

        private int siguienteId = 1;

        @Override
        public Usuario crear(
                Usuario usuario) {

            usuario.setIdUsuario(
                    siguienteId++
            );

            usuarios.add(usuario);

            return usuario;
        }

        @Override
        public Optional<Usuario> buscarPorId(
                Integer idUsuario) {

            return usuarios.stream()
                    .filter(
                            usuario ->
                                    usuario.getIdUsuario()
                                            .equals(idUsuario)
                    )
                    .findFirst();
        }

        @Override
        public Optional<Usuario> buscarPorCorreo(
                String correo) {

            return usuarios.stream()
                    .filter(
                            usuario ->
                                    usuario.getCorreo()
                                            .equalsIgnoreCase(
                                                    correo
                                            )
                    )
                    .findFirst();
        }

        @Override
        public List<Usuario> listarTodos() {
            return new ArrayList<>(usuarios);
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
                    usuario ->
                            usuario.getIdUsuario()
                                    .equals(idUsuario)
            );
        }
    }
}
