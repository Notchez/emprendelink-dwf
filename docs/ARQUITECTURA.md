# Arquitectura — EmprendeLink DWF

## 1. Estado

EmprendeLink completa la arquitectura correspondiente a la Fase 2:

```text
Persistencia Empresarial e Integración JSF
```

Arquitectura activa:

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

La arquitectura JDBC/Servlet/JSP utilizada durante el incremento anterior fue retirada del runtime.

---

## 2. Separación de responsabilidades

### Presentación

```text
src/main/webapp/
src/main/java/emprendelink/web/jsf/
```

Responsabilidades:

- mostrar información;
- recibir interacción;
- navegación;
- AJAX;
- mensajes JSF;
- invocar Services.

Los Managed Beans no ejecutan consultas JPA.

### Negocio

```text
src/main/java/emprendelink/service/
src/main/java/emprendelink/service/impl/
```

Responsabilidades:

- reglas de negocio;
- validación;
- roles;
- propiedad de recursos;
- transiciones de estado;
- inventario;
- planes;
- límites;
- coordinación entre DAO;
- límites transaccionales.

### Persistencia

```text
src/main/java/emprendelink/dao/
src/main/java/emprendelink/dao/jpa/
src/main/java/emprendelink/persistence/jpa/
```

Responsabilidades:

- operaciones CRUD;
- consultas;
- relaciones;
- persistencia;
- transformación dominio ↔ entidad.

---

## 3. Modelo de dominio

Clases principales:

```text
Rol
Usuario
Categoria
Plan
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

El modelo de dominio no depende directamente de JSF ni de las entidades JPA.

---

## 4. Entidades JPA

Entidades:

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

Las entidades representan:

- tablas;
- columnas;
- claves;
- relaciones;
- restricciones;
- enumeraciones.

---

## 5. Relaciones principales

```text
Rol 1:N Usuario

Usuario 1:N Emprendimiento

Plan 1:N Emprendimiento

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
    │   ├── Plan
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

Principales DAO JPA:

```text
JpaRolDAO
JpaUsuarioDAO
JpaCategoriaDAO
JpaPlanDAO
JpaEmprendimientoDAO
JpaPublicacionDAO
JpaPedidoDAO
```

Los Services dependen de interfaces DAO y no directamente de `EntityManager`.

---

## 8. Persistence Unit

Persistence Unit:

```text
emprendelinkPU
```

Archivo:

```text
src/main/resources/META-INF/persistence.xml
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

Entidades declaradas:

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

---

## 9. EntityManager

Los DAO utilizan:

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

Los Services utilizan Jakarta Transactions mediante:

```java
@Transactional
```

Ejemplo de creación de pedido:

```text
validar cliente
    ↓
validar negocio
    ↓
validar publicaciones
    ↓
validar stock
    ↓
crear pedido
    ↓
crear detalles
    ↓
descontar stock
    ↓
commit
```

Ante una excepción dentro de la operación transaccional, JTA puede revertir los cambios.

---

## 11. Managed Beans

Beans principales:

```text
AuthBean
UsuarioBean
CategoriaBean
EmprendimientoBean
PublicacionBean
PedidoBean
PlanBean
AdminNegocioBean
AdminComisionBean
```

Responsabilidades:

- recibir interacción JSF;
- preparar datos;
- mostrar mensajes;
- realizar navegación;
- coordinar AJAX;
- invocar Services.

---

## 12. AJAX

Se utiliza:

```xml
<f:ajax>
```

en flujos de:

- login;
- registro;
- usuarios;
- categorías;
- emprendimientos;
- publicaciones;
- catálogo;
- pedidos;
- planes;
- comisiones.

---

## 13. Validación

La aplicación utiliza varias capas de validación.

### JSF

- campos obligatorios;
- mensajes;
- conversión;
- AJAX.

### Validadores personalizados

```text
CorreoValidator
ContrasenaValidator
```

### Converter

```text
TipoRolConverter
```

### Services

Validan:

- roles;
- propiedad;
- estados;
- stock;
- categorías;
- negocios;
- planes;
- límites;
- datos requeridos.

### Bean Validation

Las entidades utilizan restricciones como:

```text
@NotNull
@NotBlank
@Size
@Min
@DecimalMin
@Digits
```

---

## 14. Seguridad

Roles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

Filtros:

```text
AutenticacionFilter
AdminFilter
EmprendedorFilter
PedidosFilter
PublicacionFilter
```

La autorización también se valida dentro de los Services.

---

## 15. Eliminación lógica

Utilizan estado `activo`:

```text
Usuario
Categoria
Plan
Emprendimiento
Publicacion
```

Esto permite preservar:

- relaciones;
- integridad;
- historial;
- pedidos existentes.

---

## 16. Planes

Cada emprendimiento puede tener un plan.

Planes:

```text
Básico
Estándar
Premium
Max
```

Límites de publicaciones activas:

```text
Básico    → 10
Estándar  → 30
Premium   → 60
Max       → 100
```

Tasas de comisión:

```text
Básico    → 7%
Estándar  → 5%
Premium   → 3%
Max       → 1%
```

El límite se valida:

```text
al crear una publicación
al reactivar una publicación
al asignar un plan
```

No se permite superar:

```text
Plan.maxProductosActivos
```

---

## 17. Pedidos

Un pedido contiene:

```text
Pedido
├── Cliente
├── Emprendimiento
├── Estado
├── Total
├── Fecha pedido
├── Fecha completado
├── Tasa comisión
├── Monto comisión
└── Detalles
    ├── Publicacion
    ├── Cantidad
    ├── Precio unitario
    └── Subtotal
```

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

Transiciones:

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

Presentación:

```text
EN_PROCESO → En camino
COMPLETADO → Entregado
```

---

## 19. Inventario

Para publicaciones:

```text
PRODUCTO
```

al crear un pedido:

```text
stock -= cantidad
```

Al cancelar:

```text
stock += cantidad
```

Las publicaciones:

```text
SERVICIO
```

no utilizan inventario como restricción.

---

## 20. Comisiones

Cuando un pedido cambia por primera vez a:

```text
COMPLETADO
```

se obtiene la tasa del plan asociado al emprendimiento.

Se persisten:

```text
tasaComision
montoComision
fechaCompletado
```

Cálculo:

```text
montoComision = totalPedido × tasaComision
```

La tasa queda almacenada en el pedido para conservar el valor histórico aunque posteriormente cambie el plan.

---

## 21. Base de datos

Base:

```text
emprendelink_dwf
```

Scripts:

```text
database/schema.sql
database/seed.sql
database/migrations/002_planes_comisiones.sql
database/migrations/003_fecha_completado.sql
```

---

## 22. Calidad

Pruebas automatizadas:

```text
41
```

Resultado:

```text
Failures: 0
Errors: 0
BUILD SUCCESS
```

Build:

```text
target/emprendelink.war
```

CI:

```text
GitHub Actions
mvn -B clean test
```

---

## 23. Preparación para Fase 3

La API REST reutilizará:

```text
REST
 ↓
Services
 ↓
DAO
 ↓
JPA
 ↓
MySQL
```

Ruta prevista:

```text
/api/v1
```

Los endpoints no deberán duplicar reglas de negocio.

---

## 24. Preparación para Fase 4

El paquete:

```text
emprendelink.spring
```

permanece reservado para la integración posterior con Spring.

La arquitectura permite reutilizar:

- dominio;
- Services;
- reglas;
- DAO;
- persistencia.