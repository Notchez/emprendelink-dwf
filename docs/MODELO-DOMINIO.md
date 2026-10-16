# Modelo de dominio — EmprendeLink DWF

## 1. Principio general

El dominio representa:

```text
usuarios
negocios
categorías
planes
publicaciones
pedidos
detalles
```

sin depender directamente de JSF ni de entidades JPA.

Ubicación:

```text
src/main/java/emprendelink/model/
```

---

## 2. `Rol`

Atributos:

```text
idRol
nombre
```

`nombre` utiliza:

```text
TipoRol
```

Valores:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

---

## 3. `Usuario`

Atributos:

```text
idUsuario
rol
nombre
apellido
correo
contrasenaHash
telefono
direccion
activo
fechaRegistro
```

Reglas:

- cada usuario tiene un rol;
- el correo es único;
- la contraseña se almacena como hash;
- el usuario debe estar activo para operar;
- el registro público crea únicamente clientes;
- un emprendedor se crea desde administración;
- teléfono y dirección pueden ser actualizados por el cliente.

Relaciones:

```text
Usuario → Rol
Usuario emprendedor → 0..1 Emprendimiento
Usuario cliente → N Pedido
```

---

## 4. `Plan`

Atributos:

```text
idPlan
nombre
maxProductosActivos
tasaComision
activo
```

Planes actuales:

```text
Básico    → 10  → 7%
Estándar  → 30  → 5%
Premium   → 60  → 3%
Max       → 100 → 1%
```

Reglas:

- nombre obligatorio;
- máximo > 0;
- tasa entre 0 y 1;
- se utiliza para limitar publicaciones activas;
- se congela como snapshot cuando se crea un pedido.

---

## 5. `Categoria`

Atributos:

```text
idCategoria
nombre
descripcion
manejaStock
activo
```

Reglas:

- nombre obligatorio;
- sin duplicados;
- una categoría inactiva no debe utilizarse en operaciones nuevas;
- `manejaStock` define si el negocio trabaja con inventario limitado.

Interpretación:

```text
manejaStock = true  → PRODUCTO
manejaStock = false → SERVICIO
```

---

## 6. `Emprendimiento`

Atributos:

```text
idEmprendimiento
propietario
plan
categoria
nombre
descripcion
contacto
logoUrl
activo
fechaRegistro
```

Reglas:

- propietario obligatorio;
- propietario con `ROLE_EMPRENDEDOR`;
- plan válido;
- categoría válida;
- un emprendedor tiene máximo un negocio;
- el administrador crea la cuenta y el negocio;
- el emprendedor puede editar descripción, contacto y logo;
- nombre, categoría y plan son datos administrativos;
- un negocio inactivo no aparece como disponible.

Relaciones:

```text
Emprendimiento → Usuario
Emprendimiento → Plan
Emprendimiento → Categoria
Emprendimiento 1:N Publicacion
Emprendimiento 1:N Pedido
```

---

## 7. `Publicacion`

Atributos:

```text
idPublicacion
emprendimiento
categoria
tipo
nombre
descripcion
imagenUrl
precio
stock
activo
fechaPublicacion
```

`tipo` utiliza:

```text
TipoPublicacion
```

Valores:

```text
PRODUCTO
SERVICIO
```

### Resolución de tipo

En el flujo actual, el emprendedor no selecciona tipo ni categoría.

El Service utiliza la categoría del negocio:

```text
Categoria.manejaStock = true
→ PRODUCTO

Categoria.manejaStock = false
→ SERVICIO
```

### Reglas

- pertenece al negocio del emprendedor;
- nombre obligatorio;
- precio no negativo;
- máximo dos decimales;
- imagen URL opcional;
- stock no negativo;
- negocio activo;
- categoría activa;
- propietario activo;
- respeta máximo de publicaciones activas del plan.

---

## 8. `Pedido`

Atributos:

```text
idPedido
cliente
emprendimiento
idPlanAplicado
nombrePlanAplicado
estado
total
tasaComision
montoComision
direccionEntrega
observaciones
fechaPedido
fechaCompletado
detalles
```

Estado inicial:

```text
PENDIENTE
```

Estados:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Transiciones:

```text
PENDIENTE → CONFIRMADO
PENDIENTE → CANCELADO

CONFIRMADO → EN_PROCESO
CONFIRMADO → CANCELADO

EN_PROCESO → COMPLETADO
EN_PROCESO → CANCELADO
```

Estados terminales:

```text
COMPLETADO
CANCELADO
```

Reglas:

- cliente activo;
- negocio activo;
- publicaciones válidas;
- cantidades > 0;
- stock suficiente cuando corresponde;
- total calculado desde detalles;
- un pedido pertenece a un solo negocio;
- cliente solo consulta sus pedidos;
- emprendedor solo administra pedidos de su negocio.

### Snapshot comercial

Al crear:

```text
idPlanAplicado
nombrePlanAplicado
tasaComision
montoComision
direccionEntrega
```

La comisión se calcula:

```text
montoComision = total × tasaComision
```

Al completar:

```text
fechaCompletado
```

---

## 9. `DetallePedido`

Atributos:

```text
idDetallePedido
pedido
publicacion
cantidad
precioUnitario
subtotal
```

Reglas:

```text
cantidad > 0
precioUnitario >= 0
subtotal = precioUnitario × cantidad
```

---

## 10. Carrito

El carrito no es una entidad persistida.

Se implementa como estado de sesión en:

```text
CarritoBean
```

Estructura conceptual:

```text
Map<idPublicacion, cantidad>
```

Puede contener publicaciones de varios negocios.

Al confirmar:

```text
agrupar por negocio
→ crear un Pedido por negocio
```

---

## 11. Perfil del cliente

El cliente puede actualizar:

```text
telefono
direccion
```

Nombre y correo permanecen de solo lectura en la interfaz de perfil.

La dirección del perfil es el valor sugerido para nuevos pedidos.

Cada pedido conserva su propia:

```text
direccionEntrega
```

---

## 12. `TipoRol`

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

---

## 13. `TipoPublicacion`

```text
PRODUCTO
SERVICIO
```

Se mantiene como parte técnica del dominio.

La interfaz del emprendedor no lo expone como selector.

---

## 14. `EstadoPedido`

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Presentación:

```text
EN_PROCESO → En camino
COMPLETADO → Entregado
```

---

## 15. Inventario

Para `PRODUCTO`:

```text
crear pedido:
stock -= cantidad

cancelar:
stock += cantidad
```

Para `SERVICIO`:

```text
stock = 0
```

---

## 16. Planes y catálogo

Cada negocio tiene un plan.

```text
Emprendimiento
    ↓
Plan
    ↓
maxProductosActivos
tasaComision
```

El contador de plan utiliza únicamente publicaciones:

```text
activo = true
```

---

## 17. Eliminación lógica

Poseen estado:

```text
Usuario
Plan
Categoria
Emprendimiento
Publicacion
```

El objetivo es preservar:

- historial;
- relaciones;
- pedidos;
- trazabilidad.

---

## 18. Separación del dominio

Adaptación de persistencia:

```text
Domain Model
     ↕
Mapper
     ↕
JPA Entity
```

Esto permite reutilizar la lógica desde JSF y, posteriormente, desde REST.