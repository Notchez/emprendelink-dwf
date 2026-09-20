package sv.edu.udb.emprendelink.service.impl;

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
import sv.edu.udb.emprendelink.model.enums.TipoPublicacion;
import sv.edu.udb.emprendelink.model.enums.TipoRol;
import sv.edu.udb.emprendelink.service.PedidoService;

import java.util.List;
import java.util.Objects;

public class PedidoServiceImpl implements PedidoService {

    private final PedidoDAO pedidoDAO;
    private final UsuarioDAO usuarioDAO;
    private final EmprendimientoDAO emprendimientoDAO;
    private final PublicacionDAO publicacionDAO;

    public PedidoServiceImpl(
            PedidoDAO pedidoDAO,
            UsuarioDAO usuarioDAO,
            EmprendimientoDAO emprendimientoDAO,
            PublicacionDAO publicacionDAO) {

        this.pedidoDAO = pedidoDAO;
        this.usuarioDAO = usuarioDAO;
        this.emprendimientoDAO = emprendimientoDAO;
        this.publicacionDAO = publicacionDAO;
    }

    @Override
    public List<Pedido> listarPorCliente(
            Integer idCliente) {

        requerirUsuarioActivo(
                idCliente,
                TipoRol.ROLE_CLIENTE
        );

        return pedidoDAO.listarPorCliente(idCliente);
    }

    @Override
    public List<Pedido> listarRecibidos(
            Integer idEmprendedor) {

        requerirUsuarioActivo(
                idEmprendedor,
                TipoRol.ROLE_EMPRENDEDOR
        );

        return pedidoDAO.listarTodos()
                .stream()
                .filter(p -> perteneceAEmprendedor(
                        p,
                        idEmprendedor
                ))
                .toList();
    }

    @Override
    public Pedido buscarVisibleParaUsuario(
            Integer idPedido,
            Integer idUsuario) {

        if (idPedido == null || idUsuario == null) {
            return null;
        }

        Usuario usuario = usuarioDAO.buscarPorId(
                idUsuario
        ).orElse(null);

        if (usuario == null || !usuario.isActivo()) {
            return null;
        }

        Pedido pedido = pedidoDAO.buscarPorId(
                idPedido
        ).orElse(null);

        if (pedido == null) {
            return null;
        }

        boolean esCliente = pedido.getCliente() != null
                && Objects.equals(
                pedido.getCliente().getIdUsuario(),
                idUsuario
        );

        boolean esEmprendedor =
                perteneceAEmprendedor(
                        pedido,
                        idUsuario
                );

        boolean esAdministrador = usuario.getRol() != null
                && usuario.getRol().getNombre()
                == TipoRol.ROLE_ADMIN;

        return esCliente || esEmprendedor || esAdministrador
                ? pedido
                : null;
    }

    @Override
    public void crear(
            Integer idCliente,
            Integer idEmprendimiento,
            List<Integer> idsPublicacion,
            List<Integer> cantidades,
            String observaciones) {

        Usuario cliente = requerirUsuarioActivo(
                idCliente,
                TipoRol.ROLE_CLIENTE
        );

        if (idEmprendimiento == null
                || idsPublicacion == null
                || cantidades == null
                || idsPublicacion.isEmpty()
                || idsPublicacion.size() != cantidades.size()) {

            throw new IllegalArgumentException(
                    "Los datos del pedido son inválidos."
            );
        }

        Emprendimiento emprendimiento =
                emprendimientoDAO.buscarPorId(
                        idEmprendimiento
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "El emprendimiento no existe."
                        )
                );

        if (!emprendimiento.isActivo()
                || emprendimiento.getPropietario() == null
                || !emprendimiento.getPropietario().isActivo()) {

            throw new IllegalArgumentException(
                    "El emprendimiento no está disponible."
            );
        }

        Pedido pedido = new Pedido(
                cliente,
                emprendimiento,
                observaciones
        );

        for (int i = 0; i < idsPublicacion.size(); i++) {

            Integer idPublicacion = idsPublicacion.get(i);
            Integer cantidad = cantidades.get(i);

            if (idPublicacion == null
                    || cantidad == null
                    || cantidad <= 0) {

                throw new IllegalArgumentException(
                        "Las cantidades del pedido son inválidas."
                );
            }

            Publicacion publicacion =
                    publicacionDAO.buscarPorId(
                            idPublicacion
                    ).orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Una publicación no existe."
                            )
                    );

            if (!publicacion.isActivo()
                    || publicacion.getEmprendimiento() == null
                    || !Objects.equals(
                    publicacion.getEmprendimiento()
                            .getIdEmprendimiento(),
                    idEmprendimiento)
                    || publicacion.getCategoria() == null
                    || !publicacion.getCategoria().isActivo()
                    || publicacion.getPrecio() == null
                    || publicacion.getPrecio().signum() < 0) {

                throw new IllegalArgumentException(
                        "Una publicación no está disponible."
                );
            }

            if (publicacion.getTipo()
                    == TipoPublicacion.PRODUCTO
                    && (publicacion.getStock() == null
                    || publicacion.getStock() < cantidad)) {

                throw new IllegalArgumentException(
                        "No hay existencias suficientes."
                );
            }

            DetallePedido detalle = new DetallePedido(
                    publicacion,
                    cantidad,
                    publicacion.getPrecio()
            );

            pedido.agregarDetalle(detalle);
        }

        pedidoDAO.crear(pedido);
    }

    @Override
    public void actualizarEstado(
            Integer idEmprendedor,
            Integer idPedido,
            EstadoPedido estado) {

        requerirUsuarioActivo(
                idEmprendedor,
                TipoRol.ROLE_EMPRENDEDOR
        );

        if (idPedido == null || estado == null) {
            throw new IllegalArgumentException(
                    "El pedido y el estado son obligatorios."
            );
        }

        Pedido pedido = pedidoDAO.buscarPorId(
                idPedido
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "El pedido no existe."
                )
        );

        if (!perteneceAEmprendedor(
                pedido,
                idEmprendedor)) {

            throw new IllegalArgumentException(
                    "El pedido no pertenece a tu emprendimiento."
            );
        }

        if (!pedidoDAO.actualizarEstado(
                idPedido,
                estado)) {

            throw new IllegalStateException(
                    "No se pudo actualizar el pedido."
            );
        }
    }

    private Usuario requerirUsuarioActivo(
            Integer idUsuario,
            TipoRol rolEsperado) {

        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El usuario es obligatorio."
            );
        }

        Usuario usuario = usuarioDAO.buscarPorId(
                idUsuario
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "El usuario no existe."
                )
        );

        if (!usuario.isActivo()
                || usuario.getRol() == null
                || usuario.getRol().getNombre()
                != rolEsperado) {

            throw new IllegalArgumentException(
                    "El usuario no tiene permisos para esta operación."
            );
        }

        return usuario;
    }

    private boolean perteneceAEmprendedor(
            Pedido pedido,
            Integer idEmprendedor) {

        return pedido != null
                && pedido.getEmprendimiento() != null
                && pedido.getEmprendimiento()
                .getPropietario() != null
                && Objects.equals(
                pedido.getEmprendimiento()
                        .getPropietario()
                        .getIdUsuario(),
                idEmprendedor
        );
    }
}