package sv.edu.udb.emprendelink.model;

import java.math.BigDecimal;
import sv.edu.udb.emprendelink.model.enums.EstadoPedido;
import sv.edu.udb.emprendelink.model.enums.TipoPublicacion;
import sv.edu.udb.emprendelink.model.enums.TipoRol;

public class DominioSmokeTest {

    public static void main(String[] args) {
        Rol rol = new Rol(TipoRol.ROLE_EMPRENDEDOR);
        Usuario propietario = new Usuario(
                rol, "Ana", "López", "ana@correo.com", "hash", "70000000"
        );

        Emprendimiento negocio = new Emprendimiento(
                propietario, "Café Local", "Venta de café", "70000000"
        );

        Categoria categoria = new Categoria("Alimentos", "Alimentos y bebidas");

        Publicacion publicacion = new Publicacion(
                negocio,
                categoria,
                TipoPublicacion.PRODUCTO,
                "Café molido",
                "Bolsa de café",
                new BigDecimal("5.50"),
                10
        );

        comprobar(publicacion.isActivo(), "La publicación debe iniciar activa");
        comprobar(publicacion.getStock() == 10, "El stock no coincide");

        esperarError(() -> publicacion.setPrecio(new BigDecimal("-1")));
        esperarError(() -> publicacion.setStock(-1));

        DetallePedido detalle = new DetallePedido(
                publicacion, 2, new BigDecimal("5.50")
        );

        comprobar(
                detalle.getSubtotal().compareTo(new BigDecimal("11.00")) == 0,
                "El subtotal es incorrecto"
        );

        esperarError(() -> detalle.setCantidad(0));

        Usuario cliente = new Usuario(
                new Rol(TipoRol.ROLE_CLIENTE),
                "Luis",
                "Pérez",
                "luis@correo.com",
                "hash",
                null
        );

        Pedido pedido = new Pedido(cliente, negocio, null);
        pedido.agregarDetalle(detalle);

        comprobar(pedido.getEstado() == EstadoPedido.PENDIENTE,
                "El pedido debe iniciar pendiente");
        comprobar(pedido.getTotal().compareTo(new BigDecimal("11.00")) == 0,
                "El total del pedido es incorrecto");

        esperarError(() -> pedido.setEstado(null));
        esperarError(() -> pedido.setTotal(new BigDecimal("-1")));

        System.out.println("Pruebas basicas del dominio completadas");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new IllegalStateException(mensaje);
        }
    }

    private static void esperarError(Runnable accion) {
        boolean fallo = false;

        try {
            accion.run();
        } catch (IllegalArgumentException e) {
            fallo = true;
        }

        if (!fallo) {
            throw new IllegalStateException("Se esperaba una validacion");
        }
    }
}
