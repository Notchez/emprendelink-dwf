# Baseline tecnológica — EmprendeLink DWF

Este documento define las versiones oficiales de tecnologías y herramientas utilizadas por el equipo para el desarrollo de EmprendeLink en la materia DWF.

Todos los integrantes deberán utilizar esta baseline para mantener compatibilidad entre los entornos de desarrollo.

## Java

- Distribución: Eclipse Temurin
- JDK: 25.0.4.1+1
- Rama: Java 25 LTS

## IDE

- IntelliJ IDEA
- Versión base: 2026.2.2

Los archivos locales de configuración de IntelliJ no deberán subirse al repositorio.

## Gestor de dependencias

- Apache Maven
- Versión: 3.9.16

No se utilizarán versiones Release Candidate o Preview como baseline del proyecto.

## Plataforma empresarial

- Jakarta EE 11

El proyecto utilizará exclusivamente APIs bajo el namespace:

`jakarta.*`

No se utilizarán APIs antiguas bajo:

`javax.*`

salvo que exista un requerimiento explícito del docente.

## Servidor de aplicaciones

- Eclipse GlassFish 8.0.4
- Jakarta EE Platform 11
- Distribución: Full Platform

## Jakarta APIs

Versiones correspondientes a Jakarta EE 11:

- Jakarta Servlet: 6.1
- Jakarta Pages: 4.0
- Jakarta Standard Tag Library: 3.0
- Jakarta Faces: 4.1
- Jakarta Persistence: 3.2
- Jakarta RESTful Web Services: 4.0
- Jakarta CDI: 4.1
- Jakarta Validation: 3.1
- Jakarta Security: 4.0
- Jakarta JSON Processing: 2.1
- Jakarta JSON Binding: 3.0

## Base de datos

- MySQL
- Rama: 8.4 LTS

La versión exacta instalada por el equipo deberá pertenecer a la rama 8.4 LTS.

Base de datos oficial del proyecto:

`emprendelink_dwf`

## JDBC

Se utilizará MySQL Connector/J compatible con MySQL 8.4 LTS.

La versión concreta será fijada en `pom.xml` al crear el proyecto Maven.

## Persistencia

- Jakarta Persistence: 3.2
- Hibernate ORM: 7.4.7.Final

Hibernate será utilizado cuando corresponda según la fase del proyecto.

## JSF

- Jakarta Faces: 4.1

La implementación incluida en GlassFish será utilizada como proveedor principal.

## PrimeFaces

Si el proyecto requiere PrimeFaces durante la integración JSF, su versión será fijada posteriormente en `pom.xml`.

No deberá agregarse individualmente sin coordinación.

## REST

La API utilizará:

- Jakarta RESTful Web Services 4.0
- JSON como formato principal de intercambio
- Ruta base: `/api/v1`

## Spring

Baseline prevista para la Fase 4:

- Spring Framework: 7.0.x
- Spring Security: 7.0.x

La versión de parche exacta será fijada al iniciar la Fase 4 para utilizar una release estable y compatible con el resto del proyecto.

## Codificación

- UTF-8

## Reglas de actualización

Las versiones no deberán modificarse individualmente.

Si una dependencia necesita actualizarse:

1. El cambio deberá realizarse en una rama independiente.
2. Se deberá verificar que el proyecto compile.
3. Se deberán ejecutar las pruebas correspondientes.
4. El cambio deberá revisarse antes de integrarlo a `develop`.
5. La nueva versión deberá actualizarse también en este documento.

## Fuente de verdad

Las versiones de librerías utilizadas realmente por el proyecto serán definidas en `pom.xml`.

Este documento funciona como referencia humana de la baseline tecnológica acordada por el equipo.