package emprendelink.service.impl;

import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.PedidoDAO;
import emprendelink.dao.PublicacionDAO;
import emprendelink.dao.UsuarioDAO;
import emprendelink.model.DetallePedido;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.model.Plan;
import emprendelink.model.Publicacion;
import emprendelink.model.Usuario;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.model.enums.TipoRol;
import emprendelink.service.PedidoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class PedidoServiceImpl
        implements PedidoService {

    private final PedidoDAO pedidoDAO;
    private final UsuarioDAO usuarioDAO;
    private final EmprendimientoDAO emprendimientoDAO;
    private final PublicacionDAO publicacionDAO;

    @Inject
    public PedidoServiceImpl(
            PedidoDAO pedidoDAO,
            UsuarioDAO usuarioDAO,
            EmprendimientoDAO emprendimientoDAO,
            PublicacionDAO publicacionDAO) {

        this.pedidoDAO =
                pedidoDAO;

        this.usuarioDAO =
                usuarioDAO;

        this.emprendimientoDAO =
                emprendimientoDAO;

        this.publicacionDAO =
                publicacionDAO;
    }

    @Override
    public List<Pedido> listarPorCliente(
            Integer idCliente) {

        requerirUsuarioActivo(
                idCliente,
                TipoRol.ROLE_CLIENTE
        );

        return pedidoDAO.listarPorCliente(
                idCliente
        );
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
                .filter(
                        pedido ->
                                perteneceAEmprendedor(
                                        pedido,
                                        idEmprendedor
                                )
                )
                .toList();
    }

    @Override
    public List<Pedido> listarActivosRecibidos(
            Integer idEmprendedor) {

        return listarRecibidos(
                idEmprendedor
        )
                .stream()
                .filter(
                        pedido ->
                                pedido.getEstado()
                                        != EstadoPedido.COMPLETADO
                                        && pedido.getEstado()
                                        != EstadoPedido.CANCELADO
                )
                .toList();
    }

    @Override
    public List<Pedido> listarCompletados(
            Integer idEmprendedor) {

        return listarRecibidos(
                idEmprendedor
        )
                .stream()
                .filter(
                        pedido ->
                                pedido.getEstado()
                                        == EstadoPedido.COMPLETADO
                )
                .toList();
    }

    @Override
    public Pedido buscarVisibleParaUsuario(
            Integer idPedido,
            Integer idUsuario) {

        if (idPedido == null
                || idUsuario == null) {

            return null;
        }

        Usuario usuario =
                usuarioDAO.buscarPorId(
                        idUsuario
                ).orElse(null);

        if (usuario == null
                || !usuario.isActivo()
                || usuario.getRol() == null) {

            return null;
        }

        Pedido pedido =
                pedidoDAO.buscarPorId(
                        idPedido
                ).orElse(null);

        if (pedido == null) {
            return null;
        }

        TipoRol rol =
                usuario.getRol()
                        .getNombre();

        return switch (rol) {

            case ROLE_ADMIN ->
                    pedido;

            case ROLE_CLIENTE ->
                    pedido.getCliente() != null
                            && Objects.equals(
                            pedido.getCliente()
                                    .getIdUsuario(),
                            idUsuario
                    )
                            ? pedido
                            : null;

            case ROLE_EMPRENDEDOR ->
                    perteneceAEmprendedor(
                            pedido,
                            idUsuario
                    )
                            ? pedido
                            : null;
        };
    }

    @Override
    public void crear(
            Integer idCliente,
            Integer idEmprendimiento,
            List<Integer> idsPublicacion,
            List<Integer> cantidades,
            String observaciones) {

        Usuario cliente =
                requerirUsuarioActivo(
                        idCliente,
                        TipoRol.ROLE_CLIENTE
                );

        validarDatosPedido(
                idEmprendimiento,
                idsPublicacion,
                cantidades
        );

        Emprendimiento emprendimiento =
                obtenerEmprendimientoDisponible(
                        idEmprendimiento
                );

        Pedido pedido =
                new Pedido(
                        cliente,
                        emprendimiento,
                        normalizarObservaciones(
                                observaciones
                        )
                );

        for (int i = 0;
             i < idsPublicacion.size();
             i++) {

            Integer idPublicacion =
                    idsPublicacion.get(i);

            Integer cantidad =
                    cantidades.get(i);

            validarCantidad(
                    idPublicacion,
                    cantidad
            );

            Publicacion publicacion =
                    obtenerPublicacionDisponible(
                            idPublicacion,
                            idEmprendimiento
                    );

            validarStock(
                    publicacion,
                    cantidad
            );

            DetallePedido detalle =
                    new DetallePedido(
                            publicacion,
                            cantidad,
                            publicacion.getPrecio()
                    );

            pedido.agregarDetalle(
                    detalle
            );

            descontarStock(
                    publicacion,
                    cantidad
            );
        }

        congelarPlanYComision(
                pedido,
                emprendimiento
        );

        pedidoDAO.crear(
                pedido
        );
    }

    @Override
    public void actualizarEstado(
            Integer idEmprendedor,
            Integer idPedido,
            EstadoPedido nuevoEstado) {

        requerirUsuarioActivo(
                idEmprendedor,
                TipoRol.ROLE_EMPRENDEDOR
        );

        if (idPedido == null
                || nuevoEstado == null) {

            throw new IllegalArgumentException(
                    "El pedido y el estado son obligatorios."
            );
        }

        Pedido pedido =
                pedidoDAO.buscarPorId(
                        idPedido
                ).orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "El pedido no existe."
                                )
                );

        if (!perteneceAEmprendedor(
                pedido,
                idEmprendedor
        )) {

            throw new IllegalArgumentException(
                    "El pedido no pertenece a tu negocio."
            );
        }

        validarTransicion(
                pedido.getEstado(),
                nuevoEstado
        );

        if (nuevoEstado
                == EstadoPedido.CANCELADO) {

            restaurarStock(
                    pedido
            );
        }

        if (!pedidoDAO.actualizarEstado(
                idPedido,
                nuevoEstado
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar el pedido."
            );
        }
    }

    private void congelarPlanYComision(
            Pedido pedido,
            Emprendimiento emprendimiento) {

        Plan plan =
                emprendimiento.getPlan();

        if (plan == null
                || plan.getIdPlan() == null
                || plan.getNombre() == null
                || plan.getTasaComision() == null) {

            /*
             * En producción esto no debería ocurrir:
             * la BD exige id_plan en emprendimientos.
             */
            return;
        }

        pedido.setIdPlanAplicado(
                plan.getIdPlan()
        );

        pedido.setNombrePlanAplicado(
                plan.getNombre()
        );

        pedido.setTasaComision(
                plan.getTasaComision()
        );

        BigDecimal monto =
                pedido.getTotal()
                        .multiply(
                                plan.getTasaComision()
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        pedido.setMontoComision(
                monto
        );
    }

    private Usuario requerirUsuarioActivo(
            Integer idUsuario,
            TipoRol rolEsperado) {

        if (idUsuario == null) {

            throw new IllegalArgumentException(
                    "El usuario es obligatorio."
            );
        }

        Usuario usuario =
                usuarioDAO.buscarPorId(
                        idUsuario
                ).orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "El usuario no existe."
                                )
                );

        if (!usuario.isActivo()
                || usuario.getRol() == null
                || usuario.getRol()
                .getNombre()
                != rolEsperado) {

            throw new IllegalArgumentException(
                    "El usuario no tiene permisos para esta operación."
            );
        }

        return usuario;
    }

    private Emprendimiento obtenerEmprendimientoDisponible(
            Integer idEmprendimiento) {

        Emprendimiento emprendimiento =
                emprendimientoDAO.buscarPorId(
                        idEmprendimiento
                ).orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "El negocio no existe."
                                )
                );

        if (!emprendimiento.isActivo()
                || emprendimiento.getPropietario()
                == null
                || !emprendimiento
                .getPropietario()
                .isActivo()) {

            throw new IllegalArgumentException(
                    "El negocio no está disponible."
            );
        }

        return emprendimiento;
    }

    private Publicacion obtenerPublicacionDisponible(
            Integer idPublicacion,
            Integer idEmprendimiento) {

        Publicacion publicacion =
                publicacionDAO.buscarPorId(
                        idPublicacion
                ).orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Una publicación no existe."
                                )
                );

        boolean disponible =
                publicacion.isActivo()
                        && publicacion.getEmprendimiento()
                        != null
                        && Objects.equals(
                        publicacion
                                .getEmprendimiento()
                                .getIdEmprendimiento(),
                        idEmprendimiento
                )
                        && publicacion.getCategoria()
                        != null
                        && publicacion.getCategoria()
                        .isActivo()
                        && publicacion.getTipo()
                        != null
                        && publicacion.getPrecio()
                        != null
                        && publicacion.getPrecio()
                        .signum() >= 0;

        if (!disponible) {

            throw new IllegalArgumentException(
                    "Una publicación no está disponible."
            );
        }

        return publicacion;
    }

    private void validarDatosPedido(
            Integer idEmprendimiento,
            List<Integer> idsPublicacion,
            List<Integer> cantidades) {

        if (idEmprendimiento == null
                || idsPublicacion == null
                || cantidades == null
                || idsPublicacion.isEmpty()
                || idsPublicacion.size()
                != cantidades.size()) {

            throw new IllegalArgumentException(
                    "Los datos del pedido son inválidos."
            );
        }
    }

    private void validarCantidad(
            Integer idPublicacion,
            Integer cantidad) {

        if (idPublicacion == null
                || cantidad == null
                || cantidad <= 0) {

            throw new IllegalArgumentException(
                    "Las cantidades del pedido son inválidas."
            );
        }
    }

    private void validarStock(
            Publicacion publicacion,
            Integer cantidad) {

        if (publicacion.getTipo()
                != TipoPublicacion.PRODUCTO) {

            return;
        }

        if (publicacion.getStock() == null
                || publicacion.getStock()
                < cantidad) {

            throw new IllegalArgumentException(
                    "No hay existencias suficientes."
            );
        }
    }

    private void descontarStock(
            Publicacion publicacion,
            Integer cantidad) {

        if (publicacion.getTipo()
                != TipoPublicacion.PRODUCTO) {

            return;
        }

        publicacion.setStock(
                publicacion.getStock()
                        - cantidad
        );

        if (!publicacionDAO.actualizar(
                publicacion
        )) {

            throw new IllegalStateException(
                    "No se pudo actualizar el stock."
            );
        }
    }

    private void restaurarStock(
            Pedido pedido) {

        for (DetallePedido detalle
                : pedido.getDetalles()) {

            Publicacion publicacion =
                    detalle.getPublicacion();

            if (publicacion == null
                    || publicacion.getTipo()
                    != TipoPublicacion.PRODUCTO) {

                continue;
            }

            int stockActual =
                    publicacion.getStock() == null
                            ? 0
                            : publicacion.getStock();

            publicacion.setStock(
                    stockActual
                            + detalle.getCantidad()
            );

            if (!publicacionDAO.actualizar(
                    publicacion
            )) {

                throw new IllegalStateException(
                        "No se pudo restaurar el stock."
                );
            }
        }
    }

    private void validarTransicion(
            EstadoPedido actual,
            EstadoPedido nuevo) {

        if (actual == null) {

            throw new IllegalStateException(
                    "El pedido no tiene un estado válido."
            );
        }

        boolean permitida =
                switch (actual) {

                    case PENDIENTE ->
                            nuevo
                                    == EstadoPedido.CONFIRMADO
                                    || nuevo
                                    == EstadoPedido.CANCELADO;

                    case CONFIRMADO ->
                            nuevo
                                    == EstadoPedido.EN_PROCESO
                                    || nuevo
                                    == EstadoPedido.CANCELADO;

                    case EN_PROCESO ->
                            nuevo
                                    == EstadoPedido.COMPLETADO
                                    || nuevo
                                    == EstadoPedido.CANCELADO;

                    case COMPLETADO,
                         CANCELADO ->
                            false;
                };

        if (!permitida) {

            throw new IllegalArgumentException(
                    "La transición de estado no está permitida."
            );
        }
    }

    private boolean perteneceAEmprendedor(
            Pedido pedido,
            Integer idEmprendedor) {

        return pedido != null
                && pedido.getEmprendimiento()
                != null
                && pedido.getEmprendimiento()
                .getPropietario()
                != null
                && Objects.equals(
                pedido.getEmprendimiento()
                        .getPropietario()
                        .getIdUsuario(),
                idEmprendedor
        );
    }

    private String normalizarObservaciones(
            String observaciones) {

        if (observaciones == null
                || observaciones.isBlank()) {

            return null;
        }

        return observaciones.trim();
    }
}