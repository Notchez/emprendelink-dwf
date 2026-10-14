package emprendelink.service;

import emprendelink.model.Pedido;

import java.util.List;

public interface AdminConsultaService {

    List<Pedido> listarPedidosPorEmprendimiento(
            Integer idEmprendimiento
    );

    List<Pedido> listarPedidosCompletados(
            Integer idEmprendimiento,
            Integer mes,
            Integer anio
    );
}