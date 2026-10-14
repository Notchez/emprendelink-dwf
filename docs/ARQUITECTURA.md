# Arquitectura — EmprendeLink DWF

## 1. Estado actual

EmprendeLink se encuentra en la Fase 2 — Persistencia Empresarial e Integración JSF.

La arquitectura activa es:

```text
XHTML / JSF
      ↓
Managed Beans CDI
      ↓
Services
      ↓
Interfaces DAO
      ↓
DAO JPA
      ↓
Entidades JPA
      ↓
EclipseLink
      ↓
JTA
      ↓
GlassFish DataSource
      ↓
MySQL
```

La arquitectura anterior basada en JDBC, Servlets y JSP fue retirada del runtime para evitar mantener dos implementaciones paralelas.

---

## 2. Principio de separación

La aplicación separa presentación, negocio y persistencia.

### Presentación

```text
src/main/webapp/
src/main/java/emprendelink/web/jsf/
```

Responsabilidades:

- mostrar información;
- recibir datos del usuario;
- ejecutar acciones JSF;
- utilizar AJAX;
- mostrar mensajes;
- invocar Services.

Los Managed Beans no ejecutan consultas JPA directamente.

### Negocio

```text
src/main/java/emprendelink/service/
src/main/java/emprendelink/service/impl/
```

Responsabilidades:

- reglas de negocio;
- autorización funcional;
- validaciones independientes de la interfaz;
- coordinación entre diferentes DAO;
- límites transaccionales.

### Persistencia

```text
src/main/java/emprendelink/dao/
src/main/java/emprendelink/dao/jpa/
src/main/java/emprendelink/persistence/jpa/
```

Responsabilidades:

- persistencia;
- consultas;
- relaciones JPA;
- transformación entre modelo de dominio y entidades JPA.

---

## 3. Modelo de dominio

El modelo se encuentra en:

```text
emprendelink.model
```

Clases principales:

```text
Rol
Usuario
Categoria
Emprendimiento
Publicacion
Pedido
DetallePedido
```

Enumeraciones:

```text
TipoRol
TipoPublicacion
EstadoPedido
```

El modelo de dominio no depende de JSF ni JPA.

---

## 4. Entidades JPA

Ubicación:

```text
emprendelink.persistence.jpa.entity
```

Entidades implementadas:

```text
RolEntity
UsuarioEntity
CategoriaEntity
EmprendimientoEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

Las entidades utilizan anotaciones Jakarta Persistence para mapear:

- tablas;
- columnas;
- claves primarias;
- claves foráneas;
- relaciones;
- restricciones;
- enumeraciones.

---

## 5. Relaciones

Relaciones principales:

```text
Rol 1:N Usuario

Usuario 1:N Emprendimiento

Emprendimiento 1:N Publicacion

Categoria 1:N Publicacion

Usuario 1:N Pedido

Emprendimiento 1:N Pedido

Pedido 1:N DetallePedido

Publicacion 1:N DetallePedido
```

Representación simplificada:

```text
Rol
└── Usuario
    ├── Emprendimiento
    │   ├── Publicacion
    │   │   └── DetallePedido
    │   └── Pedido
    └── Pedido

Categoria
└── Publicacion
```

---

## 6. Mappers

Ubicación:

```text
emprendelink.persistence.jpa.mapper
```

Mappers implementados para los módulos persistentes.

Responsabilidad:

```text
Domain Model ↔ JPA Entity
```

Los mappers no contienen reglas de negocio.

---

## 7. DAO

Contratos:

```text
emprendelink.dao
```

Implementaciones:

```text
emprendelink.dao.jpa
```

Ejemplos:

```text
JpaRolDAO
JpaUsuarioDAO
JpaCategoriaDAO
JpaEmprendimientoDAO
JpaPublicacionDAO
JpaPedidoDAO
```

Los Services dependen de interfaces DAO y no directamente de `EntityManager`.

---

## 8. Persistence Unit

Persistence Unit oficial:

```text
emprendelinkPU
```

Configuración:

```text
src/main/resources/META-INF/persistence.xml
```

Proveedor:

```text
EclipseLink
```

EclipseLink es provisto por GlassFish.

Hibernate no se incluye en el WAR.

---

## 9. EntityManager

Las implementaciones DAO utilizan:

```java
@PersistenceContext(unitName = "emprendelinkPU")
private EntityManager entityManager;
```

No se utiliza:

```text
EntityManagerFactory manual
EntityTransaction manual
RESOURCE_LOCAL
```

---

## 10. Transacciones

La aplicación utiliza JTA — Jakarta Transactions.

Los límites transaccionales se gestionan principalmente desde Services mediante:

```java
@Transactional
```

Ejemplo de operación transaccional:

```text
Crear pedido
    ↓
validar cliente
    ↓
validar emprendimiento
    ↓
validar publicaciones
    ↓
crear Pedido + DetallePedido
    ↓
actualizar stock
    ↓
commit
```

Si ocurre una excepción durante la operación transaccional, JTA puede revertir los cambios realizados dentro de la transacción.

---

## 11. DataSource

JNDI oficial:

```text
jdbc/EmprendeLinkDS
```

Pool:

```text
EmprendeLinkPool
```

GlassFish administra las conexiones hacia MySQL.

Las credenciales de base de datos no forman parte del código fuente.

---

## 12. Managed Beans JSF

Ubicación:

```text
emprendelink.web.jsf
```

Beans principales:

```text
AuthBean
UsuarioBean
CategoriaBean
EmprendimientoBean
PublicacionBean
PedidoBean
```

Responsabilidades:

- recibir interacción de XHTML;
- consultar Services;
- preparar datos para las vistas;
- mostrar mensajes;
- ejecutar navegación;
- coordinar actualizaciones AJAX.

---

## 13. Validación

La validación se realiza en diferentes niveles.

### JSF

Incluye:

- campos requeridos;
- límites;
- conversión de valores;
- mensajes;
- validación mediante AJAX.

### Validadores personalizados

Ubicación:

```text
emprendelink.web.jsf.validator
```

Incluye:

```text
CorreoValidator
ContrasenaValidator
```

### Converters

Ubicación:

```text
emprendelink.web.jsf.converter
```

Incluye:

```text
TipoRolConverter
```

### Services

Los Services validan reglas que no deben depender de la interfaz.

Ejemplos:

- roles;
- propiedad de recursos;
- estados;
- disponibilidad;
- stock;
- relaciones válidas.

### Bean Validation

Las entidades JPA utilizan restricciones cuando corresponde.

---

## 14. AJAX

Jakarta Faces utiliza:

```xml
<f:ajax>
```

Se utiliza en operaciones como:

- validación de formularios;
- actualización de tablas;
- cambio de estados;
- filtros;
- edición;
- activación y desactivación.

Las operaciones no requieren recargar completamente la vista cuando no es necesario.

---

## 15. Seguridad

La autenticación utiliza sesión HTTP.

Atributo de sesión:

```text
usuarioAutenticado
```

Roles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

Filtros principales:

```text
AutenticacionFilter
AdminFilter
EmprendedorFilter
PedidosFilter
```

La seguridad se aplica tanto por ruta como mediante reglas de negocio dentro de los Services.

---

## 16. Eliminación lógica

Para conservar integridad e historial, varios módulos utilizan eliminación lógica.

Entidades con atributo:

```text
activo
```

Incluyen:

```text
Usuario
Categoria
Emprendimiento
Publicacion
```

En lugar de eliminar físicamente el registro, se cambia su estado.

Esto reduce problemas con información histórica y relaciones existentes.

---

## 17. Pedidos

Un `Pedido` representa el agregado principal.

```text
Pedido
├── Cliente
├── Emprendimiento
├── Estado
├── Total
└── Detalles
    ├── Publicacion
    ├── Cantidad
    ├── Precio unitario
    └── Subtotal
```

`DetallePedidoEntity` se persiste junto con `PedidoEntity` mediante la relación correspondiente.

---

## 18. Estados del pedido

Estados internos:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Transiciones permitidas:

```text
PENDIENTE
├── CONFIRMADO
└── CANCELADO

CONFIRMADO
├── EN_PROCESO
└── CANCELADO

EN_PROCESO
├── COMPLETADO
└── CANCELADO
```

Estados terminales:

```text
COMPLETADO
CANCELADO
```

En la interfaz para cliente:

```text
EN_PROCESO → En camino
COMPLETADO → Entregado
```

El nombre interno permanece independiente de la presentación.

---

## 19. Stock

Para publicaciones de tipo:

```text
PRODUCTO
```

al crear un pedido:

```text
stock = stock - cantidad
```

Al cancelar un pedido antes de llegar a un estado terminal:

```text
stock = stock + cantidad
```

Las publicaciones de tipo:

```text
SERVICIO
```

no utilizan stock como restricción de inventario.

---

## 20. Arquitectura preparada para Fase 3

La futura API REST deberá ubicarse en:

```text
emprendelink.web.rest
```

y reutilizar:

```text
REST
 ↓
Services existentes
 ↓
DAO
 ↓
JPA
 ↓
MySQL
```

No se debe duplicar la lógica de negocio dentro de los endpoints.

Ruta base prevista:

```text
/api/v1
```

---

## 21. Fase 4

El paquete:

```text
emprendelink.spring
```

está reservado para la futura integración con Spring.

Los módulos Spring seleccionados deberán reutilizar el dominio y las reglas existentes siempre que sea posible.