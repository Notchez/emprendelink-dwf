# Matriz de pruebas — EmprendeLink DWF

Estados:

```text
PENDIENTE
APROBADA
FALLIDA
BLOQUEADA
```

Una prueba manual solo debe marcarse `APROBADA` cuando haya sido ejecutada realmente.

---
## 1. Regresión automatizada
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-001 | `CategoriaMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-002 | `EmprendimientoMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-003 | `PedidoMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-004 | `PublicacionMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-005 | `UsuarioMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-006 | Auth / usuarios | Unit | APROBADA |
| CP-007 | Categoría Service | Unit | APROBADA |
| CP-008 | Emprendimiento Service | Unit | APROBADA |
| CP-009 | Pedido Service | Unit | APROBADA |
| CP-010 | Publicación Service | Unit | APROBADA |
| CP-011 | `mvn clean test` | Build/Test | APROBADA |
Última ejecución:

```text
Tests run: 41
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

---
## 2. Infraestructura

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-020 | DataSource `jdbc/EmprendeLinkDS` conecta | Manual | APROBADA |
| CP-021 | JPA inicia con EclipseLink/JTA | Integración | APROBADA |
| CP-022 | Aplicación despliega en GlassFish | Manual | APROBADA |
| CP-023 | Context path `/emprendelink` responde | Manual | APROBADA |
| CP-024 | `mvn clean package` después del último incremento | Build | PENDIENTE |

---
## 3. Administración
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-030 | Admin accede a `/admin/*` | Manual | APROBADA |
| CP-031 | Crear categoría | Manual | APROBADA |
| CP-032 | Editar categoría | Manual | PENDIENTE |
| CP-033 | Activar/desactivar categoría | Manual | PENDIENTE |
| CP-034 | Configurar `manejaStock` | Manual | APROBADA |
| CP-035 | Crear emprendedor desde Admin | Manual | APROBADA |
| CP-036 | Crear negocio junto al emprendedor | Manual | APROBADA |
| CP-037 | Rechazar correo duplicado de emprendedor | Manual | PENDIENTE |
| CP-038 | Rechazar plan inactivo | Manual/Service | PENDIENTE |
| CP-039 | Rechazar categoría inactiva | Manual/Service | PENDIENTE |
---
## 4. Regla un emprendedor = un negocio

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-040 | Emprendedor creado recibe un negocio | Manual | APROBADA |
| CP-041 | `id_propietario` es único en BD | Integración | APROBADA |
| CP-042 | No existe flujo UI para crear segundo negocio | Manual | APROBADA |
| CP-043 | Segundo negocio para mismo propietario es rechazado por integridad | Integración | PENDIENTE |

---
## 5. Mi negocio
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-050 | Emprendedor ve su negocio | Manual | APROBADA |
| CP-051 | Muestra plan y comisión | Manual | APROBADA |
| CP-052 | Muestra publicaciones activas / máximo | Manual | APROBADA |
| CP-053 | Emprendedor edita descripción | Manual | PENDIENTE |
| CP-054 | Emprendedor edita contacto | Manual | PENDIENTE |
| CP-055 | Emprendedor edita logo URL | Manual | PENDIENTE |
| CP-056 | Nombre no se edita desde Mi negocio | Manual | APROBADA |
| CP-057 | Categoría no se edita desde Mi negocio | Manual | APROBADA |
---
## 6. Publicaciones
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-060 | Crear publicación propia | Manual/Service | APROBADA |
| CP-061 | Editar publicación actualiza el mismo registro | Manual | APROBADA |
| CP-062 | Editar no crea duplicado | Manual | APROBADA |
| CP-063 | Activar/desactivar publicación | Manual | APROBADA |
| CP-064 | Contador del plan cambia con AJAX | Manual | APROBADA |
| CP-065 | Espacios disponibles cambian con AJAX | Manual | APROBADA |
| CP-066 | Imagen URL se conserva | Manual | APROBADA |
| CP-067 | Precio con entrada de centavos | Manual | APROBADA |
| CP-068 | Publicación requiere negocio propio | Unit | APROBADA |
| CP-069 | Límite de plan al crear | Unit | APROBADA |
| CP-070 | Límite de plan al reactivar | Unit | APROBADA |
| CP-071 | Stock negativo rechazado | Unit | APROBADA |
| CP-072 | Categoría/tipo se derivan del negocio | Manual/Service | PENDIENTE |
---
## 7. Catálogo cliente
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-080 | Catálogo lista publicaciones visibles | Manual | APROBADA |
| CP-081 | Búsqueda AJAX | Manual | APROBADA |
| CP-082 | Filtro por categoría | Manual | APROBADA |
| CP-083 | Filtro visual Tipo no existe | Manual | APROBADA |
| CP-084 | Detalle de publicación funciona | Manual | APROBADA |
| CP-085 | Emprendedor autenticado no entra al catálogo público | Manual | PENDIENTE |
| CP-086 | Admin autenticado no entra al catálogo público | Manual | PENDIENTE |
---
## 8. Carrito
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-090 | Agregar artículo cambia a stepper | Manual | APROBADA |
| CP-091 | `+` incrementa en catálogo por AJAX | Manual | APROBADA |
| CP-092 | `-` disminuye en catálogo por AJAX | Manual | APROBADA |
| CP-093 | Cantidad 1 muestra papelera | Manual | APROBADA |
| CP-094 | Papelera elimina artículo | Manual | APROBADA |
| CP-095 | Badge del carrito se actualiza | Manual | APROBADA |
| CP-096 | `+/-` se actualiza inmediatamente dentro del carrito | Manual | APROBADA |
| CP-097 | Subtotales/totales cambian por AJAX | Manual | APROBADA |
| CP-098 | No permite superar stock | Manual | PENDIENTE |
| CP-099 | Carrito conserva estado entre vistas | Manual | PENDIENTE |
| CP-100 | Carrito admite artículos de dos negocios | Manual | PENDIENTE |
---
## 9. Checkout y pedidos
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-110 | Checkout usa dirección sugerida del perfil | Manual | PENDIENTE |
| CP-111 | Dirección obligatoria validada | Manual/Service | PENDIENTE |
| CP-112 | Carrito multi-negocio crea un pedido por negocio | Manual/Service | PENDIENTE |
| CP-113 | Total se calcula desde detalles | Unit | APROBADA |
| CP-114 | Pedido descuenta stock | Unit | APROBADA |
| CP-115 | Stock insuficiente rechazado | Unit | APROBADA |
| CP-116 | Pedido congela plan aplicado | Unit | APROBADA |
| CP-117 | Pedido congela tasa de comisión | Unit | APROBADA |
| CP-118 | Pedido calcula monto de comisión | Unit | APROBADA |
| CP-119 | Cancelar restaura stock | Unit | APROBADA |
| CP-120 | PENDIENTE → CONFIRMADO | Unit | APROBADA |
| CP-121 | Salto directo a COMPLETADO rechazado | Unit | APROBADA |
| CP-122 | Otro emprendedor no cambia pedido ajeno | Unit | APROBADA |
| CP-123 | Cliente ve sus pedidos | Manual | APROBADA |
| CP-124 | Estados se muestran correctamente en móvil | Manual | APROBADA |
| CP-125 | Detalle muestra líneas del pedido | Manual | APROBADA |
| CP-126 | Cambio posterior del perfil no altera dirección histórica | Manual | PENDIENTE |
---
## 10. Perfil cliente

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-130 | `/perfil/*` requiere cliente | Manual | PENDIENTE |
| CP-131 | Nombre es solo lectura | Manual | APROBADA |
| CP-132 | Correo es solo lectura | Manual | APROBADA |
| CP-133 | Teléfono se puede actualizar | Manual | PENDIENTE |
| CP-134 | Dirección se puede actualizar | Manual | PENDIENTE |
| CP-135 | Cerrar sesión está dentro de Mi perfil | Manual | APROBADA |

---
## 11. Seguridad
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-140 | `/admin/*` requiere Admin | Manual | APROBADA |
| CP-141 | `/publicaciones/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-142 | `/emprendimientos/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-143 | `/ventas/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-144 | `/comisiones/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-145 | `/carrito/*` requiere Cliente | Manual | PENDIENTE |
| CP-146 | `/perfil/*` requiere Cliente | Manual | PENDIENTE |
| CP-147 | Ruta privada sin sesión redirige a login | Manual | PENDIENTE |
| CP-148 | Cliente no consulta pedido ajeno | Unit/Manual | PENDIENTE |
| CP-149 | Emprendedor no gestiona publicación ajena | Unit | APROBADA |
---
## 12. Persistencia e integridad
| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-160 | Pedido y detalles se persisten relacionados | Integración | APROBADA |
| CP-161 | Emprendimiento conserva propietario | Integración | APROBADA |
| CP-162 | Emprendimiento conserva plan | Integración | APROBADA |
| CP-163 | Emprendimiento conserva categoría | Integración | APROBADA |
| CP-164 | Usuario conserva dirección | Mapper/Integración | APROBADA |
| CP-165 | Pedido conserva dirección entrega | Mapper/Integración | APROBADA |
| CP-166 | Pedido conserva snapshot del plan | Mapper/Unit | APROBADA |
| CP-167 | Rollback total si falla checkout multi-negocio | Integración | PENDIENTE |
---
## 13. Antes del PR final de Fase 2

Ejecutar:

```powershell
mvn clean test
mvn clean package
```

Registrar evidencia de los casos manuales pendientes más importantes:

```text
CP-085
CP-086
CP-098
CP-100
CP-110
CP-112
CP-126
CP-133
CP-134
CP-141
CP-142
CP-143
CP-144
CP-145
CP-146
CP-147
CP-167
```

No es obligatorio que todos los casos pendientes se automaticen en Fase 2, pero sí deben ejecutarse y documentarse si se desean presentar como evidencia de cierre.

---
## 14. Fase 3 — Pruebas de API REST e integración HTTP

**Sprint:** III — Servicios REST e integración de clientes  
**Responsable de ejecución y evidencias:** Integrante 5 (QA)  
**Fecha de ejecución:** 14 de noviembre de 2026  
**Ambiente:** Windows; Java 25; Eclipse GlassFish 8.0.5; MySQL Server 8.4.x; aplicación desplegada con contexto `/emprendelink`  
**URL base:** `http://localhost:8080/emprendelink/api/v1`  
**Herramientas utilizadas:** `curl.exe` desde CMD/PowerShell; `Invoke-WebRequest` e `Invoke-RestMethod` de PowerShell.  
**Contrato de referencia:** [`API.md`](API.md), versión v1 propuesta en `develop`.

**Alcance:** Esta sección documenta exclusivamente las pruebas observadas durante la sesión de QA de Fase 3. **No modifica los resultados históricos de Fase 2 de las secciones 1 a 13.** Los datos de prueba se obtuvieron del entorno local. Las solicitudes autenticadas reutilizaron una sesión HTTP de cliente (`ROLE_CLIENTE`) y, cuando correspondía, el encabezado `X-CSRF-Token`.

### 14.1 Criterios de aceptación y estados

- **APROBADA:** se ejecutó y el comportamiento verificado coincidió con el esperado. Si existen verificaciones complementarias no realizadas, se especifican en observaciones.
- **FALLIDA:** se ejecutó y se observó una discrepancia reproducible con el contrato o escenario probado.
- **PENDIENTE:** no se completó la verificación necesaria para decidir el resultado integral del caso.
- **BLOQUEADA:** no se ejecutó por depender de un rol, recurso o configuración no disponible.

Para los errores REST, no basta con el código HTTP correcto: el contrato también especifica `Content-Type: application/json` y un objeto con `status`, `error` y `message`. Si el script no imprimió el cuerpo o las cabeceras, **no se presume** conformidad del formato.

### 14.2 Resumen de ejecución

| Indicador | Total |
|---|---:|
| Casos definidos para esta ronda de Fase 3 | 25 |
| Casos ejecutados total o parcialmente | 23 |
| APROBADA | 19 |
| FALLIDA | 3 |
| PENDIENTE de verificación de formato (ejecutada parcialmente) | 1 |
| BLOQUEADA (sin ejecutar) | 2 |
| Defectos distintos confirmados | 2 |

> Los recuentos abarcan los 25 IDs consecutivos `CP-200` a `CP-224`. Los casos `CP-216` y `CP-217` no se ejecutaron y permanecen `BLOQUEADA`; los casos `CP-218` a `CP-224` se incorporaron durante el QA. Estos resultados no deben confundirse con nuevas ejecuciones de los casos históricos de Fase 2.

### 14.3 Disponibilidad, emprendimientos y publicaciones

| ID | Método y ruta (relativa a `/api/v1`) | Escenario | HTTP esperado | HTTP obtenido | Estado | Evidencia |
|---|---|---|---:|---:|---|---|
| CP-200 | `GET /health` | Disponibilidad de API y campos `status`, `application`, `apiVersion` | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-200-health-200.png) |
| CP-201 | `GET /emprendimientos` | Listar emprendimientos públicos | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-201-emprendimientos-200.png) |
| CP-202 | `GET /emprendimientos/999999` | ID inexistente; error estandarizado | 404 JSON | 404 HTML | FALLIDA | [Captura](EVIDENCIAS-FASE3/api/CP-202-confirmacion-accept-json-404-html.png) |
| CP-203 | `GET /publicaciones` | Listar publicaciones públicas | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-203-publicaciones-200.png) |
| CP-204 | `GET /publicaciones/999999` | Publicación inexistente; error estandarizado | 404 JSON | 404 HTML | FALLIDA | [Captura](EVIDENCIAS-FASE3/api/CP-204-publicacion-inexistente-404-html.png) |
| CP-205 | `GET /publicaciones?texto=cafe` | Aplicar filtro de texto | 200 lista filtrada | 200 lista sin filtrar | FALLIDA | [Captura](EVIDENCIAS-FASE3/api/CP-205-filtro-texto-ignorado-200.png) |
| CP-219 | `GET /emprendimientos/1` | Obtener negocio existente por ID | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-219-emprendimiento-id-1-200.png) |
| CP-220 | `GET /emprendimientos/1/publicaciones` | Consultar publicaciones vinculadas al negocio 1 | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-220-publicaciones-emprendimiento-id-1-200.png) |
| CP-221 | `GET /publicaciones/1` | Obtener publicación existente por ID | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-221-publicacion-id-1-200.png) |

**Observaciones del catálogo:**

- CP-200: respondió `{"phase":"Fase 3","status":"UP","application":"EmprendeLink","apiVersion":"v1"}`.
- CP-201: devolvió dos emprendimientos visibles, identificados con `id` 1 y 2.
- CP-203: devolvió tres publicaciones, con IDs 3, 2 y 1.
- CP-202 y CP-204: ambos devolvieron HTTP 404, pero `Content-Type: text/html` y una página `404 - Recurso no encontrado`. CP-202 se repitió enviando `Accept: application/json` y persistió el problema. **DEF-001**.
- CP-205: retornó los mismos tres registros (IDs 3, 2 y 1) que CP-203, aunque la consulta solicitaba `texto=cafe`. **DEF-002**.
- CP-219: devolvió el negocio 1, *Panadería Ruiz*. CP-220: dos publicaciones con `emprendimientoId=1`. CP-221: publicación 1, precio 2.50 y stock 40 **antes del checkout**.
- La visualización de caracteres acentuados resultó incorrecta en algunas capturas de CMD. No se ha demostrado si proviene de la codificación de terminal o del API, por lo que **no se registra como defecto confirmado**.
- Los listados observados son respuestas del API; no se efectuó en esta ronda una comparación independiente de todos esos registros mediante consultas SQL directas.

### 14.4 Autenticación, sesión y autorización

| ID | Método y ruta | Escenario | HTTP esperado | HTTP obtenido | Estado | Evidencia |
|---|---|---|---:|---:|---|---|
| CP-206 | `POST /auth/login` | Credenciales válidas de cliente | 200 | 200 | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-206-209-resultados-resumen.png) |
| CP-207 | `POST /auth/login` | Credenciales incorrectas | 401 JSON | 401 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-207-login-credenciales-invalidas-401-json.png) |
| CP-208 | `GET /auth/me` | Consulta sin sesión | 401 JSON | 401 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-208-auth-me-sin-sesion-401-json.png) |
| CP-209 | `GET /auth/me` | Consulta con sesión válida de cliente | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-206-209-resultados-resumen.png) |
| CP-210 | `POST /auth/logout`; luego `GET /auth/me` | Cerrar sesión con CSRF y comprobar invalidación | 204; luego 401 | 204; luego 401 | APROBADA | [Logout](EVIDENCIAS-FASE3/api/CP-210-logout-204.png) / [Validación](EVIDENCIAS-FASE3/api/CP-210-sesion-invalidada-401.png) |
| CP-211 | `GET /pedidos` | Usuario no autenticado consulta pedidos | 401 JSON | 401 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-211-pedidos-sin-sesion-401-json.png) |
| CP-218 | `POST /auth/login` | Cuerpo JSON vacío `{}` | 400 JSON | 400 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-218-login-body-vacio-400-json.png) |
| CP-224 | `PUT /pedidos/5/estado` | Cliente con sesión y CSRF intenta cambiar estado | 403 | 403 | APROBADA | [403](EVIDENCIAS-FASE3/api/CP-224-cliente-cambio-estado-denegado-403.png) / [Estado intacto](EVIDENCIAS-FASE3/api/CP-224-verificacion-estado-pendiente.png) |

**Observaciones de seguridad:**

- Para CP-206 y CP-209, la cuenta nueva se creó mediante el registro de la interfaz web. En ambas solicitudes REST se obtuvo `ROLE_CLIENTE`, HTTP 200 y existencia de `csrfToken` (`True`); no se guardaron contraseñas ni el valor del token en las evidencias.
- CP-207: el mensaje `401` fue genérico (`Es necesario iniciar sesion`), sin exposición de credenciales. Se verificó posteriormente que el login REST válido sí funcionaba (CP-206).
- CP-208 y CP-211: error JSON con campos `status=401`, `error=Unauthorized` y `message`.
- CP-218: error JSON con `status=400`, `error=Bad Request` y mensaje de datos inválidos.
- CP-210: luego de logout exitoso, reutilizar la misma sesión para `/auth/me` devolvió 401.
- CP-224: se obtuvo 403 al intentar actualizar el pedido desde `ROLE_CLIENTE`; la lectura posterior confirmó que el pedido 5 siguió `PENDIENTE`. El cuerpo JSON del 403 no se capturó.

### 14.5 Pedidos, checkout y protección de inventario

| ID | Método y ruta | Escenario | HTTP esperado | HTTP obtenido | Estado | Evidencia |
|---|---|---|---:|---:|---|---|
| CP-212 | `GET /pedidos` | Listar pedidos con cuenta nueva `ROLE_CLIENTE` | 200 JSON | 200 JSON `[]` | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-212-listado-pedidos-autenticado-200.png) |
| CP-213 | `POST /pedidos/checkout` | Compra válida con sesión y CSRF; verificar pedido y stock | 204; persistencia coherente | 204; pedido 5; stock 40→39 | APROBADA | [Checkout](EVIDENCIAS-FASE3/api/CP-213-checkout-csrf-valido-204.png) / [Verificación](EVIDENCIAS-FASE3/api/CP-213-verificacion-pedido-5-y-stock-39.png) |
| CP-214 | `POST /pedidos/checkout` | Compra sin encabezado CSRF | 403 | 403 | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-214-checkout-sin-csrf-403.png) |
| CP-215 | `POST /pedidos/checkout` | Solicitar 40 unidades con stock 39; conservar pedido y stock | 409; sin cambios | 409; pedido y stock intactos | APROBADA | [409](EVIDENCIAS-FASE3/api/CP-215-checkout-stock-insuficiente-409.png) / [Integridad](EVIDENCIAS-FASE3/api/CP-215-verificacion-integridad-pedido5-stock39.png) |
| CP-222 | `GET /pedidos/5` | Consultar detalle del pedido propio creado | 200 JSON | 200 JSON | APROBADA | [Captura](EVIDENCIAS-FASE3/api/CP-222-223-pedido-propio-200-inexistente-404.png) |
| CP-223 | `GET /pedidos/999999` | Consultar ID de pedido inexistente y formato de error | 404 JSON | 404; formato no confirmado | PENDIENTE | [Código](EVIDENCIAS-FASE3/api/CP-222-223-pedido-propio-200-inexistente-404.png) / [Validación incompleta](EVIDENCIAS-FASE3/api/CP-223-404-tipo-no-confirmado.png) |

**Detalle de las verificaciones de integridad:**

1. Antes de la compra, `GET /publicaciones/1` devolvía `stock: 40`.
2. CP-213 devolvió `204 No Content` al enviar `items: [{"idPublicacion":1,"cantidad":1}]` con sesión y CSRF válidos.
3. Después de la compra, `GET /pedidos` mostró el **pedido 5**, del emprendimiento 1 (*Panadería Ruiz*), `estado=PENDIENTE`, `total=2.50`.
4. `GET /publicaciones/1` mostró **stock 39**, consistente con la compra de una unidad.
5. CP-222 devolvió el detalle del pedido 5: publicación 1, cantidad 1, precio unitario 2.50, subtotal 2.50, dirección de prueba y estado `PENDIENTE`.
6. En CP-215 se intentó comprar **40 unidades con stock 39**, y el API devolvió **409 Conflict**.
7. La consulta posterior mostró **un solo pedido** para esa cuenta (ID 5) y stock todavía **39**. No se observó alteración tras el rechazo.
8. La verificación anterior corresponde a persistencia observable **mediante nuevas solicitudes HTTP**. No sustituye una inspección SQL directa de `pedido`, `detalle_pedido` y `publicacion`.

**Aspectos por completar:**

- CP-215: aunque pasan el código HTTP y la integridad de datos, **no se capturó el cuerpo ni el `Content-Type` del error 409**. Ese punto queda como comprobación documental adicional.
- CP-223: el 404 se confirmó dos veces; la segunda prueba con PowerShell no imprimió un valor visible de `Content-Type`. Su estado integral queda `PENDIENTE` hasta capturar cabeceras y cuerpo, sin asumir que falló en JSON.

### 14.6 Casos bloqueados y cobertura que falta

| ID | Método y ruta | Escenario | Esperado | Estado | Motivo |
|---|---|---|---|---|---|
| CP-216 | `PUT /pedidos/{id}/estado` | Emprendedor propietario cambia `PENDIENTE` a `CONFIRMADO` con CSRF | 204 | BLOQUEADA | No se usó cuenta de prueba `ROLE_EMPRENDEDOR` asociada al negocio del pedido |
| CP-217 | `PUT /pedidos/{id}/estado` | Emprendedor propietario intenta transición no permitida (por ejemplo, `PENDIENTE` a `COMPLETADO`) | 409 | BLOQUEADA | Misma dependencia de rol y propiedad del pedido |

**Otras verificaciones recomendadas (no ejecutadas ni incluidas en el total de 25 casos):**

- Acceso de un cliente autenticado a un pedido **ajeno** (debe permanecer oculto; normalmente 404 conforme al contrato).
- Checkout con publicaciones de dos emprendimientos (debe crear un pedido por negocio).
- Revisión directa en MySQL del pedido 5, sus detalles y el stock 39.
- Comprobación completa del formato JSON para errores 403, 404 de pedidos y 409.
- Integración de cliente externo: flujo de navegación, errores visibles y conexión real a API.
- Pruebas automatizadas HTTP reproducibles e independientes del equipo de QA.
- Ejecución final de `mvn clean test`, `mvn clean package` y verificación de GitHub Actions sobre el commit definitivo.

### 14.7 Registro de defectos identificados

| ID | Severidad sugerida | Recurso | Resultado esperado | Resultado observado | Estado | Responsable de revisión |
|---|---|---|---|---|---|---|
| DEF-001 | Media | `GET /emprendimientos/{id}` y `GET /publicaciones/{id}` inexistentes | 404 JSON `status/error/message` | 404 `text/html`, página de error HTML | ABIERTO | Integrante 1 (errores comunes) e Integrante 2 (Resources de catálogo) |
| DEF-002 | Media | `GET /publicaciones?texto=cafe` | Filtrar por texto | Devuelve las tres publicaciones del listado sin filtro | ABIERTO | Integrante 2 (PublicacionResource) |

**Pasos de reproducción de DEF-001:**

```powershell
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/emprendimientos/999999
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/publicaciones/999999
```

**Pasos de reproducción de DEF-002:**

```powershell
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/publicaciones
curl.exe -i -H "Accept: application/json" "http://localhost:8080/emprendelink/api/v1/publicaciones?texto=cafe"
```

Para ambos defectos: **corrección no verificada**, sin diagnóstico de causa raíz confirmado. El integrante de QA registra los hallazgos y solicita corrección a los responsables; no modifica los Resources ajenos.

### 14.8 Evidencias técnicas e índice de archivos

La evidencia visual de esta ronda se entrega en el directorio `docs/EVIDENCIAS-FASE3/api/`. Los nombres de las imágenes incluyen el ID del caso correspondiente y se referencian en las tablas anteriores.

| Código | Evidencia | Archivo |
|---|---|---|
| INF-001 | Ping exitoso del Connection Pool `EmprendeLinkPool` | [INF-001-jdbc-ping-exitoso.png](EVIDENCIAS-FASE3/api/INF-001-jdbc-ping-exitoso.png) |
| INF-002 | API `/health` respondiendo desde navegador | [INF-002-api-health-navegador.png](EVIDENCIAS-FASE3/api/INF-002-api-health-navegador.png) |
| CP-200 a CP-224 | Capturas de ejecución HTTP, validación de sesión, checkout, inventario y errores | Consultar enlaces individuales en las tablas 14.3 a 14.5 |

**Criterios de conservación:**

- No adjuntar contraseñas, cookies `JSESSIONID`, valores completos de token CSRF ni datos reales de usuarios de producción.
- Las respuestas y códigos se capturan como se obtuvieron, sin convertir errores reales en resultados aprobados.
- Las pruebas de compra utilizan únicamente datos académicos de entorno local.
- El PDF final consolidará las capturas y su interpretación, sin sustituir este registro reproducible en Markdown.

### 14.9 Conclusión de QA del Sprint III

La ronda de pruebas verificó satisfactoriamente la disponibilidad de la API, consultas públicas, autenticación por sesión, emisión de token CSRF, restricciones de rol, invalidación de sesión y checkout con descuento de inventario. La creación del pedido 5 y la reducción de stock de 40 a 39 fueron observadas mediante solicitudes HTTP independientes. La solicitud con inventario insuficiente produjo 409 y no alteró el pedido existente ni el stock.

Se mantienen **dos defectos abiertos** de conformidad REST/catálogo (`DEF-001` y `DEF-002`), **un caso pendiente de validación de formato** (`CP-223`) y **dos casos bloqueados** por falta de credenciales de emprendedor (`CP-216` y `CP-217`). No corresponde declarar la totalidad del Sprint III como libre de defectos hasta resolver o aceptar formalmente estas diferencias y completar la evidencia final de CI, cliente consumidor y build.
