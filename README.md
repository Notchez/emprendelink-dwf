# EmprendeLink — DWF

Proyecto de cátedra para **DWF — Desarrollo de Aplicaciones con Web Frameworks**.

## Estado actual

La aplicación completa el incremento correspondiente a:

```text
Fase 2 — Persistencia Empresarial e Integración JSF
Sprint II
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

## Módulos implementados

### Autenticación y usuarios

- inicio de sesión;
- cierre de sesión;
- registro;
- roles;
- administración de usuarios;
- activación y desactivación;
- validaciones JSF;
- seguridad por sesión y rol;
- almacenamiento seguro de contraseñas.

### Categorías

- creación;
- listado;
- edición;
- activación;
- desactivación;
- persistencia JPA;
- operaciones AJAX.

### Emprendimientos

- creación;
- listado por propietario;
- edición;
- activación/desactivación administrativa;
- validación de propiedad;
- persistencia JPA.

### Publicaciones

- productos;
- servicios;
- categorías;
- precio;
- stock;
- creación;
- edición;
- activación/desactivación;
- validación de propiedad;
- validación de plan;
- límite de publicaciones activas por plan.

### Catálogo

- publicaciones activas;
- búsqueda;
- filtros;
- detalle;
- navegación hacia pedidos.

### Pedidos

- creación;
- detalle;
- cálculo de total;
- validación de stock;
- descuento de inventario;
- restauración de inventario al cancelar;
- consulta por cliente;
- consulta por emprendedor;
- cambios de estado;
- persistencia de detalles relacionados.

### Planes

Planes disponibles:

```text
Básico    → máximo 10 publicaciones activas  → comisión 7%
Estándar  → máximo 30 publicaciones activas  → comisión 5%
Premium   → máximo 60 publicaciones activas  → comisión 3%
Max       → máximo 100 publicaciones activas → comisión 1%
```

La aplicación controla el límite de publicaciones activas tanto al crear como al reactivar publicaciones.

### Comisiones

Cuando un pedido pasa a:

```text
COMPLETADO
```

se conservan:

```text
fechaCompletado
tasaComision
montoComision
```

La comisión queda asociada al valor del plan vigente al momento de completar el pedido.

---

## Arquitectura

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
├── catalogo/
├── css/
├── emprendimientos/
├── error/
├── pedidos/
├── publicaciones/
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

Proveedor JPA:

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

Scripts:

```text
database/schema.sql
database/seed.sql
database/migrations/002_planes_comisiones.sql
database/migrations/003_fecha_completado.sql
```

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

Estado actual:

```text
41 pruebas automatizadas
0 failures
0 errors
BUILD SUCCESS
```

Artefacto:

```text
target/emprendelink.war
```

---

## Integración continua

GitHub Actions ejecuta:

```text
mvn -B clean test
```

sobre las ramas configuradas y Pull Requests hacia las ramas de integración.

El Pull Request de integración de Fase 2 completó correctamente el pipeline.

---

## Deploy local

Servidor:

```text
GlassFish 8.0.4
```

Comando:

```powershell
C:\glassfish-8.0.4\glassfish8\glassfish\bin\asadmin.bat deploy --force=true target\emprendelink.war
```

Aplicación:

```text
http://localhost:8080/emprendelink/login.xhtml
```

---

## Git

Ramas permanentes:

```text
main
develop
```

`develop` contiene el incremento integrado de Fase 2.

El trabajo se integra mediante Pull Request.

---

## Reglas técnicas

- no agregar nuevas implementaciones JDBC;
- no mantener Servlets/JSP paralelos a JSF;
- no agregar Hibernate al WAR;
- usar JPA administrado por GlassFish;
- usar JTA para transacciones;
- usar CDI para inyección;
- JSF consume Services;
- los Services consumen interfaces DAO;
- los DAO JPA gestionan persistencia;
- las reglas de negocio permanecen fuera de las vistas;
- Fase 3 deberá reutilizar los Services existentes;
- no subir secretos ni configuración local sensible.

---

## Documentación

```text
docs/ARQUITECTURA.md
docs/CIERRE-FASE2.md
docs/CONVENCIONES.md
docs/MATRIZ-PRUEBAS.md
docs/PRODUCT-BACKLOG.md
docs/RELEASE-NOTES.md
docs/ROLES-PERMISOS.md
docs/RUTAS.md
docs/VERSIONS.md
```

---

## Próximo incremento

Fase 3:

```text
Servicios REST e Integración de Clientes
```

La API reutilizará:

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