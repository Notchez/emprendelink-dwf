# Evidencia — Integrante 2

## Trabajo realizado

Se definió el alcance funcional del Sprint I y se organizaron las historias seleccionadas. También se estableció el modelo de dominio utilizado como contrato para las capas de datos y servidor.

Se implementaron los siguientes POJOs:

- Rol
- Usuario
- Emprendimiento
- Categoria
- Publicacion
- Pedido
- DetallePedido

Enumeraciones:

- TipoRol
- TipoPublicacion
- EstadoPedido

## Decisiones de POO

Las entidades mantienen sus atributos privados y exponen acceso mediante métodos públicos. Se utilizaron relaciones entre objetos en lugar de guardar solamente identificadores dentro del modelo Java.

Las reglas que pertenecen directamente al estado de un objeto se mantienen en el propio POJO. Por ejemplo, una publicación no permite precio negativo y un detalle de pedido recalcula su subtotal.

No se agregó herencia porque las entidades actuales no la necesitan. Producto y servicio se representan mediante Publicacion y TipoPublicacion para evitar duplicación.

## Correspondencia con base de datos

Los tipos del dominio fueron revisados contra el esquema SQL:

- Integer para identificadores.
- String para VARCHAR/TEXT.
- BigDecimal para DECIMAL.
- LocalDateTime para DATETIME.
- boolean para BOOLEAN.
- enums Java para valores controlados de rol, tipo de publicación y estado de pedido.

## Prueba básica

`DominioSmokeTest` comprueba:

- valores iniciales;
- precio negativo;
- stock negativo;
- cantidad inválida;
- cálculo de subtotal;
- cálculo del total del pedido.

## Pendiente de integración

La capa DAO, Servlets, JSP y pruebas integradas corresponden a otros integrantes. Los nombres de campos y relaciones de este dominio deben mantenerse coordinados para evitar romper esas capas.
