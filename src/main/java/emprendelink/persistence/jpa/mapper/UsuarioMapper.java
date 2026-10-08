package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Usuario;
import emprendelink.persistence.jpa.entity.UsuarioEntity;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario aDominio(
            UsuarioEntity entidad) {

        if (entidad == null) {
            return null;
        }

        return new Usuario(
                entidad.getIdUsuario(),
                RolMapper.aDominio(
                        entidad.getRol()
                ),
                entidad.getNombre(),
                entidad.getApellido(),
                entidad.getCorreo(),
                entidad.getContrasenaHash(),
                entidad.getTelefono(),
                entidad.isActivo(),
                entidad.getFechaRegistro()
        );
    }

    public static UsuarioEntity aEntidad(
            Usuario dominio) {

        if (dominio == null) {
            return null;
        }

        UsuarioEntity entidad =
                new UsuarioEntity();

        entidad.setIdUsuario(
                dominio.getIdUsuario()
        );

        entidad.setRol(
                RolMapper.aEntidad(
                        dominio.getRol()
                )
        );

        entidad.setNombre(
                dominio.getNombre()
        );

        entidad.setApellido(
                dominio.getApellido()
        );

        entidad.setCorreo(
                dominio.getCorreo()
        );

        entidad.setContrasenaHash(
                dominio.getContrasenaHash()
        );

        entidad.setTelefono(
                dominio.getTelefono()
        );

        entidad.setActivo(
                dominio.isActivo()
        );

        entidad.setFechaRegistro(
                dominio.getFechaRegistro()
        );

        return entidad;
    }
}