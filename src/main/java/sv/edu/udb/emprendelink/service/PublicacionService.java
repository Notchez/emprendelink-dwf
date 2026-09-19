package sv.edu.udb.emprendelink.service;

import sv.edu.udb.emprendelink.model.Publicacion;
import sv.edu.udb.emprendelink.model.enums.TipoPublicacion;

import java.math.BigDecimal;
import java.util.List;

public interface PublicacionService {

    List<Publicacion> listarActivas();

    List<Publicacion> listarTodas();

    List<Publicacion> listarPorPropietario(
            Integer idPropietario
    );

    Publicacion buscarActivaPorId(
            Integer idPublicacion
    );

    Publicacion buscarPropiaPorId(
            Integer idPublicacion,
            Integer idPropietario
    );

    void crear(
            Integer idPropietario,
            Integer idEmprendimiento,
            Integer idCategoria,
            TipoPublicacion tipo,
            String nombre,
            String descripcion,
            BigDecimal precio,
            Integer stock
    );

    void actualizar(
            Integer idPropietario,
            Integer idPublicacion,
            Integer idEmprendimiento,
            Integer idCategoria,
            TipoPublicacion tipo,
            String nombre,
            String descripcion,
            BigDecimal precio,
            Integer stock
    );

    void cambiarEstado(
            Integer idUsuario,
            Integer idPublicacion,
            boolean activo
    );
}