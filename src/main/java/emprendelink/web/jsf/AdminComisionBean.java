package emprendelink.web.jsf;

import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.service.AdminConsultaService;
import emprendelink.service.EmprendimientoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
@ViewScoped
public class AdminComisionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EmprendimientoService emprendimientoService;

    @Inject
    private AdminConsultaService adminConsultaService;

    private Integer idEmprendimiento;

    private Integer mes;

    private Integer anio;

    private List<Emprendimiento> emprendimientos;

    private List<ResumenComision> resumenes;

    private List<Pedido> pedidos;

    private BigDecimal totalVentas;

    private BigDecimal totalComisiones;

    private int totalPedidos;

    @PostConstruct
    public void inicializar() {

        LocalDate hoy =
                LocalDate.now();

        mes =
                hoy.getMonthValue();

        anio =
                hoy.getYear();

        emprendimientos =
                emprendimientoService
                        .listarTodos();

        recalcular();
    }

    public void filtrar() {

        recalcular();
    }

    private void recalcular() {

        pedidos =
                new ArrayList<>(
                        adminConsultaService
                                .listarPedidosCompletados(
                                        idEmprendimiento,
                                        mes,
                                        anio
                                )
                );

        resumenes =
                new ArrayList<>();

        totalVentas =
                pedidos.stream()
                        .map(Pedido::getTotal)
                        .filter(Objects::nonNull)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        totalComisiones =
                pedidos.stream()
                        .map(Pedido::getMontoComision)
                        .filter(Objects::nonNull)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        totalPedidos =
                pedidos.size();

        construirResumenes();
    }

    private void construirResumenes() {

        for (Emprendimiento negocio
                : emprendimientos) {

            if (idEmprendimiento != null
                    && !idEmprendimiento.equals(
                    negocio.getIdEmprendimiento()
            )) {

                continue;
            }

            List<Pedido> pedidosNegocio =
                    pedidos.stream()
                            .filter(pedido ->
                                    pedido.getEmprendimiento()
                                            != null
                            )
                            .filter(pedido ->
                                    Objects.equals(
                                            pedido
                                                    .getEmprendimiento()
                                                    .getIdEmprendimiento(),
                                            negocio
                                                    .getIdEmprendimiento()
                                    )
                            )
                            .toList();

            BigDecimal ventas =
                    pedidosNegocio.stream()
                            .map(Pedido::getTotal)
                            .filter(Objects::nonNull)
                            .reduce(
                                    BigDecimal.ZERO,
                                    BigDecimal::add
                            );

            BigDecimal comisiones =
                    pedidosNegocio.stream()
                            .map(Pedido::getMontoComision)
                            .filter(Objects::nonNull)
                            .reduce(
                                    BigDecimal.ZERO,
                                    BigDecimal::add
                            );

            if (!pedidosNegocio.isEmpty()
                    || idEmprendimiento != null) {

                resumenes.add(
                        new ResumenComision(
                                negocio,
                                pedidosNegocio.size(),
                                ventas,
                                comisiones
                        )
                );
            }
        }
    }

    public BigDecimal getComisionPedido(
            Pedido pedido) {

        if (pedido == null
                || pedido.getMontoComision()
                == null) {

            return BigDecimal.ZERO;
        }

        return pedido.getMontoComision();
    }

    public String porcentajeComision(
            Pedido pedido) {

        if (pedido == null
                || pedido.getTasaComision()
                == null) {

            return "—";
        }

        return pedido
                .getTasaComision()
                .multiply(
                        BigDecimal.valueOf(100)
                )
                .stripTrailingZeros()
                .toPlainString();
    }

    public int getTotalNegocios() {

        if (resumenes == null) {
            return 0;
        }

        return (int) resumenes
                .stream()
                .filter(resumen ->
                        resumen.getCantidadPedidos()
                                > 0
                )
                .count();
    }

    public List<Integer> getAnios() {

        int actual =
                LocalDate.now()
                        .getYear();

        List<Integer> resultado =
                new ArrayList<>();

        for (int i = 0;
             i < 5;
             i++) {

            resultado.add(
                    actual - i
            );
        }

        return resultado;
    }

    public Integer getIdEmprendimiento() {
        return idEmprendimiento;
    }

    public void setIdEmprendimiento(
            Integer idEmprendimiento) {

        this.idEmprendimiento =
                idEmprendimiento;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(
            Integer mes) {

        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(
            Integer anio) {

        this.anio = anio;
    }

    public List<Emprendimiento> getEmprendimientos() {
        return emprendimientos;
    }

    public List<ResumenComision> getResumenes() {
        return resumenes;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public BigDecimal getTotalComisiones() {
        return totalComisiones;
    }

    public int getTotalPedidos() {
        return totalPedidos;
    }

    public static class ResumenComision
            implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Emprendimiento emprendimiento;

        private final int cantidadPedidos;

        private final BigDecimal ventas;

        private final BigDecimal comision;

        public ResumenComision(
                Emprendimiento emprendimiento,
                int cantidadPedidos,
                BigDecimal ventas,
                BigDecimal comision) {

            this.emprendimiento =
                    emprendimiento;

            this.cantidadPedidos =
                    cantidadPedidos;

            this.ventas =
                    ventas;

            this.comision =
                    comision;
        }

        public Emprendimiento getEmprendimiento() {
            return emprendimiento;
        }

        public int getCantidadPedidos() {
            return cantidadPedidos;
        }

        public BigDecimal getVentas() {
            return ventas;
        }

        public BigDecimal getComision() {
            return comision;
        }
    }
}