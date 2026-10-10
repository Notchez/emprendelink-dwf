package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Emprendimiento;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.persistence.jpa.entity.EmprendimientoEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmprendimientoMapperTest {

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
                        "Café Don David",
                        "Venta de café artesanal",
                        "7777-7777",
                        true,
                        LocalDateTime.of(
                                2026, 10, 10,
                                13, 0
                        )
                );

        EmprendimientoEntity entidad =
                EmprendimientoMapper.aEntidad(
                        emprendimiento
                );

        assertNotNull(entidad);

        assertEquals(
                5,
                entidad.getIdEmprendimiento()
        );

        assertEquals(
                "Café Don David",
                entidad.getNombre()
        );

        assertEquals(
                "Venta de café artesanal",
                entidad.getDescripcion()
        );

        assertEquals(
                "7777-7777",
                entidad.getContacto()
        );

        assertEquals(
                10,
                entidad.getPropietario()
                        .getIdUsuario()
        );

        assertEquals(
                TipoRol.ROLE_EMPRENDEDOR,
                entidad.getPropietario()
                        .getRol()
                        .getNombre()
        );
    }

    @Test
    void convierteEntidadADominio() {

        EmprendimientoEntity entidad =
                new EmprendimientoEntity();

        entidad.setIdEmprendimiento(8);
        entidad.setNombre("Tienda Demo");
        entidad.setDescripcion("Descripción demo");
        entidad.setContacto("7000-0000");
        entidad.setActivo(false);

        Emprendimiento emprendimiento =
                EmprendimientoMapper.aDominio(
                        entidad
                );

        assertNotNull(emprendimiento);

        assertEquals(
                8,
                emprendimiento.getIdEmprendimiento()
        );

        assertEquals(
                "Tienda Demo",
                emprendimiento.getNombre()
        );

        assertEquals(
                "Descripción demo",
                emprendimiento.getDescripcion()
        );

        assertEquals(
                "7000-0000",
                emprendimiento.getContacto()
        );

        assertFalse(
                emprendimiento.isActivo()
        );
    }
}