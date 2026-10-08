package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoRol;
import emprendelink.persistence.jpa.entity.UsuarioEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioMapperTest {

    @Test
    void convierteDominioAEntidad() {

        Rol rol = new Rol(
                3,
                TipoRol.ROLE_CLIENTE
        );

        LocalDateTime fecha =
                LocalDateTime.now();

        Usuario usuario =
                new Usuario(
                        10,
                        rol,
                        "Tito",
                        "Nochez",
                        "tito@test.com",
                        "hash-prueba",
                        "7000-0000",
                        true,
                        fecha
                );

        UsuarioEntity entidad =
                UsuarioMapper.aEntidad(
                        usuario
                );

        assertNotNull(entidad);

        assertEquals(
                10,
                entidad.getIdUsuario()
        );

        assertEquals(
                "Tito",
                entidad.getNombre()
        );

        assertEquals(
                TipoRol.ROLE_CLIENTE,
                entidad.getRol().getNombre()
        );

        assertTrue(
                entidad.isActivo()
        );
    }

    @Test
    void convierteEntidadADominio() {

        UsuarioEntity entidad =
                new UsuarioEntity();

        var rol =
                new emprendelink.persistence.jpa.entity.RolEntity();

        rol.setIdRol(2);

        rol.setNombre(
                TipoRol.ROLE_EMPRENDEDOR
        );

        entidad.setIdUsuario(20);
        entidad.setRol(rol);
        entidad.setNombre("Carlos");
        entidad.setApellido("Ruiz");
        entidad.setCorreo("carlos@test.com");
        entidad.setContrasenaHash("hash");
        entidad.setTelefono("7000-0000");
        entidad.setActivo(true);
        entidad.setFechaRegistro(
                LocalDateTime.now()
        );

        Usuario usuario =
                UsuarioMapper.aDominio(
                        entidad
                );

        assertNotNull(usuario);

        assertEquals(
                20,
                usuario.getIdUsuario()
        );

        assertEquals(
                TipoRol.ROLE_EMPRENDEDOR,
                usuario.getRol().getNombre()
        );

        assertEquals(
                "Carlos",
                usuario.getNombre()
        );
    }
}