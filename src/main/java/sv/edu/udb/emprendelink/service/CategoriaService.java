package sv.edu.udb.emprendelink.service;

import sv.edu.udb.emprendelink.model.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> listarTodas();

    List<Categoria> listarActivas();

    void crear(
            String nombre,
            String descripcion
    );

    void actualizar(
            Integer idCategoria,
            String nombre,
            String descripcion
    );

    void cambiarEstado(
            Integer idCategoria,
            boolean activo
    );
}