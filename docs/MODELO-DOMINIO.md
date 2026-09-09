# Modelo de dominio — EmprendeLink DWF

Este documento define el modelo de dominio inicial de EmprendeLink.

El objetivo es establecer una única interpretación de las entidades principales del sistema para evitar duplicaciones o inconsistencias durante el desarrollo.

---

## 1. Rol

Representa el tipo de usuario dentro del sistema.

Permite diferenciar permisos y responsabilidades.

### Atributos principales

- `idRol`
- `nombre`

### Valores oficiales

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

### Relaciones

Un `Rol` puede estar asociado a muchos `Usuario`.

Relación:

`Rol 1 --- N Usuario`

---

## 2. Usuario

Representa una persona registrada dentro de EmprendeLink.

Un usuario puede actuar como administrador, emprendedor o cliente según el rol asignado.

### Atributos principales

- `idUsuario`
- `rol`
- `nombre`
- `apellido`
- `correo`
- `contrasenaHash`
- `telefono`
- `activo`
- `fechaRegistro`

### Reglas generales

- El correo deberá identificar de manera única al usuario.
- La contraseña no deberá almacenarse en texto plano.
- Un usuario deberá tener un rol asignado.
- Un usuario podrá marcarse como activo o inactivo.

### Relaciones

Un `Usuario` pertenece a un `Rol`.

Un usuario con rol emprendedor podrá ser propietario de uno o varios `Emprendimiento`.

Un usuario con rol cliente podrá realizar uno o varios `Pedido`.

---

## 3. Emprendimiento

Representa un negocio, proyecto o iniciativa registrada por un emprendedor.

### Atributos principales

- `idEmprendimiento`
- `propietario`
- `nombre`
- `descripcion`
- `contacto`
- `activo`
- `fechaRegistro`

### Reglas generales

- Todo emprendimiento deberá tener un propietario.
- El propietario deberá corresponder a un usuario con permisos de emprendedor.
- Un emprendimiento podrá publicar múltiples productos o servicios.
- Un emprendimiento podrá recibir múltiples pedidos.

### Relaciones

Un `Usuario` puede administrar varios `Emprendimiento`.

Relación:

`Usuario 1 --- N Emprendimiento`

Un `Emprendimiento` puede tener muchas `Publicacion`.

Relación:

`Emprendimiento 1 --- N Publicacion`

Un `Emprendimiento` puede recibir muchos `Pedido`.

Relación:

`Emprendimiento 1 --- N Pedido`

---

## 4. Categoria

Representa una clasificación utilizada para organizar las publicaciones.

Ejemplos conceptuales:

- alimentos;
- tecnología;
- artesanías;
- servicios profesionales;
- moda.

### Atributos principales

- `idCategoria`
- `nombre`
- `descripcion`
- `activo`

### Reglas generales

- Una categoría podrá asociarse a múltiples publicaciones.
- Una categoría podrá desactivarse sin eliminar necesariamente su información histórica.

### Relaciones

Una `Categoria` puede contener muchas `Publicacion`.

Relación:

`Categoria 1 --- N Publicacion`

---

## 5. Publicacion

Representa un producto o servicio ofrecido por un emprendimiento.

Se utilizará una única entidad para ambos tipos con el objetivo de evitar duplicación innecesaria.

### Atributos principales

- `idPublicacion`
- `emprendimiento`
- `categoria`
- `tipo`
- `nombre`
- `descripcion`
- `precio`
- `stock`
- `activo`
- `fechaPublicacion`

### Tipo de publicación

El atributo `tipo` utilizará la enumeración `TipoPublicacion`.

Valores oficiales:

- `PRODUCTO`
- `SERVICIO`

### Reglas generales

- Toda publicación deberá pertenecer a un emprendimiento.
- Toda publicación deberá estar asociada a una categoría.
- El precio no deberá ser negativo.
- El stock podrá utilizarse principalmente para publicaciones de tipo producto.
- La lógica relacionada con stock para servicios podrá definirse posteriormente según los requisitos funcionales.
- Una publicación podrá activarse o desactivarse.

### Relaciones

Un `Emprendimiento` puede tener muchas `Publicacion`.

Una `Categoria` puede contener muchas `Publicacion`.

Una `Publicacion` puede aparecer en muchos `DetallePedido`.

---

## 6. Pedido

Representa una solicitud realizada por un cliente a un emprendimiento.

### Atributos principales

- `idPedido`
- `cliente`
- `emprendimiento`
- `estado`
- `total`
- `observaciones`
- `fechaPedido`

### Estado del pedido

El atributo `estado` utilizará la enumeración `EstadoPedido`.

Valores oficiales:

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PROCESO`
- `COMPLETADO`
- `CANCELADO`

### Reglas generales

- Todo pedido deberá pertenecer a un cliente.
- Todo pedido deberá estar dirigido a un emprendimiento.
- Un pedido deberá contener al menos un detalle.
- El total deberá corresponder a la suma de los subtotales de sus detalles.
- El estado inicial será `PENDIENTE`.
- Los cambios de estado deberán respetar las reglas de negocio definidas por el sistema.

### Relaciones

Un `Usuario` con rol cliente puede realizar muchos `Pedido`.

Relación:

`Usuario 1 --- N Pedido`

Un `Emprendimiento` puede recibir muchos `Pedido`.

Relación:

`Emprendimiento 1 --- N Pedido`

Un `Pedido` contiene uno o varios `DetallePedido`.

Relación:

`Pedido 1 --- N DetallePedido`

---

## 7. DetallePedido

Representa cada elemento incluido dentro de un pedido.

Permite relacionar un pedido con una publicación específica y conservar la información necesaria para calcular su importe.

### Atributos principales

- `idDetallePedido`
- `pedido`
- `publicacion`
- `cantidad`
- `precioUnitario`
- `subtotal`

### Reglas generales

- Todo detalle deberá pertenecer a un pedido.
- Todo detalle deberá referenciar una publicación.
- La cantidad deberá ser mayor que cero.
- El precio unitario deberá conservar el precio utilizado al momento de realizar el pedido.
- El subtotal será calculado utilizando:

`cantidad × precioUnitario`

### Relaciones

Un `Pedido` puede contener muchos `DetallePedido`.

Una `Publicacion` puede aparecer en muchos `DetallePedido`.

---

## 8. Enumeración TipoRol

Representa los roles válidos dentro del sistema.

Valores:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

---

## 9. Enumeración TipoPublicacion

Representa el tipo de contenido comercial publicado por un emprendimiento.

Valores:

- `PRODUCTO`
- `SERVICIO`

---

## 10. Enumeración EstadoPedido

Representa el estado actual de un pedido.

Valores:

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PROCESO`
- `COMPLETADO`
- `CANCELADO`

---

## 11. Relaciones generales

Las relaciones principales del modelo son:

`Rol 1 --- N Usuario`

`Usuario 1 --- N Emprendimiento`

`Emprendimiento 1 --- N Publicacion`

`Categoria 1 --- N Publicacion`

`Usuario 1 --- N Pedido`

`Emprendimiento 1 --- N Pedido`

`Pedido 1 --- N DetallePedido`

`Publicacion 1 --- N DetallePedido`

---

## 12. Vista general del dominio

La estructura conceptual puede representarse así:

Rol
↓
Usuario
├── Emprendimiento
│   ├── Publicacion
│   │   └── DetallePedido
│   └── Pedido
└── Pedido
└── DetallePedido

Categoria
└── Publicacion

---

## 13. Principio de diseño

El modelo de dominio deberá permanecer independiente de las tecnologías utilizadas en cada fase.

Las entidades conceptuales no deberán depender directamente de:

- Servlets;
- JSP;
- JSF;
- REST;
- Spring MVC;
- interfaces gráficas.

Las tecnologías de presentación y persistencia deberán trabajar alrededor del modelo y no redefinirlo.

---

## 14. Evolución

Este modelo representa la base inicial del proyecto.

Podrán agregarse nuevos atributos, reglas o entidades si los requisitos funcionales lo requieren.

Sin embargo, cualquier cambio estructural deberá revisarse antes de modificar:

- clases;
- tablas;
- relaciones;
- contratos DAO;
- servicios;
- API.

El objetivo es evitar que diferentes integrantes implementen interpretaciones distintas del mismo concepto.