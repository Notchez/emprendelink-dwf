package sv.edu.udb.emprendelink.dao;

import sv.edu.udb.emprendelink.model.Publicacion;

import java.util.List;

public interface PublicacionDAO {

    List<Publicacion> listarActivas();
}