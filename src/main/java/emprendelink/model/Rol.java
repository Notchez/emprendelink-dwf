package emprendelink.model;

import emprendelink.model.enums.TipoRol;

import java.io.Serial;
import java.io.Serializable;

public class Rol implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idRol;
    private TipoRol nombre;

    public Rol() {
    }

    public Rol(TipoRol nombre) {
        this.nombre = nombre;
    }

    public Rol(
            Integer idRol,
            TipoRol nombre) {

        this.idRol = idRol;
        this.nombre = nombre;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(
            Integer idRol) {

        this.idRol = idRol;
    }

    public TipoRol getNombre() {
        return nombre;
    }

    public void setNombre(
            TipoRol nombre) {

        this.nombre = nombre;
    }
}