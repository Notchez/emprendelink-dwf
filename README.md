# EmprendeLink — DWF

Proyecto de cátedra para **DWF — Desarrollo de Aplicaciones con Web Frameworks**.

## Estado actual

La aplicación está en **Fase 2 — Persistencia Empresarial e Integración JSF**.

Arquitectura activa:

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

## Stack actual

- Java 25
- Maven
- Jakarta EE 11
- Jakarta Faces 4.1
- Jakarta CDI 4.1
- Jakarta Persistence 3.2
- Jakarta Transactions
- EclipseLink provisto por GlassFish
- GlassFish 8.0.4
- MySQL 8.x
- MySQL Connector/J 26.7.0
- JUnit 5.13.4

## Módulos

### Implementados

- autenticación;
- registro de usuarios;
- roles;
- administración de usuarios;
- activación y desactivación de usuarios;
- validaciones JSF;
- AJAX;
- persistencia JPA de `Rol` y `Usuario`;
- seguridad por sesión y rol.

### Pendientes de Fase 2

- categorías;
- emprendimientos;
- publicaciones;
- catálogo;
- pedidos y detalle de pedidos;
- relaciones JPA restantes;
- pruebas de integración de los módulos restantes.

## Estructura

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
├── css/
├── error/
├── WEB-INF/
│   ├── beans.xml
│   └── web.xml
├── inicio.xhtml
├── login.xhtml
└── registro.xhtml
```

## Base de datos

Base oficial:

```text
emprendelink_dwf
```

Scripts:

```text
database/schema.sql
database/seed.sql
```

Usuarios de prueba del `seed.sql` utilizan:

```text
Demo1234!
```

## GlassFish

Pool:

```text
EmprendeLinkPool
```

Recurso JNDI:

```text
jdbc/EmprendeLinkDS
```

Verificación:

```powershell
& "C:\glassfish-8.0.4\glassfish8\bin\asadmin.bat" ping-connection-pool EmprendeLinkPool
```

## Build

```powershell
mvn clean test
mvn clean package
```

WAR:

```text
target/emprendelink.war
```

## Deploy local

```powershell
& "C:\glassfish-8.0.4\glassfish8\bin\asadmin.bat" deploy `
--contextroot emprendelink `
".\target\emprendelink.war"
```

Aplicación:

```text
http://localhost:8080/emprendelink/login.xhtml
```

## Git

- `main`: estable.
- `develop`: integración.
- `feature/*`: trabajo por módulo.

Toda funcionalidad se integra mediante Pull Request hacia `develop`.

## Reglas técnicas

- No agregar nuevas implementaciones JDBC.
- No crear nuevos Servlets o JSP para Fase 2.
- No agregar Hibernate al WAR.
- La persistencia se implementa con JPA administrado por GlassFish.
- Las transacciones se manejan mediante JTA.
- La inyección se realiza con CDI.
- JSF consume Services, no DAO directamente.
- REST de Fase 3 reutilizará los mismos Services.
- No subir secretos ni configuración local sensible.

## Documentación

- `docs/ARQUITECTURA.md`
- `docs/CONVENCIONES.md`
- `docs/MATRIZ-PRUEBAS.md`
- `docs/PRODUCT-BACKLOG.md`
- `docs/ROLES-PERMISOS.md`
- `docs/RUTAS.md`
- `docs/VERSIONS.md`
- `docs/API.md`
