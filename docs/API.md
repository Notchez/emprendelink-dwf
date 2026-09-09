# API REST — EmprendeLink DWF

Este documento define el contrato inicial de la API REST de EmprendeLink.

La API será implementada formalmente durante la Fase 3 del proyecto.

Su diseño inicial se establece desde ahora para mantener consistencia entre:

- backend;
- cliente externo;
- DTO;
- pruebas;
- seguridad;
- documentación.

---

## 1. Ruta base

La API utilizará:

`/api/v1`

Ejemplo local:

`http://localhost:8080/emprendelink/api/v1`

---

## 2. Formato de intercambio

El formato principal será:

`application/json`

Las solicitudes y respuestas deberán utilizar JSON cuando corresponda.

---

## 3. Recursos principales

Los recursos REST iniciales serán:

- emprendimientos;
- publicaciones;
- pedidos.

Podrán agregarse otros recursos posteriormente según los requisitos funcionales.

---

## 4. Emprendimientos

### Listar emprendimientos

`GET /api/v1/emprendimientos`

Respuesta esperada:

- colección de emprendimientos visibles;
- código HTTP `200 OK`.

### Obtener emprendimiento por ID

`GET /api/v1/emprendimientos/{id}`

Posibles respuestas:

- `200 OK`
- `404 Not Found`

### Listar publicaciones de un emprendimiento

`GET /api/v1/emprendimientos/{id}/publicaciones`

Posibles respuestas:

- `200 OK`
- `404 Not Found`

---

## 5. Publicaciones

### Listar publicaciones

`GET /api/v1/publicaciones`

Podrá permitir filtros mediante query parameters.

Ejemplos:

`GET /api/v1/publicaciones?tipo=PRODUCTO`

`GET /api/v1/publicaciones?tipo=SERVICIO`

### Obtener publicación por ID

`GET /api/v1/publicaciones/{id}`

Posibles respuestas:

- `200 OK`
- `404 Not Found`

---

## 6. Pedidos

### Crear pedido

`POST /api/v1/pedidos`

Responsabilidad:

- recibir datos del pedido;
- validar cliente;
- validar publicaciones;
- validar cantidades;
- calcular total;
- crear pedido y detalles.

Respuesta esperada:

- `201 Created`

Posibles errores:

- `400 Bad Request`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`
- `409 Conflict`

### Obtener pedido por ID

`GET /api/v1/pedidos/{id}`

El backend deberá validar que el usuario tenga permiso para consultar el pedido.

Posibles respuestas:

- `200 OK`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`

### Actualizar estado de pedido

`PUT /api/v1/pedidos/{id}/estado`

Responsabilidad:

- validar autenticación;
- validar permisos;
- validar propiedad del emprendimiento;
- validar transición de estado.

Posibles respuestas:

- `200 OK`
- `400 Bad Request`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`
- `409 Conflict`

---

## 7. DTO previstos

La API podrá utilizar los siguientes DTO:

- `EmprendimientoDTO`
- `PublicacionDTO`
- `CrearPedidoDTO`
- `PedidoDTO`
- `DetallePedidoDTO`
- `ApiErrorDTO`

Los DTO representan contratos de transporte de datos.

No deberán contener lógica de negocio.

---

## 8. Convención JSON

Las propiedades JSON utilizarán `camelCase`.

Ejemplos:

- `idUsuario`
- `idEmprendimiento`
- `idPublicacion`
- `fechaPedido`
- `precioUnitario`

Ejemplo conceptual de publicación:

{
"idPublicacion": 10,
"nombre": "Producto de ejemplo",
"tipo": "PRODUCTO",
"precio": 25.50,
"activo": true
}

---

## 9. Estados de pedido

Los valores oficiales serán:

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PROCESO`
- `COMPLETADO`
- `CANCELADO`

La API no deberá aceptar valores alternativos para representar estos estados.

---

## 10. Tipo de publicación

Los valores oficiales serán:

- `PRODUCTO`
- `SERVICIO`

---

## 11. Códigos HTTP

La API deberá utilizar códigos HTTP apropiados.

### Éxito

- `200 OK`
- `201 Created`
- `204 No Content`

### Error del cliente

- `400 Bad Request`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`
- `409 Conflict`

### Error del servidor

- `500 Internal Server Error`

---

## 12. Errores

Los errores de la API deberán utilizar una estructura consistente.

DTO previsto:

`ApiErrorDTO`

Ejemplo conceptual:

{
"status": 404,
"error": "Not Found",
"message": "Publicación no encontrada"
}

No deberán devolverse:

- stack traces;
- consultas SQL;
- contraseñas;
- credenciales;
- detalles internos sensibles.

---

## 13. Validación

La API deberá validar como mínimo:

- campos obligatorios;
- formatos;
- cantidades;
- precios;
- existencia de recursos relacionados;
- autenticación;
- autorización;
- propiedad del recurso;
- reglas de negocio.

Las validaciones importantes deberán realizarse en el backend.

---

## 14. Seguridad

Los endpoints protegidos deberán verificar:

- identidad del usuario;
- rol;
- permisos;
- propiedad del recurso cuando corresponda.

Roles oficiales:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

---

## 15. Cliente externo

Durante la Fase 3 se desarrollará un cliente externo que consumirá esta API.

La tecnología del cliente será definida según los requisitos de la fase.

El cliente deberá demostrar comunicación real con el backend mediante HTTP y JSON.

---

## 16. Documentación y pruebas

La API deberá probarse utilizando herramientas apropiadas.

Las pruebas deberán cubrir como mínimo:

- solicitudes válidas;
- parámetros inválidos;
- recursos inexistentes;
- autenticación;
- autorización;
- códigos HTTP;
- respuestas JSON.

La documentación final deberá indicar:

- método HTTP;
- ruta;
- parámetros;
- cuerpo esperado;
- respuesta;
- códigos HTTP;
- requisitos de autenticación.

---

## 17. Evolución

Este documento representa el contrato inicial previsto para la API.

La implementación definitiva será realizada durante la Fase 3.

Si se modifica un endpoint, deberá revisarse también:

- DTO;
- servicios;
- seguridad;
- cliente externo;
- pruebas;
- `docs/RUTAS.md`;
- este documento.

Los cambios deberán mantenerse sincronizados con el código implementado.