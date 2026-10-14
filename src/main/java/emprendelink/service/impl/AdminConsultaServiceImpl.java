package emprendelink.service.impl;

import emprendelink.dao.PedidoDAO;
import emprendelink.model.Pedido;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.service.AdminConsultaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class AdminConsultaServiceImpl
        implements AdminConsultaService {

    private final PedidoDAO pedidoDAO;

    @Inject
    public AdminConsultaServiceImpl(
            PedidoDAO pedidoDAO) {

        this.pedidoDAO =
                pedidoDAO;
    }

    @Override
    public List<Pedido> listarPedidosPorEmprendimiento(
            Integer idEmprendimiento) {

        if (idEmprendimiento == null) {
            return List.of();
        }

        return pedidoDAO
                .listarPorEmprendimiento(
                        idEmprendimiento
                );
    }

    @Override
    public List<Pedido> listarPedidosCompletados(
            Integer idEmprendimiento,
            Integer mes,
            Integer anio) {

        if (mes == null
                || mes < 1
                || mes > 12
                || anio == null
                || anio < 2000) {

            throw new IllegalArgumentException(
                    "El período seleccionado no es válido."
            );
        }

        return pedidoDAO
                .listarTodos()
                .stream()
                .filter(pedido ->
                        pedido.getEstado()
                                == EstadoPedido.COMPLETADO
                )
                .filter(pedido ->
                        pedido.getFechaCompletado()
                                != null
                )
                .filter(pedido ->
                        pedido.getFechaCompletado()
                                .getMonthValue()
                                == mes
                )
                .filter(pedido ->
                        pedido.getFechaCompletado()
                                .getYear()
                                == anio
                )
                .filter(pedido ->
                        idEmprendimiento
                                == null
                                || pedido
                                .getEmprendimiento()
                                != null
                                && Objects.equals(
                                pedido
                                        .getEmprendimiento()
                                        .getIdEmprendimiento(),
                                idEmprendimiento
                        )
                )
                .toList();
    }
}