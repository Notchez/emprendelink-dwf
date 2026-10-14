# Product Backlog — EmprendeLink DWF

Estados:

```text
HECHO
EN_CURSO
PENDIENTE
```

# Fase 2 — Persistencia Empresarial e Integración JSF

## Infraestructura

### HU-F2-01 — Persistencia JPA administrada

```text
HECHO
```

Incluye:

- `emprendelinkPU`;
- JTA;
- EclipseLink;
- DataSource `jdbc/EmprendeLinkDS`;
- `@PersistenceContext`;
- GlassFish;
- MySQL.

### HU-F2-02 — CDI e inyección

```text
HECHO
```

Incluye:

- Managed Beans;
- Services;
- DAO;
- `@Inject`.

---

## Autenticación y usuarios

### HU-F2-03 — Login JSF

```text
HECHO
```

### HU-F2-04 — Registro

```text
HECHO
```

Roles públicos:

```text
ROLE_CLIENTE
ROLE_EMPRENDEDOR
```

El registro público no permite crear administradores.

### HU-F2-05 — Administración de usuarios

```text
HECHO
```

Incluye:

- listado;
- roles;
- activar/desactivar;
- evitar auto-desactivación administrativa.

### HU-F2-06 — Validaciones y AJAX

```text
HECHO
```

Incluye:

- correo;
- contraseña;
- mensajes;
- converter de rol;
- AJAX.

---

## Categorías

### HU-F2-10 — Persistencia JPA

```text
HECHO
```

### HU-F2-11 — Gestión JSF

```text
HECHO
```

Incluye:

- crear;
- listar;
- editar;
- activar/desactivar;
- AJAX.

---

## Emprendimientos

### HU-F2-20 — Persistencia JPA

```text
HECHO
```

Incluye:

- entidad;
- mapper;
- DAO;
- Service;
- relación con propietario;
- relación con plan.

### HU-F2-21 — Gestión del emprendedor

```text
HECHO
```

Incluye:

- crear;
- listar propios;
- editar;
- validar propiedad;
- AJAX.

### HU-F2-22 — Administración

```text
HECHO
```

Incluye:

- listar negocios;
- ver detalle;
- activar/desactivar;
- consultar ventas completadas;
- consultar pedidos;
- asignar plan.

---

## Publicaciones

### HU-F2-30 — Persistencia JPA

```text
HECHO
```

Relaciones:

```text
Publicacion → Emprendimiento
Publicacion → Categoria
```

### HU-F2-31 — Gestión JSF

```text
HECHO
```

Incluye:

- crear;
- editar;
- listar;
- activar/desactivar;
- precio;
- stock;
- propiedad;
- categoría;
- emprendimiento;
- AJAX.

### HU-F2-32 — Catálogo

```text
HECHO
```

Incluye:

- publicaciones activas;
- detalle;
- búsqueda;
- filtros;
- categorías;
- tipos;
- AJAX.

### HU-F2-33 — Límite de publicaciones por plan

```text
HECHO
```

La aplicación valida:

```text
crear publicación
reactivar publicación
asignar plan
```

contra:

```text
Plan.maxProductosActivos
```

Planes:

```text
Básico    → 10
Estándar  → 30
Premium   → 60
Max       → 100
```

---

## Pedidos

### HU-F2-40 — Persistencia JPA de pedidos

```text
HECHO
```

Incluye:

```text
PedidoEntity
DetallePedidoEntity
PedidoMapper
DetallePedidoMapper
JpaPedidoDAO
```

### HU-F2-41 — Crear pedido

```text
HECHO
```

Incluye:

- cliente;
- emprendimiento;
- publicaciones;
- cantidades;
- precios;
- total;
- detalles;
- stock.

### HU-F2-42 — Estados

```text
HECHO
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

### HU-F2-43 — Restauración de stock

```text
HECHO
```

### HU-F2-44 — Consulta por rol

```text
HECHO
```

Cliente:

- pedidos propios;
- detalle propio.

Emprendedor:

- pedidos recibidos;
- pedidos de negocios propios.

---

## Planes y comisiones

### HU-F2-45 — Persistencia de planes

```text
HECHO
```

Incluye:

```text
Plan
PlanEntity
PlanMapper
PlanDAO
JpaPlanDAO
PlanService
PlanServiceImpl
```

### HU-F2-46 — Gestión administrativa de planes

```text
HECHO
```

Planes definidos:

```text
Básico    → 10 publicaciones  → 7%
Estándar  → 30 publicaciones  → 5%
Premium   → 60 publicaciones  → 3%
Max       → 100 publicaciones → 1%
```

Incluye:

- crear;
- editar;
- activar/desactivar;
- asignar a negocios;
- impedir desactivar un plan en uso.

### HU-F2-47 — Comisión histórica por pedido

```text
HECHO
```

Al completar un pedido se almacenan:

```text
fechaCompletado
tasaComision
montoComision
```

### HU-F2-48 — Reporte administrativo de comisiones

```text
HECHO
```

Incluye:

- filtro por negocio;
- filtro por mes;
- filtro por año;
- ventas completadas;
- tasa;
- monto de comisión.

---

## Seguridad

### HU-F2-50 — Seguridad por rutas

```text
HECHO
```

Filtros:

```text
AutenticacionFilter
AdminFilter
EmprendedorFilter
PedidosFilter
PublicacionFilter
```

### HU-F2-51 — Propiedad de recursos

```text
HECHO
```

Se valida:

- propiedad de emprendimientos;
- propiedad de publicaciones;
- propiedad de pedidos;
- rol requerido;
- estado de usuarios.

---

## Calidad

### HU-F2-52 — Pruebas de Auth/Usuarios

```text
HECHO
```

### HU-F2-53 — Pruebas de Categorías

```text
HECHO
```

### HU-F2-54 — Pruebas de Emprendimientos

```text
HECHO
```

### HU-F2-55 — Pruebas de Publicaciones

```text
HECHO
```

Incluye:

- creación;
- normalización;
- categoría;
- propiedad;
- cambio de estado;
- límite de plan al crear;
- límite de plan al reactivar.

### HU-F2-56 — Pruebas de Pedidos

```text
HECHO
```

Incluye:

- creación;
- total;
- stock;
- roles;
- propiedad;
- transiciones;
- cancelación.

### HU-F2-57 — Integración continua

```text
HECHO
```

GitHub Actions ejecuta:

```text
mvn -B clean test
```

El pipeline del Pull Request de integración de Fase 2 finalizó correctamente.

### HU-F2-58 — Regresión Maven

```text
HECHO
```

Resultado actual:

```text
41 pruebas
0 failures
0 errors
BUILD SUCCESS
```

También:

```text
mvn clean package
BUILD SUCCESS
target/emprendelink.war
```

---

## Documentación

### HU-F2-60 — Actualizar documentación

```text
HECHO
```

Incluye:

- README;
- arquitectura;
- Product Backlog;
- matriz de pruebas;
- rutas;
- roles;
- Release Notes;
- cierre de Fase 2.

### HU-F2-61 — Pruebas manuales finales

```text
EN_CURSO
```

Pendiente ejecutar y registrar los casos manuales finales definidos en:

```text
docs/MATRIZ-PRUEBAS.md
```

### HU-F2-62 — Evidencias de Sprint Review

```text
PENDIENTE
```

Debe incluir:

- capturas;
- persistencia;
- relaciones;
- AJAX;
- validaciones;
- roles;
- pruebas;
- CI;
- WAR;
- repositorio;
- Product Backlog.

### HU-F2-63 — PDF de entrega

```text
PENDIENTE
```

Se generará después del smoke test final.

---

# Fase 3 — Servicios REST

Estado:

```text
PENDIENTE
```

Alcance:

- JAX-RS;
- `/api/v1`;
- recursos;
- métodos HTTP;
- JSON;
- DTO;
- validaciones;
- manejo de errores;
- seguridad;
- cliente consumidor;
- pruebas;
- documentación.

Los endpoints reutilizarán los Services existentes.

---

# Fase 4 — Spring, calidad y liberación

Estado:

```text
PENDIENTE
```

Alcance previsto:

- Spring;
- seguridad final;
- pruebas;
- trazabilidad;
- despliegue;
- documentación;
- liberación.