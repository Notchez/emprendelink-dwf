# Modelo de datos — EmprendeLink DWF

## 1. Base de datos

Base oficial:

```text
emprendelink_dwf
```

Motor utilizado:

```text
MySQL 8.x
```

La persistencia de la aplicación se realiza mediante JPA utilizando EclipseLink como proveedor y JTA para el manejo de transacciones.

---

## 2. Tabla `roles`

Representa los roles disponibles dentro de la aplicación.

### Columnas

```text
id_rol
nombre
```

### Llave primaria

```text
id_rol
```

### Valores oficiales

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

### Restricciones

- `nombre` es obligatorio;
- `nombre` es único.

---

## 3. Tabla `usuarios`

Representa las cuentas registradas en EmprendeLink.

### Columnas

```text
id_usuario
id_rol
nombre
apellido
correo
contrasena_hash
telefono
activo
fecha_registro
```

### Llave primaria

```text
id_usuario
```

### Llave foránea

```text
id_rol → roles.id_rol
```

### Restricciones principales

- rol obligatorio;
- nombre obligatorio;
- apellido obligatorio;
- correo obligatorio y único;
- contraseña almacenada únicamente como hash;
- estado activo obligatorio;
- fecha de registro obligatoria.

La contraseña nunca se almacena en texto plano.

---

## 4. Tabla `planes`

Representa los planes disponibles para los emprendimientos.

### Columnas

```text
id_plan
nombre
max_productos_activos
tasa_comision
activo
```

### Llave primaria

```text
id_plan
```

### Restricciones

```text
nombre UNIQUE
max_productos_activos > 0
0 <= tasa_comision <= 1
```

### Planes definidos

```text
Básico    → 10 publicaciones activas  → 0.0700 → 7%
Estándar  → 30 publicaciones activas  → 0.0500 → 5%
Premium   → 60 publicaciones activas  → 0.0300 → 3%
Max       → 100 publicaciones activas → 0.0100 → 1%
```

El plan determina:

- cantidad máxima de publicaciones activas;
- porcentaje de comisión aplicado a ventas completadas.

---

## 5. Tabla `emprendimientos`

Representa los negocios registrados por usuarios emprendedores.

### Columnas

```text
id_emprendimiento
id_propietario
id_plan
nombre
descripcion
contacto
activo
fecha_registro
```

### Llave primaria

```text
id_emprendimiento
```

### Llaves foráneas

```text
id_propietario → usuarios.id_usuario
id_plan        → planes.id_plan
```

### Restricciones principales

- propietario obligatorio;
- plan obligatorio;
- nombre obligatorio;
- estado obligatorio;
- fecha de registro obligatoria.

Cada emprendimiento pertenece a un único propietario y utiliza un plan.

---

## 6. Tabla `categorias`

Representa la clasificación de las publicaciones.

### Columnas

```text
id_categoria
nombre
descripcion
activo
```

### Llave primaria

```text
id_categoria
```

### Restricciones

- nombre obligatorio;
- nombre único;
- estado obligatorio.

Las categorías utilizan eliminación lógica mediante `activo`.

---

## 7. Tabla `publicaciones`

Representa productos y servicios ofrecidos por los emprendimientos.

### Columnas

```text
id_publicacion
id_emprendimiento
id_categoria
tipo
nombre
descripcion
precio
stock
activo
fecha_publicacion
```

### Llave primaria

```text
id_publicacion
```

### Llaves foráneas

```text
id_emprendimiento → emprendimientos.id_emprendimiento
id_categoria      → categorias.id_categoria
```

### Valores de `tipo`

```text
PRODUCTO
SERVICIO
```

### Restricciones

```text
precio >= 0
stock >= 0
```

Además:

- nombre obligatorio;
- emprendimiento obligatorio;
- categoría obligatoria;
- tipo obligatorio;
- fecha de publicación obligatoria.

Los Services verifican adicionalmente:

- propiedad del emprendimiento;
- categoría activa;
- emprendimiento activo;
- límite de publicaciones activas definido por el plan.

---

## 8. Tabla `pedidos`

Representa una compra realizada por un cliente a un emprendimiento.

### Columnas

```text
id_pedido
id_cliente
id_emprendimiento
estado
total
observaciones
fecha_pedido
fecha_completado
tasa_comision
monto_comision
```

### Llave primaria

```text
id_pedido
```

### Llaves foráneas

```text
id_cliente        → usuarios.id_usuario
id_emprendimiento → emprendimientos.id_emprendimiento
```

### Estados

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

### Restricciones

```text
total >= 0

tasa_comision IS NULL
o
0 <= tasa_comision <= 1

monto_comision IS NULL
o
monto_comision >= 0
```

El estado inicial es:

```text
PENDIENTE
```

### Comisión

Mientras el pedido no ha sido completado:

```text
fecha_completado = NULL
tasa_comision    = NULL
monto_comision   = NULL
```

Cuando el pedido pasa a:

```text
COMPLETADO
```

se almacenan:

```text
fecha_completado
tasa_comision
monto_comision
```

Cálculo:

```text
monto_comision = total × tasa_comision
```

La tasa queda almacenada como información histórica del pedido.

---

## 9. Tabla `detalle_pedido`

Representa cada elemento incluido dentro de un pedido.

### Columnas

```text
id_detalle_pedido
id_pedido
id_publicacion
cantidad
precio_unitario
subtotal
```

### Llave primaria

```text
id_detalle_pedido
```

### Llaves foráneas

```text
id_pedido      → pedidos.id_pedido
id_publicacion → publicaciones.id_publicacion
```

### Restricciones

```text
cantidad > 0
precio_unitario >= 0
subtotal >= 0
```

Al eliminar físicamente un pedido, sus detalles utilizan:

```text
ON DELETE CASCADE
```

La aplicación normalmente conserva los pedidos como información histórica.

---

## 10. Relaciones

```text
Rol 1:N Usuario

Usuario 1:N Emprendimiento

Plan 1:N Emprendimiento

Emprendimiento 1:N Publicacion

Categoria 1:N Publicacion

Usuario 1:N Pedido

Emprendimiento 1:N Pedido

Pedido 1:N DetallePedido

Publicacion 1:N DetallePedido
```

Representación:

```text
roles
  │
  └── usuarios
        │
        ├── emprendimientos ─── planes
        │       │
        │       ├── publicaciones ─── categorias
        │       │         │
        │       │         └── detalle_pedido
        │       │
        │       └── pedidos
        │             │
        │             └── detalle_pedido
        │
        └── pedidos
```

---

## 11. Inventario

Las publicaciones de tipo:

```text
PRODUCTO
```

utilizan `stock`.

Al crear un pedido válido:

```text
stock -= cantidad
```

Al cancelar un pedido cuando corresponde:

```text
stock += cantidad
```

Las publicaciones de tipo:

```text
SERVICIO
```

no utilizan el stock como restricción de inventario.

---

## 12. Límites de planes

El límite se aplica sobre publicaciones con:

```text
activo = TRUE
```

La aplicación verifica el límite:

```text
al crear una publicación
al reactivar una publicación
al asignar un plan a un emprendimiento
```

No se permite que el número de publicaciones activas sea superior a:

```text
planes.max_productos_activos
```

---

## 13. Eliminación lógica

Utilizan eliminación lógica mediante `activo`:

```text
usuarios
planes
emprendimientos
categorias
publicaciones
```

Esto permite conservar:

- historial;
- relaciones;
- pedidos anteriores;
- integridad referencial.

---

## 14. Integridad

La integridad se controla mediante:

```text
FOREIGN KEY
UNIQUE
NOT NULL
CHECK
Bean Validation
reglas de Service
JTA
```

Las reglas que dependen del usuario, rol, propiedad o estado se validan en la capa Service.

---

## 15. Scripts

Esquema completo:

```text
database/schema.sql
```

Datos iniciales:

```text
database/seed.sql
```

Migraciones adicionales:

```text
database/migrations/002_planes_comisiones.sql
database/migrations/003_fecha_completado.sql
```

---

## 16. Persistencia Java

Las tablas se representan mediante entidades JPA ubicadas en:

```text
src/main/java/emprendelink/persistence/jpa/entity/
```

Entidades:

```text
RolEntity
UsuarioEntity
CategoriaEntity
PlanEntity
EmprendimientoEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

Persistence Unit:

```text
emprendelinkPU
```

DataSource:

```text
jdbc/EmprendeLinkDS
```

Proveedor:

```text
EclipseLink
```