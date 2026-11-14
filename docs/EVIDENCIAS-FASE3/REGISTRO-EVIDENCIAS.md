# EmprendeLink — Fase 3: registro de evidencias de QA

Fecha de ejecución: **14 de noviembre de 2026**.
Entorno observado: **GlassFish 8.0.5**, API REST local en `http://localhost:8080/emprendelink/api/v1`.
Herramienta usada en las pruebas registradas: terminal PowerShell de IntelliJ mediante `curl.exe -i`.

## Pruebas ejecutadas y observadas

| ID | Solicitud | Esperado | Obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| CP-200 | `GET /health` | HTTP 200 + JSON | HTTP 200 + JSON con `status: UP`, `application: EmprendeLink`, `apiVersion: v1` | APROBADA | `api/CP-200-health-200.png` |
| CP-201 | `GET /emprendimientos` | HTTP 200 + arreglo JSON | HTTP 200 + JSON con 2 emprendimientos (`id` 1 y 2) | APROBADA | `api/CP-201-emprendimientos-200.png` |
| CP-202 | `GET /emprendimientos/999999` | HTTP 404 + cuerpo JSON de error | HTTP 404 pero `Content-Type: text/html` y página HTML de error | FALLIDA — formato | `api/CP-202-emprendimiento-inexistente-404-html.png`; `api/CP-202-confirmacion-accept-json-404-html.png` |
| CP-203 | `GET /publicaciones` | HTTP 200 + arreglo JSON | HTTP 200, `application/json`, 3 publicaciones (`id` 3, 2 y 1; `titulo`, `precio`, `stock`, `emprendimientoId`) | APROBADA | `api/CP-203-publicaciones-200.png` |
| CP-204 | `GET /publicaciones/999999` con `Accept: application/json` | HTTP 404 + JSON de error | HTTP 404, `text/html` con página de error | FALLIDA — formato (DEF-001) | `api/CP-204-publicacion-inexistente-404-html.png` |
| CP-205 | `GET /publicaciones?texto=cafe` | HTTP 200 + resultados filtrados | HTTP 200, JSON con las mismas 3 publicaciones que CP-203 | FALLIDA — filtro (DEF-002) | `api/CP-205-filtro-texto-ignorado-200.png` |
| CP-208 | `GET /auth/me` sin sesión, con `Accept: application/json` | HTTP 401 + error JSON | HTTP 401, `application/json`; `status:401`, `error:Unauthorized`, `message:Es necesario iniciar sesion.` | APROBADA | `api/CP-208-auth-me-sin-sesion-401-json.png` |
| CP-211 | `GET /pedidos` sin sesión, con `Accept: application/json` | HTTP 401 + error JSON | HTTP 401, `application/json`; `status:401`, `error:Unauthorized`, `message:Es necesario iniciar sesion.` | APROBADA | `api/CP-211-pedidos-sin-sesion-401-json.png` |
| CP-207 | `POST /auth/login` con credenciales incorrectas | HTTP 401 + error JSON | HTTP 401, `application/json`, `Unauthorized` y mensaje `Es necesario iniciar sesion.` | APROBADA — código/formato | `api/CP-207-login-credenciales-invalidas-401-json.png` |
| CP-218 | `POST /auth/login` con `{}` | HTTP 400 + error JSON | HTTP 400, `application/json`, `Bad Request`, mensaje `La solicitud contiene datos invalidos.` | APROBADA | `api/CP-218-login-body-vacio-400-json.png` |

| CP-219 | `GET /emprendimientos/1` | 200 y objeto JSON | 200 y application/json; Emprendimiento id=1, nombre=Panadería Ruiz | APROBADA | `api/CP-219-emprendimiento-id-1-200.png` |
| CP-220 | `GET /emprendimientos/1/publicaciones` | 200 y arreglo JSON | 200 y application/json; Se retornaron 2 publicaciones (id=2 y 1) con emprendimientoId=1, consistente con el filtrado por negocio | APROBADA | `api/CP-220-publicaciones-emprendimiento-id-1-200.png` |
| CP-221 | `GET /publicaciones/1` | 200 y objeto JSON | 200 y application/json; Se retornó publicación id=1, título Pan francés (docena), precio=2 | APROBADA | `api/CP-221-publicacion-id-1-200.png` |

### Comandos reproducibles

```bat
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/emprendimientos/1
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/emprendimientos/1/publicaciones
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/publicaciones/1
curl.exe -i http://localhost:8080/emprendelink/api/v1/health
curl.exe -i http://localhost:8080/emprendelink/api/v1/emprendimientos
curl.exe -i http://localhost:8080/emprendelink/api/v1/emprendimientos/999999
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/publicaciones
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/publicaciones/999999
curl.exe -i -H "Accept: application/json" "http://localhost:8080/emprendelink/api/v1/publicaciones?texto=cafe"
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/auth/me
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/pedidos
REM CP-207: usar credenciales ficticias; ver captura y registro de pruebas
REM CP-218: JSON vacio
curl.exe -i -X POST "http://localhost:8080/emprendelink/api/v1/auth/login" -H "Content-Type: application/json" -H "Accept: application/json" --data-raw "{}"
```

## Resultados de autenticación

CP-207 y CP-218 verifican los códigos 401 para credenciales incorrectas y 400 para JSON vacío; el primer caso devuelve el mensaje genérico «Es necesario iniciar sesion.», por lo que el login exitoso CP-206 deberá comprobarse para descartar que un filtro esté interceptando solicitudes de login.

CP-208 y CP-211 verifican que, sin cookie de sesión, los recursos protegidos retornan `401 Unauthorized` con un objeto JSON seguro y coherente con el contrato. No se observaron defectos en estos escenarios.

## Hallazgos pendientes de confirmar o corregir

**DEF-001 — Respuesta de error 404 no respeta el contrato JSON (CONFIRMADO).** En CP-202 y CP-204, el código HTTP `404` es correcto, pero el cuerpo se entrega en `text/html`, no como objeto JSON. `docs/API.md` (rama `develop`, apartados 3 y 10) establece el uso de `application/json` y, para errores REST controlados, un objeto con `status`, `error`, `message`. Se repitió con `Accept: application/json` y persistió `HTTP 404`, `Content-Type: text/html` con cuerpo HTML. Reportar a responsables de manejo de errores REST / recurso de emprendimientos. Evidencias: `api/CP-202-emprendimiento-inexistente-404-html.png`, `api/CP-202-confirmacion-accept-json-404-html.png` y `api/CP-204-publicacion-inexistente-404-html.png`.

**Comando usado para confirmar negociación:**

```bat
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/emprendimientos/999999
```

**DEF-002 — El filtro `texto` no modifica la lista (CONFIRMADO EN LA EJECUCIÓN).** En CP-205 se obtuvo HTTP 200 y JSON, pero el contenido fue idéntico al listado sin filtro de CP-203. Verificar el contrato definitivo y derivar corrección al Integrante 2. Evidencia: `api/CP-205-filtro-texto-ignorado-200.png`.


## Observación documental pendiente de revisión

**OBS-001 — Nombres de atributos DTO vs. contrato `docs/API.md`.** Las respuestas reales CP-201 y CP-203 muestran atributos `id`, `emprendimientoId` y `titulo`, mientras los ejemplos del contrato usan `idEmprendimiento`, `idPublicacion` y `nombre`. El contrato consultado todavía se declara «propuesto para revisión» y contiene estados de implementación antiguos, por lo que se requiere coordinación con integrantes 1 y 2 para determinar si corresponde actualizar el contrato o corregir los DTO. **No se ha contabilizado como defecto confirmado ni se ha cambiado el resultado de las pruebas HTTP/JSON.**

## Infraestructura verificada

- Pool JDBC `EmprendeLinkPool`: `Ping Succeeded` observado en GlassFish. Captura: `api/INF-001-jdbc-ping-exitoso.png`.
- Endpoint `/health` accesible también desde un navegador; JSON observado. Captura: `api/INF-002-api-health-navegador.png`.

## Resumen parcial

15 pruebas ejecutadas: **12 aprobadas** y **3 fallidas**, con **2 defectos diferentes** (DEF-001 y DEF-002).

## Alcance y limitaciones

- Los estados **APROBADA** o **FALLIDA** corresponden únicamente a los resultados capturados; DEF-001 ya fue reproducido usando `Accept: application/json`.
- Las capturas de CP-201 y CP-203 acreditan respuestas con 2 emprendimientos y 3 publicaciones, respectivamente; no constituyen por sí solas una comprobación directa de consistencia con MySQL.
- Falta ejecutar el resto de los casos definidos en `docs/MATRIZ-PRUEBAS.md` y recoger sus capturas.
- No incluir contraseñas, credenciales ni capturas de configuración con secretos en el PDF final.
- Estas evidencias están listas para incorporarse al documento académico consolidado cuando terminen los casos de prueba.


## Autenticación REST — CP-206 y CP-209 (14/11/2026)

| Caso | Solicitud | Esperado | Observado | Estado | Evidencia |
|---|---|---|---|---|---|
| CP-206 | `POST /auth/login` con credenciales válidas de cliente QA | HTTP 200, rol y token CSRF | HTTP 200; `ROLE_CLIENTE`; CSRF presente (`True`) | APROBADA | `api/CP-206-209-resultados-resumen.png` |
| CP-209 | `GET /auth/me` con sesión HTTP autenticada | HTTP 200, identidad y token CSRF | HTTP 200; `ROLE_CLIENTE`; CSRF disponible (`True`) | APROBADA | `api/CP-206-209-resultados-resumen.png` |

Pruebas ejecutadas usando `Invoke-WebRequest` con `-SessionVariable qaSesion` y `-WebSession $qaSesion`. No se muestran ni conservan contraseñas ni valores de tokens en el resumen de resultados. La captura completa está guardada con fines de trazabilidad; para el PDF final utilizar preferentemente el recorte de resultados.


## Pedidos autenticados — CP-212 (14/11/2026)

| Caso | Solicitud | Esperado | Observado | Estado | Evidencia |
|---|---|---|---|---|---|
| CP-212 | `GET /api/v1/pedidos`, con sesión de cliente QA | `200 OK` y JSON | `200 OK`; `Content-Type: application/json`; cuerpo `[]` | **APROBADA** | `api/CP-212-listado-pedidos-autenticado-200.png` |

El arreglo vacío es coherente con una cuenta QA recién creada. Esta prueba confirma que la API permite consultar pedidos autenticados, no la creación o persistencia de pedidos.


## Protección CSRF — CP-214 (14/11/2026)

| Caso | Solicitud | Esperado | Observado | Estado | Evidencia |
|---|---|---|---|---|---|
| CP-214 | `POST /api/v1/pedidos/checkout` con sesión de cliente y sin cabecera `X-CSRF-Token` | `403 Forbidden` | `403 Forbidden` | **APROBADA** | `api/CP-214-checkout-sin-csrf-403.png` |

Solicitud de prueba con publicación ID 1 y cantidad 1. Esta evidencia acredita el rechazo HTTP de una solicitud autenticada sin token; no acredita todavía el checkout exitoso ni la modificación de inventario.

**Balance actualizado:** 17 pruebas ejecutadas, 14 aprobadas y 3 fallidas. Los hallazgos DEF-001 y DEF-002 permanecen abiertos hasta su corrección y retesteo.


## Checkout autorizado — CP-213 (14/11/2026)

| Caso | Solicitud | Esperado | Observado | Estado | Evidencia |
|---|---|---|---|---|---|
| CP-213 | `POST /api/v1/pedidos/checkout`, sesión de cliente y cabecera `X-CSRF-Token` válida | `204 No Content` | `204 No Content` | **APROBADA (respuesta HTTP)** | `api/CP-213-checkout-csrf-valido-204.png` |

La respuesta `204` confirma la aceptación HTTP. **Verificación posterior completada mediante GET**: pedido ID 5 en listado del cliente y stock de publicación ID 1 reducido de 40 a 39. No repetir el POST para evitar pedidos duplicados. Verificación de tablas directamente en MySQL todavía pendiente.

**Balance actualizado:** 18 pruebas ejecutadas, 15 aprobadas, 3 fallidas. Se mantienen 2 defectos distintos (DEF-001 y DEF-002).


## Verificación posterior de checkout y stock — CP-213 (14/11/2026)

| Comprobación | Antes / esperado | Observado | Conclusión |
|---|---|---|---|
| `POST /api/v1/pedidos/checkout` autenticado con CSRF | 204 | 204 | Aceptación HTTP comprobada |
| `GET /api/v1/pedidos` con la misma sesión | Pedido nuevo consultable | 200, JSON con `idPedido: 5`, `estado: PENDIENTE`, `idEmprendimiento: 1`, `total: 2.50` | Pedido consultable en solicitud separada |
| `GET /api/v1/publicaciones/1` | Stock previo 40, después 39 | 200, `stock: 39` | Inventario decreció en una unidad |

Evidencia: `api/CP-213-verificacion-pedido-5-y-stock-39.png`. La consulta REST muestra consistencia y persistencia aparente entre solicitudes; la comprobación independiente por SQL directamente sobre MySQL se deja como evidencia adicional opcional. El checkout **no** debe repetirse para evitar pedidos duplicados.

**Balance:** 18 casos ejecutados (15 aprobados, 3 fallidos); 2 defectos distintos abiertos. La verificación posterior complementa CP-213 y no se cuenta como un caso nuevo.


## Consultas de pedidos por ID — CP-222 y CP-223 (14/11/2026)

| Caso | Solicitud | Esperado | Observado | Estado | Evidencia |
|---|---|---|---|---|---|
| CP-222 | `GET /api/v1/pedidos/5` con sesión del comprador | 200 y detalle JSON | 200 y objeto JSON: pedido 5, pendiente, 1 unidad, subtotal 2.50 y total 2.50 | **APROBADA** | `api/CP-222-223-pedido-propio-200-inexistente-404.png` |
| CP-223 | `GET /api/v1/pedidos/999999` con sesión | 404 y error JSON | 404; cuerpo y Content-Type no registrados | **PENDIENTE verificar formato** | Misma captura |

**Balance actualizado:** 20 casos ejecutados: 16 aprobados en su totalidad, 3 fallidos, 1 con resultado HTTP correcto pero validación JSON pendiente. Siguen abiertos DEF-001 (404 HTML en recursos públicos) y DEF-002 (filtro `texto` sin aplicar). No se ha actualizado todavía `docs/MATRIZ-PRUEBAS.md`, a petición del usuario.


## Stock insuficiente — CP-215 (14/11/2026)

| Caso | Solicitud | Esperado | Observado | Estado | Evidencia |
|---|---|---|---|---|---|
| CP-215 | `POST /api/v1/pedidos/checkout` con cliente autenticado, token CSRF y cantidad 40 de publicación id=1 (stock conocido 39) | `409 Conflict`, error JSON; ningún pedido nuevo ni reducción de inventario | `409 Conflict`; el cuerpo no se capturó en el script | **HTTP correcto; PENDIENTE verificar JSON e integridad de datos** | `api/CP-215-checkout-stock-insuficiente-409.png` |

Para cerrar el caso: consultar `GET /api/v1/pedidos` con la misma sesión (se espera que solo permanezca el pedido #5 de la cuenta QA) y `GET /api/v1/publicaciones/1` (se espera `stock:39`). No repetir el `POST` de checkout.

**Balance:** 21 casos ejecutados: 16 aprobados en su totalidad, 3 fallidos, 2 con HTTP esperado pero validación complementaria pendiente (CP-223, CP-215). Dos defectos distintos confirmados (DEF-001 y DEF-002).


### Confirmación complementaria CP-215 — Integridad después de 409 (14/11/2026)

Evidencia `api/CP-215-verificacion-integridad-pedido5-stock39.png`: en la misma sesión autenticada, `GET /api/v1/pedidos` devolvió únicamente **1** pedido, con `idPedido:5`, y `GET /api/v1/publicaciones/1` mostró `stock:39`. Por tanto, la operación rechazada por falta de stock no generó un pedido nuevo ni modificó el inventario. **HTTP y consistencia de los datos aprobados**. Permanece pendiente capturar y verificar el **Content-Type** y cuerpo JSON de la respuesta `409`, ya que la prueba original solo mostró el estado. No repetir POST.


## CP-224 — Protección de rol en cambio de estado (14/11/2026)

| Esperado | Observado | Estado | Evidencia |
|---|---|---|---|
| `PUT /api/v1/pedidos/5/estado` con ROLE_CLIENTE, sesión y CSRF válido: HTTP 403 | HTTP 403 Forbidden | **APROBADA (autorización HTTP)**; estado persistido y cuerpo JSON pendientes | `api/CP-224-cliente-cambio-estado-denegado-403.png` |

Balance: 22 pruebas ejecutadas, 18 aprobadas por criterio principal, 3 fallidas y 1 pendiente de JSON (CP-223). Verificación adicional requerida: `GET /pedidos/5` debe conservar `PENDIENTE`; no está acreditado aún.


## CP-210 — Logout e invalidación de sesión (14/11/2026)

| Paso | Resultado esperado | Observado | Estado | Evidencia |
|---|---|---|---|---|
| `POST /api/v1/auth/logout` con sesión y CSRF | 204 No Content | 204 No Content | **APROBADA** | `api/CP-210-logout-204.png` |
| `GET /api/v1/auth/me` reutilizando sesión después de logout | 401 Unauthorized | 401 Unauthorized | **APROBADA** | `api/CP-210-sesion-invalidada-401.png` |

Con ello se comprueba invalidación de la sesión autenticada. Se adjunta además la captura que confirma que el pedido #5 continuó `PENDIENTE` tras el intento no autorizado CP-224 (`api/CP-224-verificacion-estado-pendiente.png`). Para CP-223 la nueva captura confirma únicamente HTTP 404; PowerShell no mostró Content-Type y el formato permanece pendiente de verificación (`api/CP-223-404-tipo-no-confirmado.png`).

**Corte del registro:** 23 casos ejecutados; 19 aprobados en el criterio principal, 3 fallidos (CP-202, CP-204, CP-205) y 1 pendiente de comprobación de formato de error (CP-223). CP-215 aprobó HTTP e integridad, pero el cuerpo JSON del 409 sigue sin captura. CP-216 y CP-217 no se han ejecutado por necesitar identidad de emprendedor. La matriz del repositorio no se ha actualizado todavía, según lo acordado.
