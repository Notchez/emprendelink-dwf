package sv.edu.udb.emprendelink.service;

import sv.edu.udb.emprendelink.model.Publicacion;

import java.util.List;

public interface PublicacionService {

    List<Publicacion> listarActivas();
}