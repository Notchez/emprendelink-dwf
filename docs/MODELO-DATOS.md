# Modelo de datos — EmprendeLink DWF

## 1. Base de datos

Base oficial:

```text
emprendelink_dwf
```

Motor:

```text
MySQL 8.x
```

Persistencia:

```text
JPA
EclipseLink
JTA
```

Scripts oficiales:

```text
database/schema.sql
database/seed.sql
```

El esquema actual es consolidado; no se mantienen migraciones incrementales de Fase 2.

---

## 2. `roles`

Columnas:

```text
id_rol
nombre
```

Valores oficiales:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

Restricciones:

- `id_rol` PK;
- `nombre` obligatorio;
- `nombre` único.

---

## 3. `usuarios`

Columnas:

```text
id_usuario
id_rol
nombre
apellido
correo
contrasena_hash
telefono
direccion
activo
fecha_registro
```

Relación:

```text
id_rol → roles.id_rol
```

Reglas:

- correo obligatorio y único;
- contraseña almacenada como hash;
- teléfono opcional;
- dirección opcional;
- estado activo obligatorio;
- fecha de registro obligatoria.

### Registro público

El registro público crea únicamente:

```text
ROLE_CLIENTE
```

Las cuentas:

```text
ROLE_EMPRENDEDOR
```

son creadas por un administrador.

---

## 4. `planes`

Columnas:

```text
id_plan
nombre
max_productos_activos
tasa_comision
activo
```

Restricciones:

```text
nombre UNIQUE
max_productos_activos > 0
0 <= tasa_comision <= 1
```

Planes definidos:

```text
Básico    → 10  → 0.0700
Estándar  → 30  → 0.0500
Premium   → 60  → 0.0300
Max       → 100 → 0.0100
```

---

## 5. `categorias`

Columnas:

```text
id_categoria
nombre
descripcion
maneja_stock
activo
```

`maneja_stock` define el comportamiento de las publicaciones del negocio.

```text
TRUE
→ inventario limitado
→ tipo técnico PRODUCTO

FALSE
→ sin inventario limitado
→ tipo técnico SERVICIO
```

La categoría se asigna al negocio desde administración.

---

## 6. `emprendimientos`

Columnas:

```text
id_emprendimiento
id_propietario
id_plan
id_categoria
nombre
descripcion
contacto
logo_url
activo
fecha_registro
```

Llaves foráneas:

```text
id_propietario → usuarios.id_usuario
id_plan        → planes.id_plan
id_categoria   → categorias.id_categoria
```

Restricción estructural principal:

```text
UNIQUE (id_propietario)
```

Esto implementa:

```text
1 emprendedor → máximo 1 negocio
```

El negocio conserva:

- plan actual;
- categoría;
- nombre;
- descripción;
- contacto;
- logo por URL;
- estado.

---

## 7. `publicaciones`

Columnas:

```text
id_publicacion
id_emprendimiento
id_categoria
tipo
nombre
descripcion
imagen_url
precio
stock
activo
fecha_publicacion
```

Llaves foráneas:

```text
id_emprendimiento → emprendimientos.id_emprendimiento
id_categoria      → categorias.id_categoria
```

Valores técnicos de `tipo`:

```text
PRODUCTO
SERVICIO
```

La interfaz del emprendedor no pide manualmente categoría ni tipo.

La lógica resuelve esos valores a partir de:

```text
emprendimiento.categoria
```

y:

```text
categoria.maneja_stock
```

Restricciones:

```text
precio >= 0
stock >= 0
```

Además se valida:

- negocio propio;
- negocio activo;
- propietario activo;
- categoría activa;
- plan activo;
- límite de publicaciones activas;
- formato de imagen URL;
- precio válido.

---

## 8. `pedidos`

Columnas:

```text
id_pedido
id_cliente
id_emprendimiento
id_plan_aplicado
nombre_plan_aplicado
estado
total
tasa_comision
monto_comision
direccion_entrega
observaciones
fecha_pedido
fecha_completado
```

Llaves foráneas:

```text
id_cliente          → usuarios.id_usuario
id_emprendimiento   → emprendimientos.id_emprendimiento
id_plan_aplicado    → planes.id_plan
```

Estados:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Restricciones:

```text
total >= 0
0 <= tasa_comision <= 1
monto_comision >= 0
```

### Snapshot del plan

Al crear el pedido se guardan:

```text
id_plan_aplicado
nombre_plan_aplicado
tasa_comision
monto_comision
```

Esto evita que cambios futuros del plan modifiquen el historial del pedido.

### Snapshot de dirección

El pedido guarda:

```text
direccion_entrega
```

La dirección se copia al confirmar el checkout.

Si el cliente cambia posteriormente la dirección de su perfil, los pedidos anteriores conservan su valor histórico.

### Fecha completado

Al completar:

```text
fecha_completado
```

registra la fecha real de finalización.

---

## 9. `detalle_pedido`

Columnas:

```text
id_detalle_pedido
id_pedido
id_publicacion
cantidad
precio_unitario
subtotal
```

Relaciones:

```text
id_pedido      → pedidos.id_pedido
id_publicacion → publicaciones.id_publicacion
```

Restricciones:

```text
cantidad > 0
precio_unitario >= 0
subtotal >= 0
```

Al eliminar físicamente un pedido:

```text
detalle_pedido
ON DELETE CASCADE
```

La aplicación normalmente conserva los pedidos como historial.

---

## 10. Relaciones

```text
Rol 1:N Usuario

Usuario emprendedor 1:0..1 Emprendimiento

Plan 1:N Emprendimiento

Categoria 1:N Emprendimiento

Emprendimiento 1:N Publicacion

Categoria 1:N Publicacion

Usuario cliente 1:N Pedido

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
        │       ├── categorias
        │       │
        │       ├── publicaciones
        │       │       │
        │       │       └── detalle_pedido
        │       │
        │       └── pedidos
        │               │
        │               └── detalle_pedido
        │
        └── pedidos
```

---

## 11. Carrito multi-negocio y persistencia

El carrito no se persiste como tabla en Fase 2.

Se mantiene en sesión mediante:

```text
CarritoBean
```

Al confirmar:

```text
publicaciones
      ↓
agrupadas por id_emprendimiento
      ↓
un registro pedidos por negocio
      ↓
múltiples detalle_pedido
```

Por tanto, la base conserva la regla:

```text
un pedido pertenece a un único emprendimiento
```

aunque el cliente perciba una sola experiencia de carrito.

---

## 12. Inventario

Cuando la categoría del negocio maneja stock:

```text
tipo = PRODUCTO
```

Al confirmar:

```text
stock -= cantidad
```

Al cancelar:

```text
stock += cantidad
```

Cuando la categoría no maneja stock:

```text
tipo = SERVICIO
stock = 0
```

---

## 13. Límites de plan

El máximo se aplica a publicaciones con:

```text
activo = TRUE
```

Se valida al:

```text
crear
reactivar
asignar/cambiar plan
```

---

## 14. Eliminación lógica

Utilizan:

```text
activo
```

las tablas:

```text
usuarios
planes
categorias
emprendimientos
publicaciones
```

Esto preserva:

- historial;
- pedidos;
- relaciones;
- trazabilidad.

---

## 15. Integridad

La integridad se controla mediante:

```text
FOREIGN KEY
UNIQUE
NOT NULL
CHECK
Bean Validation
Services
JTA
```

Las reglas dependientes de rol, propiedad, estado, límites e inventario se validan en la capa Service.