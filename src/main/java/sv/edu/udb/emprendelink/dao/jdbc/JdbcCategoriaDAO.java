package sv.edu.udb.emprendelink.dao.jdbc;

import sv.edu.udb.emprendelink.config.ConexionBD;
import sv.edu.udb.emprendelink.dao.CategoriaDAO;
import sv.edu.udb.emprendelink.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCategoriaDAO implements CategoriaDAO {

    @Override
    public Categoria crear(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre, descripcion, activo) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setBoolean(3, categoria.isActivo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt(1));
                }
            }

            return categoria;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Categoria> buscarPorId(Integer idCategoria) {
        String sql = "SELECT id_categoria, nombre, descripcion, activo FROM categorias WHERE id_categoria = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idCategoria);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Categoria> listarTodos() {
        String sql = "SELECT id_categoria, nombre, descripcion, activo FROM categorias ORDER BY id_categoria";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapear(rs));
            }

            return categorias;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar categorías: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ?, activo = ? WHERE id_categoria = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setBoolean(3, categoria.isActivo());
            stmt.setInt(4, categoria.getIdCategoria());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar categoría: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Integer idCategoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idCategoria);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoría: " + e.getMessage(), e);
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(
            rs.getInt("id_categoria"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getBoolean("activo")
        );
    }
}
