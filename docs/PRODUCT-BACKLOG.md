
# Product Backlog — EmprendeLink DWF

Estados:

```text
HECHO
EN_CURSO
PENDIENTE
```

> **Referencia de estado (19/10/2026):** Fase 2 fue presentada y su incremento funcional está cerrado. Se conserva a continuación el registro histórico de `develop` sin convertir pruebas o evidencias pendientes en `HECHO` sin comprobación. La preparación técnica de Fase 3 sucede en `feature/fase3-base-rest`; los cambios de esa rama solo estarán disponibles al grupo tras su integración en `develop`.

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

Último resultado documentado al cierre de Fase 2:

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

La funcionalidad principal ha sido probada durante el desarrollo, pero la matriz conserva como pendientes los casos manuales que todavía deban ejecutarse formalmente para evidencia. Este estado se conserva como registro histórico: no bloquea por sí solo la preparación de Fase 3 y tampoco implica que esa evidencia se completó.

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

El estado original no se cambia sin evidencia adicional de su cierre académico.

### HU-F2-85 — PDF de entrega

```text
PENDIENTE
```

El estado original no se cambia sin evidencia adicional de su cierre académico.

---

# Fase 3 — Servicios REST e Integración de Clientes (Sprint III)

## Objetivo y alcance

**Fecha académica:** 7 u 8 de noviembre de 2026. **Valor:** 25% de la asignatura.

Diseñar, implementar y consumir servicios REST que expongan las operaciones existentes de EmprendeLink mediante HTTP/JSON, contrato coherente, seguridad, validaciones y errores. La API debe reutilizar los Services, DAO y JPA actuales; no sustituye el cliente JSF ni adelanta Spring de Fase 4.

**Estado general: `EN_CURSO` (preparación técnica).** Las historias funcionales de abajo siguen `PENDIENTE` hasta su implementación y verificación.

**Fuente de requisitos:** Ruta de aprendizaje DWF901, anexo Proyecto Fase 3, páginas 19–22. **Contrato provisional:** `docs/API.md` (aún no definitivo).

### HU-F3-00 — Infraestructura inicial de la API

```text
EN_CURSO
```

**Alcance de la rama base** `feature/fase3-base-rest` (no asignable como tarea funcional a los demás integrantes):

- Mantener `RestApplication` con `@ApplicationPath("/api/v1")`.
- Mantener `HealthResource` con `GET /api/v1/health` que devuelve JSON.
- Conservar intactos los flujos JSF y la persistencia actual.
- Actualizar `AGENTS.md` y este Backlog para guiar el trabajo colaborativo.
- Confirmar `mvn clean test`, `mvn clean package` y la respuesta del endpoint en GlassFish.
- Abrir PR de la base a `develop`, revisar y fusionar antes de crear ramas funcionales.

**Evidencia:** en la rama local se ejecutaron exitosamente los builds y se observó el JSON de `/health` el 19/10/2026. **Pendiente:** verificar commit, push, Pull Request y disponibilidad final en `develop`.

**No incluye:** DTO funcionales, endpoints de negocio, login REST, seguridad, frontend externo, pruebas API completas ni documento de entrega.

### HU-F3-01 — Refinamiento del contrato REST

```text
PENDIENTE
```

- Revisar los recursos candidatos de `docs/API.md`: `emprendimientos`, `publicaciones`, `pedidos`.
- Definir rutas, métodos HTTP, filtros, parámetros, cuerpos JSON, campos DTO, respuestas y códigos de estado.
- Precisar el contrato de autenticación/credenciales y operaciones públicas/protegidas.
- Resolver explícitamente el contrato de checkout multi-negocio; no asumir que un pedido equivale a todo el carrito.
- Identificar historias de cliente y reglas de negocio preservadas.

**Criterio de aceptación:** contrato único, revisado por el equipo, con ejemplos completos de éxito y error. No iniciar interfaces dependientes de un contrato cambiante sin coordinación.

### HU-F3-02 — DTO de solicitud, respuesta y error

```text
PENDIENTE
```

- Sustituir scaffolding vacío por DTO reales, según contrato definitivo.
- Serializar/deserializar JSON sin exponer entidades JPA, relaciones internas ni credenciales.
- Aplicar `camelCase` y campos explícitos de entrada/salida.
- Asegurar compatibilidad entre DTO, Services, respuestas y cliente consumidor.

**Criterio de aceptación:** JSON correcto, estable, probado y documentado; datos privados excluidos.

### HU-F3-03 — REST de emprendimientos

```text
PENDIENTE
```

- Implementar las operaciones acordadas para consulta de negocios visibles y sus detalles.
- Reutilizar `EmprendimientoService`, sin consultar DAO directamente.
- Aplicar visibilidad, validación de parámetros, filtros y códigos HTTP del contrato.

**Criterio de aceptación:** endpoints conectados a datos reales, con casos 200/404 y otros errores aplicables; sin fugas de datos.

### HU-F3-04 — REST de publicaciones

```text
PENDIENTE
```

- Implementar consulta de publicaciones visibles y detalle, con filtros autorizados por el contrato.
- Reutilizar `PublicacionService` y las reglas de categoría, estado, stock y visibilidad.
- Documentar comportamiento ante filtros incorrectos y publicaciones no disponibles.

**Criterio de aceptación:** catálogo REST consistente con el catálogo JSF existente y sus reglas de acceso.

### HU-F3-05 — REST de checkout y creación de pedidos

```text
PENDIENTE
```

- Definir la solicitud y respuesta según `docs/API.md` revisado.
- Reutilizar `PedidoService.crearDesdeCarrito(...)` cuando el contrato adoptado lo requiera.
- Conservar agrupación por negocio, stock, dirección de entrega y snapshots de plan/comisión.
- Verificar transaccionalidad, entradas inválidas y conflictos de inventario.
- Prohibir la suplantación de identidad del cliente a través del JSON de entrada.

**Criterio de aceptación:** operación autorizada, sin duplicación de reglas, y resultado verificable en MySQL.

### HU-F3-06 — REST de consulta y estado de pedidos

```text
PENDIENTE
```

- Implementar las lecturas y transiciones acordadas para pedidos.
- Reutilizar `PedidoService.buscarVisibleParaUsuario` y `actualizarEstado` u operaciones equivalentes existentes.
- Validar identidad, propiedad del pedido y transiciones autorizadas.
- Conservar integridad del historial y devoluciones de stock al cancelar.

**Criterio de aceptación:** ninguna persona consulta ni modifica un pedido ajeno; los estados inválidos se rechazan de forma consistente.

### HU-F3-07 — Autenticación y autorización REST

```text
PENDIENTE
```

- Acordar/documentar mecanismo de acceso para consumidor externo: sesión o credenciales/tokens conforme al diseño autorizado.
- Proteger endpoints privados **independientemente** de los filtros que cubren URLs JSF.
- Verificar usuario activo, `ROLE_ADMIN`, `ROLE_EMPRENDEDOR`, `ROLE_CLIENTE`, propiedad y estado del recurso según cada caso.
- Comprobar solicitudes sin autenticación y con perfiles sin permisos.

**Criterio de aceptación:** controles verificables con respuestas adecuadas; no se exponen operaciones privadas por acceso directo a `/api/v1/*`.

### HU-F3-08 — Validaciones y errores REST

```text
PENDIENTE
```

- Validar campos faltantes, valores inválidos, IDs, filtros y cantidades.
- Unificar errores con DTO y mecanismo de excepciones acordados.
- Distinguir 400, 401, 403, 404, 409 y 500 según contexto, sin revelar información interna.
- Registrar fallos relevantes sin mostrar detalles sensibles al cliente.

**Criterio de aceptación:** estructura y códigos de error consistentes y probados en todas las operaciones relevantes.

### HU-F3-09 — Cliente externo consumidor

```text
PENDIENTE
```

- Implementar un cliente independiente o módulo externo con JavaScript, React, Vue u otra tecnología autorizada.
- Consumir endpoints REST reales; no usar datos simulados para la demostración principal.
- Mostrar información obtenida, formularios, cargas, errores y resultados.
- Demostrar flujo completo del cliente a la API, Service y base de datos.

**Criterio de aceptación:** aplicación cliente funcional, estable, alineada al contrato, con estados de carga y errores comprensibles.

### HU-F3-10 — Pruebas de API e integración

```text
PENDIENTE
```

- Crear colección de Postman, Bruno o herramienta especializada equivalente.
- Probar recursos, métodos, status HTTP, JSON, validaciones, permisos y datos reales.
- Registrar casos positivos, negativos y límites, incluyendo acceso ajeno, stock insuficiente y transiciones inválidas.
- Mantener la regresión de los Services y flujos JSF existentes.
- Registrar evidencia de ejecución y defectos detectados/corregidos.

**Criterio de aceptación:** colección reproducible y matriz de resultados trazable a historias/endpoints.

### HU-F3-11 — Documentación de consumo

```text
PENDIENTE
```

- Actualizar `docs/API.md` de contrato preliminar a definitivo, con ejemplos completos de request/response/error.
- Documentar autenticación, permisos, rutas, filtros y pasos de ejecución.
- Mantener README, arquitectura y backlog coherentes con el incremento realmente integrado.
- No declarar como implementados recursos que estén solamente en scaffolding.

**Criterio de aceptación:** otro integrante puede arrancar el proyecto y consumir la API con la documentación y la colección de pruebas.

### HU-F3-12 — Evidencias y PDF de Fase 3

```text
PENDIENTE
```

- Consolidar repositorio, PR, integrantes y evidencias individuales.
- Capturar endpoints, JSON, casos de éxito/error, seguridad y consumo desde el cliente externo.
- Adjuntar o enlazar colección de pruebas, contrato y resultados.
- Actualizar Product Backlog con estados reales.

**Criterio de aceptación:** PDF de evidencias y enlaces suficientes para validar la entrega, sin confundir pruebas pendientes con pruebas ejecutadas.

### HU-F3-13 — Sprint Review y defensa

```text
PENDIENTE
```

- Demostrar al docente la API REST y al cliente consumidor en una defensa virtual.
- Ejecutar al menos flujos completos desde cliente hasta persistencia, casos exitosos y casos de error.
- Explicar métodos HTTP, códigos, JSON, separación arquitectónica, seguridad y validaciones.
- Preparar participación verificable y respuestas técnicas de todos los integrantes.

**Criterio de aceptación:** Sprint III demostrable, con aplicación desplegada para la defensa, registro de defectos y trazabilidad del incremento.

---

## Dependencias y secuencia de colaboración

1. **Integrar HU-F3-00 a `develop`**: todos parten de la misma base técnica.
2. **Acordar HU-F3-01**: el contrato evita discrepancias entre backend, DTO y cliente.
3. **Trabajar HU-F3-02 y endpoints por módulos**: respetar Services y reglas actuales.
4. **Resolver HU-F3-07/HU-F3-08 antes de liberar operaciones protegidas**: no dejar escrituras sin permisos.
5. **Desarrollar HU-F3-09 contra el contrato revisado** y conectarlo a los endpoints reales.
6. **Ejecutar HU-F3-10** continuamente, no únicamente antes de la defensa.
7. **Cerrar HU-F3-11 a HU-F3-13** con evidencias reales y participación verificable.

Las tareas son del **equipo de cinco**; sus responsables nominales se asignarán con el documento de reparto. Este Backlog define **qué hay que entregar** y **cómo verificarlo**, no realiza ni sustituye el trabajo de los integrantes.

## Control de calidad para nota 9.00–10.00 (columna Estratégico)

| Criterio de la ruta | Peso | Evidencia para nivel Estratégico |
|---|---:|---|
| Contrato y arquitectura REST | 20% | Contrato riguroso, claro y extensible; estándares y necesidades de integración |
| Endpoints e integración | 25% | Servicios robustos, desacoplados, mantenibles y trazables |
| Seguridad, validaciones y errores | 20% | Controles verificables y API predecible con reducción de riesgos |
| Cliente consumidor | 20% | Integración externa profesional y resistente a errores, alineada al contrato |
| Pruebas, documentación y Sprint Review | 15% | Documentación completa, pruebas positivas/negativas y dominio técnico en defensa |

**Entregables académicos:** API funcional integrada con lógica y MySQL; cliente consumidor externo; documentación de rutas, entradas/salidas y errores; colección/especificación de pruebas; repositorio actualizado; PDF de evidencias con enlaces; demostración virtual.

## Riesgos heredados que afectan Fase 3

- No dar por cubiertas las pruebas manuales de Fase 2 sin evidencia: revisar las reglas reutilizadas durante pruebas de integración de Fase 3, sin reabrir la entrega anterior.
- La API no está protegida automáticamente por filtros de rutas `.xhtml`; verificar acceso HTTP real.
- La API y el cliente deben consumir los mismos Services y reglas de stock/comisiones/propiedad.
- No fusionar antes de comprobar `mvn clean test`, `mvn clean package` y las pruebas HTTP relevantes.

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
