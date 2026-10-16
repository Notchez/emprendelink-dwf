# EmprendeLink — DWF

Proyecto de cátedra para **DWF — Desarrollo de Aplicaciones con Web Frameworks**.

## Estado actual

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

## Fase 3

Existen paquetes y clases preparatorias:

```text
emprendelink.dto
emprendelink.web.rest
```

pero las clases REST y DTO actuales son únicamente scaffolding.

La API REST funcional **no forma parte todavía del incremento cerrado de Fase 2**.

El siguiente incremento deberá implementar formalmente:

```text
JAX-RS
JSON
endpoints
códigos HTTP
DTO reales
manejo de errores
seguridad REST
cliente consumidor externo
pruebas de API
documentación de consumo
```

La API deberá reutilizar los Services existentes para evitar duplicar reglas de negocio.

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