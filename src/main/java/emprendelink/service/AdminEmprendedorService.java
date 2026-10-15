package emprendelink.service;

public interface AdminEmprendedorService {

    void crearEmprendedorConNegocio(
            Integer idAdministrador,
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String telefono,
            String nombreNegocio,
            String descripcionNegocio,
            String contactoNegocio,
            String logoUrl,
            Integer idPlan,
            Integer idCategoria
    );
}