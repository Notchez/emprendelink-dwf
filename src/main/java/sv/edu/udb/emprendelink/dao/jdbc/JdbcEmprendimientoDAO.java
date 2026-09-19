package sv.edu.udb.emprendelink.dao.jdbc;

import sv.edu.udb.emprendelink.config.ConexionBD;
import sv.edu.udb.emprendelink.dao.EmprendimientoDAO;
import sv.edu.udb.emprendelink.model.Emprendimiento;
import sv.edu.udb.emprendelink.model.Rol;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.TipoRol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcEmprendimientoDAO implements EmprendimientoDAO {


    private static final String SELECT_BASE =
        "SELECT e.id_emprendimiento, e.nombre AS e_nombre, e.descripcion, " +
        "e.contacto, e.activo, e.fecha_registro, " +
        "u.id_usuario, u.nombre AS u_nombre, u.apellido, u.correo, " +
        "u.contrasena_hash, u.telefono, u.activo AS u_activo, u.fecha_registro AS u_fecha_registro, " +
        "r.id_rol, r.nombre AS rol_nombre " +
        "FROM emprendimientos e " +
        "JOIN usuarios u ON e.id_propietario = u.id_usuario " +
        "JOIN roles r ON u.id_rol = r.id_rol ";

    @Override
    public Emprendimiento crear(Emprendimiento emprendimiento) {
        String sql = "INSERT INTO emprendimientos (id_propietario, nombre, descripcion, contacto, activo) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, emprendimiento.getPropietario().getIdUsuario());
            stmt.setString(2, emprendimiento.getNombre());
            stmt.setString(3, emprendimiento.getDescripcion());
            stmt.setString(4, emprendimiento.getContacto());
            stmt.setBoolean(5, emprendimiento.isActivo());

            stmt.executeUpdate();

            int idGenerado;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                idGenerado = rs.getInt(1);
            }


            return buscarPorId(idGenerado)
                .orElseThrow(() -> new RuntimeException("El emprendimiento se creó pero no se pudo releer"));

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear emprendimiento: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Emprendimiento> buscarPorId(Integer idEmprendimiento) {
        String sql = SELECT_BASE + "WHERE e.id_emprendimiento = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idEmprendimiento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar emprendimiento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Emprendimiento> listarTodos() {
        String sql = SELECT_BASE + "ORDER BY e.id_emprendimiento";
        return ejecutarListado(sql, null);
    }

    @Override
    public List<Emprendimiento> listarPorPropietario(Integer idUsuario) {
        String sql = SELECT_BASE + "WHERE u.id_usuario = ? ORDER BY e.id_emprendimiento";
        return ejecutarListado(sql, idUsuario);
    }

    private List<Emprendimiento> ejecutarListado(String sql, Integer parametroOpcional) {
        List<Emprendimiento> resultado = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            if (parametroOpcional != null) {
                stmt.setInt(1, parametroOpcional);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }

            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar emprendimientos: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Emprendimiento emprendimiento) {
        String sql = "UPDATE emprendimientos SET nombre = ?, descripcion = ?, contacto = ?, activo = ? " +
            "WHERE id_emprendimiento = ?";


        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, emprendimiento.getNombre());
            stmt.setString(2, emprendimiento.getDescripcion());
            stmt.setString(3, emprendimiento.getContacto());
            stmt.setBoolean(4, emprendimiento.isActivo());
            stmt.setInt(5, emprendimiento.getIdEmprendimiento());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar emprendimiento: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Integer idEmprendimiento) {
        String sql = "DELETE FROM emprendimientos WHERE id_emprendimiento = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idEmprendimiento);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar emprendimiento: " + e.getMessage(), e);
        }
    }

    private Emprendimiento mapear(ResultSet rs) throws SQLException {
        Rol rol = new Rol(
            rs.getInt("id_rol"),
            TipoRol.valueOf(rs.getString("rol_nombre"))
        );

        Timestamp tsUsuario = rs.getTimestamp("u_fecha_registro");
        Usuario propietario = new Usuario(
            rs.getInt("id_usuario"),
            rol,
            rs.getString("u_nombre"),
            rs.getString("apellido"),
            rs.getString("correo"),
            rs.getString("contrasena_hash"),
            rs.getString("telefono"),
            rs.getBoolean("u_activo"),
            tsUsuario != null ? tsUsuario.toLocalDateTime() : null
        );

        Timestamp tsEmprendimiento = rs.getTimestamp("fecha_registro");

        return new Emprendimiento(
            rs.getInt("id_emprendimiento"),
            propietario,
            rs.getString("e_nombre"),
            rs.getString("descripcion"),
            rs.getString("contacto"),
            rs.getBoolean("activo"),
            tsEmprendimiento != null ? tsEmprendimiento.toLocalDateTime() : null
        );
    }
}
