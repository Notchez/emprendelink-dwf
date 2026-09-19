package sv.edu.udb.emprendelink.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import sv.edu.udb.emprendelink.model.enums.TipoPublicacion;

public class Publicacion {

    private Integer idPublicacion;
    private Emprendimiento emprendimiento;
    private Categoria categoria;
    private TipoPublicacion tipo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private boolean activo;
    private LocalDateTime fechaPublicacion;

    public Publicacion() {
        this.activo = true;
        this.fechaPublicacion = LocalDateTime.now();
    }

    public Publicacion(Integer idPublicacion, Emprendimiento emprendimiento, Categoria categoria,
                       TipoPublicacion tipo, String nombre, String descripcion, BigDecimal precio,
                       Integer stock, boolean activo, LocalDateTime fechaPublicacion) {
        this.idPublicacion = idPublicacion;
        this.emprendimiento = emprendimiento;
        this.categoria = categoria;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        setPrecio(precio);
        setStock(stock);
        this.activo = activo;
        this.fechaPublicacion = fechaPublicacion;
    }

    public Publicacion(Emprendimiento emprendimiento, Categoria categoria, TipoPublicacion tipo,
                       String nombre, String descripcion, BigDecimal precio, Integer stock) {
        this();
        this.emprendimiento = emprendimiento;
        this.categoria = categoria;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        setPrecio(precio);
        setStock(stock);
    }

    public Integer getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Integer idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public Emprendimiento getEmprendimiento() {
        return emprendimiento;
    }

    public void setEmprendimiento(Emprendimiento emprendimiento) {
        this.emprendimiento = emprendimiento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public TipoPublicacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoPublicacion tipo) {
        this.tipo = tipo;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        if (precio != null && precio.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        if (stock != null && stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
}
