# Rutas — EmprendeLink DWF

## Context path

```text
/emprendelink
```

Ejemplo:

```text
http://localhost:8080/emprendelink
```

---

## 1. Rutas públicas

### Login

```text
/login.xhtml
```

Función:

- iniciar sesión;
- redirigir según rol.

### Registro

```text
/registro.xhtml
```

Función:

- crear únicamente cuentas `ROLE_CLIENTE`.

Las cuentas de emprendedor se crean desde administración.

### Catálogo

```text
/catalogo/index.xhtml
```

Función:

- búsqueda;
- filtro por categoría;
- publicaciones visibles;
- acceso al detalle;
- carrito para clientes autenticados.

Acceso:

```text
Visitante    → Sí
Cliente      → Sí
Emprendedor  → No, redirige a Pedidos
Admin        → No, redirige a Administración
```

### Detalle de publicación

```text
/catalogo/detalle.xhtml
```

Misma regla de acceso que el catálogo.

---

## 2. Ruta de compatibilidad

### Inicio

```text
/inicio.xhtml
```

Requiere sesión mediante:

```text
AutenticacionFilter
```

No es una landing visual.

Ejecuta redirección según rol:

```text
ROLE_ADMIN       → /admin/inicio.xhtml
ROLE_EMPRENDEDOR → /pedidos/recibidos.xhtml
ROLE_CLIENTE     → /catalogo/index.xhtml
```

---

## 3. Administración

Protección:

```text
AdminFilter
```

Requiere:

```text
ROLE_ADMIN
```

### Inicio

```text
/admin/inicio.xhtml
```

### Usuarios

```text
/admin/usuarios.xhtml
```

Funciones:

- listar usuarios;
- activar/desactivar;
- crear emprendedor con su negocio.

### Categorías

```text
/admin/categorias.xhtml
```

Funciones:

- crear;
- editar;
- activar/desactivar;
- definir si la categoría maneja inventario limitado.

### Emprendimientos

```text
/admin/emprendimientos.xhtml
```

Funciones:

- listar;
- filtrar;
- navegar al detalle.

### Detalle administrativo del negocio

```text
/admin/negocio.xhtml
```

Funciones:

- consultar negocio;
- consultar propietario;
- cambiar nombre;
- cambiar categoría;
- cambiar plan;
- cambiar estado;
- consultar información asociada.

### Planes

```text
/admin/planes.xhtml
```

Funciones:

- crear;
- editar;
- activar/desactivar;
- configurar máximo de publicaciones;
- configurar tasa de comisión.

### Comisiones administrativas

```text
/admin/comisiones.xhtml
```

Funciones:

- consultar ventas completadas;
- comisiones;
- filtros por negocio y período.

---

## 4. Emprendedor

Requiere:

```text
ROLE_EMPRENDEDOR
```

### Pedidos — landing

```text
/pedidos/recibidos.xhtml
```

Protección:

```text
PedidosFilter
```

Funciones:

- listar pedidos recibidos;
- gestionar estados permitidos.

### Mi negocio

```text
/emprendimientos/index.xhtml
```

Protección:

```text
EmprendedorFilter
```

Funciones:

- ver el único negocio asociado;
- consultar plan;
- consultar tasa;
- consultar límite del catálogo;
- editar descripción;
- editar contacto;
- editar logo;
- cerrar sesión.

No permite crear un segundo negocio.

### Catálogo del emprendedor

```text
/publicaciones/index.xhtml
```

Protección:

```text
PublicacionFilter
```

Funciones:

- crear publicación;
- editar;
- activar/desactivar;
- imagen URL;
- precio;
- stock cuando corresponde;
- contador de publicaciones activas;
- espacios disponibles.

La categoría y el tipo se derivan del negocio.

### Ventas

```text
/ventas/index.xhtml
```

Protección:

```text
EmprendedorFilter
```

Funciones:

- ventas totales;
- ventas completadas;
- detalle de pedido;
- filtro por mes/año.

### Comisiones

```text
/comisiones/index.xhtml
```

Protección:

```text
EmprendedorFilter
```

Funciones:

- consultar comisión histórica;
- consultar tasa aplicada;
- revisar ventas relacionadas.

---

## 5. Cliente

Requiere:

```text
ROLE_CLIENTE
```

### Carrito

```text
/carrito/index.xhtml
```

Protección:

```text
ClienteFilter
```

Funciones:

- agrupar artículos por negocio;
- incrementar;
- disminuir;
- eliminar;
- mostrar subtotal por negocio;
- total general;
- actualización AJAX.

### Checkout

```text
/pedidos/nuevo.xhtml
```

Protección:

```text
PedidosFilter
```

Funciones:

- revisar carrito;
- definir dirección de entrega;
- agregar observaciones;
- confirmar compra.

Un carrito con varios negocios genera un pedido por negocio.

### Mis pedidos

```text
/pedidos/mis-pedidos.xhtml
```

Protección:

```text
PedidosFilter
```

Función:

- consultar pedidos del cliente autenticado.

### Mi perfil

```text
/perfil/index.xhtml
```

Protección:

```text
ClienteFilter
```

Funciones:

- consultar nombre;
- consultar correo;
- editar teléfono;
- editar dirección;
- cerrar sesión.

---

## 6. Detalle de pedido

```text
/pedidos/detalle.xhtml
```

Requiere sesión.

La visibilidad se valida en el Service.

Cliente:

```text
solo sus pedidos
```

Emprendedor:

```text
solo pedidos de su negocio
```

La interfaz muestra:

- negocio;
- estado;
- fecha;
- detalle;
- cantidades;
- precios;
- total;
- dirección histórica;
- observaciones.

---

## 7. Matriz de rutas

| Ruta | Visitante | Cliente | Emprendedor | Admin |
|---|---:|---:|---:|---:|
| `/login.xhtml` | Sí | Sí | Sí | Sí |
| `/registro.xhtml` | Sí | Sí | Sí | Sí |
| `/catalogo/index.xhtml` | Sí | Sí | Redirect | Redirect |
| `/catalogo/detalle.xhtml` | Sí | Sí | Redirect | Redirect |
| `/inicio.xhtml` | No | Redirect | Redirect | Redirect |
| `/carrito/index.xhtml` | No | Sí | No | No |
| `/perfil/index.xhtml` | No | Sí | No | No |
| `/pedidos/nuevo.xhtml` | No | Sí | No | No |
| `/pedidos/mis-pedidos.xhtml` | No | Sí | No | No |
| `/pedidos/recibidos.xhtml` | No | No | Sí | No |
| `/pedidos/detalle.xhtml` | No | Según propiedad | Según propiedad | Según Service |
| `/emprendimientos/index.xhtml` | No | No | Sí | No |
| `/publicaciones/index.xhtml` | No | No | Sí | No |
| `/ventas/index.xhtml` | No | No | Sí | No |
| `/comisiones/index.xhtml` | No | No | Sí | No |
| `/admin/*` | No | No | No | Sí |

---

## 8. Filtros activos

```text
AutenticacionFilter
AdminFilter
CatalogoFilter
ClienteFilter
EmprendedorFilter
PedidosFilter
PublicacionFilter
```

---

## 9. Errores

Páginas configuradas:

```text
/error/403.html
/error/404.html
/error/500.html
```

---

## 10. Welcome file

Configurado:

```text
login.xhtml
```

Por tanto:

```text
/emprendelink/
```

entra al flujo de autenticación.

---

## 11. Fase 3 REST

Ruta prevista:

```text
/api/v1
```

Actualmente existen clases de scaffolding en:

```text
src/main/java/emprendelink/web/rest/
src/main/java/emprendelink/dto/
```

pero todavía no existen endpoints REST funcionales de Fase 3.