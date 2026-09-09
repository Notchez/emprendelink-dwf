# EmprendeLink — DWF

Proyecto de cátedra para la materia **Desarrollo de Aplicaciones con Web Frameworks (DWF)**.

## Descripción

**EmprendeLink** es una plataforma digital orientada a emprendedores que permite registrar y administrar emprendimientos, publicar productos o servicios y facilitar su consulta por parte de posibles clientes.

El proyecto será desarrollado de manera incremental durante el ciclo académico, incorporando progresivamente las tecnologías requeridas por la materia DWF.

## Objetivo del proyecto

Construir una solución web empresarial en Java que permita gestionar emprendimientos, publicaciones, usuarios y pedidos, aplicando una arquitectura organizada, persistencia de datos, servicios REST, seguridad y buenas prácticas de desarrollo.

## Evolución del proyecto

El mismo sistema será ampliado durante las cuatro fases de la materia:

### Fase 1 — Fundamentos Java Web y Arquitectura MVC

- Java Web
- Arquitectura MVC
- JDBC
- DAO
- Servlets
- JSP
- JSTL
- MySQL

### Fase 2 — Persistencia Empresarial e Integración JSF

- JPA / Hibernate
- JSF
- Managed Beans
- AJAX
- Validadores
- Convertidores

### Fase 3 — Servicios REST e Integración de Clientes

- API REST
- JSON
- Endpoints HTTP
- Cliente consumidor externo
- Documentación y pruebas de API

### Fase 4 — Spring, Seguridad y Liberación

- Spring
- Inyección de dependencias
- Seguridad por roles
- Manejo centralizado de errores
- Pruebas
- Documentación
- Despliegue

## Arquitectura

El proyecto seguirá una separación por responsabilidades:

- Modelo de dominio
- Acceso a datos
- Persistencia
- Servicios
- Controladores
- Vistas
- API REST
- Seguridad
- Configuración

La arquitectura está diseñada para permitir que nuevas tecnologías sean incorporadas durante las diferentes fases sin reconstruir el núcleo del sistema.

## Entidades principales

El dominio inicial de EmprendeLink estará compuesto por:

- Rol
- Usuario
- Emprendimiento
- Categoría
- Publicación
- Pedido
- Detalle de pedido

Una publicación podrá representar tanto un **producto** como un **servicio**.

## Roles del sistema

- Administrador
- Emprendedor
- Cliente

Los identificadores internos oficiales serán:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

## Repositorio

Repositorio oficial:

`Notchez/emprendelink-dwf`

### Ramas principales

- `main`: versión estable del proyecto.
- `develop`: integración del trabajo realizado durante el desarrollo.

### Ramas de trabajo

Cada nueva funcionalidad deberá desarrollarse utilizando ramas independientes.

Ejemplos:

- `feature/usuarios`
- `feature/emprendimientos`
- `feature/publicaciones`
- `feature/pedidos`

Las funcionalidades terminadas deberán integrarse mediante Pull Request hacia `develop`.

## Convenciones

Las convenciones oficiales de nombres, paquetes, base de datos, rutas y código se encuentran en:

`docs/CONVENCIONES.md`

Todos los integrantes deberán respetar estas convenciones para evitar incompatibilidades entre módulos.

## Versiones

La baseline tecnológica oficial utilizada por el equipo estará documentada en:

`docs/VERSIONS.md`

Las versiones no deberán actualizarse individualmente sin verificar primero la compatibilidad con el resto del proyecto.

## Base de datos

Nombre oficial:

`emprendelink_dwf`

Los scripts se encuentran en:

```text
database/
├── schema.sql
└── seed.sql