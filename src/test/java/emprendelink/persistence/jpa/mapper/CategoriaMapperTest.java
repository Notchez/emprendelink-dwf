package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Categoria;
import emprendelink.persistence.jpa.entity.CategoriaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaMapperTest {

    @Test
    void convierteDominioAEntidad() {

        Categoria categoria =
                new Categoria(
                        1,
                        "Tecnología",
                        "Productos tecnológicos",
                        true
                );

        CategoriaEntity entidad =
                CategoriaMapper.aEntidad(
                        categoria
                );

        assertNotNull(entidad);
        assertEquals(
                1,
                entidad.getIdCategoria()
        );
        assertEquals(
                "Tecnología",
                entidad.getNombre()
        );
        assertEquals(
                "Productos tecnológicos",
                entidad.getDescripcion()
        );
        assertTrue(
                entidad.isActivo()
        );
    }

    @Test
    void convierteEntidadADominio() {

        CategoriaEntity entidad =
                new CategoriaEntity();

        entidad.setIdCategoria(2);
        entidad.setNombre("Servicios");
        entidad.setDescripcion(
                "Servicios profesionales"
        );
        entidad.setActivo(true);

        Categoria categoria =
                CategoriaMapper.aDominio(
                        entidad
                );

        assertNotNull(categoria);
        assertEquals(
                2,
                categoria.getIdCategoria()
        );
        assertEquals(
                "Servicios",
                categoria.getNombre()
        );
        assertTrue(
                categoria.isActivo()
        );
    }
}