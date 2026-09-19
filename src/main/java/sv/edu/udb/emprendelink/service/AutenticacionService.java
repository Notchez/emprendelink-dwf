package sv.edu.udb.emprendelink.service;

import sv.edu.udb.emprendelink.model.Usuario;

public interface AutenticacionService {

    Usuario autenticar(String correo, String contrasena);
}