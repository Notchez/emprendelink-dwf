package emprendelink.dto;

import java.util.ArrayList;
import java.util.List;

public class CrearPedidoDTO {

    private List<Item> items = new ArrayList<>();
    private String direccionEntrega;
    private String observaciones;

    public CrearPedidoDTO() {
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public static class Item {

        private Integer idPublicacion;
        private Integer cantidad;

        public Item() {
        }

        public Integer getIdPublicacion() {
            return idPublicacion;
        }

        public void setIdPublicacion(Integer idPublicacion) {
            this.idPublicacion = idPublicacion;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}