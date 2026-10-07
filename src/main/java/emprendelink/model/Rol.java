package emprendelink.model;

import emprendelink.model.enums.TipoRol;

public class Rol {

    private Integer idRol;
    private TipoRol nombre;

    public Rol() {
    }

    public Rol(Integer idRol, TipoRol nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
    }

    public Rol(TipoRol nombre) {
        this.nombre = nombre;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public TipoRol getNombre() {
        return nombre;
    }

    public void setNombre(TipoRol nombre) {
        this.nombre = nombre;
    }
}
