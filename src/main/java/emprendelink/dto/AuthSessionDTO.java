
package emprendelink.dto;

public class AuthSessionDTO {

    private Integer idUsuario;
    private String nombre;
    private String rol;
    private String csrfToken;

    public AuthSessionDTO() {
    }

    public AuthSessionDTO(
            Integer idUsuario,
            String nombre,
            String rol,
            String csrfToken) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
        this.csrfToken = csrfToken;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }
}
