# Rutas — EmprendeLink DWF

## Context path

```text
/emprendelink
```

Ejemplo local:

```text
http://localhost:8080/emprendelink
```

---

# 1. Rutas públicas

## Login

```text
/login.xhtml
```

Función:

- inicio de sesión.

URL:

```text
http://localhost:8080/emprendelink/login.xhtml
```

---

## Registro

```text
/registro.xhtml
```

Permite registrar:

```text
ROLE_CLIENTE
ROLE_EMPRENDEDOR
```

No permite registrar:

```text
ROLE_ADMIN
```

---

## Catálogo

```text
/catalogo/index.xhtml
```

Función:

- consultar publicaciones visibles;
- búsqueda;
- filtros;
- navegación al detalle.

---

## Detalle del catálogo

```text
/catalogo/detalle.xhtml
```

Función:

- mostrar información de una publicación visible.

---

# 2. Ruta autenticada general

## Inicio

```text
/inicio.xhtml
```

Requiere sesión.

Protección:

```text
AutenticacionFilter
```

---

# 3. Administración

Todas las rutas:

```text
/admin/*
```

requieren:

```text
ROLE_ADMIN
```

Protección:

```text
AdminFilter
```

---

## Inicio administrativo

```text
/admin/inicio.xhtml
```

Función:

- entrada al backoffice.

---

## Usuarios

```text
/admin/usuarios.xhtml
```

Función:

- listar usuarios;
- activar/desactivar;
- consultar roles y estado.

---

## Categorías

```text
/admin/categorias.xhtml
```

Función:

- crear;
- listar;
- editar;
- activar/desactivar;
- operaciones AJAX.

---

## Emprendimientos

```text
/admin/emprendimientos.xhtml
```

Función:

- listar negocios;
- filtrar;
- acceder al detalle administrativo.

---

## Detalle de negocio

```text
/admin/negocio.xhtml
```

Función:

- consultar información del emprendimiento;
- consultar propietario;
- consultar plan;
- cambiar plan;
- activar/desactivar;
- consultar ventas completadas;
- consultar pedidos relacionados.

---

## Planes

```text
/admin/planes.xhtml
```

Función:

- crear planes;
- editar;
- activar/desactivar;
- consultar máximo de publicaciones;
- consultar tasa de comisión.

Planes oficiales:

```text
Básico
Estándar
Premium
Max
```

---

## Comisiones

```text
/admin/comisiones.xhtml
```

Función:

- consultar pedidos completados;
- ventas;
- comisiones;
- filtrar por negocio;
- filtrar por mes;
- filtrar por año.

---

# 4. Emprendedor

Estas rutas requieren:

```text
ROLE_EMPRENDEDOR
```

---

## Mis emprendimientos

```text
/emprendimientos/index.xhtml
```

Protección:

```text
EmprendedorFilter
```

Función:

- listar emprendimientos propios;
- crear;
- editar.

---

## Mis publicaciones

```text
/publicaciones/index.xhtml
```

Protección:

```text
EmprendedorFilter
PublicacionFilter
```

Función:

- listar publicaciones propias;
- crear;
- editar;
- activar/desactivar;
- gestionar precio;
- gestionar stock;
- seleccionar categoría;
- seleccionar emprendimiento.

La creación y reactivación respetan el límite del plan.

---

# 5. Pedidos del cliente

## Nuevo pedido

```text
/pedidos/nuevo.xhtml
```

Requiere:

```text
ROLE_CLIENTE
```

Protección:

```text
PedidosFilter
```

Función:

- seleccionar cantidad;
- crear pedido;
- registrar detalle;
- descontar stock.

---

## Mis pedidos

```text
/pedidos/mis-pedidos.xhtml
```

Requiere:

```text
ROLE_CLIENTE
```

Función:

- consultar pedidos propios.

---

# 6. Pedidos del emprendedor

## Pedidos recibidos

```text
/pedidos/recibidos.xhtml
```

Requiere:

```text
ROLE_EMPRENDEDOR
```

Protección:

```text
PedidosFilter
```

Función:

- consultar pedidos de los emprendimientos propios;
- gestionar estados permitidos.

---

# 7. Detalle del pedido

```text
/pedidos/detalle.xhtml
```

La ruta está dentro de:

```text
/pedidos/*
```

y por lo tanto requiere una sesión válida.

La visibilidad del pedido se valida además en la lógica de negocio según:

```text
cliente propietario del pedido
o
emprendedor propietario del negocio
```

La interfaz muestra:

- pedido;
- negocio;
- estado;
- detalles;
- cantidades;
- precios;
- total.

---

# 8. Matriz de rutas

| Ruta | Visitante | Cliente | Emprendedor | Admin |
|---|---:|---:|---:|---:|
| `/login.xhtml` | Sí | Sí | Sí | Sí |
| `/registro.xhtml` | Sí | Sí | Sí | Sí |
| `/catalogo/index.xhtml` | Sí | Sí | Sí | Sí |
| `/catalogo/detalle.xhtml` | Sí | Sí | Sí | Sí |
| `/inicio.xhtml` | No | Sí | Sí | Sí |
| `/emprendimientos/index.xhtml` | No | No | Sí | No |
| `/publicaciones/index.xhtml` | No | No | Sí | No |
| `/pedidos/nuevo.xhtml` | No | Sí | No | No |
| `/pedidos/mis-pedidos.xhtml` | No | Sí | No | No |
| `/pedidos/recibidos.xhtml` | No | No | Sí | No |
| `/pedidos/detalle.xhtml` | No | Según propiedad | Según propiedad | Según lógica |
| `/admin/*` | No | No | No | Sí |

---

# 9. Seguridad de rutas

Filtros activos:

```text
AutenticacionFilter
AdminFilter
EmprendedorFilter
PedidosFilter
PublicacionFilter
```

Regla general:

```text
request
   ↓
Filter
   ↓
JSF
   ↓
Service
   ↓
validación de rol/propiedad
```

Los filtros controlan acceso general.

Los Services continúan validando la autorización sobre recursos concretos.

---

# 10. Errores

Páginas configuradas:

```text
/error/403.html
/error/404.html
/error/500.html
```

Significado:

```text
403 → acceso prohibido
404 → recurso no encontrado
500 → error interno
```

---

# 11. Página inicial

El `welcome-file` configurado es:

```text
login.xhtml
```

Por tanto:

```text
http://localhost:8080/emprendelink/
```

dirige al flujo inicial de autenticación.

---

# 12. API REST — Fase 3

Ruta base prevista:

```text
/api/v1
```

Recursos iniciales documentados en:

```text
docs/API.md
```

La API deberá reutilizar:

```text
REST
 ↓
Services
 ↓
DAO
 ↓
JPA
 ↓
MySQL
```

No deberá duplicar reglas de negocio existentes.