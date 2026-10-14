package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Categoria;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Publicacion;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.model.enums.TipoRol;
import emprendelink.persistence.jpa.entity.PublicacionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PublicacionMapperTest {

    @Test
    void convierteDominioAEntidad() {

        Rol rol = new Rol(
                2,
                TipoRol.ROLE_EMPRENDEDOR
        );

        Usuario propietario = new Usuario(
                10,
                rol,
                "David",
                "Martinez",
                "david@emprendelink.com",
                "hash",
                "7777-7777",
                true,
                LocalDateTime.of(
                        2026, 10, 10,
                        12, 0
                )
        );

        Emprendimiento emprendimiento =
                new Emprendimiento(
                        5,
                        propietario,
                        "Café Central",
                        "Café artesanal",
                        "7777-7777",
                        true,
                        LocalDateTime.of(
                                2026, 10, 10,
                                13, 0
                        )
                );

        Categoria categoria =
                new Categoria(
                        3,
                        "Alimentos",
                        "Productos alimenticios",
                        true
                );

        Publicacion publicacion =
                new Publicacion(
                        7,
                        emprendimiento,
                        categoria,
                        TipoPublicacion.PRODUCTO,
                        "Café Premium",
                        "Bolsa de café de 500g",
                        new BigDecimal("8.50"),
                        12,
                        true,
                        LocalDateTime.of(
                                2026, 10, 10,
                                14, 0
                        )
                );

        PublicacionEntity entidad =
                PublicacionMapper.aEntidad(
                        publicacion
                );

        assertNotNull(entidad);

        assertEquals(
                7,
                entidad.getIdPublicacion()
        );

        assertEquals(
                "Café Premium",
                entidad.getNombre()
        );

        assertEquals(
                TipoPublicacion.PRODUCTO,
                entidad.getTipo()
        );

        assertEquals(
                new BigDecimal("8.50"),
                entidad.getPrecio()
        );

        assertEquals(
                12,
                entidad.getStock()
        );

        assertEquals(
                5,
                entidad.getEmprendimiento()
                        .getIdEmprendimiento()
        );

        assertEquals(
                3,
                entidad.getCategoria()
                        .getIdCategoria()
        );
    }

    @Test
    void convierteEntidadADominio() {

        PublicacionEntity entidad =
                new PublicacionEntity();

        entidad.setIdPublicacion(9);
        entidad.setTipo(
                TipoPublicacion.SERVICIO
        );

        entidad.setNombre(
                "Diseño de logotipo"
        );

        entidad.setDescripcion(
                "Servicio de diseño gráfico"
        );

        entidad.setPrecio(
                new BigDecimal("25.00")
        );

        entidad.setStock(0);
        entidad.setActivo(false);

        entidad.setFechaPublicacion(
                LocalDateTime.of(
                        2026, 10, 10,
                        15, 0
                )
        );

        Publicacion publicacion =
                PublicacionMapper.aDominio(
                        entidad
                );

        assertNotNull(publicacion);

        assertEquals(
                9,
                publicacion.getIdPublicacion()
        );

        assertEquals(
                "Diseño de logotipo",
                publicacion.getNombre()
        );

        assertEquals(
                TipoPublicacion.SERVICIO,
                publicacion.getTipo()
        );

        assertEquals(
                new BigDecimal("25.00"),
                publicacion.getPrecio()
        );

        assertEquals(
                0,
                publicacion.getStock()
        );

        assertFalse(
                publicacion.isActivo()
        );
    }
}