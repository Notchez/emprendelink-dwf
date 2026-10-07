# Release Notes — EmprendeLink DWF

Este documento registra los cambios relevantes realizados durante la evolución de EmprendeLink.

Debe actualizarse cuando exista:

- una nueva fase;
- una entrega;
- un cambio arquitectónico importante;
- una modificación de base de datos;
- un cambio relevante de dependencias;
- una nueva funcionalidad estable;
- una corrección importante.

---

# Convención de versiones

Formato:

```text
MAJOR.MINOR.PATCH
```

Ejemplos:

```text
0.1.0
0.2.0
1.0.0
```

Durante el desarrollo académico se utilizarán versiones anteriores a `1.0.0`.

---

# Versión 0.1.0 — Fase 1

Estado:

```text
COMPLETADA
```

## Objetivo

Construir la base funcional de EmprendeLink utilizando Java Web tradicional, arquitectura MVC, JDBC y MySQL.

---

## Arquitectura implementada

Flujo principal:

```text
JSP
 ↓
Servlet
 ↓
Service
 ↓
DAO
 ↓
JDBC
 ↓
MySQL
```

---

## Funcionalidades implementadas

Se desarrolló soporte funcional para:

- autenticación;
- registro de usuarios;
- usuarios y roles;
- emprendimientos;
- categorías;
- publicaciones;
- catálogo;
- pedidos;
- detalle de pedidos;
- manejo de errores;
- control básico de permisos.

---

## Modelo de dominio

Entidades:

```text
Rol
Usuario
Emprendimiento
Categoria
Publicacion
Pedido
DetallePedido
```

Enums:

```text
TipoRol
TipoPublicacion
EstadoPedido
```

---

## Persistencia JDBC

Se implementaron interfaces DAO y sus implementaciones JDBC.

Ejemplos:

```text
JdbcRolDAO
JdbcUsuarioDAO
JdbcEmprendimientoDAO
JdbcCategoriaDAO
JdbcPublicacionDAO
JdbcPedidoDAO
```

---

## Services

Se implementaron:

```text
AutenticacionServiceImpl
UsuarioServiceImpl
EmprendimientoServiceImpl
CategoriaServiceImpl
PublicacionServiceImpl
PedidoServiceImpl
```

---

## Controladores

Se incorporaron:

```text
AuthServlet
CatalogoServlet
UsuarioServlet
EmprendimientoServlet
CategoriaServlet
PublicacionServlet
PedidoServlet
```

---

## Seguridad

Se incorporó manejo de contraseñas mediante una utilidad propia.

Las contraseñas reales no deben persistirse en texto plano.

---

## Base de datos

Se creó:

```text
database/schema.sql
```

con la estructura principal de:

```text
roles
usuarios
emprendimientos
categorias
publicaciones
pedidos
detalle_pedido
```

También existe:

```text
database/seed.sql
```

para datos iniciales de prueba.

---

## Documentación

Se incorporaron documentos relacionados con:

- arquitectura;
- convenciones;
- roles;
- modelo de dominio;
- modelo de datos;
- rutas;
- backlog;
- pruebas;
- versiones;
- flujo Git;
- API prevista.

---

## Build

El proyecto genera:

```text
target/emprendelink.war
```

mediante:

```text
mvn clean package
```

---

## Limitaciones conocidas de Fase 1

- La persistencia principal utiliza JDBC.
- La interfaz utiliza principalmente JSP.
- La cobertura automatizada de pruebas todavía es limitada.
- `DominioSmokeTest` no representa todavía una suite completa de pruebas.
- Los valores de contraseña existentes en `database/seed.sql` deben actualizarse para ser compatibles con el mecanismo real de hashing.
- JPA y JSF todavía no forman parte de la funcionalidad completa.

---

# Versión 0.2.0 — Fase 2

Estado:

```text
EN DESARROLLO
```

## Objetivo

Evolucionar la aplicación existente incorporando:

```text
JPA
Hibernate
Jakarta Faces
Managed Beans
AJAX
Validators
Converters
Transacciones
Pruebas
```

sin reconstruir desde cero la lógica implementada durante Fase 1.

---

## Cambios estructurales iniciales

El package raíz Java fue simplificado de:

```text
sv.edu.udb.emprendelink
```

a:

```text
emprendelink
```

Nueva raíz:

```text
src/main/java/emprendelink/
```

Pruebas:

```text
src/test/java/emprendelink/
```

---

## Arquitectura prevista de Fase 2

```text
XHTML / JSF
     ↓
Managed Bean
     ↓
Service
     ↓
DAO
     ↓
JPA / Hibernate
     ↓
MySQL
```

---

## Persistencia JPA

Se implementará:

```text
emprendelink/dao/jpa/
```

Entidades:

```text
emprendelink/persistence/jpa/entity/
```

Mappers:

```text
emprendelink/persistence/jpa/mapper/
```

Conversión:

```text
JPA Entity ↔ Domain Model
```

---

## Entidades previstas

```text
RolEntity
UsuarioEntity
EmprendimientoEntity
CategoriaEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

---

## JSF

Managed Beans previstos:

```text
AuthBean
CatalogoBean
CategoriaBean
EmprendimientoBean
PublicacionBean
PedidoBean
```

Las vistas de Fase 2 utilizarán XHTML y Jakarta Faces.

---

## AJAX

Se incorporarán actualizaciones parciales mediante mecanismos de Jakarta Faces cuando aporten funcionalidad real.

Ejemplo:

```text
<f:ajax>
```

---

## Validación

Se utilizarán:

- validaciones de vista;
- Jakarta Validation cuando corresponda;
- validadores JSF;
- reglas de negocio en Services.

---

## Converters

Se incorporarán converters cuando JSF necesite transformar correctamente entre valores de vista y objetos Java.

---

## Transacciones

Las operaciones de múltiples registros, especialmente pedidos y detalles, deberán asegurar:

```text
commit completo
```

o:

```text
rollback completo
```

ante errores.

---

## Trabajo modular

La Fase 2 se divide principalmente en:

```text
Autenticación y usuarios
Categorías
Emprendimientos
Publicaciones y catálogo
Pedidos
```

---

## Documentación actualizada

Para iniciar Fase 2 se actualizaron:

```text
README.md
AGENTS.md
docs/CONVENCIONES.md
docs/ARQUITECTURA.md
docs/FLUJO-GIT.md
docs/RELEASE-NOTES.md
docs/PRODUCT-BACKLOG.md
docs/ROLES-PERMISOS.md
docs/MATRIZ-PRUEBAS.md
```

---

## Pendiente de implementación

- configuración JPA definitiva;
- `persistence.xml`;
- dependencias definitivas de Fase 2;
- entidades JPA;
- relaciones JPA;
- DAO JPA;
- Managed Beans funcionales;
- vistas XHTML;
- AJAX;
- validators;
- converters;
- pruebas automatizadas;
- pruebas de rollback;
- pruebas de integración.

---

# Versiones futuras

## 0.3.0 — Fase 3

Objetivo previsto:

```text
API REST
cliente externo
integración
```

---

## 0.4.0 — Fase 4

Objetivo previsto:

```text
Spring
seguridad
pruebas finales
despliegue
```

---

# Plantilla para futuras versiones

## Versión X.Y.Z

Fecha:

```text
YYYY-MM-DD
```

### Estado

```text
EN DESARROLLO / COMPLETADA
```

### Nuevas funcionalidades

-

### Cambios

-

### Correcciones

-

### Arquitectura

-

### Base de datos

-

### Dependencias

-

### Seguridad

-

### Pruebas

-

### Limitaciones conocidas

-

### Observaciones

-