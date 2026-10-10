package emprendelink.service.impl;

import emprendelink.dao.CategoriaDAO;
import emprendelink.model.Categoria;
import emprendelink.service.CategoriaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class CategoriaServiceImpl
        implements CategoriaService {

    private static final int MAX_NOMBRE = 80;

    private final CategoriaDAO categoriaDAO;

    @Inject
    public CategoriaServiceImpl(
            CategoriaDAO categoriaDAO) {

        this.categoriaDAO =
                categoriaDAO;
    }

    @Override
    public List<Categoria> listarTodas() {

        return categoriaDAO.listarTodos();
    }

    @Override
    public List<Categoria> listarActivas() {

        return categoriaDAO
                .listarTodos()
                .stream()
                .filter(Categoria::isActivo)
                .toList();
    }

    @Override
    public void crear(
            String nombre,
            String descripcion) {

        String nombreNormalizado =
                validarYNormalizarNombre(
                        nombre
                );

        validarDuplicado(
                null,
                nombreNormalizado
        );

        Categoria categoria =
                new Categoria(
                        nombreNormalizado,
                        normalizarOpcional(
                                descripcion
                        )
                );

        categoriaDAO.crear(
                categoria
        );
    }

    @Override
    public void actualizar(
            Integer idCategoria,
            String nombre,
            String descripcion) {

        if (idCategoria == null) {
            throw new IllegalArgumentException(
                    "La categoría es obligatoria."
            );
        }

        String nombreNormalizado =
                validarYNormalizarNombre(
                        nombre
                );

        Categoria categoria =
                categoriaDAO
                        .buscarPorId(
                                idCategoria
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La categoría no existe."
                                )
                        );

        validarDuplicado(
                idCategoria,
                nombreNormalizado
        );

        categoria.setNombre(
                nombreNormalizado
        );

        categoria.setDescripcion(
                normalizarOpcional(
                        descripcion
                )
        );

        if (!categoriaDAO.actualizar(
                categoria
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar la categoría."
            );
        }
    }

    @Override
    public void cambiarEstado(
            Integer idCategoria,
            boolean activo) {

        if (idCategoria == null) {
            throw new IllegalArgumentException(
                    "La categoría es obligatoria."
            );
        }

        Categoria categoria =
                categoriaDAO
                        .buscarPorId(
                                idCategoria
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La categoría no existe."
                                )
                        );

        categoria.setActivo(
                activo
        );

        if (!categoriaDAO.actualizar(
                categoria
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar la categoría."
            );
        }
    }

    private String validarYNormalizarNombre(
            String nombre) {

        if (nombre == null
                || nombre.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre de la categoría es obligatorio."
            );
        }

        String normalizado =
                nombre.trim();

        if (normalizado.length()
                > MAX_NOMBRE) {

            throw new IllegalArgumentException(
                    "El nombre no puede superar 80 caracteres."
            );
        }

        return normalizado;
    }

    private void validarDuplicado(
            Integer idActual,
            String nombre) {

        boolean existe =
                categoriaDAO
                        .listarTodos()
                        .stream()
                        .anyMatch(c ->
                                c.getNombre() != null
                                        && c.getNombre()
                                        .equalsIgnoreCase(
                                                nombre
                                        )
                                        && (idActual == null
                                        || !idActual.equals(
                                        c.getIdCategoria()
                                ))
                        );

        if (existe) {

            throw new IllegalArgumentException(
                    "Ya existe una categoría con ese nombre."
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