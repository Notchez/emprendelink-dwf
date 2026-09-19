package sv.edu.udb.emprendelink.model;

public class Categoria {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public Categoria() {
        this.activo = true;
    }

    public Categoria(Integer idCategoria, String nombre, String descripcion, boolean activo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Categoria(String nombre, String descripcion) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
