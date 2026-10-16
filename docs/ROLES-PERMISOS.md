# Roles y permisos — EmprendeLink DWF

## 1. Roles oficiales

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

La autorización se aplica mediante:

```text
Servlet Filters
+
Services
```

Ocultar una opción en la interfaz no sustituye las reglas de autorización del backend.

---

## 2. Visitante

Puede acceder a:

```text
/login.xhtml
/registro.xhtml
/catalogo/index.xhtml
/catalogo/detalle.xhtml
```

Puede:

- iniciar sesión;
- crear una cuenta de cliente;
- consultar el catálogo;
- consultar detalle de publicaciones.

No puede:

- utilizar carrito;
- consultar pedidos privados;
- administrar negocios;
- administrar publicaciones;
- acceder a administración.

---

## 3. `ROLE_CLIENTE`

Landing:

```text
/catalogo/index.xhtml
```

Navegación:

```text
Explorar
Mis pedidos
Mi perfil
Carrito
```

Puede:

- consultar catálogo;
- usar búsqueda y filtro por categoría;
- consultar detalle;
- agregar al carrito;
- cambiar cantidades;
- eliminar artículos;
- comprar a varios negocios dentro del mismo carrito;
- confirmar checkout;
- consultar sus pedidos;
- consultar detalle propio;
- actualizar teléfono;
- actualizar dirección;
- cerrar sesión desde Mi perfil.

Rutas principales:

```text
/catalogo/index.xhtml
/catalogo/detalle.xhtml
/carrito/index.xhtml
/pedidos/nuevo.xhtml
/pedidos/mis-pedidos.xhtml
/pedidos/detalle.xhtml
/perfil/index.xhtml
```

No puede:

- acceder a `/admin/*`;
- acceder a áreas del emprendedor;
- modificar publicaciones;
- modificar negocios;
- consultar pedidos ajenos.

---

## 4. `ROLE_EMPRENDEDOR`

Landing:

```text
/pedidos/recibidos.xhtml
```

Navegación:

```text
Pedidos
Mi negocio
Catálogo
Ventas
Comisiones
```

Puede:

- consultar pedidos recibidos de su negocio;
- cambiar estados permitidos;
- consultar detalle de pedidos autorizados;
- consultar su único negocio;
- editar descripción, contacto y logo;
- crear publicaciones;
- editar publicaciones;
- activar/desactivar publicaciones;
- consultar límite del plan;
- consultar ventas;
- filtrar ventas por período;
- consultar comisiones.

Rutas:

```text
/pedidos/recibidos.xhtml
/pedidos/detalle.xhtml
/emprendimientos/index.xhtml
/publicaciones/index.xhtml
/ventas/index.xhtml
/comisiones/index.xhtml
```

No puede:

- acceder al catálogo público como consumidor mientras está autenticado como emprendedor;
- acceder a `/admin/*`;
- crear un segundo negocio;
- cambiar por sí mismo nombre, categoría o plan;
- gestionar publicaciones ajenas;
- gestionar pedidos ajenos.

---

## 5. `ROLE_ADMIN`

Landing:

```text
/admin/inicio.xhtml
```

Puede:

- listar usuarios;
- activar/desactivar usuarios;
- crear cuentas de emprendedor;
- crear simultáneamente el negocio asociado;
- administrar categorías;
- administrar planes;
- listar negocios;
- consultar detalle de negocio;
- cambiar datos administrativos;
- activar/desactivar negocios;
- cambiar plan;
- consultar ventas;
- consultar comisiones.

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

El administrador no se crea desde registro público.

---

## 6. Creación de cuentas

### Registro público

Permite exclusivamente:

```text
ROLE_CLIENTE
```

### Emprendedor

Se crea mediante:

```text
ROLE_ADMIN
   ↓
AdminEmprendedorBean
   ↓
AdminEmprendedorService
```

La misma operación crea:

```text
Usuario ROLE_EMPRENDEDOR
+
Emprendimiento
```

con:

```text
plan
categoria
nombre
contacto
logo opcional
```

---

## 7. Regla de un negocio por emprendedor

La base aplica:

```text
UNIQUE (id_propietario)
```

Por tanto:

```text
ROLE_EMPRENDEDOR
→ máximo 1 Emprendimiento
```

La interfaz utiliza el concepto:

```text
Mi negocio
```

y no “Mis emprendimientos”.

---

## 8. Propiedad de recursos

### Publicación

Para gestionar una publicación:

```text
publicacion.emprendimiento.propietario.idUsuario
==
usuario.idUsuario
```

### Pedido — Cliente

Para consultar un pedido:

```text
pedido.cliente.idUsuario
==
usuario.idUsuario
```

### Pedido — Emprendedor

Para gestionar un pedido:

```text
pedido.emprendimiento.propietario.idUsuario
==
usuario.idUsuario
```

---

## 9. Filtros

### `AutenticacionFilter`

Protege:

```text
/inicio.xhtml
```

La ruta actúa como redirección por rol.

### `AdminFilter`

Protege:

```text
/admin/*
```

Requiere:

```text
ROLE_ADMIN
```

### `CatalogoFilter`

Protege el comportamiento de:

```text
/catalogo/*
```

Reglas:

```text
Visitante     → permitido
ROLE_CLIENTE  → permitido
ROLE_EMPRENDEDOR → redirect a pedidos
ROLE_ADMIN       → redirect a administración
```

### `ClienteFilter`

Protege:

```text
/carrito/*
/perfil/*
```

Requiere:

```text
ROLE_CLIENTE
```

### `EmprendedorFilter`

Protege:

```text
/emprendimientos/*
/ventas/*
/comisiones/*
```

Requiere:

```text
ROLE_EMPRENDEDOR
```

### `PublicacionFilter`

Protege:

```text
/publicaciones/*
```

Requiere usuario activo:

```text
ROLE_EMPRENDEDOR
```

### `PedidosFilter`

Protege:

```text
/pedidos/*
```

Rutas cliente:

```text
/pedidos/nuevo.xhtml
/pedidos/mis-pedidos.xhtml
```

Ruta emprendedor:

```text
/pedidos/recibidos.xhtml
```

El detalle se complementa con la autorización del Service.

---

## 10. Usuario activo

Un usuario con:

```text
activo = false
```

permanece en la base para preservar historial, pero no debe operar normalmente en rutas protegidas.

---

## 11. Datos del negocio

### Emprendedor puede modificar

```text
descripcion
contacto
logoUrl
```

### Administración controla

```text
nombre
categoria
plan
estado
```

---

## 12. Categoría e inventario

La categoría define:

```text
manejaStock
```

El emprendedor no selecciona tipo de publicación.

```text
manejaStock = true
→ PRODUCTO
→ stock

manejaStock = false
→ SERVICIO
→ stock = 0
```

---

## 13. Pedido

El cliente puede crear pedidos mediante checkout.

El emprendedor puede ejecutar únicamente transiciones válidas:

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

## 14. Contraseñas

Las contraseñas:

- no se almacenan en texto plano;
- se guardan mediante hash;
- no se muestran en vistas;
- no deben exponerse en futuros DTO REST.

---

## 15. Principio de seguridad

```text
Ruta
 ↓
Filter
 ↓
Managed Bean / futura API REST
 ↓
Service
 ↓
rol + propiedad + estado + disponibilidad
 ↓
DAO
```

La futura Fase 3 deberá reutilizar estas reglas desde los Services.