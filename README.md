# EmprendeLink — DWF

Proyecto de cátedra para **DWF — Desarrollo de Aplicaciones con Web Frameworks**.

## Estado actual

**Actualización:** El Sprint III incorpora API REST funcional y evidencia QA del 14/11/2026. Ver la sección Fase 3 para el resultado de pruebas, limitaciones y pendientes.
La aplicación completa el incremento funcional de:

```text
Fase 2 — Persistencia Empresarial e Integración JSF
Sprint II
```

La versión actual incorpora además refinamientos de experiencia de usuario para los perfiles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

La arquitectura activa es:

```text
XHTML / JSF
    ↓
Managed Beans CDI
    ↓
Services
    ↓
DAO
    ↓
JPA
    ↓
EclipseLink
    ↓
JTA
    ↓
GlassFish DataSource
    ↓
MySQL
```

La implementación anterior basada en JDBC, Servlets y JSP fue retirada del runtime para evitar mantener dos arquitecturas paralelas.

---

## Stack

- Java 25
- Maven
- Jakarta EE 11
- Jakarta Faces 4.1
- Jakarta CDI 4.1
- Jakarta Persistence 3.2
- Jakarta Transactions
- EclipseLink
- GlassFish 8.0.4
- MySQL 8.x
- MySQL Connector/J 26.7.0
- JUnit 5.13.4
- GitHub Actions

---

## Roles y navegación

### Administrador

El administrador utiliza el backoffice y puede:

- administrar usuarios;
- crear cuentas de emprendedor;
- crear el negocio inicial asociado a cada emprendedor;
- administrar categorías;
- administrar planes;
- cambiar datos administrativos del negocio;
- activar/desactivar usuarios y negocios;
- consultar ventas y comisiones.

La cuenta de emprendedor **no se crea desde el registro público**.

### Emprendedor

Cada emprendedor puede tener **un único negocio** asociado.

Su navegación principal es:

```text
Pedidos
Mi negocio
Catálogo
Ventas
Comisiones
```

El emprendedor no utiliza el catálogo público como consumidor.

Puede editar desde `Mi negocio`:

- descripción;
- contacto;
- logo mediante URL.

Los datos administrativos del negocio, como nombre, categoría y plan, son gestionados por el administrador.

### Cliente

El cliente puede:

- explorar el catálogo público;
- usar filtros de búsqueda y categoría;
- administrar un carrito;
- comprar publicaciones de uno o varios negocios;
- confirmar pedidos;
- consultar sus pedidos;
- editar teléfono y dirección;
- cerrar sesión desde `Mi perfil`.

Su navegación principal es:

```text
Explorar
Mis pedidos
Mi perfil
Carrito
```

---

## Categorías e inventario

Cada negocio pertenece a una categoría.

La categoría define si sus publicaciones manejan inventario limitado mediante:

```text
manejaStock
```

Conceptualmente:

```text
manejaStock = true
→ publicación técnica tipo PRODUCTO
→ utiliza stock

manejaStock = false
→ publicación técnica tipo SERVICIO
→ stock normalizado a 0
```

El emprendedor no selecciona manualmente tipo ni categoría al crear una publicación.

---

## Publicaciones

Las publicaciones soportan:

- nombre;
- descripción;
- imagen mediante URL;
- precio;
- stock cuando la categoría maneja inventario;
- creación;
- edición;
- activación/desactivación;
- validación de propiedad;
- validación del negocio;
- límite de publicaciones activas según plan.

El precio utiliza una entrada monetaria con formato de centavos para mejorar la experiencia de captura.

La activación/desactivación utiliza AJAX y actualiza inmediatamente:

- estado de la publicación;
- contador de publicaciones activas;
- espacios disponibles del plan.

---

## Planes

Planes definidos:

```text
Básico    → máximo 10 publicaciones activas  → comisión 7%
Estándar  → máximo 30 publicaciones activas  → comisión 5%
Premium   → máximo 60 publicaciones activas  → comisión 3%
Max       → máximo 100 publicaciones activas → comisión 1%
```

La aplicación controla el límite de publicaciones activas al:

```text
crear
reactivar
cambiar/asignar plan
```

---

## Pedidos, carrito y checkout

El carrito se mantiene en sesión mediante:

```text
CarritoBean
@SessionScoped
```

Permite:

- agregar publicaciones;
- incrementar cantidades;
- disminuir cantidades;
- eliminar cuando la cantidad llega a uno;
- validar stock;
- mostrar subtotal por negocio;
- mostrar total general;
- actualizar cantidades y totales mediante AJAX.

Un mismo carrito puede contener publicaciones de varios negocios.

Al confirmar:

```text
1 carrito visual
      ↓
agrupación por negocio
      ↓
1 Pedido por cada negocio
```

Esto conserva la regla:

```text
Pedido → un Emprendimiento
```

y permite calcular correctamente el plan y la comisión de cada venta.

---

## Snapshot de pedido

Al crear un pedido se conserva la información comercial aplicable a ese momento:

```text
idPlanAplicado
nombrePlanAplicado
tasaComision
montoComision
direccionEntrega
```

Esto evita que un cambio posterior de plan o de dirección del cliente altere el historial del pedido.

Cuando el pedido pasa a:

```text
COMPLETADO
```

se registra además:

```text
fechaCompletado
```

---

## Inventario

Para publicaciones con inventario:

```text
al crear pedido:
stock -= cantidad

al cancelar pedido:
stock += cantidad
```

No se permite superar el stock disponible desde el carrito ni desde la lógica de negocio.

---

## Perfil del cliente

El cliente dispone de:

```text
/perfil/index.xhtml
```

Puede modificar:

- teléfono;
- dirección.

Permanecen de solo lectura en esta fase:

- nombre;
- correo.

La dirección guardada se utiliza como sugerencia para nuevos pedidos, pero cada pedido conserva su propia copia histórica.

---

## Panel del emprendedor

### Pedidos

Landing principal del emprendedor.

Muestra pedidos recibidos y permite transiciones válidas de estado.

### Mi negocio

Muestra:

- negocio actual;
- plan;
- porcentaje de comisión;
- límite del catálogo;
- publicaciones activas;
- espacios disponibles;
- datos editables del negocio.

### Catálogo

Permite crear, editar, activar y desactivar publicaciones propias.

### Ventas

Permite consultar ventas completadas y filtrar por período.

### Comisiones

Permite consultar la comisión asociada a ventas completadas.

---

## Estados de pedido

Valores internos:

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

Presentación:

```text
EN_PROCESO → En camino
COMPLETADO → Entregado
```

---

## Arquitectura del proyecto

```text
src/main/java/emprendelink/
├── dao/
│   └── jpa/
├── dto/
├── exception/
├── model/
│   └── enums/
├── persistence/
│   └── jpa/
│       ├── entity/
│       └── mapper/
├── security/
├── service/
│   └── impl/
├── spring/
└── web/
    ├── jsf/
    └── rest/
```

```text
src/main/webapp/
├── admin/
├── carrito/
├── catalogo/
├── comisiones/
├── css/
├── emprendimientos/
├── error/
├── js/
├── pedidos/
├── perfil/
├── publicaciones/
├── ventas/
├── WEB-INF/
├── inicio.xhtml
├── login.xhtml
└── registro.xhtml
```

---

## Persistencia

Persistence Unit:

```text
emprendelinkPU
```

DataSource JNDI:

```text
jdbc/EmprendeLinkDS
```

Pool de GlassFish:

```text
EmprendeLinkPool
```

Proveedor:

```text
EclipseLink
```

Transacciones:

```text
JTA
```

No se utiliza Hibernate en el runtime actual.

---

## Base de datos

Base oficial:

```text
emprendelink_dwf
```

Scripts oficiales:

```text
database/schema.sql
database/seed.sql
```

El proyecto utiliza actualmente un **schema consolidado**. Las migraciones incrementales anteriores fueron retiradas del repositorio.

Los usuarios de prueba definidos en `seed.sql` utilizan:

```text
Demo1234!
```

---

## Build y pruebas

Ejecutar:

```powershell
mvn clean test
mvn clean package
```

Última regresión ejecutada:

```text
41 pruebas automatizadas
0 failures
0 errors
BUILD SUCCESS
```

Antes de integrar a `develop` se recomienda ejecutar nuevamente ambos comandos.

Artefacto esperado:

```text
target/emprendelink.war
```

---

## Integración continua

GitHub Actions ejecuta:

```text
mvn -B clean test
```

El trabajo se integra mediante Pull Request hacia:

```text
develop
```

y posteriormente mediante el flujo de liberación definido por el equipo.

---

## Deploy local

Servidor:

```text
GlassFish 8.0.4
```

Context path:

```text
/emprendelink
```

Aplicación:

```text
http://localhost:8080/emprendelink/login.xhtml
```

---

## Seguridad

La seguridad se aplica en dos niveles:

```text
HTTP request
   ↓
Servlet Filter
   ↓
JSF / Managed Bean
   ↓
Service
   ↓
reglas de rol, propiedad y estado
```

Filtros activos:

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

## Fase 3 — Servicios REST, integración y pruebas (Sprint III)

**Estado al 14 de noviembre de 2026:** API REST desplegada y sometida a pruebas HTTP locales. El resultado es **parcialmente conforme**, con hallazgos abiertos documentados por QA. Este apartado actualiza el estado preparatorio de Fase 3; los apartados anteriores del README describen la funcionalidad JSF y la arquitectura de Fase 2, que se conservan.

### Arquitectura REST

La aplicación conserva la lógica de negocio y la persistencia existentes:

```text
Cliente HTTP / interfaz consumidora
             ↓
      JAX-RS (/api/v1)
             ↓
   Autenticación y DTO
             ↓
         Services
             ↓
          DAO / JPA
             ↓
     EclipseLink / JTA
             ↓
 DataSource GlassFish / MySQL
```

No se consulta MySQL directamente desde el cliente consumidor. El contrato oficial de rutas, permisos y cuerpos JSON se mantiene en [`docs/API.md`](docs/API.md), a cargo del integrante 1.

### Requisitos de ejecución local

- Java 25, Maven, Jakarta EE 11 y GlassFish 8.x (pruebas realizadas en GlassFish **8.0.5**).
- MySQL con el esquema `emprendelink_dwf` y sus tablas, inicializado según `database/schema.sql` y `database/seed.sql` cuando corresponda. No ejecutar el seed sobre una base que contenga datos que deban conservarse sin revisar previamente el script.
- Pool JDBC en GlassFish: `EmprendeLinkPool`.
- Recurso JNDI habilitado: `jdbc/EmprendeLinkDS`.
- Clase del DataSource: `com.mysql.cj.jdbc.MysqlDataSource`; el controlador MySQL Connector/J debe ser accesible para el dominio GlassFish (por ejemplo, en `domains/domain1/lib`, seguido de un reinicio cuando se incorpora).
- Usuario de base de datos con permisos sobre `emprendelink_dwf`. **No almacenar contraseñas ni tokens en Git.**

Para comprobar la conexión, abrir **Resources → JDBC → JDBC Connection Pools → EmprendeLinkPool → Ping** y confirmar `Ping Succeeded` antes de desplegar el WAR.

### Construcción y despliegue

Desde la raíz del repositorio:

```powershell
mvn clean test
mvn clean package
```

El artefacto esperado es `target/emprendelink.war`. Desplegarlo desde **GlassFish → Applications → Deploy** con *Context Root* `emprendelink`. La construcción final debe volver a ejecutarse y documentarse antes de fusionar o presentar la entrega; el resultado de compilaciones anteriores no se asume automáticamente para esta rama.

Comprobación inicial:

```powershell
curl.exe -i http://localhost:8080/emprendelink/api/v1/health
```

Resultado observado el **14/11/2026**: `HTTP 200`, `application/json`, `status: UP`, `application: EmprendeLink`, `apiVersion: v1`.

### Endpoints utilizados en QA

| Método | Ruta relativa a `/emprendelink/api/v1` | Acceso |
|---|---|---|
| GET | `/health` | Público |
| GET | `/emprendimientos` y `/emprendimientos/{id}` | Público |
| GET | `/emprendimientos/{id}/publicaciones` | Público |
| GET | `/publicaciones` y `/publicaciones/{id}` | Público |
| POST | `/auth/login` | Público |
| GET | `/auth/me` | Sesión autenticada |
| POST | `/auth/logout` | Sesión autenticada + CSRF |
| GET | `/pedidos` y `/pedidos/{id}` | Identidad y permisos del usuario |
| POST | `/pedidos/checkout` | `ROLE_CLIENTE` + CSRF |
| PUT | `/pedidos/{id}/estado` | `ROLE_EMPRENDEDOR` propietario + CSRF |

La API se consume desde el prefijo `http://localhost:8080/emprendelink/api/v1`. Las operaciones de escritura autenticadas requieren **cookie de sesión** y encabezado `X-CSRF-Token`. El token se entrega en la respuesta de login y en `/auth/me`. Las pruebas se ejecutaron desde PowerShell con `curl.exe` o `Invoke-WebRequest`, manteniendo la sesión mediante `-SessionVariable` y `-WebSession`.

Ejemplos sin credenciales:

```powershell
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/emprendimientos
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/publicaciones
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/auth/me
```

El último ejemplo, sin cookie válida, debe devolver `401 Unauthorized` con cuerpo JSON.

### Resultados reproducibles de QA (14/11/2026)

En la matriz se definieron **25 casos de Fase 3**: **19 aprobados, 3 fallidos, 1 pendiente de verificación integral y 2 bloqueados**. Se ejecutaron **23 casos**, de los cuales uno (`CP-223`) solo confirmó el código HTTP, no el formato completo del error. Los dos bloqueados no cuentan como ejecutados.

Comportamientos verificados:

- Listados públicos y consultas individuales con `200` y JSON.
- Login REST válido (`200`), sesión autenticada (`200`), rechazo sin sesión (`401`) y cierre efectivo de sesión (`204`; consulta posterior `401`).
- Rechazo de checkout sin CSRF (`403`) y acceso denegado a cambio de estado desde `ROLE_CLIENTE` (`403`).
- Checkout autorizado (`204`): se observó el **pedido ID 5** por **2.50** y el cambio del stock de la publicación 1 de **40 a 39** mediante consultas HTTP posteriores.
- Checkout con cantidad superior al stock (`409`): no se observó otro pedido ni disminución adicional del inventario.

**Hallazgos abiertos:**

- `DEF-001`: solicitudes a emprendimiento/publicación inexistentes devolvieron `404` con **HTML (`text/html`)**, aun solicitando JSON; el contrato REST especifica error JSON.
- `DEF-002`: `GET /publicaciones?texto=cafe` devolvió la lista completa sin aplicar el filtro `texto` esperado.

**Cobertura no concluida:** `CP-223` (tipo y cuerpo de error `404` para pedido), `CP-216` y `CP-217` (transiciones con rol emprendedor autorizado); cuerpo JSON del `409`; acceso a pedidos ajenos; compra multi-negocio; verificación directa de SQL; demostración extremo a extremo del cliente consumidor; y ejecución de la integración continua sobre la revisión final.

Consultar la evidencia detallada y el estado individual de cada caso en:

- [`docs/MATRIZ-PRUEBAS.md`](docs/MATRIZ-PRUEBAS.md), sección **14**.
- [`docs/EVIDENCIAS-FASE3/`](docs/EVIDENCIAS-FASE3/), con capturas y registros.
- [`docs/RELEASE-NOTES.md`](docs/RELEASE-NOTES.md), notas del Sprint III.

**Nota:** La aplicación cliente consumidora forma parte del alcance del Sprint III, pero en esta ronda de QA **no se documentó una verificación completa de su interfaz ni de su recorrido extremo a extremo**; no debe presentarse como prueba aprobada sin capturas adicionales.

---

## Documentación

```text
docs/API.md
docs/ARQUITECTURA.md
docs/CIERRE-FASE2.md
docs/CONVENCIONES.md
docs/MATRIZ-PRUEBAS.md
docs/MODELO-DATOS.md
docs/MODELO-DOMINIO.md
docs/PRODUCT-BACKLOG.md
docs/RELEASE-NOTES.md
docs/ROLES-PERMISOS.md
docs/RUTAS.md
docs/VERSIONS.md
```