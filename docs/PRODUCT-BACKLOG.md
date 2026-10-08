# Product Backlog — EmprendeLink DWF

Estados:

```text
HECHO
EN_CURSO
PENDIENTE
```

# Fase 2 — Persistencia Empresarial e Integración JSF

## Infraestructura JPA

### HU-F2-01 — Configurar JPA administrado

Estado:

```text
HECHO
```

Incluye:

- Persistence Unit `emprendelinkPU`;
- JTA;
- DataSource `jdbc/EmprendeLinkDS`;
- EclipseLink;
- `@PersistenceContext`.

### HU-F2-02 — Usar CDI

Estado:

```text
HECHO
```

## Auth y Usuarios

### HU-F2-03 — Login JSF

```text
HECHO
```

### HU-F2-04 — Registro Cliente/Emprendedor

```text
HECHO
```

### HU-F2-05 — Administración de usuarios

```text
HECHO
```

### HU-F2-06 — Validación y AJAX de usuarios

```text
HECHO
```

## Categorías

### HU-F2-10 — Persistencia JPA de categorías

```text
PENDIENTE
```

### HU-F2-11 — Gestión JSF de categorías

```text
PENDIENTE
```

Incluye listar, crear, editar, activar/desactivar, validar duplicados y AJAX.

## Emprendimientos

### HU-F2-20 — Persistencia JPA de emprendimientos

```text
PENDIENTE
```

### HU-F2-21 — Gestión JSF de emprendimientos propios

```text
PENDIENTE
```

Debe validar propiedad del recurso.

## Publicaciones

### HU-F2-30 — Persistencia JPA de publicaciones

```text
PENDIENTE
```

### HU-F2-31 — Gestión JSF de publicaciones

```text
PENDIENTE
```

### HU-F2-32 — Catálogo JSF

```text
PENDIENTE
```

Incluye búsqueda, filtros y AJAX.

## Pedidos

### HU-F2-40 — Persistencia JPA de pedidos y detalles

```text
PENDIENTE
```

### HU-F2-41 — Crear pedido transaccionalmente

```text
PENDIENTE
```

Pedido y detalles deben guardarse de forma atómica.

### HU-F2-42 — Gestión de estados

```text
PENDIENTE
```

Transiciones oficiales:

```text
PENDIENTE → CONFIRMADO
PENDIENTE → CANCELADO
CONFIRMADO → EN_PROCESO
CONFIRMADO → CANCELADO
EN_PROCESO → COMPLETADO
EN_PROCESO → CANCELADO
```

`COMPLETADO` y `CANCELADO` son terminales.

## Calidad

### HU-F2-50 — Pruebas automatizadas de Auth/Usuarios

```text
HECHO
```

### HU-F2-51 — CI en GitHub

```text
EN_CURSO
```

Se considera HECHO cuando el workflow pase en GitHub.

### HU-F2-52 — Pruebas de persistencia y relaciones restantes

```text
PENDIENTE
```

# Fase 3 — Servicios REST

Pendiente:

- contrato `/api/v1`;
- endpoints;
- JSON;
- DTO;
- seguridad;
- cliente consumidor;
- documentación de API.

REST debe reutilizar Services existentes.

# Fase 4 — Spring, calidad y liberación

Pendiente:

- seleccionar módulos Spring pertinentes;
- seguridad final;
- pruebas de integración/regresión/aceptación;
- manejo centralizado de errores;
- despliegue accesible;
- manuales y Release Notes finales.
