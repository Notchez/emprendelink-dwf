# Rutas — EmprendeLink DWF

Context path local:

```text
/emprendelink
```

URL base:

```text
http://localhost:8080/emprendelink
```

---

# 1. Rutas públicas

## Login

```text
/login.xhtml
```

Permite autenticarse.

---

## Registro

```text
/registro.xhtml
```

Permite crear:

```text
ROLE_CLIENTE
ROLE_EMPRENDEDOR
```

No permite crear:

```text
ROLE_ADMIN
```

---

## Catálogo

```text
/catalogo/index.xhtml
```

Muestra publicaciones visibles.

---

## Detalle del catálogo

```text
/catalogo/detalle.xhtml?id={idPublicacion}
```

Muestra una publicación activa.

---

# 2. Ruta autenticada general

## Inicio

```text
/inicio.xhtml
```

Requiere sesión válida.

Protección:

```text
AutenticacionFilter
```

La navegación presentada depende del rol del usuario.

---

# 3. Administración

Protección general:

```text
/admin/* → ROLE_ADMIN
```

Filtro:

```text
AdminFilter
```

## Usuarios

```text
/admin/usuarios.xhtml
```

Funciones:

- listar usuarios;
- activar/desactivar;
- impedir que el administrador desactive su propia cuenta.

---

## Categorías

```text
/admin/categorias.xhtml
```

Funciones:

- crear;
- editar;
- listar;
- activar/desactivar.

---

## Emprendimientos

```text
/admin/emprendimientos.xhtml
```

Funciones:

- listar emprendimientos;
- consultar propietario;
- activar/desactivar.

---

## Publicaciones

```text
/admin/publicaciones.xhtml
```

Funciones:

- listar publicaciones;
- consultar emprendimiento;
- consultar propietario;
- consultar categoría;
- activar/desactivar.

---

# 4. Emprendedor

Protección:

```text
ROLE_EMPRENDEDOR
```

Filtro:

```text
EmprendedorFilter
```

## Mis emprendimientos

```text
/emprendimientos/index.xhtml
```

Funciones:

- crear;
- listar propios;
- editar propios.

El emprendedor no modifica directamente el estado administrativo de su emprendimiento.

---

## Mis publicaciones

```text
/publicaciones/index.xhtml
```

Funciones:

- crear;
- editar;
- listar propias;
- activar/desactivar;
- seleccionar emprendimiento;
- seleccionar categoría.

---

# 5. Pedidos del cliente

Filtro:

```text
PedidosFilter
```

## Nuevo pedido

```text
/pedidos/nuevo.xhtml?id={idPublicacion}
```

Requiere:

```text
ROLE_CLIENTE
```

Permite crear un pedido a partir de una publicación visible.

---

## Mis pedidos

```text
/pedidos/mis-pedidos.xhtml
```

Requiere:

```text
ROLE_CLIENTE
```

Muestra únicamente pedidos correspondientes al cliente autenticado.

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

Permite:

- listar pedidos de sus emprendimientos;
- confirmar;
- marcar en proceso;
- completar;
- cancelar.

La interfaz muestra:

```text
EN_PROCESO → En camino
COMPLETADO → Entregado
```

---

# 7. Detalle del pedido

```text
/pedidos/detalle.xhtml?id={idPedido}
```

El acceso final también es validado por el Service.

Un usuario únicamente obtiene el pedido cuando tiene visibilidad sobre él.

---

# 8. Seguridad de rutas

Resumen:

```text
/login.xhtml                     pública
/registro.xhtml                  pública

/catalogo/*                      pública

/inicio.xhtml                    sesión válida

/admin/*                         ROLE_ADMIN

/emprendimientos/*               ROLE_EMPRENDEDOR
/publicaciones/*                 ROLE_EMPRENDEDOR

/pedidos/nuevo.xhtml             ROLE_CLIENTE
/pedidos/mis-pedidos.xhtml       ROLE_CLIENTE
/pedidos/recibidos.xhtml         ROLE_EMPRENDEDOR
/pedidos/detalle.xhtml           validación de sesión + visibilidad
```

Además del filtro HTTP, los Services aplican validación funcional y propiedad de recursos.

---

# 9. Errores

Páginas configuradas:

```text
/error/403.html
/error/404.html
/error/500.html
```

El `web.xml` define las páginas correspondientes.

---

# 10. Página inicial de la aplicación

El WAR utiliza como página inicial:

```text
login.xhtml
```

Por lo tanto:

```text
http://localhost:8080/emprendelink/
```

lleva al flujo de autenticación.

---

# 11. API REST — Fase 3

Ruta base prevista:

```text
/api/v1
```

Recursos iniciales previstos:

```text
GET  /api/v1/emprendimientos

GET  /api/v1/emprendimientos/{id}

GET  /api/v1/publicaciones

GET  /api/v1/publicaciones/{id}

POST /api/v1/pedidos

GET  /api/v1/pedidos/{id}

PUT  /api/v1/pedidos/{id}/estado
```

Las rutas REST deberán reutilizar la capa Service existente.