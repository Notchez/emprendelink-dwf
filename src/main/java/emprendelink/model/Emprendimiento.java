package emprendelink.model;

import java.time.LocalDateTime;

public class Emprendimiento {

    private Integer idEmprendimiento;
    private Usuario propietario;
    private Plan plan;
    private String nombre;
    private String descripcion;
    private String contacto;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    public Emprendimiento() {
        this.activo = true;
        this.fechaRegistro =
                LocalDateTime.now();
    }

    public Emprendimiento(
            Integer idEmprendimiento,
            Usuario propietario,
            String nombre,
            String descripcion,
            String contacto,
            boolean activo,
            LocalDateTime fechaRegistro) {

        this(
                idEmprendimiento,
                propietario,
                null,
                nombre,
                descripcion,
                contacto,
                activo,
                fechaRegistro
        );
    }

    public Emprendimiento(
            Integer idEmprendimiento,
            Usuario propietario,
            Plan plan,
            String nombre,
            String descripcion,
            String contacto,
            boolean activo,
            LocalDateTime fechaRegistro) {

        this.idEmprendimiento =
                idEmprendimiento;

        this.propietario =
                propietario;

        this.plan =
                plan;

        this.nombre =
                nombre;

        this.descripcion =
                descripcion;

        this.contacto =
                contacto;

        this.activo =
                activo;

        this.fechaRegistro =
                fechaRegistro;
    }

    public Emprendimiento(
            Usuario propietario,
            String nombre,
            String descripcion,
            String contacto) {

        this();

        this.propietario =
                propietario;

        this.nombre =
                nombre;

        this.descripcion =
                descripcion;

        this.contacto =
                contacto;
    }

    public Integer getIdEmprendimiento() {
        return idEmprendimiento;
    }

    public void setIdEmprendimiento(
            Integer idEmprendimiento) {

        this.idEmprendimiento =
                idEmprendimiento;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(
            Usuario propietario) {

        this.propietario =
                propietario;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(
            Plan plan) {

        this.plan =
                plan;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre) {

        this.nombre =
                nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion =
                descripcion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(
            String contacto) {

        this.contacto =
                contacto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(
            boolean activo) {

        this.activo =
                activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(
            LocalDateTime fechaRegistro) {

        this.fechaRegistro =
                fechaRegistro;
    }
}