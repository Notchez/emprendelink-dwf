# Release Notes — EmprendeLink DWF

# Fase 1 — Sprint I

Se construyó el primer incremento funcional utilizando:

- Java Web;
- JDBC;
- Servlets;
- JSP;
- JSTL;
- Services;
- DAO;
- MySQL;
- arquitectura MVC.

Esta versión permitió validar:

- dominio;
- esquema inicial de base de datos;
- flujo petición-respuesta;
- acceso a datos;
- primeras funcionalidades.

---

# Fase 2 — Sprint II

La aplicación evolucionó hacia una arquitectura empresarial basada en Jakarta EE y persistencia administrada.

---

## Arquitectura

Arquitectura actual:

```text
XHTML / JSF
      ↓
Managed Beans CDI
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
GlassFish DataSource
      ↓
MySQL
```

Se retiraron del runtime:

- implementaciones JDBC anteriores;
- Servlets de Fase 1;
- JSP de Fase 1;
- `DriverManager`;
- conexión manual;
- `EntityManagerFactory` manual;
- Hibernate empaquetado en el WAR.

---

## Persistencia

Entidades JPA implementadas:

```text
RolEntity
UsuarioEntity
CategoriaEntity
EmprendimientoEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

Persistence Unit:

```text
emprendelinkPU
```

Proveedor:

```text
EclipseLink
```

Transacciones:

```text
JTA
```

DataSource:

```text
jdbc/EmprendeLinkDS
```

---

## Autenticación y usuarios

Implementado:

- login JSF;
- registro;
- roles;
- sesión;
- logout;
- administración de usuarios;
- activación/desactivación;
- validación de correo;
- validación de contraseña;
- hashing PBKDF2;
- AJAX.

---

## Categorías

Implementado:

- persistencia JPA;
- mapper;
- DAO;
- Service;
- gestión JSF;
- creación;
- edición;
- activación/desactivación;
- AJAX.

---

## Emprendimientos

Implementado:

- persistencia JPA;
- relación con propietario;
- mapper;
- DAO;
- Service;
- creación;
- edición;
- listado por propietario;
- validación de propiedad;
- administración del estado.

---

## Publicaciones

Implementado:

- persistencia JPA;
- relación con emprendimiento;
- relación con categoría;
- creación;
- edición;
- activación/desactivación;
- productos;
- servicios;
- precio;
- stock;
- reglas de propietario.

---

## Catálogo

Implementado:

- listado de publicaciones visibles;
- búsqueda;
- filtros;
- detalle;
- categorías;
- tipos;
- navegación hacia pedidos.

---

## Pedidos

Implementado:

- `PedidoEntity`;
- `DetallePedidoEntity`;
- mappers;
- DAO JPA;
- Service;
- creación de pedido;
- cálculo de total;
- relaciones;
- validación de usuario;
- validación de emprendimiento;
- validación de publicaciones;
- control de stock;
- restauración de stock al cancelar;
- consulta de pedidos propios;
- pedidos recibidos;
- detalle.

---

## Estados de pedido

Estados internos:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Transiciones válidas:

```text
PENDIENTE → CONFIRMADO
PENDIENTE → CANCELADO

CONFIRMADO → EN_PROCESO
CONFIRMADO → CANCELADO

EN_PROCESO → COMPLETADO
EN_PROCESO → CANCELADO
```

En interfaz:

```text
EN_PROCESO → En camino
COMPLETADO → Entregado
```

---

## Seguridad

Implementado:

```text
AutenticacionFilter
AdminFilter
EmprendedorFilter
PedidosFilter
```

Además, los Services validan:

- roles;
- propiedad;
- estado;
- disponibilidad.

---

## AJAX y validaciones

Implementado:

- `<f:ajax>`;
- validadores personalizados;
- converter;
- mensajes JSF;
- validación en cliente/interfaz;
- validación de reglas en Service;
- Bean Validation en persistencia.

---

## Pruebas

La línea estable cuenta actualmente con:

```text
39 pruebas automatizadas
```

Resultado esperado:

```text
Failures: 0
Errors: 0
```

Regresión:

```bash
mvn clean test
mvn clean package
```

También se realizaron pruebas manuales sobre:

- login;
- publicaciones;
- catálogo;
- pedidos;
- estados;
- stock;
- cancelación;
- deploy en GlassFish.

---

## Infraestructura

Servidor:

```text
GlassFish 8.0.4
```

Base:

```text
MySQL 8
```

WAR:

```text
target/emprendelink.war
```

Context path:

```text
/emprendelink
```

---

## Decisiones técnicas

### Eliminación lógica

Usuarios, categorías, emprendimientos y publicaciones pueden desactivarse sin eliminación física.

Objetivo:

- preservar relaciones;
- mantener historial;
- evitar pérdida de datos relacionados.

### Servicios reutilizables

La lógica de negocio permanece fuera de JSF para que Fase 3 pueda reutilizarla desde REST.

### EclipseLink

JPA utiliza EclipseLink, proveedor integrado con GlassFish.

Hibernate no forma parte del runtime actual.

---

# Próximo incremento — Fase 3

Pendiente:

- API REST;
- JAX-RS o tecnología autorizada;
- JSON;
- DTO;
- manejo de errores;
- seguridad REST;
- cliente consumidor;
- pruebas de API;
- documentación de endpoints.

La API reutilizará la capa Service y persistencia implementadas durante Fase 2.