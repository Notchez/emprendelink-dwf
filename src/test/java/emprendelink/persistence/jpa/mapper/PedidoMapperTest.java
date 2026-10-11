package emprendelink.persistence.jpa.mapper;

import emprendelink.model.Categoria;
import emprendelink.model.DetallePedido;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Pedido;
import emprendelink.model.Publicacion;
import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.model.enums.EstadoPedido;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.model.enums.TipoRol;
import emprendelink.persistence.jpa.entity.DetallePedidoEntity;
import emprendelink.persistence.jpa.entity.PedidoEntity;
import emprendelink.persistence.jpa.entity.PublicacionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PedidoMapperTest {

    @Test
    void convierteDominioAEntidadConDetalles() {

        Rol rol =
                new Rol(
                        3,
                        TipoRol.ROLE_CLIENTE
                );

        Usuario cliente =
                new Usuario(
                        10,
                        rol,
                        "David",
                        "Gómez",
                        "david@test.com",
                        "hash",
                        "7000-0000",
                        true,
                        LocalDateTime.now()
                );

        Usuario propietario =
                new Usuario(
                        20,
                        new Rol(
                                2,
                                TipoRol.ROLE_EMPRENDEDOR
                        ),
                        "Carlos",
                        "Ruiz",
                        "carlos@test.com",
                        "hash",
                        "7000-0001",
                        true,
                        LocalDateTime.now()
                );

        Emprendimiento emprendimiento =
                new Emprendimiento(
                        5,
                        propietario,
                        "Panadería Ruiz",
                        null,
                        null,
                        true,
                        LocalDateTime.now()
                );

        Categoria categoria =
                new Categoria(
                        3,
                        "Alimentos",
                        null,
                        true
                );

        Publicacion publicacion =
                new Publicacion(
                        7,
                        emprendimiento,
                        categoria,
                        TipoPublicacion.PRODUCTO,
                        "Pan",
                        null,
                        new BigDecimal("2.50"),
                        20,
                        true,
                        LocalDateTime.now()
                );

        Pedido pedido =
                new Pedido(
                        cliente,
                        emprendimiento,
                        "Entrega por la tarde"
                );

        pedido.agregarDetalle(
                new DetallePedido(
                        publicacion,
                        2,
                        new BigDecimal("2.50")
                )
        );

        PedidoEntity entidad =
                PedidoMapper.aEntidad(
                        pedido
                );

        assertNotNull(entidad);

        assertEquals(
                EstadoPedido.PENDIENTE,
                entidad.getEstado()
        );

        assertEquals(
                new BigDecimal("5.00"),
                entidad.getTotal()
        );

        assertEquals(
                1,
                entidad.getDetalles().size()
        );

        assertSame(
                entidad,
                entidad.getDetalles()
                        .get(0)
                        .getPedido()
        );
    }

    @Test
    void convierteEntidadADominioConDetalles() {

        PedidoEntity pedidoEntity =
                new PedidoEntity();

        pedidoEntity.setIdPedido(8);
        pedidoEntity.setEstado(
                EstadoPedido.CONFIRMADO
        );
        pedidoEntity.setTotal(
                new BigDecimal("20.00")
        );
        pedidoEntity.setObservaciones(
                "Sin observaciones"
        );
        pedidoEntity.setFechaPedido(
                LocalDateTime.now()
        );

        PublicacionEntity publicacionEntity =
                new PublicacionEntity();

        publicacionEntity.setIdPublicacion(3);
        publicacionEntity.setTipo(
                TipoPublicacion.PRODUCTO
        );
        publicacionEntity.setNombre(
                "Producto"
        );
        publicacionEntity.setPrecio(
                new BigDecimal("10.00")
        );
        publicacionEntity.setStock(10);
        publicacionEntity.setActivo(true);
        publicacionEntity.setFechaPublicacion(
                LocalDateTime.now()
        );

        DetallePedidoEntity detalle =
                new DetallePedidoEntity();

        detalle.setIdDetallePedido(15);
        detalle.setPublicacion(
                publicacionEntity
        );
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(
                new BigDecimal("10.00")
        );
        detalle.setSubtotal(
                new BigDecimal("20.00")
        );

        pedidoEntity.agregarDetalle(
                detalle
        );

        Pedido pedido =
                PedidoMapper.aDominio(
                        pedidoEntity
                );

        assertNotNull(pedido);

        assertEquals(
                8,
                pedido.getIdPedido()
        );

        assertEquals(
                EstadoPedido.CONFIRMADO,
                pedido.getEstado()
        );

        assertEquals(
                new BigDecimal("20.00"),
                pedido.getTotal()
        );

        assertEquals(
                1,
                pedido.getDetalles().size()
        );

        assertEquals(
                2,
                pedido.getDetalles()
                        .get(0)
                        .getCantidad()
        );
    }
}