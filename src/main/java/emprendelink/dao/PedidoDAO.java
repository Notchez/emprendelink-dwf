package emprendelink.dao;

import emprendelink.model.Pedido;
import emprendelink.model.enums.EstadoPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoDAO {

    Pedido crear(Pedido pedido);

    Optional<Pedido> buscarPorId(Integer idPedido);

    List<Pedido> listarTodos();

    List<Pedido> listarPorCliente(Integer idCliente);

    List<Pedido> listarPorEmprendimiento(Integer idEmprendimiento);

    boolean actualizarEstado(Integer idPedido, EstadoPedido nuevoEstado);

    boolean eliminar(Integer idPedido);
}