package sv.edu.udb.emprendelink.dao;

import sv.edu.udb.emprendelink.model.Publicacion;

import java.util.List;
import java.util.Optional;

public interface PublicacionDAO {

    Publicacion crear(Publicacion publicacion);

    Optional<Publicacion> buscarPorId(Integer idPublicacion);

    List<Publicacion> listarTodos();

    List<Publicacion> listarPorEmprendimiento(Integer idEmprendimiento);

    List<Publicacion> listarPorCategoria(Integer idCategoria);

    boolean actualizar(Publicacion publicacion);

    boolean eliminar(Integer idPublicacion);
}