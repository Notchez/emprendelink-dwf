package emprendelink.persistence.jpa.mapper;

import emprendelink.model.DetallePedido;
import emprendelink.model.Pedido;
import emprendelink.persistence.jpa.entity.DetallePedidoEntity;
import emprendelink.persistence.jpa.entity.PedidoEntity;

public final class PedidoMapper {

    private PedidoMapper() {
    }

    public static PedidoEntity aEntidad(
            Pedido pedido) {

        if (pedido == null) {
            return null;
        }

        PedidoEntity entidad =
                new PedidoEntity();

        entidad.setIdPedido(
                pedido.getIdPedido()
        );

        entidad.setCliente(
                UsuarioMapper.aEntidad(
                        pedido.getCliente()
                )
        );

        entidad.setEmprendimiento(
                EmprendimientoMapper.aEntidad(
                        pedido.getEmprendimiento()
                )
        );

        entidad.setEstado(
                pedido.getEstado()
        );

        entidad.setTotal(
                pedido.getTotal()
        );

        entidad.setObservaciones(
                pedido.getObservaciones()
        );

        entidad.setFechaPedido(
                pedido.getFechaPedido()
        );

        for (DetallePedido detalle
                : pedido.getDetalles()) {

            DetallePedidoEntity detalleEntity =
                    DetallePedidoMapper.aEntidad(
                            detalle
                    );

            entidad.agregarDetalle(
                    detalleEntity
            );
        }

        return entidad;
    }

    public static Pedido aDominio(
            PedidoEntity entidad) {

        if (entidad == null) {
            return null;
        }

        Pedido pedido =
                new Pedido(
                        entidad.getIdPedido(),
                        UsuarioMapper.aDominio(
                                entidad.getCliente()
                        ),
                        EmprendimientoMapper.aDominio(
                                entidad.getEmprendimiento()
                        ),
                        entidad.getEstado(),
                        entidad.getTotal(),
                        entidad.getObservaciones(),
                        entidad.getFechaPedido()
                );

        for (DetallePedidoEntity detalleEntity
                : entidad.getDetalles()) {

            DetallePedido detalle =
                    DetallePedidoMapper.aDominio(
                            detalleEntity
                    );

            pedido.agregarDetalle(
                    detalle
            );
        }

        return pedido;
    }
}