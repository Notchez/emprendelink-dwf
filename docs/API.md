
# API REST — EmprendeLink DWF

## 1. Objetivo

Contrato de integración para el Sprint III (Fase 3).

La API permite consultar emprendimientos y publicaciones, gestionar pedidos autorizados y consumir la aplicación mediante HTTP/JSON.

La API REST utiliza JAX-RS, CDI y los Services existentes. No implementa una segunda capa de negocio o persistencia.

Este documento define la versión 1 del contrato para que backend, seguridad y cliente externo trabajen sobre las mismas convenciones.

**Estado:** contrato v1 propuesto para revisión e integración del equipo. Los endpoints de negocio se consideran pendientes hasta que sus responsables los implementen y prueben.

## 2. URL base

Contexto de la aplicación:

```text
/emprendelink
```

Prefijo REST:

```text
/api/v1
```

URL local:

```text
http://localhost:8080/emprendelink/api/v1
```

Ejemplo:

```http
GET /emprendelink/api/v1/publicaciones
```

## 3. Convenciones generales

- Solicitudes y respuestas JSON con `application/json`.
- Nombres de propiedades en `camelCase`.
- IDs como números enteros positivos.
- Importes monetarios como números JSON decimales.
- Enumeraciones con los nombres oficiales de Java.
- Fechas como cadenas ISO 8601, sin zona horaria cuando el valor de dominio sea `LocalDateTime`.
- Las colecciones vacías devuelven `200 OK` con `[]`.
- Los recursos individuales inexistentes o no visibles devuelven `404 Not Found`.
- Las respuestas `204 No Content` no incluyen cuerpo.
- No exponer entidades JPA directamente.
- No devolver contraseñas, hashes, credenciales, SQL ni stack traces.
- No confiar en IDs de usuario enviados por el cliente; la identidad se obtiene de la sesión.

## 4. Autenticación y seguridad

### 4.1 Mecanismo

La versión 1 utilizará sesiones HTTP administradas por GlassFish.

Se reutiliza:

```text
AutenticacionService.autenticar(correo, contrasena)
```

La sesión almacena al usuario bajo:

```text
usuarioAutenticado
```

La API ofrecerá operaciones REST de login, consulta de sesión y logout.

El navegador conserva la cookie de sesión. Las peticiones autenticadas deben enviarla.

**No se utilizarán JWT ni Basic Authentication en v1.**

### 4.2 Protección de operaciones

En cada solicitud protegida, el backend debe comprobar:

1. Existencia de una sesión válida.
2. Identidad del usuario.
3. Existencia y estado activo del usuario en la base de datos.
4. Rol autorizado.
5. Propiedad del recurso cuando corresponda.

No se debe confiar únicamente en el usuario almacenado en sesión si su estado o rol pudo cambiar después de iniciar sesión.

### 4.3 Protección CSRF

Toda solicitud autenticada que modifique datos deberá incluir:

```http
X-CSRF-Token: <token-de-la-sesion>
```

El token se entrega al iniciar sesión y al consultar `/auth/me`.

Debe ser impredecible, asociado a la sesión y comprobado por el servidor.

Las operaciones protegidas de escritura rechazan solicitudes sin token o con token incorrecto.

El login inicial se trata separadamente porque todavía no existe una sesión autenticada.

### 4.4 Origen del cliente

La integración inicial utilizará el mismo origen HTTP para frontend y API.

El proyecto cliente puede mantenerse en un directorio independiente y publicarse desde el mismo servidor durante la demostración.

No habilitar CORS permisivo ni cookies cross-site sin una decisión de integración revisada por el equipo.

Para cualquier despliegue no local, utilizar HTTPS y configurar correctamente las cookies de sesión.

## 5. Roles oficiales

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

### Permisos principales

| Operación | Público | Cliente | Emprendedor | Admin |
|---|---|---|---|---|
| Consultar emprendimientos visibles | Sí | Sí | Sí | Sí |
| Consultar publicaciones visibles | Sí | Sí | Sí | Sí |
| Crear pedidos / checkout | No | Sí | No | No |
| Listar pedidos propios | No | Sí | Sí, recibidos | No en v1 |
| Consultar un pedido | No | Propio | De su negocio | Sí |
| Cambiar estado de pedido | No | No | De su negocio | No en v1 |

Los permisos deben validarse en backend y no solo en la interfaz.

## 6. Autenticación REST

### POST /auth/login

Iniciar sesión mediante las credenciales existentes.

**Público.**

Solicitud:

```json
{
  "correo": "cliente@ejemplo.com",
  "contrasena": "contrasena-del-usuario"
}
```

Respuesta exitosa:

```http
200 OK
Content-Type: application/json
```

```json
{
  "idUsuario": 1,
  "nombre": "Cliente",
  "rol": "ROLE_CLIENTE",
  "csrfToken": "token-generado-por-el-servidor"
}
```

El backend crea una nueva sesión y renueva su identificador para evitar fijación de sesión.

Errores:

- `400 Bad Request`: estructura incompleta o inválida.
- `401 Unauthorized`: credenciales incorrectas o cuenta no disponible.
- `415 Unsupported Media Type`: tipo de contenido no admitido.

Nunca retornar contraseña ni hash.

### GET /auth/me

Consultar la identidad autenticada y obtener el token CSRF de la sesión.

**Requiere sesión válida.**

Respuesta:

```http
200 OK
```

```json
{
  "idUsuario": 1,
  "nombre": "Cliente",
  "rol": "ROLE_CLIENTE",
  "csrfToken": "token-de-la-sesion"
}
```

Errores:

- `401 Unauthorized`: no existe una sesión válida.
- `403 Forbidden`: cuenta desactivada o no habilitada.

### POST /auth/logout

Finalizar la sesión del usuario.

**Requiere sesión válida y token CSRF.**

Respuesta:

```http
204 No Content
```

Errores:

- `401 Unauthorized`.
- `403 Forbidden`: token CSRF incorrecto o ausente.

## 7. Emprendimientos

Los recursos de esta sección son públicos y solo muestran negocios visibles mediante los Services existentes.

### GET /emprendimientos

Listar emprendimientos activos y visibles.

Respuesta:

```http
200 OK
```

```json
[
  {
    "idEmprendimiento": 10,
    "nombre": "Tienda Ejemplo",
    "descripcion": "Productos locales",
    "contacto": "7000-0000",
    "logoUrl": null,
    "idCategoria": 2,
    "nombreCategoria": "Alimentos"
  }
]
```

Sin resultados:

```json
[]
```

### GET /emprendimientos/{id}

Consultar un emprendimiento visible por ID.

Respuesta:

```http
200 OK
```

El cuerpo utiliza el mismo objeto de emprendimiento definido anteriormente.

Errores:

- `400 Bad Request`: ID inválido.
- `404 Not Found`: emprendimiento inexistente o no visible.

### GET /emprendimientos/{id}/publicaciones

Listar las publicaciones visibles de un negocio.

Respuesta:

```http
200 OK
```

El cuerpo es un arreglo de objetos `PublicacionDTO`.

Errores:

- `400 Bad Request`: ID inválido.
- `404 Not Found`: negocio inexistente o no visible.

## 8. Publicaciones

Los endpoints públicos solo exponen publicaciones visibles conforme a `PublicacionService`.

### GET /publicaciones

Listar publicaciones visibles.

Filtros opcionales:

```text
texto
categoria
emprendimiento
```

Ejemplo:

```http
GET /publicaciones?texto=cafe&categoria=2
```

Respuesta:

```http
200 OK
```

```json
[
  {
    "idPublicacion": 25,
    "idEmprendimiento": 10,
    "idCategoria": 2,
    "tipo": "PRODUCTO",
    "nombre": "Café artesanal",
    "descripcion": "Café de producción local",
    "imagenUrl": null,
    "precio": 5.50,
    "stock": 20
  }
]
```

Para publicaciones de tipo `SERVICIO`, el stock puede representarse como `0` según las reglas de dominio.

Sin resultados:

```json
[]
```

Los filtros deben aplicarse sin exponer publicaciones o negocios inactivos.

Errores:

- `400 Bad Request`: filtros incorrectos.

### GET /publicaciones/{id}

Consultar una publicación visible.

Respuesta:

```http
200 OK
```

El cuerpo utiliza el objeto de publicación definido anteriormente.

Errores:

- `400 Bad Request`: ID inválido.
- `404 Not Found`: publicación inexistente o no visible.

## 9. Pedidos

Estos endpoints requieren autenticación.

Los pedidos mantienen la regla:

```text
1 Pedido → 1 Emprendimiento
```

Un checkout puede contener publicaciones de distintos negocios y generar varios pedidos.

### GET /pedidos

Listar pedidos accesibles al usuario autenticado.

- `ROLE_CLIENTE`: sus pedidos.
- `ROLE_EMPRENDEDOR`: pedidos recibidos por su negocio.
- `ROLE_ADMIN`: no se implementa listado general en v1.

Se utilizarán los métodos existentes:

```java
PedidoService.listarPorCliente(idCliente)
PedidoService.listarRecibidos(idEmprendedor)
```

Respuesta:

```http
200 OK
```

```json
[
  {
    "idPedido": 100,
    "idEmprendimiento": 10,
    "nombreEmprendimiento": "Tienda Ejemplo",
    "estado": "PENDIENTE",
    "total": 11.00,
    "fechaPedido": "2026-10-20T18:30:00"
  }
]
```

Errores:

- `401 Unauthorized`.
- `403 Forbidden`: rol no autorizado.

### GET /pedidos/{id}

Obtener detalle de un pedido permitido.

El backend utilizará:

```java
PedidoService.buscarVisibleParaUsuario(
    idPedido,
    idUsuarioAutenticado
)
```

Respuesta:

```http
200 OK
```

```json
{
  "idPedido": 100,
  "idEmprendimiento": 10,
  "nombreEmprendimiento": "Tienda Ejemplo",
  "estado": "PENDIENTE",
  "total": 11.00,
  "direccionEntrega": "Dirección registrada en la compra",
  "observaciones": null,
  "fechaPedido": "2026-10-20T18:30:00",
  "fechaCompletado": null,
  "detalles": [
    {
      "idPublicacion": 25,
      "nombrePublicacion": "Café artesanal",
      "cantidad": 2,
      "precioUnitario": 5.50,
      "subtotal": 11.00
    }
  ]
}
```

Errores:

- `400 Bad Request`: ID inválido.
- `401 Unauthorized`.
- `404 Not Found`: pedido inexistente o no visible para el usuario.

No revelar información sobre pedidos de terceros.

### POST /pedidos/checkout

Crear uno o más pedidos a partir de las publicaciones seleccionadas.

**Únicamente `ROLE_CLIENTE`.**

Solicitud:

```json
{
  "items": [
    {
      "idPublicacion": 25,
      "cantidad": 2
    }
  ],
  "direccionEntrega": "San Salvador, dirección de ejemplo",
  "observaciones": "Entregar por la tarde"
}
```

El ID del cliente **no** se recibe desde el JSON.

El backend obtiene el usuario desde la sesión y delega en:

```java
PedidoService.crearDesdeCarrito(
    idCliente,
    idsPublicacion,
    cantidades,
    direccionEntrega,
    observaciones
)
```

Respuesta:

```http
204 No Content
```

**Justificación:** el Service actual realiza la operación sin retornar IDs de pedidos creados. `204` permite respetar su interfaz sin duplicar persistencia ni inventar identificadores.

Para consultar los pedidos generados, el cliente puede invocar posteriormente:

```http
GET /pedidos
```

Errores:

- `400 Bad Request`: estructura inválida, cantidades incorrectas o dirección ausente.
- `401 Unauthorized`.
- `403 Forbidden`: rol incorrecto o token CSRF inválido.
- `404 Not Found`: publicación no disponible según el contrato de visibilidad.
- `409 Conflict`: stock insuficiente o conflicto de negocio.

### PUT /pedidos/{id}/estado

Actualizar estado de un pedido propio del negocio.

**Únicamente `ROLE_EMPRENDEDOR`.**

Solicitud:

```json
{
  "estado": "CONFIRMADO"
}
```

El backend delega en:

```java
PedidoService.actualizarEstado(
    idEmprendedor,
    idPedido,
    nuevoEstado
)
```

Respuesta:

```http
204 No Content
```

Estados válidos:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Transiciones permitidas:

```text
PENDIENTE → CONFIRMADO
PENDIENTE → CANCELADO

CONFIRMADO → EN_PROCESO
CONFIRMADO → CANCELADO

EN_PROCESO → COMPLETADO
EN_PROCESO → CANCELADO
```

Errores:

- `400 Bad Request`: ID o estado inválido.
- `401 Unauthorized`.
- `403 Forbidden`: rol incorrecto o token CSRF inválido.
- `404 Not Found`: pedido inexistente o ajeno.
- `409 Conflict`: transición no permitida.

La cancelación debe preservar las reglas existentes de restauración de stock.

## 10. Respuestas de error

Todos los errores REST controlados utilizarán la misma estructura:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Recurso no encontrado"
}
```

Campos obligatorios:

| Campo | Tipo | Descripción |
|---|---|---|
| `status` | integer | Código HTTP |
| `error` | string | Nombre del estado HTTP |
| `message` | string | Mensaje seguro para el consumidor |

Códigos principales:

| Estado | Significado |
|---|---|
| 400 | Solicitud o datos inválidos |
| 401 | Usuario no autenticado o credenciales inválidas |
| 403 | Usuario sin permiso o protección CSRF rechazada |
| 404 | Recurso inexistente o no visible |
| 409 | Conflicto de negocio |
| 415 | Tipo de contenido no soportado |
| 500 | Error interno inesperado |

No retornar stack traces, consultas SQL, contraseñas ni mensajes de infraestructura.

Las excepciones existentes de los Services no siempre distinguen entre validaciones, permisos y conflictos: **no convertir todas las `IllegalArgumentException` automáticamente en el mismo código HTTP**.

Cada Resource debe clasificar las condiciones conocidas de acuerdo con este contrato. Los errores inesperados deben registrarse internamente y devolver un mensaje genérico.

## 11. DTO acordados

| DTO | Contenido |
|---|---|
| `EmprendimientoDTO` | Datos públicos de negocio definidos en sección 7 |
| `PublicacionDTO` | Datos públicos de publicación definidos en sección 8 |
| `CrearPedidoDTO` | `items`, `direccionEntrega`, `observaciones` |
| `PedidoDTO` | Resumen del pedido definido en sección 9 |
| `DetallePedidoDTO` | Información de línea de pedido; puede formar parte de la respuesta detallada |
| `ApiErrorDTO` | `status`, `error`, `message` |

Las estructuras JSON son el contrato. La organización interna de clases Java puede incluir DTO auxiliares cuando se necesiten.

Los datos de plan y comisión deben preservarse en la persistencia histórica. No deben exponerse al consumidor sin una necesidad autorizada y una regla de visibilidad definida.

## 12. Resumen de endpoints

| Método | Ruta | Acceso | Éxito |
|---|---|---|---|
| GET | `/health` | Público | 200 |
| POST | `/auth/login` | Público | 200 |
| GET | `/auth/me` | Autenticado | 200 |
| POST | `/auth/logout` | Autenticado | 204 |
| GET | `/emprendimientos` | Público | 200 |
| GET | `/emprendimientos/{id}` | Público | 200 |
| GET | `/emprendimientos/{id}/publicaciones` | Público | 200 |
| GET | `/publicaciones` | Público | 200 |
| GET | `/publicaciones/{id}` | Público | 200 |
| GET | `/pedidos` | Cliente / Emprendedor | 200 |
| GET | `/pedidos/{id}` | Usuario autorizado | 200 |
| POST | `/pedidos/checkout` | Cliente | 204 |
| PUT | `/pedidos/{id}/estado` | Emprendedor propietario | 204 |

## 13. Ejemplo de consumo

Iniciar sesión:

```javascript
const respuesta = await fetch("/emprendelink/api/v1/auth/login", {
  method: "POST",
  credentials: "same-origin",
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify({
    correo: "cliente@ejemplo.com",
    contrasena: "contrasena-del-usuario"
  })
});

const sesion = await respuesta.json();

if (!respuesta.ok) {
  throw new Error(sesion.message ?? "No se pudo iniciar sesión");
}

const csrfToken = sesion.csrfToken;
```

Consulta pública:

```javascript
const respuesta = await fetch(
  "/emprendelink/api/v1/publicaciones"
);

const publicaciones = await respuesta.json();
```

Ejemplo de checkout autenticado:

```javascript
const respuesta = await fetch(
  "/emprendelink/api/v1/pedidos/checkout",
  {
    method: "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json",
      "X-CSRF-Token": csrfToken
    },
    body: JSON.stringify({
      items: [
        { idPublicacion: 25, cantidad: 2 }
      ],
      direccionEntrega: "Dirección de entrega",
      observaciones: null
    })
  }
);

if (respuesta.status === 204) {
  // Operación completada correctamente.
} else {
  const error = await respuesta.json();
  throw new Error(error.message);
}
```

Estos ejemplos suponen que el cliente se sirve desde el mismo origen de la aplicación.

## 14. Pruebas obligatorias

Los responsables de cada módulo deben comprobar:

- Respuestas JSON y códigos HTTP.
- IDs y filtros válidos e inválidos.
- Colecciones vacías.
- Recursos inexistentes.
- Solicitudes sin autenticación.
- Usuarios inactivos.
- Usuarios con rol incorrecto.
- Acceso a pedidos ajenos.
- Tokens CSRF ausentes e incorrectos.
- Stock insuficiente.
- Transiciones de pedido inválidas.
- Ausencia de datos sensibles.
- Regresión de las funcionalidades JSF existentes.

El responsable de QA consolidará la colección y matriz de pruebas, pero cada desarrollador prueba inicialmente su módulo.

## 15. Responsabilidades de implementación

- Integrante 1: contrato, autenticación, seguridad y manejo compartido de errores.
- Integrante 2: emprendimientos y publicaciones.
- Integrante 3: pedidos y checkout.
- Integrante 4: cliente consumidor.
- Integrante 5: QA, documentación de ejecución y evidencias.

No modificar Resources ajenos sin coordinación previa.

## 16. Estado del proyecto

```text
JAX-RS /api/v1                 IMPLEMENTADO
GET /health                    IMPLEMENTADO Y PROBADO

Contrato v1                    PROPUESTO PARA REVISIÓN
Autenticación REST             PENDIENTE
Seguridad REST                 PENDIENTE
Manejo de errores REST         PENDIENTE

Emprendimientos REST           PENDIENTE
Publicaciones REST             PENDIENTE
Pedidos REST                   PENDIENTE
Cliente consumidor             PENDIENTE
Pruebas integrales             PENDIENTE
Sprint Review                  PENDIENTE
```

Los endpoints documentados no deben presentarse como implementados hasta que sus responsables los integren y prueben.
