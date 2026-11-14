# Release Notes — EmprendeLink DWF

# Fase 1 — Sprint I

## Objetivo

Construir el primer incremento funcional utilizando:

```text
Java
Servlets
JSP/JSTL
JDBC
MySQL
```

Fase 1 estableció:

- dominio inicial;
- DAO;
- Services;
- autenticación inicial;
- MVC;
- conexión a base;
- flujo Git.

---

# Fase 2 — Sprint II

## Objetivo

Evolucionar el proyecto hacia una aplicación empresarial con:

```text
Jakarta Faces
CDI
JPA
JTA
EclipseLink
GlassFish
MySQL
```

---

## Arquitectura

Arquitectura activa:

```text
XHTML / JSF
      ↓
Managed Beans
      ↓
Services
      ↓
DAO
      ↓
JPA
      ↓
EclipseLink
      ↓
JTA
      ↓
MySQL
```

Se retiraron del runtime las implementaciones paralelas de Fase 1.

---

## Persistencia

Se incorporaron:

```text
RolEntity
UsuarioEntity
CategoriaEntity
PlanEntity
EmprendimientoEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

La base usa un esquema consolidado:

```text
database/schema.sql
database/seed.sql
```

Las migraciones incrementales anteriores fueron retiradas.

---

## Autenticación y roles

Se implementó:

- login;
- logout;
- sesión;
- roles;
- hash de contraseña;
- filtros;
- autorización por Service.

Roles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

---

## Cambio: registro público

Antes:

```text
Cliente o Emprendedor
```

Ahora:

```text
solo ROLE_CLIENTE
```

Los emprendedores son creados exclusivamente por administración.

---

## Cambio: creación administrativa de emprendedores

El administrador puede crear:

```text
Usuario ROLE_EMPRENDEDOR
+
Emprendimiento
```

en una misma operación.

El negocio recibe:

- plan;
- categoría;
- nombre;
- descripción;
- contacto;
- logo opcional.

---

## Cambio: un negocio por emprendedor

Nueva regla:

```text
1 emprendedor → máximo 1 negocio
```

Aplicada mediante:

```text
UNIQUE(id_propietario)
```

La experiencia se renombró de:

```text
Mis emprendimientos
```

a:

```text
Mi negocio
```

---

## Cambio: categoría e inventario

La categoría ahora incluye:

```text
manejaStock
```

La categoría del negocio define si las publicaciones utilizan inventario.

La interfaz administrativa muestra:

```text
¿Cuenta con inventario limitado?
```

---

## Cambio: catálogo del emprendedor

Se refinó la gestión de publicaciones:

- edición real del registro;
- imagen URL;
- precio con formato monetario;
- stock condicional;
- activar/desactivar;
- contador del plan;
- espacios disponibles;
- actualización AJAX.

La edición fue corregida para ejecutar `UPDATE` en lugar de crear duplicados.

---

## Cambio: tipo/categoría de publicación

El emprendedor ya no selecciona manualmente:

```text
Tipo
Categoría
```

La lógica deriva:

```text
categoría del negocio
+
manejaStock
```

El enum `TipoPublicacion` se conserva técnicamente en el dominio.

---

## Cambio: navegación emprendedor

Landing:

```text
/pedidos/recibidos.xhtml
```

Navegación:

```text
Pedidos
Mi negocio
Catálogo
Ventas
Comisiones
```

El emprendedor autenticado ya no utiliza el catálogo público como consumidor.

---

## Cambio: Mi negocio

Muestra:

- plan;
- tasa;
- publicaciones activas;
- máximo;
- espacios disponibles.

Editable:

- descripción;
- contacto;
- logo.

Administrativo:

- nombre;
- categoría;
- plan;
- estado.

---

## Cambio: ventas

Se agregó:

```text
/ventas/index.xhtml
```

Incluye:

- ventas totales;
- filtro por mes/año;
- ventas completadas;
- detalle de líneas.

---

## Cambio: comisiones del emprendedor

Se agregó:

```text
/comisiones/index.xhtml
```

para consultar la comisión histórica asociada a ventas.

---

## Cambio: catálogo cliente

Se retiró el filtro visual:

```text
Tipo
```

Se mantiene:

```text
búsqueda
categoría
```

---

## Cambio: carrito

Se agregó:

```text
CarritoBean @SessionScoped
```

con:

- agregar;
- `+`;
- `-`;
- papelera;
- badge;
- subtotal;
- total;
- validación de stock;
- AJAX.

---

## Cambio: carrito multi-negocio

Un cliente puede agregar artículos de varios negocios.

Al confirmar:

```text
1 carrito
→ N pedidos
→ 1 por negocio
```

---

## Cambio: snapshot comercial

El pedido conserva desde su creación:

```text
idPlanAplicado
nombrePlanAplicado
tasaComision
montoComision
direccionEntrega
```

Esto protege el historial frente a cambios posteriores.

---

## Cambio: perfil cliente

Se agregó:

```text
/perfil/index.xhtml
```

Editable:

```text
telefono
direccion
```

Solo lectura:

```text
nombre
correo
```

El logout del cliente se movió a Mi perfil.

---

## Cambio: seguridad

Filtros activos:

```text
AutenticacionFilter
AdminFilter
CatalogoFilter
ClienteFilter
EmprendedorFilter
PedidosFilter
PublicacionFilter
```

---

## Pruebas

Última regresión:

```text
41 tests
0 failures
0 errors
BUILD SUCCESS
```

---

## Estado de Fase 2

Funcionalidad:

```text
CERRADA
```

Pendiente de entrega académica:

- smoke test final documentado;
- evidencias;
- matriz final;
- `mvn clean package`;
- PDF de Sprint Review;
- integración definitiva a `develop`.

---

# Preparación de Fase 3 (antecedente histórico)

> Este apartado conserva el estado previo al Sprint III. La implementación y los resultados más recientes se documentan en la sección «Fase 3 — Sprint III» al final de este archivo.
Existen Resources y DTO vacíos como scaffolding.

No hay todavía API REST funcional.

La Fase 3 deberá implementar formalmente:

- contrato REST;
- endpoints;
- JSON;
- seguridad;
- cliente externo;
- pruebas;
- documentación de consumo.

---

# Fase 3 — Sprint III: Servicios REST e integración de clientes

**Registro de validación QA:** 14 de noviembre de 2026.  
**Entorno utilizado:** Java 25, Jakarta EE 11, GlassFish 8.0.5, MySQL y API local en `http://localhost:8080/emprendelink/api/v1`.  
**Estado:** incremento API operativo con defectos abiertos y verificaciones pendientes; **no se declara cierre libre de defectos**.

## Alcance incorporado al Sprint III

- Servicios HTTP/JSON mediante JAX-RS, usando los Services y la persistencia empresarial existentes.
- Endpoints de consulta de emprendimientos y publicaciones, consultas individuales y publicaciones de un emprendimiento.
- Autenticación REST por sesión (`/auth/login`, `/auth/me`, `/auth/logout`), validación de rol y protección CSRF en operaciones protegidas de escritura.
- Endpoints de pedidos: listado, consulta individual, checkout y cambio de estado según rol.
- Componente cliente consumidor externo incorporado al trabajo del equipo; su recorrido completo de interfaz no quedó cubierto por las evidencias de QA de este corte.
- Incorporación de pruebas manuales HTTP, matriz de resultados y capturas reproducibles del integrante 5.

El contrato de consumo se encuentra en [`API.md`](API.md); la evidencia individual en [`MATRIZ-PRUEBAS.md`](MATRIZ-PRUEBAS.md) y [`EVIDENCIAS-FASE3/`](EVIDENCIAS-FASE3/).

## Resumen de QA ejecutado

| Clasificación | Cantidad |
|---|---:|
| Casos previstos | 25 |
| Ejecutados | 23 |
| Aprobados | 19 |
| Fallidos | 3 |
| Pendientes de validación integral (ejecutados parcialmente) | 1 |
| Bloqueados, no ejecutados | 2 |

Se observaron los siguientes comportamientos satisfactorios:

1. `GET /health` → `200`, JSON con `status=UP`.
2. Consulta de emprendimientos y publicaciones → `200` y JSON con datos disponibles.
3. Login REST válido → `200`, `ROLE_CLIENTE` y token CSRF disponible; login incorrecto → `401`; solicitud vacía → `400`.
4. Acceso sin sesión a `/auth/me` y `/pedidos` → `401` con JSON estandarizado.
5. Checkout sin CSRF → `403`; checkout válido de cliente → `204`.
6. Pedido **#5**, estado `PENDIENTE`, total **2.50**, recuperable mediante GET; inventario de publicación 1 reducido de **40 a 39** tras la compra.
7. Compra con stock insuficiente → `409`, sin nuevo pedido ni variación posterior de stock.
8. Intento de cambio de estado desde `ROLE_CLIENTE` → `403`, y pedido #5 permaneció `PENDIENTE`.
9. Logout → `204`; al reutilizar la sesión, `/auth/me` → `401`.

Las observaciones corresponden a pruebas realizadas desde PowerShell/cURL contra una instancia local. El comportamiento de persistencia se corroboró mediante solicitudes HTTP independientes, **no mediante una inspección SQL directa**.

## Defectos abiertos

| ID | Descripción | Impacto | Estado |
|---|---|---|---|
| `DEF-001` | GET de emprendimiento o publicación inexistente devuelve `404` con página HTML (`text/html`) pese a `Accept: application/json` | Incumplimiento del contrato de error JSON | Abierto; requiere corrección y retesteo |
| `DEF-002` | `GET /publicaciones?texto=cafe` devuelve la misma lista que la consulta sin filtro | Filtro de consulta no aplicado como se espera | Abierto; confirmar contrato definitivo, corregir y retestear |

No se atribuye una causa raíz sin diagnóstico del equipo responsable. Los dos `404` con HTML corresponden a **un solo defecto de formato** documentado por dos casos (`CP-202`, `CP-204`).

## Pendientes y bloqueos

- **CP-223:** pedido inexistente devuelve `404`, pero no quedó confirmado el `Content-Type` ni el cuerpo completo del error. Estado: pendiente de verificación integral.
- **CP-216 y CP-217:** transiciones de pedido válidas e inválidas desde un emprendedor propietario, bloqueadas por falta de una cuenta de prueba con rol/propiedad correspondientes.
- **Cobertura adicional no ejecutada:** pedidos de terceros, checkout multi-negocio, respuesta JSON completa de `409`, inspección directa de tablas SQL y recorrido del cliente externo hasta la base de datos.
- **Verificación final de entrega:** nueva ejecución documentada de `mvn clean test`, `mvn clean package`, revisión del workflow de GitHub Actions para el commit final y Sprint Review del equipo.

## Ejecución local

1. Confirmar MySQL y la base `emprendelink_dwf`.
2. Confirmar `EmprendeLinkPool` y JNDI `jdbc/EmprendeLinkDS` en GlassFish; probar **Ping**.
3. Ejecutar `mvn clean test` y `mvn clean package`.
4. Desplegar `target/emprendelink.war` bajo `/emprendelink`.
5. Ejecutar `curl.exe -i http://localhost:8080/emprendelink/api/v1/health`.
6. Reproducir los escenarios de [`MATRIZ-PRUEBAS.md`](MATRIZ-PRUEBAS.md), sección 14, sin reutilizar contraseñas ni tokens reales en archivos versionados.

## Responsabilidades y cierre

El integrante 5 consolida resultados, defectos, instrucciones y evidencias. Las correcciones funcionales del catálogo, Resources o seguridad compartida corresponden a los responsables de dichos módulos. Los errores abiertos y las pruebas bloqueadas deben resolverse, aceptarse explícitamente o declararse como limitaciones antes del cierre académico del Sprint III.
