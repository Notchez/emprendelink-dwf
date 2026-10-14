# Roles y permisos — EmprendeLink DWF

Roles oficiales:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

La autorización se aplica mediante:

- filtros HTTP;
- sesión;
- Services;
- validación de propiedad de recursos.

---

# 1. Visitante

Puede:

- abrir login;
- registrarse;
- consultar el catálogo público;
- consultar el detalle de publicaciones activas.

Puede registrarse como:

```text
ROLE_CLIENTE
ROLE_EMPRENDEDOR
```

No puede:

- crear `ROLE_ADMIN`;
- crear pedidos sin autenticarse;
- acceder a administración;
- consultar información privada;
- gestionar emprendimientos;
- gestionar publicaciones.

---

# 2. ROLE_CLIENTE

Puede:

- iniciar sesión;
- cerrar sesión;
- consultar catálogo;
- consultar detalle de publicaciones;
- crear pedidos;
- consultar sus propios pedidos;
- consultar el detalle de pedidos visibles para él.

No puede:

- administrar usuarios;
- administrar categorías;
- administrar emprendimientos;
- gestionar publicaciones;
- acceder a pedidos de otros clientes;
- cambiar estados de pedidos recibidos.

Rutas principales:

```text
/catalogo/*
/pedidos/nuevo.xhtml
/pedidos/mis-pedidos.xhtml
/pedidos/detalle.xhtml
```

---

# 3. ROLE_EMPRENDEDOR

Puede:

- iniciar/cerrar sesión;
- gestionar sus emprendimientos;
- crear emprendimientos;
- editar emprendimientos propios;
- gestionar publicaciones de sus emprendimientos;
- consultar pedidos recibidos;
- cambiar estados permitidos de pedidos correspondientes a sus negocios.

Puede realizar:

```text
PENDIENTE → CONFIRMADO

CONFIRMADO → EN_PROCESO

EN_PROCESO → COMPLETADO
```

También puede cancelar pedidos desde estados permitidos.

No puede:

- editar emprendimientos ajenos;
- editar publicaciones ajenas;
- modificar pedidos de otros emprendimientos;
- administrar usuarios;
- administrar categorías globales;
- acceder al área administrativa.

Rutas principales:

```text
/emprendimientos/*
/publicaciones/*
/pedidos/recibidos.xhtml
/pedidos/detalle.xhtml
```

---

# 4. ROLE_ADMIN

Puede:

- iniciar/cerrar sesión;
- consultar usuarios;
- activar/desactivar usuarios;
- administrar categorías;
- consultar emprendimientos;
- activar/desactivar emprendimientos;
- consultar publicaciones;
- activar/desactivar publicaciones;
- supervisar información general del sistema según las funciones administrativas implementadas.

No puede desactivar su propia cuenta desde la tabla de administración de usuarios.

Rutas:

```text
/admin/*
```

---

# 5. Propiedad de recursos

El rol por sí solo no autoriza todas las operaciones.

También se comprueba propiedad.

## Emprendimiento

Un emprendedor únicamente puede editar un emprendimiento cuando:

```text
emprendimiento.propietario.idUsuario
==
usuarioAutenticado.idUsuario
```

---

## Publicación

Una publicación solamente puede ser gestionada por el propietario del emprendimiento correspondiente.

---

## Pedido — Cliente

Un cliente solamente puede consultar pedidos asociados a su usuario.

---

## Pedido — Emprendedor

Un emprendedor solamente puede gestionar pedidos cuando:

```text
pedido.emprendimiento.propietario.idUsuario
==
usuarioAutenticado.idUsuario
```

---

# 6. Seguridad por filtros

## AutenticacionFilter

Protege:

```text
/inicio.xhtml
```

Requiere sesión.

---

## AdminFilter

Protege:

```text
/admin/*
```

Requiere:

```text
ROLE_ADMIN
```

---

## EmprendedorFilter

Protege:

```text
/emprendimientos/*
/publicaciones/*
```

Requiere:

```text
ROLE_EMPRENDEDOR
```

---

## PedidosFilter

Aplica reglas según la ruta.

Cliente:

```text
/pedidos/nuevo.xhtml
/pedidos/mis-pedidos.xhtml
```

Emprendedor:

```text
/pedidos/recibidos.xhtml
```

El detalle vuelve a validarse en la capa Service.

---

# 7. Autenticación

La sesión utiliza:

```text
usuarioAutenticado
```

Al cerrar sesión:

- se invalida la sesión;
- el usuario vuelve a login.

---

# 8. Contraseñas

Las contraseñas nunca deben:

- almacenarse en texto plano;
- mostrarse en UI;
- incluirse en logs;
- exponerse en servicios externos;
- almacenarse directamente en sesión.

El sistema utiliza:

```text
PBKDF2
```

La columna persistida es:

```text
contrasena_hash
```

---

# 9. Eliminación lógica

Para preservar datos históricos y relaciones, se utiliza activación/desactivación en:

```text
Usuario
Categoria
Emprendimiento
Publicacion
```

Un registro inactivo permanece en la base de datos, pero sus operaciones disponibles se restringen.

---

# 10. Fase 3

La futura API REST deberá aplicar las mismas reglas de:

- autenticación;
- rol;
- propiedad;
- estado;
- disponibilidad.

Los endpoints no deben duplicar reglas de autorización que ya viven en Services.

---

# 11. Fase 4

Spring Security podrá reemplazar o complementar parte de la protección HTTP.

Sin embargo, las reglas de negocio y propiedad deberán mantenerse independientes de la tecnología de presentación.