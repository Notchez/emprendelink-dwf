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

# Preparación de Fase 3

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