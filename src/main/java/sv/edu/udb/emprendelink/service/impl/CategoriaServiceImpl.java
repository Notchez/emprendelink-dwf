package sv.edu.udb.emprendelink.service.impl;

import sv.edu.udb.emprendelink.dao.CategoriaDAO;
import sv.edu.udb.emprendelink.model.Categoria;
import sv.edu.udb.emprendelink.service.CategoriaService;

import java.util.ArrayList;
import java.util.List;

public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaServiceImpl(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public List<Categoria> listarTodas() {
        return categoriaDAO.listarTodos();
    }

    @Override
    public List<Categoria> listarActivas() {

        List<Categoria> activas = new ArrayList<>();

        for (Categoria categoria : categoriaDAO.listarTodos()) {
            if (categoria.isActivo()) {
                activas.add(categoria);
            }
        }

        return activas;
    }

    @Override
    public void crear(String nombre, String descripcion) {

        validarNombre(nombre);

        Categoria categoria = new Categoria(
                nombre.trim(),
                descripcion
        );

        categoriaDAO.crear(categoria);
    }

    @Override
    public void actualizar(
            Integer idCategoria,
            String nombre,
            String descripcion) {

        validarNombre(nombre);

        Categoria categoria = categoriaDAO.buscarPorId(idCategoria)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La categoría no existe."
                        )
                );

        categoria.setNombre(nombre.trim());
        categoria.setDescripcion(descripcion);

        categoriaDAO.actualizar(categoria);
    }

    @Override
    public void cambiarEstado(Integer idCategoria, boolean activo) {

        Categoria categoria = categoriaDAO.buscarPorId(idCategoria)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La categoría no existe."
                        )
                );

        categoria.setActivo(activo);

        categoriaDAO.actualizar(categoria);
    }

    private void validarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la categoría es obligatorio."
            );
        }
    }
}