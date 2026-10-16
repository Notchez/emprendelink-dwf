package emprendelink.service.impl;

import emprendelink.dao.RolDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.security.Contrasenas;
import emprendelink.service.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@ApplicationScoped
@Transactional
public class UsuarioServiceImpl
        implements UsuarioService {

    private static final int MAX_NOMBRE = 80;
    private static final int MAX_APELLIDO = 80;
    private static final int MAX_CORREO = 120;
    private static final int MAX_TELEFONO = 20;
    private static final int MAX_DIRECCION = 255;
    private static final int MAX_CONTRASENA = 128;

    private static final Pattern PATRON_CORREO =
            Pattern.compile(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
            );

    private final UsuarioDAO usuarioDAO;
    private final RolDAO rolDAO;

    @Inject
    public UsuarioServiceImpl(
            UsuarioDAO usuarioDAO,
            RolDAO rolDAO) {

        this.usuarioDAO = usuarioDAO;
        this.rolDAO = rolDAO;
    }

    @Override
    public List<Usuario> listarTodos() {

        List<Usuario> usuarios =
                usuarioDAO.listarTodos();

        for (Usuario usuario : usuarios) {
            usuario.setContrasenaHash(null);
        }

        return usuarios;
    }

    @Override
    public Usuario buscarPorId(
            Integer idUsuario) {

        Usuario usuario =
                usuarioDAO.buscarPorId(
                                idUsuario
                        )
                        .orElse(null);

        if (usuario != null) {
            usuario.setContrasenaHash(null);
        }

        return usuario;
    }

    @Override
    public void registrar(
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            TipoRol rol) {

        validarRegistro(
                nombre,
                apellido,
                correo,
                contrasena,
                telefono,
                rol
        );

        String correoNormalizado =
                correo.trim()
                        .toLowerCase(
                                Locale.ROOT
                        );

        if (usuarioDAO
                .buscarPorCorreo(
                        correoNormalizado
                )
                .isPresent()) {

            throw new IllegalArgumentException(
                    "El correo ya está registrado."
            );
        }

        Rol rolEncontrado =
                rolDAO.listarTodos()
                        .stream()
                        .filter(
                                item ->
                                        item.getNombre()
                                                == rol
                        )
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalStateException(
                                                "El rol no existe en la base de datos."
                                        )
                        );

        Usuario usuario =
                new Usuario(
                        rolEncontrado,
                        nombre.trim(),
                        apellido.trim(),
                        correoNormalizado,
                        Contrasenas.generarHash(
                                contrasena
                        ),
                        normalizarOpcional(
                                telefono
                        )
                );

        usuarioDAO.crear(
                usuario
        );
    }

    @Override
    public Usuario actualizarPerfilCliente(
            Integer idUsuario,
            String telefono,
            String direccion) {

        Usuario usuario =
                usuarioDAO
                        .buscarPorId(
                                idUsuario
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "El usuario no existe."
                                        )
                        );

        if (!usuario.isActivo()
                || usuario.getRol() == null
                || usuario
                .getRol()
                .getNombre()
                != TipoRol.ROLE_CLIENTE) {

            throw new IllegalArgumentException(
                    "Se requiere una cuenta de cliente activa."
            );
        }

        String telefonoNormalizado =
                normalizarOpcional(
                        telefono
                );

        String direccionNormalizada =
                normalizarOpcional(
                        direccion
                );

        if (telefonoNormalizado != null
                && telefonoNormalizado.length()
                > MAX_TELEFONO) {

            throw new IllegalArgumentException(
                    "El teléfono no puede superar 20 caracteres."
            );
        }

        if (direccionNormalizada != null
                && direccionNormalizada.length()
                > MAX_DIRECCION) {

            throw new IllegalArgumentException(
                    "La dirección no puede superar 255 caracteres."
            );
        }

        usuario.setTelefono(
                telefonoNormalizado
        );

        usuario.setDireccion(
                direccionNormalizada
        );

        if (!usuarioDAO.actualizar(
                usuario
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar el perfil."
            );
        }

        Usuario actualizado =
                usuarioDAO
                        .buscarPorId(
                                idUsuario
                        )
                        .orElseThrow();

        actualizado.setContrasenaHash(
                null
        );

        return actualizado;
    }

    @Override
    public void cambiarEstado(
            Integer idUsuario,
            boolean activo) {

        if (idUsuario == null) {

            throw new IllegalArgumentException(
                    "El identificador es obligatorio."
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
                                                "El usuario no existe."
                                        )
                        );

        usuario.setActivo(
                activo
        );

        if (!usuarioDAO.actualizar(
                usuario
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar el usuario."
            );
        }
    }

    private void validarRegistro(
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            TipoRol rol) {

        validarTextoObligatorio(
                nombre,
                "El nombre es obligatorio.",
                "El nombre no puede superar 80 caracteres.",
                MAX_NOMBRE
        );

        validarTextoObligatorio(
                apellido,
                "El apellido es obligatorio.",
                "El apellido no puede superar 80 caracteres.",
                MAX_APELLIDO
        );

        validarTextoObligatorio(
                correo,
                "El correo es obligatorio.",
                "El correo no puede superar 120 caracteres.",
                MAX_CORREO
        );

        if (!PATRON_CORREO
                .matcher(
                        correo.trim()
                )
                .matches()) {

            throw new IllegalArgumentException(
                    "Ingrese un correo electrónico válido."
            );
        }

        if (contrasena == null
                || contrasena.length() < 8) {

            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres."
            );
        }

        if (contrasena.length()
                > MAX_CONTRASENA) {

            throw new IllegalArgumentException(
                    "La contraseña no puede superar 128 caracteres."
            );
        }

        if (telefono != null
                && telefono.trim().length()
                > MAX_TELEFONO) {

            throw new IllegalArgumentException(
                    "El teléfono no puede superar 20 caracteres."
            );
        }

        if (rol != TipoRol.ROLE_CLIENTE) {

            throw new IllegalArgumentException(
                    "El registro público solo permite cuentas de cliente."
            );
        }
    }

    private void validarTextoObligatorio(
            String valor,
            String mensajeObligatorio,
            String mensajeLongitud,
            int longitudMaxima) {

        if (valor == null
                || valor.isBlank()) {

            throw new IllegalArgumentException(
                    mensajeObligatorio
            );
        }

        if (valor.trim().length()
                > longitudMaxima) {

            throw new IllegalArgumentException(
                    mensajeLongitud
            );
        }
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