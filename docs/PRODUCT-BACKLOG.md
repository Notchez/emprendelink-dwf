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
- DataSource;
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

## Autenticación y cuentas

### HU-F2-03 — Login JSF

```text
HECHO
```

Incluye:

- autenticación;
- sesión;
- redirección por rol;
- logout.

### HU-F2-04 — Registro público de cliente

```text
HECHO
```

El registro público permite únicamente:

```text
ROLE_CLIENTE
```

### HU-F2-05 — Administración de usuarios

```text
HECHO
```

Incluye:

- listado;
- roles;
- activar/desactivar;
- control de permisos.

### HU-F2-06 — Crear emprendedor desde administración

```text
HECHO
```

El administrador crea en una operación:

```text
Usuario ROLE_EMPRENDEDOR
+
Emprendimiento
```

El negocio recibe:

- plan;
- categoría;
- nombre;
- descripción;
- contacto;
- logo opcional.

### HU-F2-07 — Un negocio por emprendedor

```text
HECHO
```

Se refuerza con:

```text
UNIQUE(id_propietario)
```

---

## Categorías

### HU-F2-10 — Persistencia y gestión

```text
HECHO
```

Incluye:

- crear;
- listar;
- editar;
- activar/desactivar;
- AJAX.

### HU-F2-11 — Configuración de inventario por categoría

```text
HECHO
```

Campo:

```text
manejaStock
```

Define si las publicaciones del negocio utilizan inventario limitado.

---

## Negocio del emprendedor

### HU-F2-20 — Persistencia JPA

```text
HECHO
```

Incluye:

- entidad;
- mapper;
- DAO;
- Service;
- propietario;
- plan;
- categoría.

### HU-F2-21 — Vista “Mi negocio”

```text
HECHO
```

Incluye:

- negocio único;
- plan actual;
- comisión;
- publicaciones activas;
- máximo del plan;
- espacios disponibles;
- edición de descripción;
- edición de contacto;
- edición de logo.

### HU-F2-22 — Datos administrativos del negocio

```text
HECHO
```

Nombre, categoría, plan y estado se gestionan desde administración.

---

## Publicaciones

### HU-F2-30 — Persistencia JPA

```text
HECHO
```

### HU-F2-31 — CRUD del catálogo del emprendedor

```text
HECHO
```

Incluye:

- crear;
- editar;
- listar;
- activar/desactivar;
- nombre;
- descripción;
- imagen URL;
- precio;
- stock cuando corresponde;
- AJAX.

### HU-F2-32 — Derivación de categoría y tipo

```text
HECHO
```

La publicación utiliza la categoría del negocio.

```text
manejaStock = true  → PRODUCTO
manejaStock = false → SERVICIO
```

El emprendedor no selecciona manualmente tipo ni categoría.

### HU-F2-33 — Límite por plan

```text
HECHO
```

Se valida al:

```text
crear
reactivar
asignar/cambiar plan
```

### HU-F2-34 — Contador AJAX de catálogo

```text
HECHO
```

Activar/desactivar actualiza:

- contador;
- espacios disponibles;
- estado.

---

## Catálogo cliente

### HU-F2-35 — Explorar catálogo

```text
HECHO
```

Incluye:

- publicaciones activas;
- agrupación por negocio;
- búsqueda;
- filtro por categoría;
- detalle.

El filtro visual `Tipo` fue retirado.

---

## Carrito y checkout

### HU-F2-40 — Carrito en sesión

```text
HECHO
```

Implementado mediante:

```text
CarritoBean @SessionScoped
```

### HU-F2-41 — Controles de cantidad

```text
HECHO
```

Incluye:

- agregar;
- incrementar;
- disminuir;
- papelera cuando cantidad = 1;
- AJAX;
- validación de stock.

### HU-F2-42 — Carrito multi-negocio

```text
HECHO
```

El cliente puede mantener publicaciones de distintos negocios dentro de un mismo carrito visual.

### HU-F2-43 — Checkout agrupado por negocio

```text
HECHO
```

El Service agrupa las líneas y crea:

```text
1 Pedido por negocio
```

dentro de la operación transaccional.

### HU-F2-44 — Dirección histórica del pedido

```text
HECHO
```

El checkout guarda:

```text
direccionEntrega
```

como snapshot.

---

## Pedidos

### HU-F2-45 — Persistencia de pedidos y detalles

```text
HECHO
```

### HU-F2-46 — Total y stock

```text
HECHO
```

Incluye:

- total desde detalles;
- validación de stock;
- descuento al crear;
- restauración al cancelar.

### HU-F2-47 — Estados

```text
HECHO
```

```text
PENDIENTE → CONFIRMADO
PENDIENTE → CANCELADO

CONFIRMADO → EN_PROCESO
CONFIRMADO → CANCELADO

EN_PROCESO → COMPLETADO
EN_PROCESO → CANCELADO
```

### HU-F2-48 — Consulta por rol

```text
HECHO
```

Cliente:

- pedidos propios;
- detalle propio.

Emprendedor:

- pedidos recibidos;
- detalle de su negocio.

---

## Planes y comisiones

### HU-F2-50 — Planes

```text
HECHO
```

```text
Básico    → 10  → 7%
Estándar  → 30  → 5%
Premium   → 60  → 3%
Max       → 100 → 1%
```

### HU-F2-51 — Snapshot comercial

```text
HECHO
```

Al crear el pedido se conservan:

```text
idPlanAplicado
nombrePlanAplicado
tasaComision
montoComision
```

### HU-F2-52 — Fecha de completado

```text
HECHO
```

Al finalizar:

```text
fechaCompletado
```

### HU-F2-53 — Ventas del emprendedor

```text
HECHO
```

Incluye:

- ventas totales;
- filtro de período;
- ventas completadas;
- detalle del pedido.

### HU-F2-54 — Comisiones del emprendedor

```text
HECHO
```

Permite consultar la comisión asociada al histórico de ventas.

### HU-F2-55 — Reporte administrativo

```text
HECHO
```

Incluye:

- ventas;
- comisiones;
- filtros.

---

## Perfil del cliente

### HU-F2-60 — Mi perfil

```text
HECHO
```

Cliente puede modificar:

```text
telefono
direccion
```

Nombre y correo son de solo lectura.

### HU-F2-61 — Logout dentro del perfil

```text
HECHO
```

El botón se retiró del header para el cliente.

---

## Seguridad y navegación

### HU-F2-70 — Navegación por rol

```text
HECHO
```

Landing:

```text
Admin       → /admin/inicio.xhtml
Emprendedor → /pedidos/recibidos.xhtml
Cliente     → /catalogo/index.xhtml
```

### HU-F2-71 — Bloqueo del catálogo al emprendedor

```text
HECHO
```

Implementado mediante:

```text
CatalogoFilter
```

### HU-F2-72 — Rutas cliente

```text
HECHO
```

Implementado mediante:

```text
ClienteFilter
PedidosFilter
```

### HU-F2-73 — Rutas emprendedor

```text
HECHO
```

Implementado mediante:

```text
EmprendedorFilter
PublicacionFilter
PedidosFilter
```

### HU-F2-74 — Rutas administrativas

```text
HECHO
```

Implementado mediante:

```text
AdminFilter
```

---

## Calidad

### HU-F2-80 — Pruebas automatizadas

```text
HECHO
```

Último resultado:

```text
41 tests
0 failures
0 errors
BUILD SUCCESS
```

### HU-F2-81 — Integración continua

```text
HECHO
```

GitHub Actions:

```text
mvn -B clean test
```

### HU-F2-82 — Documentación técnica actualizada

```text
HECHO
```

Incluye actualización de:

- README;
- arquitectura;
- modelos;
- rutas;
- permisos;
- backlog;
- matriz;
- release notes;
- cierre de Fase 2;
- contrato preliminar de API.

### HU-F2-83 — Smoke test manual final documentado

```text
EN_CURSO
```

La funcionalidad principal ha sido probada durante el desarrollo, pero la matriz conserva como pendientes los casos manuales que todavía deban ejecutarse formalmente para evidencia.

### HU-F2-84 — Evidencias de Sprint Review

```text
PENDIENTE
```

Debe consolidar:

- capturas;
- persistencia;
- CRUD;
- AJAX;
- roles;
- carrito;
- pedidos;
- pruebas;
- CI;
- repositorio.

### HU-F2-85 — PDF de entrega

```text
PENDIENTE
```

---

# Fase 3 — Servicios REST e Integración de Clientes

Estado:

```text
PENDIENTE
```

Existe scaffolding de Resources y DTO, pero no endpoints funcionales.

Alcance:

- contrato REST definitivo;
- JAX-RS;
- JSON;
- DTO reales;
- códigos HTTP;
- manejo de errores;
- seguridad REST;
- cliente consumidor;
- pruebas de API;
- documentación de consumo.

---

# Fase 4 — Spring, calidad y liberación

Estado:

```text
PENDIENTE
```

Alcance previsto:

- integración Spring;
- seguridad final;
- pruebas sistemáticas;
- despliegue;
- manuales;
- Release Notes;
- liberación final.