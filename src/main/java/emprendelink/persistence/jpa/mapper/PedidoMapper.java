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

        entidad.setIdPlanAplicado(
                pedido.getIdPlanAplicado()
        );

        entidad.setNombrePlanAplicado(
                pedido.getNombrePlanAplicado()
        );

        entidad.setEstado(
                pedido.getEstado()
        );

        entidad.setTotal(
                pedido.getTotal()
        );

        entidad.setTasaComision(
                pedido.getTasaComision()
        );

        entidad.setMontoComision(
                pedido.getMontoComision()
        );

        entidad.setDireccionEntrega(
                pedido.getDireccionEntrega()
        );

        entidad.setObservaciones(
                pedido.getObservaciones()
        );

        entidad.setFechaPedido(
                pedido.getFechaPedido()
        );

        entidad.setFechaCompletado(
                pedido.getFechaCompletado()
        );

        for (DetallePedido detalle
                : pedido.getDetalles()) {

            entidad.agregarDetalle(
                    DetallePedidoMapper.aEntidad(
                            detalle
                    )
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

        pedido.setIdPlanAplicado(
                entidad.getIdPlanAplicado()
        );

        pedido.setNombrePlanAplicado(
                entidad.getNombrePlanAplicado()
        );

        pedido.setTasaComision(
                entidad.getTasaComision()
        );

        pedido.setMontoComision(
                entidad.getMontoComision()
        );

        pedido.setDireccionEntrega(
                entidad.getDireccionEntrega()
        );

        pedido.setFechaCompletado(
                entidad.getFechaCompletado()
        );

        for (DetallePedidoEntity detalleEntity
                : entidad.getDetalles()) {

            pedido.agregarDetalle(
                    DetallePedidoMapper.aDominio(
                            detalleEntity
                    )
            );
        }

        return pedido;
    }
}