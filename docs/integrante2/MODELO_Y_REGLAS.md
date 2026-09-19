# Modelo de dominio y reglas

## Entidades

### Rol
Representa el rol de un usuario.

Campos:
- idRol: Integer
- nombre: TipoRol

### Usuario
Representa a una persona registrada en la plataforma.

Campos:
- idUsuario: Integer
- rol: Rol
- nombre: String
- apellido: String
- correo: String
- contrasenaHash: String
- telefono: String
- activo: boolean
- fechaRegistro: LocalDateTime

### Emprendimiento
Representa un negocio administrado por un emprendedor.

Campos:
- idEmprendimiento: Integer
- propietario: Usuario
- nombre: String
- descripcion: String
- contacto: String
- activo: boolean
- fechaRegistro: LocalDateTime

### Categoria
Clasifica las publicaciones.

Campos:
- idCategoria: Integer
- nombre: String
- descripcion: String
- activo: boolean

### Publicacion
Representa un producto o servicio ofrecido por un emprendimiento.

Campos:
- idPublicacion: Integer
- emprendimiento: Emprendimiento
- categoria: Categoria
- tipo: TipoPublicacion
- nombre: String
- descripcion: String
- precio: BigDecimal
- stock: Integer
- activo: boolean
- fechaPublicacion: LocalDateTime

### Pedido
Representa una solicitud realizada por un cliente.

Campos:
- idPedido: Integer
- cliente: Usuario
- emprendimiento: Emprendimiento
- estado: EstadoPedido
- total: BigDecimal
- observaciones: String
- fechaPedido: LocalDateTime
- detalles: List<DetallePedido>

### DetallePedido
Representa una línea de un pedido.

Campos:
- idDetallePedido: Integer
- pedido: Pedido
- publicacion: Publicacion
- cantidad: int
- precioUnitario: BigDecimal
- subtotal: BigDecimal

## Relaciones

- Un Usuario puede ser propietario de uno o más Emprendimientos.
- Un Emprendimiento puede tener varias Publicaciones.
- Una Categoria puede clasificar varias Publicaciones.
- Un Usuario cliente puede realizar Pedidos.
- Un Pedido pertenece a un Emprendimiento.
- Un Pedido contiene varios DetallePedido.
- Cada DetallePedido referencia una Publicacion.

## Matriz de reglas por campo

| Entidad.campo | Obligatorio | Regla |
|---|---|---|
| Rol.nombre | Sí | Debe ser un valor de TipoRol |
| Usuario.rol | Sí | Debe existir |
| Usuario.nombre | Sí | Hasta 100 caracteres |
| Usuario.apellido | Sí | Hasta 100 caracteres |
| Usuario.correo | Sí | Hasta 150 caracteres y formato de correo |
| Usuario.contrasenaHash | Sí | Hasta 255 caracteres; no guardar texto plano |
| Usuario.telefono | No | Hasta 25 caracteres |
| Emprendimiento.propietario | Sí | Debe referenciar un usuario válido |
| Emprendimiento.nombre | Sí | Hasta 150 caracteres |
| Emprendimiento.descripcion | No | Texto libre |
| Emprendimiento.contacto | No | Hasta 255 caracteres |
| Categoria.nombre | Sí | Hasta 100 caracteres y no duplicado |
| Categoria.descripcion | No | Texto libre |
| Publicacion.emprendimiento | Sí | Debe existir |
| Publicacion.categoria | Sí | Debe existir |
| Publicacion.tipo | Sí | PRODUCTO o SERVICIO |
| Publicacion.nombre | Sí | Hasta 150 caracteres |
| Publicacion.descripcion | No | Texto libre |
| Publicacion.precio | Sí | Mayor o igual a 0 |
| Publicacion.stock | No | Si existe, mayor o igual a 0 |
| Pedido.cliente | Sí | Debe existir |
| Pedido.emprendimiento | Sí | Debe existir |
| Pedido.estado | Sí | Valor de EstadoPedido |
| Pedido.total | Sí | Mayor o igual a 0 |
| DetallePedido.pedido | Sí al persistir | Debe existir |
| DetallePedido.publicacion | Sí | Debe existir |
| DetallePedido.cantidad | Sí | Mayor que 0 |
| DetallePedido.precioUnitario | Sí | Mayor o igual a 0 |
| DetallePedido.subtotal | Sí | cantidad por precioUnitario |

## Invariantes implementadas en los POJOs

- Publicacion no acepta precio negativo.
- Publicacion no acepta stock negativo.
- Pedido no acepta estado nulo.
- Pedido no acepta total negativo.
- DetallePedido exige cantidad mayor que cero.
- DetallePedido no acepta precio unitario negativo.
- DetallePedido recalcula el subtotal cuando cambia cantidad o precio.
- Pedido recalcula el total a partir de sus detalles.

Las validaciones de formato, obligatoriedad completa y existencia en base de datos se complementan en las capas de servidor y persistencia.
