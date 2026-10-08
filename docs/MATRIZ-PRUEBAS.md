# Matriz de pruebas — EmprendeLink DWF

Estados:

```text
PENDIENTE
APROBADA
FALLIDA
BLOQUEADA
```

## Auth / Usuarios

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-001 | Registro válido de cliente | Unit/Manual | APROBADA |
| CP-002 | Correo normalizado | Unit | APROBADA |
| CP-003 | Contraseña almacenada como hash | Unit | APROBADA |
| CP-004 | Registro público como ADMIN rechazado | Unit | APROBADA |
| CP-005 | Correo duplicado rechazado | Unit | APROBADA |
| CP-006 | Correo inválido rechazado | Unit | APROBADA |
| CP-007 | Longitudes máximas validadas | Unit | APROBADA |
| CP-008 | Login válido | Unit/Manual | APROBADA |
| CP-009 | Usuario inactivo no autentica | Unit | APROBADA |
| CP-010 | Hash no queda en usuario autenticado | Unit | APROBADA |
| CP-011 | Admin accede a usuarios | Manual | APROBADA |
| CP-012 | Cliente autenticado accede a inicio | Manual | APROBADA |
| CP-013 | Emprendedor autenticado accede a inicio | Manual | APROBADA |
| CP-014 | Usuario creado desde registro puede iniciar sesión | Manual | APROBADA |

## Mappers

| ID | Caso | Estado |
|---|---|---|
| CP-016 | Usuario dominio → entidad | APROBADA |
| CP-017 | Usuario entidad → dominio | APROBADA |

## Infraestructura

| ID | Caso | Estado |
|---|---|---|
| CP-018 | `mvn clean test` | APROBADA |
| CP-019 | `mvn clean package` | APROBADA |
| CP-020 | DataSource responde ping | APROBADA |
| CP-021 | WAR despliega en GlassFish | APROBADA |
| CP-022 | Persistence Unit JTA inicia con EclipseLink | APROBADA |

## Categorías

| ID | Caso | Estado |
|---|---|---|
| CP-030 | Crear categoría | PENDIENTE |
| CP-031 | Listar categorías | PENDIENTE |
| CP-032 | Editar categoría | PENDIENTE |
| CP-033 | Activar/desactivar categoría | PENDIENTE |
| CP-034 | Nombre duplicado rechazado | PENDIENTE |
| CP-035 | AJAX en gestión de categorías | PENDIENTE |

## Emprendimientos

| ID | Caso | Estado |
|---|---|---|
| CP-040 | Crear emprendimiento propio | PENDIENTE |
| CP-041 | Listar emprendimientos propios | PENDIENTE |
| CP-042 | Editar emprendimiento propio | PENDIENTE |
| CP-043 | Editar emprendimiento ajeno rechazado | PENDIENTE |

## Publicaciones / Catálogo

| ID | Caso | Estado |
|---|---|---|
| CP-050 | Crear publicación | PENDIENTE |
| CP-051 | Validar categoría y emprendimiento | PENDIENTE |
| CP-052 | Precio inválido rechazado | PENDIENTE |
| CP-053 | Stock inválido rechazado | PENDIENTE |
| CP-054 | Listar catálogo | PENDIENTE |
| CP-055 | Filtrar catálogo con AJAX | PENDIENTE |

## Pedidos

| ID | Caso | Estado |
|---|---|---|
| CP-060 | Crear pedido y detalles en una transacción | PENDIENTE |
| CP-061 | Rollback si falla un detalle | PENDIENTE |
| CP-062 | Cliente consulta pedidos propios | PENDIENTE |
| CP-063 | Cliente no consulta pedido ajeno | PENDIENTE |
| CP-064 | Emprendedor consulta pedidos de sus emprendimientos | PENDIENTE |
| CP-065 | Emprendedor no modifica pedido ajeno | PENDIENTE |
| CP-066 | Transiciones de estado válidas | PENDIENTE |
| CP-067 | Transición inválida rechazada | PENDIENTE |

## Regresión obligatoria

Después de integrar cualquier módulo:

```text
mvn clean test
mvn clean package
```

Comprobar además:

- login;
- registro;
- sesión;
- permisos de administrador;
- persistencia JPA.

Una prueba solo se marca APROBADA cuando fue ejecutada realmente.
