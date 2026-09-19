package sv.edu.udb.emprendelink.dao;

import sv.edu.udb.emprendelink.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaDAO {

    Categoria crear(Categoria categoria);

    Optional<Categoria> buscarPorId(Integer idCategoria);

    List<Categoria> listarTodos();

    boolean actualizar(Categoria categoria);

    boolean eliminar(Integer idCategoria);
}