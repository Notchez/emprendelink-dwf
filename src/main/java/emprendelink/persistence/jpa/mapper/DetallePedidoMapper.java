package emprendelink.persistence.jpa.mapper;

import emprendelink.model.DetallePedido;
import emprendelink.persistence.jpa.entity.DetallePedidoEntity;

public final class DetallePedidoMapper {

    private DetallePedidoMapper() {
    }

    public static DetallePedidoEntity aEntidad(
            DetallePedido detalle) {

        if (detalle == null) {
            return null;
        }

        DetallePedidoEntity entidad =
                new DetallePedidoEntity();

        entidad.setIdDetallePedido(
                detalle.getIdDetallePedido()
        );

        entidad.setPublicacion(
                PublicacionMapper.aEntidad(
                        detalle.getPublicacion()
                )
        );

        entidad.setCantidad(
                detalle.getCantidad()
        );

        entidad.setPrecioUnitario(
                detalle.getPrecioUnitario()
        );

        entidad.setSubtotal(
                detalle.getSubtotal()
        );

        return entidad;
    }

    public static DetallePedido aDominio(
            DetallePedidoEntity entidad) {

        if (entidad == null) {
            return null;
        }

        return new DetallePedido(
                entidad.getIdDetallePedido(),
                null,
                PublicacionMapper.aDominio(
                        entidad.getPublicacion()
                ),
                entidad.getCantidad(),
                entidad.getPrecioUnitario()
        );
    }
}