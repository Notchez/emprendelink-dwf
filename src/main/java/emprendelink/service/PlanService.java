package emprendelink.service;

import emprendelink.model.Plan;

import java.math.BigDecimal;
import java.util.List;

public interface PlanService {

    List<Plan> listarTodos();

    List<Plan> listarActivos();

    Plan buscarPorId(
            Integer idPlan
    );

    void crear(
            String nombre,
            Integer maxProductosActivos,
            BigDecimal tasaComision
    );

    void actualizar(
            Integer idPlan,
            String nombre,
            Integer maxProductosActivos,
            BigDecimal tasaComision
    );

    void cambiarEstado(
            Integer idPlan,
            boolean activo
    );

    void asignarAEmprendimiento(
            Integer idEmprendimiento,
            Integer idPlan
    );
}