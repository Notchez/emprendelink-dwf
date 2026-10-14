package emprendelink.service;

import emprendelink.dao.CategoriaDAO;
import emprendelink.model.Categoria;
import emprendelink.service.impl.CategoriaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoriaServiceTest {

    private FakeCategoriaDAO categoriaDAO;
    private CategoriaServiceImpl categoriaService;

    @BeforeEach
    void preparar() {

        categoriaDAO =
                new FakeCategoriaDAO();

        categoriaService =
                new CategoriaServiceImpl(
                        categoriaDAO
                );
    }

    @Test
    void creaCategoriaNormalizada() {

        categoriaService.crear(
                "  Tecnología  ",
                "  Productos tecnológicos  "
        );

        Categoria categoria =
                categoriaDAO
                        .listarTodos()
                        .get(0);

        assertEquals(
                "Tecnología",
                categoria.getNombre()
        );

        assertEquals(
                "Productos tecnológicos",
                categoria.getDescripcion()
        );

        assertTrue(
                categoria.isActivo()
        );
    }

    @Test
    void rechazaCategoriaDuplicada() {

        categoriaService.crear(
                "Tecnología",
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> categoriaService.crear(
                        " tecnología ",
                        null
                )
        );
    }

    @Test
    void rechazaNombreDemasiadoLargo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> categoriaService.crear(
                        "A".repeat(81),
                        null
                )
        );
    }

    @Test
    void cambiaEstadoCategoria() {

        categoriaService.crear(
                "Servicios",
                null
        );

        Categoria categoria =
                categoriaDAO
                        .listarTodos()
                        .get(0);

        categoriaService.cambiarEstado(
                categoria.getIdCategoria(),
                false
        );

        assertFalse(
                categoria.isActivo()
        );
    }

    private static class FakeCategoriaDAO
            implements CategoriaDAO {

        private final List<Categoria> categorias =
                new ArrayList<>();

        private int siguienteId = 1;

        @Override
        public Categoria crear(
                Categoria categoria) {

            categoria.setIdCategoria(
                    siguienteId++
            );

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
                    .filter(categoria ->
                            categoria
                                    .getIdCategoria()
                                    .equals(
                                            idCategoria
                                    )
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
                    categoria ->
                            categoria
                                    .getIdCategoria()
                                    .equals(
                                            idCategoria
                                    )
            );
        }
    }
}