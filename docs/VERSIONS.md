# Baseline tecnológica — EmprendeLink DWF

> **Sprint IV:** Spring Framework 7.0.11 (`spring-context`) añadido a `pom.xml`; Jakarta EE 11, Java 25, GlassFish, EclipseLink/JTA y MySQL se conservan. No se agrega Spring Boot, Spring Security ni Hibernate. CI ejecuta `mvn -B clean verify`.

## Java

- Java: 25
- Distribución recomendada: Eclipse Temurin

## Maven

- Apache Maven: 3.9.x
- `maven-compiler-plugin`: 3.16.0
- `maven-war-plugin`: 3.5.1
- `maven-surefire-plugin`: 3.5.4

## Jakarta EE

- Jakarta EE: 11
- Jakarta Servlet: 6.1
- Jakarta Faces: 4.1
- Jakarta Persistence: 3.2
- Jakarta CDI: 4.1
- Jakarta Validation: 3.1
- Jakarta Transactions: incluido en Jakarta EE 11

Namespace oficial:

```text
jakarta.*
```

## Servidor

- Eclipse GlassFish: 8.0.4

## Persistencia

- JPA: Jakarta Persistence 3.2
- Proveedor: EclipseLink incluido en GlassFish
- Transacciones: JTA administrado por GlassFish
- Persistence Unit: `emprendelinkPU`
- DataSource: `jdbc/EmprendeLinkDS`

Hibernate no forma parte de la configuración actual.

## Base de datos

- MySQL 8.x
- Base: `emprendelink_dwf`
- MySQL Connector/J: 26.7.0

## JSF

- Jakarta Faces 4.1
- Implementación provista por GlassFish

## Tests

- JUnit Jupiter: 5.13.4

## REST — Fase 3

Previsto:

- Jakarta RESTful Web Services;
- JSON;
- `/api/v1`.

## Spring — Fase 4

Spring Framework 7.0.11 (`spring-context`), administrado por el contexto de la aplicación en el WAR. No se incorpora Spring Boot ni se sustituye el proveedor JPA.

## Codificación

```text
UTF-8
```

## Fuente de verdad

Dependencias:

```text
pom.xml
```

Configuración JPA:

```text
src/main/resources/META-INF/persistence.xml
```
