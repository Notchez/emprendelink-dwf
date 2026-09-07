# Modelo de datos — EmprendeLink DWF

Este documento define la estructura relacional inicial de la base de datos de EmprendeLink.

Nombre oficial de la base de datos:

`emprendelink_dwf`

La implementación SQL real se mantendrá en:

`database/schema.sql`

---

## 1. Tabla roles

Representa los roles disponibles dentro del sistema.

### Columnas

- `id_rol`
- `nombre`

### Llave primaria

- `id_rol`

### Restricciones principales

- `nombre` deberá ser único.
- `nombre` no deberá permitir valores nulos.

### Valores iniciales previstos

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

---

## 2. Tabla usuarios

Representa los usuarios registrados dentro del sistema.

### Columnas

- `id_usuario`
- `id_rol`
- `nombre`
- `apellido`
- `correo`
- `contrasena_hash`
- `telefono`
- `activo`
- `fecha_registro`

### Llave primaria

- `id_usuario`

### Llave foránea

`id_rol` → `roles.id_rol`

### Restricciones principales

- `correo` deberá ser único.
- `correo` no deberá permitir valores nulos.
- `contrasena_hash` no deberá permitir valores nulos.
- `id_rol` deberá referenciar un rol existente.
- `activo` deberá indicar si el usuario puede utilizar el sistema.

---

## 3. Tabla emprendimientos

Representa los emprendimientos registrados por los usuarios emprendedores.

### Columnas

- `id_emprendimiento`
- `id_propietario`
- `nombre`
- `descripcion`
- `contacto`
- `activo`
- `fecha_registro`

### Llave primaria

- `id_emprendimiento`

### Llave foránea

`id_propietario` → `usuarios.id_usuario`

### Restricciones principales

- Todo emprendimiento deberá tener un propietario.
- `nombre` no deberá permitir valores nulos.
- `id_propietario` deberá referenciar un usuario existente.

La validación de que el propietario posea rol de emprendedor será responsabilidad de la lógica de negocio.

---

## 4. Tabla categorias

Representa las categorías utilizadas para clasificar publicaciones.

### Columnas

- `id_categoria`
- `nombre`
- `descripcion`
- `activo`

### Llave primaria

- `id_categoria`

### Restricciones principales

- `nombre` deberá ser único.
- `nombre` no deberá permitir valores nulos.

---

## 5. Tabla publicaciones

Representa los productos o servicios ofrecidos por los emprendimientos.

### Columnas

- `id_publicacion`
- `id_emprendimiento`
- `id_categoria`
- `tipo`
- `nombre`
- `descripcion`
- `precio`
- `stock`
- `activo`
- `fecha_publicacion`

### Llave primaria

- `id_publicacion`

### Llaves foráneas

`id_emprendimiento` → `emprendimientos.id_emprendimiento`

`id_categoria` → `categorias.id_categoria`

### Valores permitidos para tipo

- `PRODUCTO`
- `SERVICIO`

### Restricciones principales

- Toda publicación deberá pertenecer a un emprendimiento.
- Toda publicación deberá pertenecer a una categoría.
- `nombre` no deberá permitir valores nulos.
- `tipo` no deberá permitir valores nulos.
- `precio` no deberá ser negativo.
- `stock` no deberá ser negativo cuando sea utilizado.
- `activo` permitirá ocultar una publicación sin eliminarla físicamente.

---

## 6. Tabla pedidos

Representa los pedidos realizados por clientes a emprendimientos.

### Columnas

- `id_pedido`
- `id_cliente`
- `id_emprendimiento`
- `estado`
- `total`
- `observaciones`
- `fecha_pedido`

### Llave primaria

- `id_pedido`

### Llaves foráneas

`id_cliente` → `usuarios.id_usuario`

`id_emprendimiento` → `emprendimientos.id_emprendimiento`

### Valores permitidos para estado

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PROCESO`
- `COMPLETADO`
- `CANCELADO`

### Restricciones principales

- Todo pedido deberá tener un cliente.
- Todo pedido deberá pertenecer a un emprendimiento.
- `estado` no deberá permitir valores nulos.
- `total` no deberá ser negativo.
- El estado inicial será `PENDIENTE`.

La validación de que el usuario asociado a `id_cliente` posea permisos de cliente será responsabilidad de la lógica de negocio.

---

## 7. Tabla detalle_pedido

Representa los elementos incluidos dentro de un pedido.

### Columnas

- `id_detalle_pedido`
- `id_pedido`
- `id_publicacion`
- `cantidad`
- `precio_unitario`
- `subtotal`

### Llave primaria

- `id_detalle_pedido`

### Llaves foráneas

`id_pedido` → `pedidos.id_pedido`

`id_publicacion` → `publicaciones.id_publicacion`

### Restricciones principales

- Todo detalle deberá pertenecer a un pedido.
- Todo detalle deberá referenciar una publicación.
- `cantidad` deberá ser mayor que cero.
- `precio_unitario` no deberá ser negativo.
- `subtotal` no deberá ser negativo.

El valor de `precio_unitario` deberá conservar el precio utilizado al momento de crear el pedido.

---

## 8. Relaciones

Las relaciones principales serán:

`roles.id_rol`
→ `usuarios.id_rol`

`usuarios.id_usuario`
→ `emprendimientos.id_propietario`

`emprendimientos.id_emprendimiento`
→ `publicaciones.id_emprendimiento`

`categorias.id_categoria`
→ `publicaciones.id_categoria`

`usuarios.id_usuario`
→ `pedidos.id_cliente`

`emprendimientos.id_emprendimiento`
→ `pedidos.id_emprendimiento`

`pedidos.id_pedido`
→ `detalle_pedido.id_pedido`

`publicaciones.id_publicacion`
→ `detalle_pedido.id_publicacion`

---

## 9. Cardinalidades

- `roles` 1:N `usuarios`
- `usuarios` 1:N `emprendimientos`
- `emprendimientos` 1:N `publicaciones`
- `categorias` 1:N `publicaciones`
- `usuarios` 1:N `pedidos`
- `emprendimientos` 1:N `pedidos`
- `pedidos` 1:N `detalle_pedido`
- `publicaciones` 1:N `detalle_pedido`

---

## 10. Convenciones de tipos SQL

Como referencia inicial:

### Identificadores

Utilizar tipos enteros positivos autoincrementales cuando corresponda.

### Texto corto

Utilizar `VARCHAR` con longitud apropiada.

Ejemplos:

- nombres;
- correo;
- teléfono;
- estado;
- tipo.

### Texto largo

Utilizar `TEXT` cuando corresponda.

Ejemplos:

- descripciones;
- observaciones.

### Valores monetarios

Utilizar `DECIMAL`.

No utilizar `FLOAT` o `DOUBLE` para valores monetarios.

### Fechas

Utilizar tipos de fecha y hora apropiados de MySQL.

### Booleanos

Los campos como `activo` deberán representarse mediante un tipo compatible con valores booleanos en MySQL.

---

## 11. Integridad referencial

Las relaciones deberán implementarse mediante llaves foráneas.

No se deberán guardar identificadores que referencien registros inexistentes.

Las reglas específicas de eliminación y actualización como:

- `CASCADE`
- `RESTRICT`
- `SET NULL`

serán definidas cuidadosamente en `schema.sql` según el comportamiento requerido por cada relación.

No se utilizará eliminación en cascada de forma indiscriminada.

---

## 12. Eliminación lógica

Las entidades que posean el atributo `activo` podrán utilizar eliminación lógica.

Inicialmente aplica a:

- usuarios;
- emprendimientos;
- categorias;
- publicaciones.

Esto permitirá conservar información histórica sin eliminar físicamente registros relacionados.

---

## 13. Seguridad de datos

Las contraseñas nunca deberán almacenarse en texto plano.

La columna:

`contrasena_hash`

almacenará únicamente el resultado de un mecanismo seguro de hashing.

Las credenciales de conexión a MySQL tampoco deberán guardarse dentro de los scripts del repositorio.

---

## 14. Datos iniciales

El archivo:

`database/seed.sql`

contendrá datos mínimos necesarios para inicializar el sistema.

Como mínimo podrá incluir:

- roles oficiales;
- categorías de prueba;
- datos controlados necesarios para desarrollo.

No deberán incluirse contraseñas reales ni información sensible.

---

## 15. Evolución del esquema

La estructura definida en este documento representa la versión inicial del modelo de datos.

Si durante el proyecto se requiere:

- agregar una tabla;
- agregar una columna;
- modificar una relación;
- cambiar una restricción;

el cambio deberá actualizar simultáneamente:

- `docs/MODELO-DATOS.md`
- `database/schema.sql`
- clases relacionadas;
- DAO;
- servicios;
- pruebas afectadas.

Los cambios estructurales deberán integrarse de forma coordinada para evitar incompatibilidades entre los integrantes del equipo.