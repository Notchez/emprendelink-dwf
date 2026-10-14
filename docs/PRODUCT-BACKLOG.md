# Product Backlog — EmprendeLink DWF

Estados utilizados:

```text
HECHO
EN_CURSO
PENDIENTE
```

# Fase 2 — Persistencia Empresarial e Integración JSF

## Infraestructura

### HU-F2-01 — Configurar persistencia JPA administrada

Estado:

```text
HECHO
```

Incluye:

- Persistence Unit `emprendelinkPU`;
- JTA;
- DataSource `jdbc/EmprendeLinkDS`;
- EclipseLink;
- `@PersistenceContext`;
- GlassFish;
- MySQL.

---

### HU-F2-02 — Implementar CDI

Estado:

```text
HECHO
```

Incluye:

- Services administrados;
- DAO administrados;
- Managed Beans;
- inyección mediante `@Inject`.

---

## Autenticación y usuarios

### HU-F2-03 — Login JSF

```text
HECHO
```

### HU-F2-04 — Registro Cliente/Emprendedor

```text
HECHO
```

El registro público no permite crear administradores.

### HU-F2-05 — Administración de usuarios

```text
HECHO
```

Incluye:

- listar;
- activar;
- desactivar;
- protección contra auto-desactivación del administrador.

### HU-F2-06 — Validaciones y AJAX de usuarios

```text
HECHO
```

Incluye:

- correo;
- contraseña;
- mensajes;
- converter de rol;
- actualización asíncrona.

---

## Categorías

### HU-F2-10 — Persistencia JPA de categorías

```text
HECHO
```

Incluye:

- `CategoriaEntity`;
- `CategoriaMapper`;
- `JpaCategoriaDAO`;
- `CategoriaService`.

### HU-F2-11 — Gestión JSF de categorías

```text
HECHO
```

Incluye:

- listar;
- crear;
- editar;
- activar/desactivar;
- validaciones;
- AJAX.

La eliminación se implementa de forma lógica mediante el atributo `activo`.

---

## Emprendimientos

### HU-F2-20 — Persistencia JPA de emprendimientos

```text
HECHO
```

Incluye:

- `EmprendimientoEntity`;
- mapper;
- DAO JPA;
- Service;
- relación con propietario.

### HU-F2-21 — Gestión JSF de emprendimientos propios

```text
HECHO
```

Incluye:

- listar emprendimientos propios;
- crear;
- editar;
- validar propiedad;
- AJAX.

### HU-F2-22 — Administración del estado de emprendimientos

```text
HECHO
```

El administrador puede activar o desactivar emprendimientos.

---

## Publicaciones

### HU-F2-30 — Persistencia JPA de publicaciones

```text
HECHO
```

Incluye relaciones con:

- emprendimiento;
- categoría.

### HU-F2-31 — Gestión JSF de publicaciones

```text
HECHO
```

Incluye:

- crear;
- editar;
- listar;
- activar/desactivar;
- validación del propietario;
- validación de categoría;
- validación de emprendimiento;
- precio;
- stock.

### HU-F2-32 — Catálogo JSF

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

---

## Pedidos

### HU-F2-40 — Persistencia JPA de pedidos y detalles

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

### HU-F2-41 — Crear pedido transaccionalmente

```text
HECHO
```

Incluye:

- validación de cliente;
- validación de emprendimiento;
- validación de publicaciones;
- cantidad;
- precio;
- cálculo del total;
- persistencia de detalles;
- descuento de stock.

### HU-F2-42 — Gestión de estados

```text
HECHO
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

Estados terminales:

```text
COMPLETADO
CANCELADO
```

### HU-F2-43 — Restauración de stock al cancelar

```text
HECHO
```

Cuando se cancela un pedido de productos, el stock utilizado es restaurado.

### HU-F2-44 — Consulta de pedidos por rol

```text
HECHO
```

Cliente:

- consulta sus pedidos;
- consulta el detalle de sus pedidos.

Emprendedor:

- consulta pedidos recibidos;
- administra únicamente pedidos de sus emprendimientos.

Administrador:

- puede consultar un pedido cuando la lógica de visibilidad lo autoriza.

---

## Seguridad

### HU-F2-45 — Seguridad por rutas

```text
HECHO
```

Incluye:

```text
AdminFilter
AutenticacionFilter
EmprendedorFilter
PedidosFilter
```

### HU-F2-46 — Propiedad de recursos

```text
HECHO
```

Se valida que:

- un emprendedor edite únicamente sus emprendimientos;
- un emprendedor gestione únicamente sus publicaciones;
- un emprendedor gestione únicamente pedidos correspondientes a sus negocios;
- un cliente consulte únicamente pedidos visibles para él.

---

## Calidad

### HU-F2-50 — Pruebas automatizadas Auth/Usuarios

```text
HECHO
```

### HU-F2-51 — CI con GitHub Actions

```text
EN_CURSO
```

Existe workflow para:

```text
mvn -B clean test
```

Se marcará como `HECHO` cuando el pipeline de la rama oficial utilizada para la entrega sea confirmado en verde.

### HU-F2-52 — Pruebas de Categorías

```text
HECHO
```

Incluye mappers y reglas de Service.

### HU-F2-53 — Pruebas de Emprendimientos

```text
HECHO
```

Incluye mappers y reglas de Service.

### HU-F2-54 — Pruebas de Publicaciones

```text
HECHO
```

Incluye:

- mappers;
- creación;
- propiedad;
- categoría;
- stock;
- estado.

### HU-F2-55 — Pruebas de Pedidos

```text
HECHO
```

Incluye:

- mapper;
- creación;
- stock;
- roles;
- propiedad;
- transiciones válidas;
- transiciones inválidas;
- cancelación.

### HU-F2-56 — Regresión Maven

```text
HECHO
```

Última línea estable:

```text
39 pruebas automatizadas
0 failures
0 errors
```

Comandos:

```bash
mvn clean test
mvn clean package
```

---

## Documentación

### HU-F2-60 — Actualizar documentación de Fase 2

```text
EN_CURSO
```

Incluye:

- arquitectura;
- Product Backlog;
- matriz de pruebas;
- rutas;
- roles;
- Release Notes;
- README.

### HU-F2-61 — Preparar evidencias de Sprint Review

```text
PENDIENTE
```

Incluye:

- capturas;
- resultados de pruebas;
- evidencia de persistencia;
- evidencia AJAX;
- repositorio;
- Product Backlog;
- PDF final.

---

# Fase 3 — Servicios REST

Estado general:

```text
PENDIENTE
```

Alcance previsto:

- contrato `/api/v1`;
- endpoints;
- métodos HTTP;
- JSON;
- DTO;
- validación;
- manejo de errores;
- seguridad;
- cliente consumidor;
- pruebas;
- documentación.

REST deberá reutilizar los Services existentes.

---

# Fase 4 — Spring, calidad y liberación

Estado general:

```text
PENDIENTE
```

Incluye:

- integración con módulos Spring autorizados;
- seguridad final;
- pruebas de integración;
- pruebas de regresión;
- manejo centralizado de errores;
- despliegue accesible;
- documentación final;
- Release Notes de producto.