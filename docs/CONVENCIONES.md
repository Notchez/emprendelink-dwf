# Convenciones — EmprendeLink DWF

## Java

Clases:

```text
PascalCase
```

Métodos y variables:

```text
camelCase
```

Constantes:

```text
MAYUSCULAS_CON_GUION_BAJO
```

## Paquete raíz

```text
emprendelink
```

No crear nuevamente paquetes bajo `sv.edu.udb`.

El `groupId` de Maven puede seguir siendo:

```text
sv.edu.udb
```

## Capas

### Dominio

```text
emprendelink.model
emprendelink.model.enums
```

### DAO

```text
emprendelink.dao
emprendelink.dao.jpa
```

Implementación:

```text
Jpa<Entidad>DAO
```

No crear `Jdbc<Entidad>DAO`.

### Persistencia JPA

```text
emprendelink.persistence.jpa.entity
emprendelink.persistence.jpa.mapper
```

Entidades:

```text
<Entidad>Entity
```

Mappers:

```text
<Entidad>Mapper
```

### Services

```text
emprendelink.service
emprendelink.service.impl
```

Interfaces:

```text
<Entidad>Service
```

Implementaciones:

```text
<Entidad>ServiceImpl
```

### JSF

```text
emprendelink.web.jsf
```

Beans:

```text
<Modulo>Bean
```

Validators:

```text
emprendelink.web.jsf.validator
```

Converters:

```text
emprendelink.web.jsf.converter
```

### REST

```text
emprendelink.web.rest
```

Se implementará en Fase 3.

## Vistas

Las vistas de Fase 2 utilizan XHTML.

No crear JSP nuevos.

## Persistencia

Usar:

```java
@PersistenceContext(unitName = "emprendelinkPU")
```

DataSource:

```text
jdbc/EmprendeLinkDS
```

No usar:

```text
DriverManager
RESOURCE_LOCAL
EntityTransaction manual
EntityManagerFactory manual
```

## CDI

Usar `@Inject` para dependencias administradas.

No registrar Services en `ServletContext`.

## Transacciones

Las operaciones transaccionales se controlan en la capa Service mediante JTA.

## Base de datos

Tablas y columnas:

```text
snake_case
```

Java:

```text
camelCase
```

## Roles

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

## Estados de pedido

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

## Commits

Prefijos recomendados:

```text
feat:
fix:
refactor:
test:
docs:
chore:
```

## Seguridad

- No subir secretos.
- No guardar contraseñas en texto plano.
- No exponer `contrasenaHash`.
- Validar permisos en backend.
- Validar propiedad de recursos cuando corresponda.

## Antes de integrar

```powershell
mvn clean test
mvn clean package
```
