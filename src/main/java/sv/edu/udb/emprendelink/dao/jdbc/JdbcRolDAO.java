package sv.edu.udb.emprendelink.dao.jdbc;

import sv.edu.udb.emprendelink.config.ConexionBD;
import sv.edu.udb.emprendelink.dao.RolDAO;
import sv.edu.udb.emprendelink.model.Rol;
import sv.edu.udb.emprendelink.model.enums.TipoRol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcRolDAO implements RolDAO {

    @Override
    public Rol crear(Rol rol) {
        String sql = "INSERT INTO roles (nombre) VALUES (?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, rol.getNombre().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    rol.setIdRol(rs.getInt(1));
                }
            }

            return rol;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear rol: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Rol> buscarPorId(Integer idRol) {
        String sql = "SELECT id_rol, nombre FROM roles WHERE id_rol = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idRol);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar rol: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Rol> listarTodos() {
        String sql = "SELECT id_rol, nombre FROM roles ORDER BY id_rol";
        List<Rol> roles = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roles.add(mapear(rs));
            }

            return roles;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar roles: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Rol rol) {
        String sql = "UPDATE roles SET nombre = ? WHERE id_rol = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, rol.getNombre().name());
            stmt.setInt(2, rol.getIdRol());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar rol: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Integer idRol) {
        String sql = "DELETE FROM roles WHERE id_rol = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idRol);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar rol: " + e.getMessage(), e);
        }
    }

    private Rol mapear(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id_rol");
        TipoRol nombre = TipoRol.valueOf(rs.getString("nombre"));
        return new Rol(id, nombre);
    }
}
