package emprendelink.service;

import emprendelink.model.Pedido;
import emprendelink.model.enums.EstadoPedido;

import java.util.List;

public interface PedidoService {

    List<Pedido> listarPorCliente(
            Integer idCliente
    );

    List<Pedido> listarRecibidos(
            Integer idEmprendedor
    );

    Pedido buscarVisibleParaUsuario(
            Integer idPedido,
            Integer idUsuario
    );

    void crear(
            Integer idCliente,
            Integer idEmprendimiento,
            List<Integer> idsPublicacion,
            List<Integer> cantidades,
            String observaciones
    );

    void actualizarEstado(
            Integer idEmprendedor,
            Integer idPedido,
            EstadoPedido estado
    );
}