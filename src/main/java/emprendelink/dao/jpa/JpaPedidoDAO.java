package emprendelink.dao.jpa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import emprendelink.dao.PedidoDAO;
import emprendelink.model.Pedido;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.persistence.jpa.entity.DetallePedidoEntity;
import emprendelink.persistence.jpa.entity.EmprendimientoEntity;
import emprendelink.persistence.jpa.entity.PedidoEntity;
import emprendelink.persistence.jpa.entity.PublicacionEntity;
import emprendelink.persistence.jpa.entity.UsuarioEntity;
import emprendelink.persistence.jpa.entity.PlanEntity;
import emprendelink.persistence.jpa.mapper.PedidoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaPedidoDAO
        implements PedidoDAO {

    @PersistenceContext(
            unitName = "emprendelinkPU"
    )
    private EntityManager entityManager;

    @Override
    public Pedido crear(
            Pedido pedido) {

        validarRelaciones(
                pedido
        );

        PedidoEntity entidad =
                PedidoMapper.aEntidad(
                        pedido
                );

        entidad.setCliente(
                entityManager.getReference(
                        UsuarioEntity.class,
                        pedido.getCliente()
                                .getIdUsuario()
                )
        );

        entidad.setEmprendimiento(
                entityManager.getReference(
                        EmprendimientoEntity.class,
                        pedido.getEmprendimiento()
                                .getIdEmprendimiento()
                )
        );

        for (DetallePedidoEntity detalle
                : entidad.getDetalles()) {

            Integer idPublicacion =
                    detalle.getPublicacion()
                            .getIdPublicacion();

            detalle.setPublicacion(
                    entityManager.getReference(
                            PublicacionEntity.class,
                            idPublicacion
                    )
            );

            detalle.setPedido(
                    entidad
            );
        }

        entityManager.persist(
                entidad
        );

        entityManager.flush();

        return buscarPorId(
                entidad.getIdPedido()
        ).orElseThrow(() ->
                new IllegalStateException(
                        "No se pudo recuperar el pedido creado."
                )
        );
    }

    @Override
    public Optional<Pedido> buscarPorId(
            Integer idPedido) {

        if (idPedido == null) {
            return Optional.empty();
        }

        return entityManager.createQuery(
                        """
                        SELECT DISTINCT p
                        FROM PedidoEntity p
                        JOIN FETCH p.cliente c
                        JOIN FETCH c.rol
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.plan
                        JOIN FETCH e.propietario propietario
                        JOIN FETCH propietario.rol
                        LEFT JOIN FETCH p.detalles d
                        LEFT JOIN FETCH d.publicacion pub
                        LEFT JOIN FETCH pub.categoria
                        WHERE p.idPedido = :id
                        """,
                        PedidoEntity.class
                )
                .setParameter(
                        "id",
                        idPedido
                )
                .getResultStream()
                .findFirst()
                .map(
                        PedidoMapper::aDominio
                );
    }

    @Override
    public List<Pedido> listarTodos() {

        return entityManager.createQuery(
                        """
                        SELECT DISTINCT p
                        FROM PedidoEntity p
                        JOIN FETCH p.cliente c
                        JOIN FETCH c.rol
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.plan
                        JOIN FETCH e.propietario propietario
                        JOIN FETCH propietario.rol
                        LEFT JOIN FETCH p.detalles d
                        LEFT JOIN FETCH d.publicacion pub
                        LEFT JOIN FETCH pub.categoria
                        ORDER BY p.fechaPedido DESC
                        """,
                        PedidoEntity.class
                )
                .getResultList()
                .stream()
                .map(
                        PedidoMapper::aDominio
                )
                .toList();
    }

    @Override
    public List<Pedido> listarPorCliente(
            Integer idCliente) {

        if (idCliente == null) {
            return List.of();
        }

        return entityManager.createQuery(
                        """
                        SELECT DISTINCT p
                        FROM PedidoEntity p
                        JOIN FETCH p.cliente c
                        JOIN FETCH c.rol
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.plan
                        JOIN FETCH e.propietario propietario
                        JOIN FETCH propietario.rol
                        LEFT JOIN FETCH p.detalles d
                        LEFT JOIN FETCH d.publicacion pub
                        LEFT JOIN FETCH pub.categoria
                        WHERE c.idUsuario = :idCliente
                        ORDER BY p.fechaPedido DESC
                        """,
                        PedidoEntity.class
                )
                .setParameter(
                        "idCliente",
                        idCliente
                )
                .getResultList()
                .stream()
                .map(
                        PedidoMapper::aDominio
                )
                .toList();
    }

    @Override
    public List<Pedido> listarPorEmprendimiento(
            Integer idEmprendimiento) {

        if (idEmprendimiento == null) {
            return List.of();
        }

        return entityManager.createQuery(
                        """
                        SELECT DISTINCT p
                        FROM PedidoEntity p
                        JOIN FETCH p.cliente c
                        JOIN FETCH c.rol
                        JOIN FETCH p.emprendimiento e
                        JOIN FETCH e.plan
                        JOIN FETCH e.propietario propietario
                        JOIN FETCH propietario.rol
                        LEFT JOIN FETCH p.detalles d
                        LEFT JOIN FETCH d.publicacion pub
                        LEFT JOIN FETCH pub.categoria
                        WHERE e.idEmprendimiento = :idEmprendimiento
                        ORDER BY p.fechaPedido DESC
                        """,
                        PedidoEntity.class
                )
                .setParameter(
                        "idEmprendimiento",
                        idEmprendimiento
                )
                .getResultList()
                .stream()
                .map(
                        PedidoMapper::aDominio
                )
                .toList();
    }

    @Override
    public boolean actualizarEstado(
            Integer idPedido,
            EstadoPedido nuevoEstado) {

        if (idPedido == null
                || nuevoEstado == null) {

            return false;
        }

        PedidoEntity entidad =
                entityManager.find(
                        PedidoEntity.class,
                        idPedido
                );

        if (entidad == null) {
            return false;
        }

        if (nuevoEstado
                == EstadoPedido.COMPLETADO
                && entidad.getMontoComision()
                == null) {

            if (entidad.getEmprendimiento()
                    == null
                    || entidad
                    .getEmprendimiento()
                    .getPlan() == null
                    || entidad
                    .getEmprendimiento()
                    .getPlan()
                    .getTasaComision() == null) {

                throw new IllegalStateException(
                        "El negocio no tiene un plan válido para calcular la comisión."
                );
            }

            PlanEntity plan =
                    entidad
                            .getEmprendimiento()
                            .getPlan();

            BigDecimal tasa =
                    plan.getTasaComision();

            BigDecimal monto =
                    entidad.getTotal()
                            .multiply(
                                    tasa
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );

            entidad.setTasaComision(
                    tasa
            );

            entidad.setMontoComision(
                    monto
            );
        }

        if (nuevoEstado
                == EstadoPedido.COMPLETADO
                && entidad.getFechaCompletado()
                == null) {

            entidad.setFechaCompletado(
                    LocalDateTime.now()
            );
        }

        entidad.setEstado(
                nuevoEstado
        );

        return true;
    }

    @Override
    public boolean eliminar(
            Integer idPedido) {

        if (idPedido == null) {
            return false;
        }

        PedidoEntity entidad =
                entityManager.find(
                        PedidoEntity.class,
                        idPedido
                );

        if (entidad == null) {
            return false;
        }

        entityManager.remove(
                entidad
        );

        return true;
    }

    private void validarRelaciones(
            Pedido pedido) {

        if (pedido == null
                || pedido.getCliente() == null
                || pedido.getCliente()
                .getIdUsuario() == null
                || pedido.getEmprendimiento() == null
                || pedido.getEmprendimiento()
                .getIdEmprendimiento() == null
                || pedido.getDetalles().isEmpty()) {

            throw new IllegalArgumentException(
                    "El pedido requiere cliente, emprendimiento y detalles válidos."
            );
        }

        boolean detalleInvalido =
                pedido.getDetalles()
                        .stream()
                        .anyMatch(detalle ->
                                detalle == null
                                        || detalle.getPublicacion() == null
                                        || detalle.getPublicacion()
                                        .getIdPublicacion() == null
                        );

        if (detalleInvalido) {
            throw new IllegalArgumentException(
                    "El pedido contiene detalles inválidos."
            );
        }
    }
}