package emprendelink.service;

import emprendelink.model.Usuario;

public interface AutenticacionService {

    Usuario autenticar(String correo, String contrasena);
}