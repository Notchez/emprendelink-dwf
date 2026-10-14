# Modelo de dominio — EmprendeLink DWF

## 1. Principio general

El dominio de EmprendeLink representa usuarios, emprendimientos, publicaciones, planes y pedidos sin depender directamente de JSF ni de las entidades JPA.

Ubicación:

```text
src/main/java/emprendelink/model/
```

Enumeraciones:

```text
src/main/java/emprendelink/model/enums/
```

---

## 2. `Rol`

Representa el rol asignado a un usuario.

### Atributos

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

### Relación

```text
Rol 1:N Usuario
```

---

## 3. `Usuario`

Representa una cuenta registrada.

### Atributos

```text
idUsuario
rol
nombre
apellido
correo
contrasenaHash
telefono
activo
fechaRegistro
```

### Reglas

- cada usuario debe poseer un rol;
- el correo identifica la cuenta;
- la contraseña se almacena como hash;
- un usuario inactivo no debe poder operar normalmente dentro de las funciones protegidas;
- el registro público únicamente permite Cliente o Emprendedor.

### Relaciones

```text
Usuario → Rol
Usuario → Emprendimiento
Usuario → Pedido
```

---

## 4. `Plan`

Representa las condiciones comerciales asignadas a un emprendimiento.

### Atributos

```text
idPlan
nombre
maxProductosActivos
tasaComision
activo
```

### Planes actuales

```text
Básico    → 10 publicaciones  → 7%
Estándar  → 30 publicaciones  → 5%
Premium   → 60 publicaciones  → 3%
Max       → 100 publicaciones → 1%
```

### Reglas

- el nombre es obligatorio;
- el máximo de publicaciones debe ser mayor que cero;
- la tasa debe encontrarse entre 0 y 1;
- un plan asignado a negocios no debe desactivarse si ello deja referencias inválidas;
- un negocio no puede cambiar a un plan cuyo límite sea inferior a su cantidad actual de publicaciones activas.

### Relaciones

```text
Plan 1:N Emprendimiento
```

---

## 5. `Emprendimiento`

Representa un negocio registrado por un emprendedor.

### Atributos

```text
idEmprendimiento
propietario
plan
nombre
descripcion
contacto
activo
fechaRegistro
```

### Reglas

- debe tener propietario;
- el propietario debe tener rol de emprendedor;
- debe tener un plan válido;
- únicamente el propietario puede modificar sus datos;
- el administrador puede gestionar su estado;
- un emprendimiento inactivo no debe aparecer como opción disponible en los flujos normales.

### Relaciones

```text
Emprendimiento → Usuario
Emprendimiento → Plan
Emprendimiento 1:N Publicacion
Emprendimiento 1:N Pedido
```

---

## 6. `Categoria`

Representa una clasificación de publicaciones.

### Atributos

```text
idCategoria
nombre
descripcion
activo
```

### Reglas

- el nombre es obligatorio;
- no deben existir categorías duplicadas;
- una categoría inactiva no puede utilizarse para nuevas publicaciones.

### Relaciones

```text
Categoria 1:N Publicacion
```

---

## 7. `Publicacion`

Representa un producto o servicio ofrecido por un emprendimiento.

### Atributos

```text
idPublicacion
emprendimiento
categoria
tipo
nombre
descripcion
precio
stock
activo
fechaPublicacion
```

### Tipo

Utiliza:

```text
TipoPublicacion
```

Valores:

```text
PRODUCTO
SERVICIO
```

### Reglas

- debe pertenecer a un emprendimiento;
- debe utilizar una categoría válida;
- nombre obligatorio;
- precio no negativo;
- stock no negativo;
- solamente el propietario del emprendimiento puede administrarla;
- el emprendimiento debe estar activo;
- la categoría debe estar activa;
- no puede superar el límite de publicaciones activas definido por el plan.

El límite se valida:

```text
al crear
al reactivar
```

### Productos

Para:

```text
PRODUCTO
```

se utiliza el stock como control de inventario.

### Servicios

Para:

```text
SERVICIO
```

el stock se normaliza a:

```text
0
```

### Relaciones

```text
Publicacion → Emprendimiento
Publicacion → Categoria
Publicacion 1:N DetallePedido
```

---

## 8. `Pedido`

Representa una compra realizada por un cliente a un emprendimiento.

### Atributos

```text
idPedido
cliente
emprendimiento
estado
total
tasaComision
montoComision
observaciones
fechaPedido
fechaCompletado
detalles
```

### Estado inicial

```text
PENDIENTE
```

### Estados

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

### Transiciones permitidas

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

### Reglas

- debe pertenecer a un cliente;
- debe estar asociado a un emprendimiento;
- sus publicaciones deben pertenecer al mismo emprendimiento;
- cantidades mayores que cero;
- debe existir stock suficiente;
- el total se calcula desde los detalles;
- un emprendedor únicamente puede administrar pedidos de sus propios negocios;
- un cliente únicamente puede consultar pedidos autorizados para él.

### Comisión

Al completar el pedido se conserva:

```text
fechaCompletado
tasaComision
montoComision
```

Cálculo:

```text
montoComision = total × tasaComision
```

La tasa se almacena como snapshot histórico del plan utilizado al completar la venta.

### Relaciones

```text
Pedido → Usuario
Pedido → Emprendimiento
Pedido 1:N DetallePedido
```

---

## 9. `DetallePedido`

Representa una línea de un pedido.

### Atributos

```text
idDetallePedido
pedido
publicacion
cantidad
precioUnitario
subtotal
```

### Reglas

```text
cantidad > 0
precioUnitario >= 0
```

Cálculo:

```text
subtotal = precioUnitario × cantidad
```

Cuando se agrega un detalle al pedido, el total del pedido se recalcula a partir de todos los subtotales.

### Relaciones

```text
DetallePedido → Pedido
DetallePedido → Publicacion
```

---

## 10. `TipoRol`

Enumeración:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

Se utiliza para autorización y control de funciones.

---

## 11. `TipoPublicacion`

Enumeración:

```text
PRODUCTO
SERVICIO
```

Permite diferenciar publicaciones que manejan inventario de aquellas que representan servicios.

---

## 12. `EstadoPedido`

Enumeración:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

La interfaz puede mostrar nombres diferentes sin modificar el valor interno.

Ejemplo:

```text
EN_PROCESO → En camino
COMPLETADO → Entregado
```

---

## 13. Inventario

Cuando se crea un pedido con productos:

```text
stock -= cantidad
```

Al cancelar un pedido cuando corresponde:

```text
stock += cantidad
```

No se permite generar un pedido cuando el stock solicitado no está disponible.

---

## 14. Planes y publicaciones

Cada emprendimiento tiene un plan.

```text
Emprendimiento
      ↓
     Plan
      ↓
maxProductosActivos
tasaComision
```

El límite de publicaciones se aplica exclusivamente sobre publicaciones activas.

---

## 15. Eliminación lógica

Las siguientes clases poseen estado:

```text
activo
```

```text
Usuario
Plan
Emprendimiento
Categoria
Publicacion
```

El objetivo es preservar:

- referencias históricas;
- pedidos;
- relaciones;
- trazabilidad.

---

## 16. Separación del dominio

El modelo no conoce directamente:

```text
JSF
XHTML
EntityManager
JPA Entity
REST
Spring
```

La adaptación hacia persistencia se realiza mediante mappers:

```text
Domain Model ↔ JPA Entity
```

Esta separación permite reutilizar el dominio y los Services en fases posteriores.