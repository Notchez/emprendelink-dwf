# Release Notes — EmprendeLink DWF

# Fase 1 — Sprint I

## Objetivo

Construir el primer incremento funcional utilizando Java Web y arquitectura MVC.

Tecnologías utilizadas durante esa fase:

```text
Java
Servlets
JSP/JSTL
JDBC
MySQL
```

La Fase 1 permitió establecer:

- modelo inicial;
- contratos DAO;
- Services;
- autenticación inicial;
- estructura MVC;
- conexión con base de datos;
- repositorio y flujo de trabajo.

---

# Fase 2 — Sprint II

## Objetivo

Evolucionar EmprendeLink hacia una aplicación empresarial basada en componentes JSF y persistencia JPA administrada.

Stack principal:

```text
Java 25
Jakarta EE 11
Jakarta Faces 4.1
Jakarta CDI 4.1
Jakarta Persistence 3.2
Jakarta Transactions
EclipseLink
GlassFish 8.0.4
MySQL 8.x
```

---

## Arquitectura

Arquitectura activa:

```text
XHTML / JSF
      ↓
Managed Beans
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

Se retiraron del runtime las implementaciones paralelas utilizadas en Fase 1:

```text
DAO JDBC
Servlets de negocio
JSP
EntityManagerFactory manual
```

---

## Persistencia

Se implementó persistencia mediante JPA.

Persistence Unit:

```text
emprendelinkPU
```

Proveedor:

```text
EclipseLink
```

DataSource:

```text
jdbc/EmprendeLinkDS
```

Transacciones:

```text
JTA
```

Entidades:

```text
RolEntity
UsuarioEntity
CategoriaEntity
PlanEntity
EmprendimientoEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

---

## Autenticación y usuarios

Implementado:

- login;
- logout;
- registro;
- roles;
- sesiones;
- administración de usuarios;
- activación/desactivación;
- validación de correo;
- validación de contraseña;
- almacenamiento mediante hash;
- protección contra registro público como administrador.

Roles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

---

## Categorías

Implementado:

- persistencia JPA;
- creación;
- listado;
- edición;
- activación/desactivación;
- AJAX;
- validación de nombre.

---

## Emprendimientos

Implementado:

- persistencia JPA;
- relación con propietario;
- relación con plan;
- creación;
- edición;
- listado por propietario;
- validación de propiedad;
- gestión administrativa de estado;
- vista administrativa de detalle.

---

## Publicaciones

Implementado:

- productos;
- servicios;
- persistencia JPA;
- relación con emprendimiento;
- relación con categoría;
- creación;
- edición;
- activación/desactivación;
- precio;
- stock;
- validación de propiedad;
- validación de categoría activa;
- validación de emprendimiento activo.

---

## Planes

Se agregó el modelo de planes.

Planes oficiales:

```text
Básico    → 10 publicaciones  → 7%
Estándar  → 30 publicaciones  → 5%
Premium   → 60 publicaciones  → 3%
Max       → 100 publicaciones → 1%
```

Implementado:

- `Plan`;
- `PlanEntity`;
- `PlanMapper`;
- `PlanDAO`;
- `JpaPlanDAO`;
- `PlanService`;
- administración JSF;
- asignación de plan a negocios;
- activación/desactivación;
- validación de planes en uso.

---

## Límite de publicaciones

La cantidad máxima de publicaciones activas depende del plan.

Se valida al:

```text
crear publicación
reactivar publicación
asignar plan al negocio
```

No se permite superar:

```text
Plan.maxProductosActivos
```

---

## Catálogo

Implementado:

- listado de publicaciones activas;
- búsqueda;
- filtros;
- filtro por categoría;
- filtro por tipo;
- vista de detalle;
- operaciones AJAX.

El refinamiento visual del catálogo puede continuar posteriormente sin modificar la arquitectura del Sprint II.

---

## Pedidos

Implementado:

- creación de pedidos;
- detalle;
- cálculo de total;
- validación de publicaciones;
- validación de emprendimiento;
- control de stock;
- descuento de inventario;
- restauración al cancelar;
- consulta por cliente;
- pedidos recibidos por emprendedor;
- detalle de pedido;
- control de propiedad.

---

## Estados de pedido

Estados:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Transiciones:

```text
PENDIENTE → CONFIRMADO
PENDIENTE → CANCELADO

CONFIRMADO → EN_PROCESO
CONFIRMADO → CANCELADO

EN_PROCESO → COMPLETADO
EN_PROCESO → CANCELADO
```

---

## Comisiones

Al completar una venta se guarda:

```text
fechaCompletado
tasaComision
montoComision
```

Cálculo:

```text
montoComision = total × tasaComision
```

La tasa se almacena directamente en el pedido para conservar la información histórica.

---

## Administración financiera

Se implementó:

- gestión de planes;
- asignación de plan;
- consulta de ventas completadas;
- reporte de comisiones;
- filtro por negocio;
- filtro por mes;
- filtro por año;
- total de ventas;
- total de comisiones.

---

## Seguridad

Filtros implementados:

```text
AutenticacionFilter
AdminFilter
EmprendedorFilter
PedidosFilter
PublicacionFilter
```

Además de los filtros, los Services validan:

- roles;
- propiedad;
- estado;
- disponibilidad;
- relaciones.

---

## AJAX y validaciones

La interfaz utiliza:

```xml
<f:ajax>
```

en diferentes operaciones.

Validadores personalizados activos:

```text
CorreoValidator
ContrasenaValidator
```

Converter activo:

```text
TipoRolConverter
```

Las entidades utilizan además Bean Validation.

---

## Base de datos

Scripts:

```text
database/schema.sql
database/seed.sql
database/migrations/002_planes_comisiones.sql
database/migrations/003_fecha_completado.sql
```

La estructura incorpora:

- planes;
- relación plan-emprendimiento;
- comisión histórica;
- fecha real de completado.

---

## Pruebas

Regresión automatizada actual:

```text
41 pruebas
0 failures
0 errors
0 skipped
```

Resultado:

```text
BUILD SUCCESS
```

Comandos:

```bash
mvn clean test
mvn clean package
```

El WAR se genera correctamente:

```text
target/emprendelink.war
```

---

## Integración continua

GitHub Actions ejecuta:

```text
mvn -B clean test
```

El Pull Request de integración completa de Fase 2 pasó correctamente el pipeline.

---

## Documentación

Documentación consolidada:

```text
README.md
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

Se retiró documentación histórica individual que ya no representaba la arquitectura activa.

---

# Pendiente de cierre del Sprint II

Antes de la entrega final:

```text
ejecutar smoke test manual
cerrar MATRIZ-PRUEBAS.md
capturar evidencias
generar PDF de Sprint Review
```

---

# Próximo incremento — Fase 3

La Fase 3 incorporará servicios REST.

Arquitectura prevista:

```text
REST
 ↓
Services existentes
 ↓
DAO
 ↓
JPA
 ↓
MySQL
```

Pendiente:

- JAX-RS;
- endpoints;
- JSON;
- DTO;
- códigos HTTP;
- manejo de errores;
- seguridad REST;
- cliente consumidor;
- pruebas de API;
- documentación de endpoints.