# Arquitectura — EmprendeLink DWF

## 1. Estado

EmprendeLink completa la arquitectura correspondiente a:

```text
Fase 2 — Persistencia Empresarial e Integración JSF
Sprint II
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

La arquitectura JDBC/Servlet/JSP utilizada en Fase 1 fue retirada del runtime.

---

## 2. Separación de responsabilidades

### Presentación

Ubicación:

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
- mantener estado de vista o sesión cuando corresponde;
- invocar Services.

Los Managed Beans no ejecutan consultas JPA directamente.

### Negocio

Ubicación:

```text
src/main/java/emprendelink/service/
src/main/java/emprendelink/service/impl/
```

Responsabilidades:

- validaciones;
- autorización funcional;
- propiedad de recursos;
- reglas de inventario;
- planes;
- límites;
- comisiones;
- agrupación de pedidos;
- transiciones de estado;
- coordinación transaccional.

### Persistencia

Ubicación:

```text
src/main/java/emprendelink/dao/
src/main/java/emprendelink/dao/jpa/
src/main/java/emprendelink/persistence/jpa/
```

Responsabilidades:

- CRUD;
- consultas;
- persistencia;
- relaciones;
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

El modelo no depende directamente de:

```text
JSF
XHTML
EntityManager
JPA Entity
REST
Spring
```

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

---

## 5. Relaciones principales

```text
Rol 1:N Usuario

Usuario emprendedor 1:0..1 Emprendimiento

Plan 1:N Emprendimiento

Categoria 1:N Emprendimiento

Emprendimiento 1:N Publicacion

Categoria 1:N Publicacion

Usuario cliente 1:N Pedido

Emprendimiento 1:N Pedido

Pedido 1:N DetallePedido

Publicacion 1:N DetallePedido
```

La restricción:

```text
UNIQUE (emprendimientos.id_propietario)
```

garantiza que un emprendedor no tenga más de un negocio.

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

DAO JPA principales:

```text
JpaRolDAO
JpaUsuarioDAO
JpaCategoriaDAO
JpaPlanDAO
JpaEmprendimientoDAO
JpaPublicacionDAO
JpaPedidoDAO
```

Los Services dependen de interfaces DAO.

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

---

## 9. Transacciones

Los Services utilizan:

```java
@Transactional
```

Flujo general de checkout:

```text
validar cliente
      ↓
validar carrito
      ↓
resolver publicaciones
      ↓
validar negocio / categoría / stock
      ↓
agrupar por emprendimiento
      ↓
crear un Pedido por negocio
      ↓
congelar plan y comisión
      ↓
copiar dirección de entrega
      ↓
crear detalles
      ↓
descontar stock
      ↓
commit
```

Si una operación transaccional falla, JTA permite revertir el conjunto de cambios.

---

## 10. Managed Beans

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
AdminEmprendedorBean
CatalogoBean
ClientePerfilBean
CarritoBean
```

### `CarritoBean`

Alcance:

```text
@SessionScoped
```

Motivo:

El carrito debe mantenerse mientras el cliente navega entre catálogo, detalle, carrito y checkout.

Responsabilidades:

- cantidades;
- grupos por negocio;
- total;
- subtotales;
- validación de stock visible;
- datos temporales del checkout;
- confirmación del pedido.

### `ClientePerfilBean`

Alcance:

```text
@ViewScoped
```

Responsabilidades:

- cargar perfil;
- editar teléfono;
- editar dirección;
- refrescar el usuario almacenado en sesión.

### `AdminEmprendedorBean`

Responsabilidad:

```text
crear ROLE_EMPRENDEDOR
+
crear su único Emprendimiento
```

La operación se delega a `AdminEmprendedorService`.

---

## 11. Perfiles y navegación

### Admin

Landing:

```text
/admin/inicio.xhtml
```

### Emprendedor

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

### Cliente

Landing:

```text
/catalogo/index.xhtml
```

Navegación:

```text
Explorar
Mis pedidos
Mi perfil
Carrito
```

---

## 12. Catálogo y publicaciones

El negocio tiene una categoría administrativa.

La categoría define:

```text
manejaStock
```

La publicación continúa conservando técnicamente:

```text
Categoria
TipoPublicacion
```

pero el emprendedor no los selecciona manualmente.

Resolución:

```text
categoria del negocio
      ↓
manejaStock = true
      → PRODUCTO
      → stock

manejaStock = false
      → SERVICIO
      → stock = 0
```

Esto mantiene compatibilidad con el dominio actual y simplifica la interfaz.

---

## 13. AJAX

Se utiliza:

```xml
<f:ajax>
```

en flujos como:

- categorías;
- publicaciones;
- búsqueda de catálogo;
- activación/desactivación;
- contador de plan;
- carrito;
- cambios de cantidades;
- actualización de totales;
- estados y formularios.

El objetivo es mantener el estado de interacción sin recargar completamente la vista.

---

## 14. Validación

La aplicación utiliza varias capas:

### JSF

- required;
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
- usuarios activos;
- negocio;
- categoría;
- plan;
- límite de publicaciones;
- stock;
- transiciones de pedido;
- dirección;
- precios;
- datos requeridos.

### Bean Validation

Las entidades utilizan anotaciones como:

```text
@NotNull
@NotBlank
@Size
@DecimalMin
@DecimalMax
@Digits
```

### Base de datos

La base utiliza:

```text
PRIMARY KEY
FOREIGN KEY
UNIQUE
NOT NULL
CHECK
```

---

## 15. Seguridad

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
CatalogoFilter
ClienteFilter
EmprendedorFilter
PedidosFilter
PublicacionFilter
```

La autorización no depende únicamente de ocultar opciones en la interfaz.

Modelo:

```text
Request
  ↓
Filter
  ↓
Managed Bean
  ↓
Service
  ↓
rol + propiedad + estado
  ↓
DAO
```

---

## 16. Administración de emprendedores

La creación de emprendedores es responsabilidad exclusiva del administrador.

Flujo:

```text
ROLE_ADMIN
   ↓
AdminEmprendedorBean
   ↓
AdminEmprendedorService
   ↓
crear Usuario ROLE_EMPRENDEDOR
   ↓
crear Emprendimiento
      ├── propietario
      ├── plan
      ├── categoría
      ├── nombre
      ├── contacto
      └── logo
```

El registro público únicamente crea clientes.

---

## 17. Negocio del emprendedor

El emprendedor puede modificar:

```text
descripcion
contacto
logoUrl
```

Los datos administrativos permanecen fuera de su edición normal:

```text
nombre
categoria
plan
estado
```

---

## 18. Planes y límites

Planes:

```text
Básico    → 10 publicaciones activas → 7%
Estándar  → 30 publicaciones activas → 5%
Premium   → 60 publicaciones activas → 3%
Max       → 100 publicaciones activas → 1%
```

El límite se valida:

```text
al crear publicación
al reactivar publicación
al asignar/cambiar plan
```

---

## 19. Pedido y snapshot comercial

Un pedido contiene:

```text
Pedido
├── Cliente
├── Emprendimiento
├── Estado
├── Total
├── Id plan aplicado
├── Nombre plan aplicado
├── Tasa comisión
├── Monto comisión
├── Dirección entrega
├── Observaciones
├── Fecha pedido
├── Fecha completado
└── Detalles
```

Al crear el pedido se congelan:

```text
idPlanAplicado
nombrePlanAplicado
tasaComision
montoComision
direccionEntrega
```

Al completar se registra:

```text
fechaCompletado
```

---

## 20. Carrito multi-negocio

La interfaz presenta un solo carrito.

Internamente:

```text
Carrito
├── Negocio A
│   ├── publicación 1
│   └── publicación 2
└── Negocio B
    └── publicación 3
```

Durante checkout:

```text
Grupo A → Pedido A
Grupo B → Pedido B
```

Esto conserva la relación:

```text
Pedido N:1 Emprendimiento
```

y permite aplicar correctamente el plan y comisión de cada negocio.

---

## 21. Inventario

Para publicaciones técnicas `PRODUCTO`:

```text
crear pedido:
stock -= cantidad

cancelar pedido:
stock += cantidad
```

Para `SERVICIO`:

```text
stock = 0
sin restricción de cantidad física
```

---

## 22. Base de datos

Base:

```text
emprendelink_dwf
```

Scripts oficiales:

```text
database/schema.sql
database/seed.sql
```

No se mantienen migraciones incrementales de Fase 2 en el estado actual del proyecto.

---

## 23. Calidad

Última regresión ejecutada:

```text
41 tests
0 failures
0 errors
BUILD SUCCESS
```

CI:

```text
GitHub Actions
mvn -B clean test
```

---

## 24. Preparación para Fase 3

Existen clases preparatorias vacías en:

```text
emprendelink.web.rest
emprendelink.dto
```

Estas clases **no representan una API REST funcional todavía**.

Arquitectura prevista:

```text
REST
 ↓
DTO
 ↓
Services existentes
 ↓
DAO
 ↓
JPA
 ↓
MySQL
```

La implementación formal de endpoints pertenece a Fase 3.

---

## 25. Preparación para Fase 4

El paquete:

```text
emprendelink.spring
```

permanece reservado para la integración posterior con Spring.

La arquitectura permite reutilizar:

- dominio;
- Services;
- DAO;
- persistencia;
- reglas de negocio;
- pruebas.