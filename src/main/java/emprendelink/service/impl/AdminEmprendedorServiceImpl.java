package emprendelink.service.impl;

import emprendelink.dao.CategoriaDAO;
import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.PlanDAO;
import emprendelink.dao.RolDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.Categoria;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Plan;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.security.Contrasenas;
import emprendelink.service.AdminEmprendedorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.Locale;
import java.util.regex.Pattern;

@ApplicationScoped
@Transactional
public class AdminEmprendedorServiceImpl
        implements AdminEmprendedorService {

    private static final int MAX_LOGO_URL = 1000;

    private static final Pattern PATRON_CORREO =
            Pattern.compile(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
            );

    private final UsuarioDAO usuarioDAO;
    private final RolDAO rolDAO;
    private final EmprendimientoDAO emprendimientoDAO;
    private final PlanDAO planDAO;
    private final CategoriaDAO categoriaDAO;

    @Inject
    public AdminEmprendedorServiceImpl(
            UsuarioDAO usuarioDAO,
            RolDAO rolDAO,
            EmprendimientoDAO emprendimientoDAO,
            PlanDAO planDAO,
            CategoriaDAO categoriaDAO) {

        this.usuarioDAO =
                usuarioDAO;

        this.rolDAO =
                rolDAO;

        this.emprendimientoDAO =
                emprendimientoDAO;

        this.planDAO =
                planDAO;

        this.categoriaDAO =
                categoriaDAO;
    }

    @Override
    public void crearEmprendedorConNegocio(
            Integer idAdministrador,
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            String nombreNegocio,
            String descripcionNegocio,
            String contactoNegocio,
            String logoUrl,
            Integer idPlan,
            Integer idCategoria) {

        validarAdministrador(
                idAdministrador
        );

        validarDatos(
                nombre,
                apellido,
                correo,
                contrasena,
                telefono,
                nombreNegocio,
                contactoNegocio,
                idPlan,
                idCategoria
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

        Rol rolEmprendedor =
                rolDAO.listarTodos()
                        .stream()
                        .filter(
                                rol ->
                                        rol.getNombre()
                                                == TipoRol.ROLE_EMPRENDEDOR
                        )
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalStateException(
                                                "No existe el rol de emprendedor."
                                        )
                        );

        Plan plan =
                planDAO.buscarPorId(
                                idPlan
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "El plan seleccionado no existe."
                                        )
                        );

        if (!plan.isActivo()) {

            throw new IllegalArgumentException(
                    "El plan seleccionado está inactivo."
            );
        }

        Categoria categoria =
                categoriaDAO
                        .buscarPorId(
                                idCategoria
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "La categoría seleccionada no existe."
                                        )
                        );

        if (!categoria.isActivo()) {

            throw new IllegalArgumentException(
                    "La categoría seleccionada está inactiva."
            );
        }

        Usuario usuario =
                new Usuario(
                        rolEmprendedor,
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

        Usuario usuarioCreado =
                usuarioDAO.crear(
                        usuario
                );

        Emprendimiento negocio =
                new Emprendimiento(
                        usuarioCreado,
                        nombreNegocio.trim(),
                        normalizarOpcional(
                                descripcionNegocio
                        ),
                        normalizarOpcional(
                                contactoNegocio
                        )
                );

        negocio.setPlan(
                plan
        );

        negocio.setCategoria(
                categoria
        );

        negocio.setLogoUrl(
                normalizarUrlImagen(
                        logoUrl
                )
        );

        emprendimientoDAO.crear(
                negocio
        );
    }

    private void validarAdministrador(
            Integer idAdministrador) {

        Usuario administrador =
                usuarioDAO
                        .buscarPorId(
                                idAdministrador
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "El administrador no existe."
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
    }

    private void validarDatos(
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            String nombreNegocio,
            String contactoNegocio,
            Integer idPlan,
            Integer idCategoria) {

        validarTexto(
                nombre,
                80,
                "El nombre del propietario es obligatorio."
        );

        validarTexto(
                apellido,
                80,
                "El apellido del propietario es obligatorio."
        );

        validarTexto(
                correo,
                120,
                "El correo es obligatorio."
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
                || contrasena.length() < 8
                || contrasena.length() > 128) {

            throw new IllegalArgumentException(
                    "La contraseña debe contener entre 8 y 128 caracteres."
            );
        }

        if (telefono != null
                && telefono.trim().length()
                > 20) {

            throw new IllegalArgumentException(
                    "El teléfono no puede superar 20 caracteres."
            );
        }

        validarTexto(
                nombreNegocio,
                120,
                "El nombre del negocio es obligatorio."
        );

        if (contactoNegocio != null
                && contactoNegocio
                .trim()
                .length() > 120) {

            throw new IllegalArgumentException(
                    "El contacto no puede superar 120 caracteres."
            );
        }

        if (idPlan == null) {

            throw new IllegalArgumentException(
                    "Debe seleccionar un plan."
            );
        }

        if (idCategoria == null) {

            throw new IllegalArgumentException(
                    "Debe seleccionar una categoría."
            );
        }
    }

    private void validarTexto(
            String valor,
            int maximo,
            String mensaje) {

        if (valor == null
                || valor.isBlank()) {

            throw new IllegalArgumentException(
                    mensaje
            );
        }

        if (valor.trim().length()
                > maximo) {

            throw new IllegalArgumentException(
                    "El valor supera la longitud permitida."
            );
        }
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
                    !esquema.equalsIgnoreCase("http")
                            && !esquema.equalsIgnoreCase("https")
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