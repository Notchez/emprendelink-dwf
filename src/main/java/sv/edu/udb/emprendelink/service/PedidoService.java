package sv.edu.udb.emprendelink.service;

import sv.edu.udb.emprendelink.model.Pedido;
import sv.edu.udb.emprendelink.model.enums.EstadoPedido;

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