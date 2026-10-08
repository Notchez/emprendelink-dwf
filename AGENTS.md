# AGENTS.md — EmprendeLink DWF

Reglas técnicas obligatorias para desarrolladores y asistentes de IA.

## Arquitectura vigente

Fase actual: **Fase 2 — JPA + JSF**.

```text
JSF / XHTML
    ↓
Managed Bean CDI
    ↓
Service
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

## Reglas obligatorias

1. No crear nuevas implementaciones JDBC.
2. No crear Servlets para funcionalidades de Fase 2.
3. No crear vistas JSP.
4. No agregar Hibernate como dependencia.
5. No usar `DriverManager`.
6. No crear `EntityManagerFactory` manualmente.
7. No usar `RESOURCE_LOCAL`.
8. No registrar Services manualmente en `ServletContext`.
9. Usar CDI para inyección de dependencias.
10. Usar `@PersistenceContext` para `EntityManager`.
11. Usar JTA para transacciones.
12. Los Managed Beans llaman Services, nunca DAO directamente.
13. Los Services contienen reglas de negocio.
14. Los DAO contienen acceso a persistencia.
15. Las entidades JPA viven en `persistence/jpa/entity`.
16. Los modelos de dominio viven en `model`.
17. Los mappers convierten entre dominio y entidad.
18. No exponer hashes de contraseña en vistas, sesión, DTO o API.
19. No permitir registro público con `ROLE_ADMIN`.
20. No introducir secretos al repositorio.

## Persistencia

Persistence Unit:

```text
emprendelinkPU
```

DataSource:

```text
jdbc/EmprendeLinkDS
```

Proveedor:

```text
EclipseLink provisto por GlassFish
```

Toda entidad nueva debe agregarse a `persistence.xml` mientras exista listado explícito de entidades.

## Paquetes

```text
emprendelink.dao
emprendelink.dao.jpa
emprendelink.dto
emprendelink.exception
emprendelink.model
emprendelink.model.enums
emprendelink.persistence.jpa.entity
emprendelink.persistence.jpa.mapper
emprendelink.security
emprendelink.service
emprendelink.service.impl
emprendelink.web.jsf
emprendelink.web.jsf.converter
emprendelink.web.jsf.validator
emprendelink.web.rest
emprendelink.spring
```

## Fase 2

Cada módulo se entrega verticalmente:

```text
Entity
↓
Mapper
↓
JpaDAO
↓
Service
↓
Bean
↓
XHTML
↓
AJAX / validaciones
↓
tests
```

Módulos:

- Auth / Usuarios: implementado.
- Categorías: pendiente.
- Emprendimientos: pendiente.
- Publicaciones / catálogo: pendiente.
- Pedidos / detalle: pendiente.

## Seguridad

Sesión oficial:

```text
usuarioAutenticado
```

Roles oficiales:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

La autorización debe comprobarse en backend.

## Fase 3

La API REST utilizará `/api/v1` y reutilizará la misma capa Service.

No duplicar lógica de negocio para REST.

## Fase 4

Spring se integrará cuando corresponda a Fase 4.

No introducir Spring antes de definir qué problema resolverá.

## Build obligatorio

```powershell
mvn clean test
mvn clean package
```

No integrar una rama con tests fallidos o build roto.

## Git

- No trabajar directamente sobre `main`.
- No trabajar directamente sobre `develop`.
- Utilizar `feature/*`.
- Integrar mediante Pull Request.
- Mantener commits descriptivos.
- Actualizar documentación cuando cambie arquitectura, rutas o reglas.
