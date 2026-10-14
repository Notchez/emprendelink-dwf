package emprendelink.web.jsf;

import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.service.EmprendimientoService;
import emprendelink.service.PedidoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class AdminNegocioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmprendimientoService emprendimientoService;

    @Inject
    private PedidoService pedidoService;

    private Integer idEmprendimiento;

    private Emprendimiento emprendimiento;

    private List<Pedido> pedidos =
            Collections.emptyList();

    @PostConstruct
    public void inicializar() {

        String parametro =
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get("id");

        if (parametro == null
                || parametro.isBlank()) {

            return;
        }

        try {

            idEmprendimiento =
                    Integer.valueOf(parametro);

        } catch (NumberFormatException e) {

            return;
        }

        cargar();
    }

    private void cargar() {

        emprendimiento =
                emprendimientoService
                        .listarTodos()
                        .stream()
                        .filter(item ->
                                idEmprendimiento.equals(
                                        item.getIdEmprendimiento()
                                )
                        )
                        .findFirst()
                        .orElse(null);

        if (emprendimiento == null
                || emprendimiento.getPropietario() == null
                || emprendimiento
                .getPropietario()
                .getIdUsuario() == null) {

            pedidos =
                    Collections.emptyList();

            return;
        }

        Integer idPropietario =
                emprendimiento
                        .getPropietario()
                        .getIdUsuario();

        pedidos =
                pedidoService
                        .listarRecibidos(idPropietario)
                        .stream()
                        .filter(pedido ->
                                pedido.getEmprendimiento()
                                        != null
                        )
                        .filter(pedido ->
                                idEmprendimiento.equals(
                                        pedido
                                                .getEmprendimiento()
                                                .getIdEmprendimiento()
                                )
                        )
                        .toList();
    }

    public BigDecimal getTotalVentas() {

        return pedidos.stream()
                .map(Pedido::getTotal)
                .filter(total ->
                        total != null
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public Integer getIdEmprendimiento() {
        return idEmprendimiento;
    }

    public Emprendimiento getEmprendimiento() {
        return emprendimiento;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public boolean isEncontrado() {
        return emprendimiento != null;
    }
}