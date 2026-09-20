package sv.edu.udb.emprendelink.dao.jdbc;

import sv.edu.udb.emprendelink.config.ConexionBD;
import sv.edu.udb.emprendelink.dao.EmprendimientoDAO;
import sv.edu.udb.emprendelink.dao.PublicacionDAO;
import sv.edu.udb.emprendelink.model.Categoria;
import sv.edu.udb.emprendelink.model.Emprendimiento;
import sv.edu.udb.emprendelink.model.Publicacion;
import sv.edu.udb.emprendelink.model.enums.TipoPublicacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPublicacionDAO implements PublicacionDAO {

    private static final String SELECT_BASE =
        "SELECT p.id_publicacion, p.id_emprendimiento, p.tipo, p.nombre, p.descripcion, " +
        "p.precio, p.stock, p.activo, p.fecha_publicacion, " +
        "c.id_categoria, c.nombre AS categoria_nombre, c.descripcion AS categoria_descripcion, " +
        "c.activo AS categoria_activo " +
        "FROM publicaciones p JOIN categorias c ON p.id_categoria = c.id_categoria ";

    private final EmprendimientoDAO emprendimientoDAO = new JdbcEmprendimientoDAO();

    @Override
    public Publicacion crear(Publicacion publicacion) {
        String sql = "INSERT INTO publicaciones " +
            "(id_emprendimiento, id_categoria, tipo, nombre, descripcion, precio, stock, activo) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, publicacion.getEmprendimiento().getIdEmprendimiento());
            stmt.setInt(2, publicacion.getCategoria().getIdCategoria());
            stmt.setString(3, publicacion.getTipo().name());
            stmt.setString(4, publicacion.getNombre());
            stmt.setString(5, publicacion.getDescripcion());
            stmt.setBigDecimal(6, publicacion.getPrecio());
            stmt.setObject(7, publicacion.getStock());
            stmt.setBoolean(8, publicacion.isActivo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    publicacion.setIdPublicacion(rs.getInt(1));
                }
            }

            return publicacion;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear publicación: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Publicacion> buscarPorId(Integer idPublicacion) {
        String sql = SELECT_BASE + "WHERE p.id_publicacion = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPublicacion);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar publicación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Publicacion> listarTodos() {
        return listarConFiltro(SELECT_BASE + "ORDER BY p.id_publicacion", null);
    }

    @Override
    public List<Publicacion> listarPorEmprendimiento(Integer idEmprendimiento) {
        String sql = SELECT_BASE + "WHERE p.id_emprendimiento = ? ORDER BY p.id_publicacion";
        return listarConFiltro(sql, idEmprendimiento);
    }

    @Override
    public List<Publicacion> listarPorCategoria(Integer idCategoria) {
        String sql = SELECT_BASE + "WHERE p.id_categoria = ? ORDER BY p.id_publicacion";
        return listarConFiltro(sql, idCategoria);
    }

    private List<Publicacion> listarConFiltro(String sql, Integer parametro) {
        List<Publicacion> publicaciones = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            if (parametro != null) {
                stmt.setInt(1, parametro);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    publicaciones.add(mapear(rs));
                }
            }

            return publicaciones;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar publicaciones: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Publicacion publicacion) {
        String sql = "UPDATE publicaciones SET id_categoria = ?, tipo = ?, nombre = ?, " +
            "descripcion = ?, precio = ?, stock = ?, activo = ? WHERE id_publicacion = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, publicacion.getCategoria().getIdCategoria());
            stmt.setString(2, publicacion.getTipo().name());
            stmt.setString(3, publicacion.getNombre());
            stmt.setString(4, publicacion.getDescripcion());
            stmt.setBigDecimal(5, publicacion.getPrecio());
            stmt.setObject(6, publicacion.getStock());
            stmt.setBoolean(7, publicacion.isActivo());
            stmt.setInt(8, publicacion.getIdPublicacion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar publicación: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Integer idPublicacion) {
        String sql = "DELETE FROM publicaciones WHERE id_publicacion = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPublicacion);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar publicación: " + e.getMessage(), e);
        }
    }

    private Publicacion mapear(ResultSet rs) throws SQLException {
        Emprendimiento emprendimiento = emprendimientoDAO
            .buscarPorId(rs.getInt("id_emprendimiento"))
            .orElse(null);

        Categoria categoria = new Categoria(
            rs.getInt("id_categoria"),
            rs.getString("categoria_nombre"),
            rs.getString("categoria_descripcion"),
            rs.getBoolean("categoria_activo")
        );

        Timestamp timestamp = rs.getTimestamp("fecha_publicacion");

        int stockValor = rs.getInt("stock");
        Integer stock = rs.wasNull() ? null : stockValor;

        return new Publicacion(
            rs.getInt("id_publicacion"),
            emprendimiento,
            categoria,
            TipoPublicacion.valueOf(rs.getString("tipo")),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getBigDecimal("precio"),
            stock,
            rs.getBoolean("activo"),
            timestamp != null ? timestamp.toLocalDateTime() : null
        );
    }
}
