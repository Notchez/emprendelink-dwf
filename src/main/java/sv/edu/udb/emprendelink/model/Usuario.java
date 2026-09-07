package sv.edu.udb.emprendelink.model;

import java.time.LocalDateTime;

public class Usuario {

    private Integer idUsuario;
    private Rol rol;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenaHash;
    private String telefono;
    private Boolean activo;
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    public Usuario(
            Integer idUsuario,
            Rol rol,
            String nombre,
            String apellido,
            String correo,
            String contrasenaHash,
            String telefono,
            Boolean activo,
            LocalDateTime fechaRegistro
    ) {
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
        this.telefono = telefono;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}