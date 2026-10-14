# Roles y permisos — EmprendeLink DWF

## 1. Roles oficiales

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

El rol forma parte del usuario autenticado y se utiliza tanto para proteger rutas como para aplicar reglas dentro de Services.

---

# 2. Visitante

Un visitante no autenticado puede acceder a:

```text
/login.xhtml
/registro.xhtml
/catalogo/index.xhtml
/catalogo/detalle.xhtml
```

Puede:

- iniciar sesión;
- registrarse;
- consultar el catálogo público;
- consultar información pública de publicaciones.

No puede:

- administrar usuarios;
- administrar categorías;
- administrar negocios;
- crear publicaciones;
- gestionar pedidos privados;
- administrar planes;
- consultar comisiones.

---

# 3. ROLE_CLIENTE

Un Cliente puede:

- iniciar y cerrar sesión;
- consultar el catálogo;
- consultar publicaciones;
- crear pedidos;
- consultar sus pedidos;
- consultar el detalle de sus pedidos.

Rutas principales:

```text
/inicio.xhtml
/catalogo/index.xhtml
/catalogo/detalle.xhtml
/pedidos/nuevo.xhtml
/pedidos/mis-pedidos.xhtml
/pedidos/detalle.xhtml
```

No puede:

- acceder a `/admin/*`;
- administrar emprendimientos;
- administrar publicaciones;
- consultar pedidos recibidos de emprendimientos;
- modificar pedidos de otros clientes.

---

# 4. ROLE_EMPRENDEDOR

Un Emprendedor puede:

- iniciar y cerrar sesión;
- administrar sus emprendimientos;
- administrar publicaciones de sus propios emprendimientos;
- consultar pedidos recibidos;
- consultar detalles autorizados;
- cambiar estados de pedidos asociados a sus negocios.

Rutas principales:

```text
/inicio.xhtml
/emprendimientos/index.xhtml
/publicaciones/index.xhtml
/pedidos/recibidos.xhtml
/pedidos/detalle.xhtml
```

No puede:

- modificar emprendimientos de otros usuarios;
- modificar publicaciones ajenas;
- gestionar pedidos pertenecientes a negocios ajenos;
- acceder a `/admin/*`.

---

# 5. ROLE_ADMIN

Un Administrador puede utilizar el backoffice.

Rutas:

```text
/admin/inicio.xhtml
/admin/usuarios.xhtml
/admin/categorias.xhtml
/admin/emprendimientos.xhtml
/admin/negocio.xhtml
/admin/planes.xhtml
/admin/comisiones.xhtml
```

Puede:

- consultar usuarios;
- activar/desactivar usuarios;
- administrar categorías;
- consultar emprendimientos;
- activar/desactivar emprendimientos;
- ver detalle administrativo de negocios;
- consultar pedidos del negocio desde su detalle;
- administrar planes;
- asignar planes;
- consultar ventas completadas;
- consultar comisiones.

El administrador no se crea mediante el registro público.

---

# 6. Propiedad de recursos

Además de verificar el rol, los Services validan propiedad.

## Emprendimiento

Para modificar un emprendimiento:

```text
usuario.idUsuario == emprendimiento.propietario.idUsuario
```

excepto operaciones administrativas explícitamente autorizadas.

---

## Publicación

Para que un emprendedor gestione una publicación:

```text
publicacion.emprendimiento.propietario.idUsuario
==
usuario.idUsuario
```

El administrador únicamente puede realizar las operaciones que los Services autoricen expresamente.

---

## Pedido — Cliente

Un cliente no debe consultar pedidos pertenecientes a otro cliente.

Regla conceptual:

```text
pedido.cliente.idUsuario
==
usuario.idUsuario
```

---

## Pedido — Emprendedor

Un emprendedor solamente puede administrar pedidos asociados a sus negocios.

Regla:

```text
pedido.emprendimiento.propietario.idUsuario
==
usuario.idUsuario
```

---

# 7. Seguridad por filtros

## `AutenticacionFilter`

Protege:

```text
/inicio.xhtml
```

Si no existe sesión válida:

```text
redirect → /login.xhtml
```

---

## `AdminFilter`

Protege:

```text
/admin/*
```

Requiere:

```text
ROLE_ADMIN
```

Sin sesión:

```text
redirect → /login.xhtml
```

Rol incorrecto:

```text
HTTP 403
```

---

## `EmprendedorFilter`

Protege:

```text
/emprendimientos/*
/publicaciones/*
```

Requiere:

```text
ROLE_EMPRENDEDOR
```

Sin sesión:

```text
redirect → /login.xhtml
```

Rol incorrecto:

```text
HTTP 403
```

---

## `PublicacionFilter`

Aplica específicamente sobre:

```text
/publicaciones/*
```

Comprueba adicionalmente:

```text
usuario activo
ROLE_EMPRENDEDOR
```

La autorización final sobre propiedad continúa dentro de los Services.

---

## `PedidosFilter`

Protege:

```text
/pedidos/*
```

Reglas específicas:

```text
/pedidos/nuevo.xhtml
/pedidos/mis-pedidos.xhtml
```

requieren:

```text
ROLE_CLIENTE
```

La ruta:

```text
/pedidos/recibidos.xhtml
```

requiere:

```text
ROLE_EMPRENDEDOR
```

El acceso al detalle se complementa con validaciones de propiedad dentro del Service.

---

# 8. Usuario activo

Un usuario puede conservar su registro en la base de datos y encontrarse:

```text
activo = false
```

Un usuario inactivo no debe utilizar las operaciones normales protegidas.

La desactivación es lógica para conservar:

- historial;
- pedidos;
- relaciones.

---

# 9. Planes

Los planes son administrados desde:

```text
/admin/planes.xhtml
```

El administrador puede:

- crear;
- editar;
- activar;
- desactivar;
- asignar planes a emprendimientos.

Un plan en uso no puede desactivarse si la regla de negocio lo impide.

---

# 10. Comisiones

Las comisiones son información administrativa.

Ruta:

```text
/admin/comisiones.xhtml
```

Requiere:

```text
ROLE_ADMIN
```

Permite consultar:

- pedidos completados;
- ventas completadas;
- tasas aplicadas;
- montos de comisión;
- filtros por negocio;
- mes;
- año.

---

# 11. Estados de pedido

El emprendedor únicamente puede realizar transiciones válidas.

```text
PENDIENTE → CONFIRMADO
PENDIENTE → CANCELADO

CONFIRMADO → EN_PROCESO
CONFIRMADO → CANCELADO

EN_PROCESO → COMPLETADO
EN_PROCESO → CANCELADO
```

No se permiten transiciones arbitrarias.

---

# 12. Autenticación

El usuario autenticado se mantiene en sesión mediante:

```text
usuarioAutenticado
```

Las vistas no deben confiar exclusivamente en ocultar botones.

La autorización real se valida mediante:

```text
Filtros
+
Services
```

---

# 13. Contraseñas

Las contraseñas:

- no se almacenan en texto plano;
- se procesan mediante el mecanismo de hash utilizado por la aplicación;
- el hash no debe exponerse innecesariamente en la sesión ni en la interfaz.

---

# 14. Eliminación lógica

Utilizan estado `activo`:

```text
Usuario
Categoria
Plan
Emprendimiento
Publicacion
```

Esto permite conservar la información relacionada con operaciones históricas.

---

# 15. Fase 3

Los futuros endpoints REST deberán mantener las mismas reglas de:

- autenticación;
- roles;
- propiedad;
- estado;
- integridad.

La API no debe duplicar las reglas existentes: deberá reutilizar la capa Service.

---

# 16. Principio de seguridad

La autorización no depende únicamente de la interfaz.

Modelo:

```text
Ruta
 ↓
Filter
 ↓
Managed Bean / futura API
 ↓
Service
 ↓
Regla de rol + propiedad + estado
 ↓
DAO
```

De esta manera, ocultar o modificar un enlace en la interfaz no permite evitar las reglas de negocio.