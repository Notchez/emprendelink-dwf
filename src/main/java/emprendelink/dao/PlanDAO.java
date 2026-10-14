package emprendelink.dao;

import emprendelink.model.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanDAO {

    Plan crear(Plan plan);

    Optional<Plan> buscarPorId(
            Integer idPlan
    );

    Optional<Plan> buscarPorNombre(
            String nombre
    );

    List<Plan> listarTodos();

    boolean actualizar(
            Plan plan
    );

    boolean eliminar(
            Integer idPlan
    );
}