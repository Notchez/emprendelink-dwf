package sv.edu.udb.emprendelink.dao.jdbc;

import sv.edu.udb.emprendelink.config.ConexionBD;
import sv.edu.udb.emprendelink.dao.EmprendimientoDAO;
import sv.edu.udb.emprendelink.dao.PedidoDAO;
import sv.edu.udb.emprendelink.dao.PublicacionDAO;
import sv.edu.udb.emprendelink.dao.UsuarioDAO;
import sv.edu.udb.emprendelink.model.DetallePedido;
import sv.edu.udb.emprendelink.model.Emprendimiento;
import sv.edu.udb.emprendelink.model.Pedido;
import sv.edu.udb.emprendelink.model.Publicacion;
import sv.edu.udb.emprendelink.model.Usuario;
import sv.edu.udb.emprendelink.model.enums.EstadoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPedidoDAO implements PedidoDAO {

    private static final String SQL_INSERT_PEDIDO =
            "INSERT INTO pedidos (id_cliente, id_emprendimiento, estado, total, observaciones, fecha_pedido) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_DETALLE =
            "INSERT INTO detalle_pedido (id_pedido, id_publicacion, cantidad, precio_unitario, subtotal) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_HEADER_BASE =
            "SELECT id_pedido, id_cliente, id_emprendimiento, estado, total, observaciones, fecha_pedido " +
                    "FROM pedidos ";

    private static final String SQL_SELECT_DETALLES =
            "SELECT id_detalle_pedido, id_publicacion, cantidad, precio_unitario, subtotal " +
                    "FROM detalle_pedido WHERE id_pedido = ?";

    private final UsuarioDAO usuarioDAO = new JdbcUsuarioDAO();
    private final EmprendimientoDAO emprendimientoDAO = new JdbcEmprendimientoDAO();
    private final PublicacionDAO publicacionDAO = new JdbcPublicacionDAO();

    @Override
    public Pedido crear(Pedido pedido) {
        Connection con = null;

        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            try (PreparedStatement stmt = con.prepareStatement(SQL_INSERT_PEDIDO, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, pedido.getCliente().getIdUsuario());
                stmt.setInt(2, pedido.getEmprendimiento().getIdEmprendimiento());
                stmt.setString(3, pedido.getEstado().name());
                stmt.setBigDecimal(4, pedido.getTotal());
                stmt.setString(5, pedido.getObservaciones());
                stmt.setTimestamp(6, Timestamp.valueOf(pedido.getFechaPedido()));
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setIdPedido(rs.getInt(1));
                    }
                }
            }

            try (PreparedStatement stmt = con.prepareStatement(SQL_INSERT_DETALLE)) {
                for (DetallePedido detalle : pedido.getDetalles()) {
                    stmt.setInt(1, pedido.getIdPedido());
                    stmt.setInt(2, detalle.getPublicacion().getIdPublicacion());
                    stmt.setInt(3, detalle.getCantidad());
                    stmt.setBigDecimal(4, detalle.getPrecioUnitario());
                    stmt.setBigDecimal(5, detalle.getSubtotal());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            con.commit();
            return pedido;

        } catch (SQLException e) {
            hacerRollback(con);
            throw new RuntimeException("Error al crear pedido: " + e.getMessage(), e);
        } finally {
            cerrarConexion(con);
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(Integer idPedido) {
        String sql = SQL_SELECT_HEADER_BASE + "WHERE id_pedido = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = mapearHeader(rs);
                    cargarDetalles(con, pedido);
                    return Optional.of(pedido);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pedido> listarTodos() {
        return listarConFiltro(SQL_SELECT_HEADER_BASE + "ORDER BY id_pedido", null);
    }

    @Override
    public List<Pedido> listarPorCliente(Integer idCliente) {
        String sql = SQL_SELECT_HEADER_BASE + "WHERE id_cliente = ? ORDER BY id_pedido";
        return listarConFiltro(sql, idCliente);
    }

    @Override
    public List<Pedido> listarPorEmprendimiento(Integer idEmprendimiento) {
        String sql = SQL_SELECT_HEADER_BASE + "WHERE id_emprendimiento = ? ORDER BY id_pedido";
        return listarConFiltro(sql, idEmprendimiento);
    }

    private List<Pedido> listarConFiltro(String sql, Integer valorParametro) {
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            if (valorParametro != null) {
                stmt.setInt(1, valorParametro);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = mapearHeader(rs);
                    cargarDetalles(con, pedido);
                    pedidos.add(pedido);
                }
            }

            return pedidos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarEstado(Integer idPedido, EstadoPedido nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id_pedido = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado.name());
            stmt.setInt(2, idPedido);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado del pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Integer idPedido) {
        Connection con = null;

        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM detalle_pedido WHERE id_pedido = ?")) {
                stmt.setInt(1, idPedido);
                stmt.executeUpdate();
            }

            boolean eliminado;
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM pedidos WHERE id_pedido = ?")) {
                stmt.setInt(1, idPedido);
                eliminado = stmt.executeUpdate() > 0;
            }

            con.commit();
            return eliminado;

        } catch (SQLException e) {
            hacerRollback(con);
            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);
        } finally {
            cerrarConexion(con);
        }
    }

    private void cargarDetalles(Connection con, Pedido pedido) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(SQL_SELECT_DETALLES)) {
            stmt.setInt(1, pedido.getIdPedido());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Publicacion publicacion = publicacionDAO
                            .buscarPorId(rs.getInt("id_publicacion"))
                            .orElse(null);

                    DetallePedido detalle = new DetallePedido(
                            rs.getInt("id_detalle_pedido"),
                            pedido,
                            publicacion,
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio_unitario")
                    );

                    pedido.agregarDetalle(detalle);
                }
            }
        }
    }

    private Pedido mapearHeader(ResultSet rs) throws SQLException {
        Usuario cliente = usuarioDAO.buscarPorId(rs.getInt("id_cliente")).orElse(null);
        Emprendimiento emprendimiento = emprendimientoDAO
                .buscarPorId(rs.getInt("id_emprendimiento"))
                .orElse(null);

        Timestamp timestamp = rs.getTimestamp("fecha_pedido");

        return new Pedido(
                rs.getInt("id_pedido"),
                cliente,
                emprendimiento,
                EstadoPedido.valueOf(rs.getString("estado")),
                rs.getBigDecimal("total"),
                rs.getString("observaciones"),
                timestamp != null ? timestamp.toLocalDateTime() : null
        );
    }

    private void hacerRollback(Connection con) {
        if (con == null) {
            return;
        }
        try {
            con.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Error al revertir transacción del pedido: " + e.getMessage(), e);
        }
    }

    private void cerrarConexion(Connection con) {
        if (con == null) {
            return;
        }
        try {
            con.setAutoCommit(true);
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar conexión: " + e.getMessage(), e);
        }
    }
}