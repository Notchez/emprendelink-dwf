# Rutas del proyecto — EmprendeLink DWF

Este documento define las rutas oficiales previstas para EmprendeLink.

El objetivo es mantener consistencia entre:

- Servlets
- JSP
- JSF
- API REST
- Spring
- enlaces internos
- pruebas
- documentación

Las rutas podrán evolucionar durante el proyecto, pero cualquier cambio deberá realizarse de forma coordinada.

---

## 1. Context path

Context path oficial:

`/emprendelink`

Ejemplo local:

`http://localhost:8080/emprendelink`

---

## 2. Rutas públicas

Las siguientes rutas podrán ser accesibles sin autenticación.

### Inicio

`/`

Responsabilidad:

- mostrar la página principal del sistema.

### Catálogo

`/catalogo`

Responsabilidad:

- mostrar publicaciones activas;
- permitir consulta pública del catálogo.

### Detalle de publicación

`/catalogo/publicacion`

Parámetro esperado:

`id`

Ejemplo:

`/catalogo/publicacion?id=10`

Responsabilidad:

- mostrar el detalle de una publicación específica.

### Emprendimientos públicos

`/emprendimientos`

Responsabilidad:

- consultar emprendimientos visibles o activos.

### Detalle de emprendimiento

`/emprendimientos/detalle`

Parámetro esperado:

`id`

Ejemplo:

`/emprendimientos/detalle?id=3`

---

## 3. Autenticación

### Login

`/auth`

Uso inicial:

- mostrar formulario de inicio de sesión;
- procesar autenticación.

### Logout

`/logout`

Responsabilidad:

- cerrar la sesión actual.

### Registro

`/registro`

Responsabilidad prevista:

- permitir registro de usuarios cuando corresponda.

---

## 4. Rutas de cliente

Estas rutas requerirán autenticación con rol:

`ROLE_CLIENTE`

### Pedidos del cliente

`/pedidos`

Responsabilidad:

- listar pedidos realizados por el cliente autenticado.

### Crear pedido

`/pedidos/crear`

Responsabilidad:

- crear un nuevo pedido.

### Detalle de pedido

`/pedidos/detalle`

Parámetro esperado:

`id`

Ejemplo:

`/pedidos/detalle?id=25`

El backend deberá verificar que el pedido pertenezca al cliente autenticado.

---

## 5. Rutas de emprendedor

Estas rutas requerirán autenticación con rol:

`ROLE_EMPRENDEDOR`

### Gestión de emprendimientos

`/emprendimientos/gestion`

Responsabilidad:

- listar emprendimientos pertenecientes al usuario autenticado.

### Crear emprendimiento

`/emprendimientos/nuevo`

### Editar emprendimiento

`/emprendimientos/editar`

Parámetro esperado:

`id`

El backend deberá verificar propiedad.

### Gestión de publicaciones

`/publicaciones`

Responsabilidad:

- listar publicaciones pertenecientes a los emprendimientos del usuario autenticado.

### Crear publicación

`/publicaciones/nueva`

### Editar publicación

`/publicaciones/editar`

Parámetro esperado:

`id`

El backend deberá verificar propiedad.

### Pedidos recibidos

`/pedidos/recibidos`

Responsabilidad:

- mostrar pedidos realizados a emprendimientos del usuario autenticado.

### Gestión del estado de un pedido

`/pedidos/estado`

Responsabilidad:

- actualizar el estado de un pedido recibido.

El backend deberá verificar:

- propiedad del emprendimiento;
- transición válida de estado;
- permisos del usuario.

---

## 6. Rutas administrativas

Estas rutas requerirán:

`ROLE_ADMIN`

Todas las rutas administrativas utilizarán el prefijo:

`/admin`

### Usuarios

`/admin/usuarios`

Responsabilidad:

- consultar usuarios;
- activar o desactivar usuarios.

### Categorías

`/admin/categorias`

Responsabilidad:

- crear;
- consultar;
- actualizar;
- activar o desactivar categorías.

### Emprendimientos

`/admin/emprendimientos`

Responsabilidad:

- supervisar emprendimientos.

### Publicaciones

`/admin/publicaciones`

Responsabilidad:

- supervisar publicaciones.

---

## 7. Rutas JSP previstas

Las vistas JSP se ubicarán bajo:

`/WEB-INF/views/`

Esto evita acceso directo a los JSP desde el navegador.

Estructura inicial:

`/WEB-INF/views/auth/login.jsp`

`/WEB-INF/views/catalogo/lista.jsp`

`/WEB-INF/views/catalogo/detalle.jsp`

`/WEB-INF/views/emprendimientos/lista.jsp`

`/WEB-INF/views/emprendimientos/formulario.jsp`

`/WEB-INF/views/publicaciones/lista.jsp`

`/WEB-INF/views/publicaciones/formulario.jsp`

`/WEB-INF/views/pedidos/lista.jsp`

`/WEB-INF/views/pedidos/detalle.jsp`

`/WEB-INF/views/admin/usuarios.jsp`

`/WEB-INF/views/admin/categorias.jsp`

`/WEB-INF/views/error/403.jsp`

`/WEB-INF/views/error/404.jsp`

`/WEB-INF/views/error/500.jsp`

---

## 8. API REST

La API utilizará como ruta base:

`/api/v1`

Las rutas REST deberán utilizar:

- minúsculas;
- recursos en plural;
- métodos HTTP apropiados.

---

## 9. Emprendimientos API

### Listar emprendimientos

`GET /api/v1/emprendimientos`

### Obtener emprendimiento

`GET /api/v1/emprendimientos/{id}`

### Listar publicaciones de un emprendimiento

`GET /api/v1/emprendimientos/{id}/publicaciones`

---

## 10. Publicaciones API

### Listar publicaciones

`GET /api/v1/publicaciones`

### Obtener publicación

`GET /api/v1/publicaciones/{id}`

Las búsquedas y filtros podrán agregarse mediante parámetros de consulta.

Ejemplo conceptual:

`GET /api/v1/publicaciones?tipo=PRODUCTO`

---

## 11. Pedidos API

### Crear pedido

`POST /api/v1/pedidos`

### Obtener pedido

`GET /api/v1/pedidos/{id}`

### Actualizar estado

`PUT /api/v1/pedidos/{id}/estado`

El acceso dependerá del usuario autenticado y de la propiedad del recurso.

---

## 12. Códigos HTTP

La API deberá utilizar códigos HTTP apropiados.

Ejemplos:

- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`
- `409 Conflict`
- `500 Internal Server Error`

No deberá utilizarse siempre `200 OK` para representar errores.

---

## 13. Parámetros

Los parámetros deberán utilizar nombres consistentes.

Ejemplos:

- `id`
- `tipo`
- `estado`
- `categoria`
- `pagina`

No deberán crearse variantes diferentes para representar el mismo valor sin necesidad.

Ejemplo a evitar:

- `publicationId`
- `id_publicacion`
- `idPublicacion`

si todos representan el mismo parámetro dentro de un mismo contrato.

---

## 14. Separación entre rutas web y API

Las rutas HTML utilizarán rutas como:

`/catalogo`

`/publicaciones`

`/pedidos`

Las rutas REST utilizarán exclusivamente el prefijo:

`/api/v1`

Ejemplo:

Web:

`/publicaciones`

API:

`/api/v1/publicaciones`

Esto permite mantener separados los controladores destinados a vistas y los recursos destinados a clientes externos.

---

## 15. Protección de rutas

Las rutas protegidas deberán validarse en el backend.

No será suficiente ocultar enlaces o botones en la interfaz.

Ejemplos:

`ROLE_ADMIN`

deberá ser requerido para:

`/admin/*`

`ROLE_EMPRENDEDOR`

deberá ser requerido para las operaciones de administración de sus propios emprendimientos y publicaciones.

`ROLE_CLIENTE`

deberá ser requerido para operaciones privadas relacionadas con sus pedidos.

---

## 16. Propiedad del recurso

Además del rol, algunas operaciones deberán validar que el recurso pertenezca al usuario autenticado.

Ejemplo:

`/publicaciones/editar?id=15`

Un emprendedor solamente podrá editar la publicación si pertenece a uno de sus emprendimientos.

La misma regla deberá aplicarse en REST.

---

## 17. Evolución

Estas rutas representan la estructura inicial prevista.

Podrán modificarse o ampliarse si los requisitos funcionales lo requieren.

Si una ruta cambia, deberán revisarse:

- controladores;
- vistas;
- enlaces;
- API;
- cliente externo;
- seguridad;
- pruebas;
- documentación.

Los cambios deberán actualizar este archivo antes de integrarse a `develop`.