package emprendelink.dto;

import java.math.BigDecimal;

public class PublicacionDTO {
    private Long id;
    private String titulo;
    private BigDecimal precio;
    private Integer stock;
    private Long emprendimientoId;

    public PublicacionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Long getEmprendimientoId() { return emprendimientoId; }
    public void setEmprendimientoId(Long emprendimientoId) { this.emprendimientoId = emprendimientoId; }
}