# Cierre de Fase 2 — EmprendeLink DWF

## Sprint II — Persistencia Empresarial e Integración JSF

EmprendeLink completa el incremento técnico correspondiente a la Fase 2 mediante Jakarta Faces, CDI, JPA, JTA, EclipseLink, GlassFish y MySQL.

---

## 1. Arquitectura

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
GlassFish
      ↓
MySQL
```

La aplicación mantiene separadas:

```text
Presentación
Negocio
Persistencia
```

La arquitectura JDBC/Servlet/JSP anterior fue retirada del runtime.

---

## 2. Módulos entregados

### Autenticación

- login;
- logout;
- registro;
- sesión;
- roles;
- contraseña segura.

### Usuarios

- listado;
- roles;
- activación/desactivación;
- validaciones;
- AJAX.

### Categorías

- crear;
- listar;
- editar;
- activar/desactivar;
- AJAX.

### Emprendimientos

- crear;
- listar propios;
- editar;
- propiedad;
- estado administrativo;
- plan.

### Publicaciones

- productos;
- servicios;
- categorías;
- negocio;
- precio;
- stock;
- creación;
- edición;
- estado;
- límite de publicaciones por plan.

### Catálogo

- publicaciones activas;
- búsqueda;
- filtros;
- detalle.

### Pedidos

- creación;
- detalles;
- total;
- inventario;
- pedidos propios;
- pedidos recibidos;
- estados;
- cancelación.

### Planes

```text
Básico    → 10  → 7%
Estándar  → 30  → 5%
Premium   → 60  → 3%
Max       → 100 → 1%
```

### Comisiones

Al completar un pedido se conserva:

```text
fechaCompletado
tasaComision
montoComision
```

---

## 3. Persistencia JPA

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

Persistence Unit:

```text
emprendelinkPU
```

Proveedor:

```text
EclipseLink
```

DataSource:

```text
jdbc/EmprendeLinkDS
```

Transacciones:

```text
JTA
```

---

## 4. Relaciones

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

---

## 5. Reglas de negocio

### Usuarios

Un usuario inactivo no puede operar normalmente en rutas protegidas.

### Emprendimientos

Solo el propietario puede modificar sus emprendimientos.

El administrador gestiona su estado y plan.

### Publicaciones

Una publicación debe:

- pertenecer a un emprendimiento válido;
- utilizar una categoría activa;
- tener precio válido;
- respetar stock;
- respetar el límite de publicaciones activas del plan.

El límite se valida:

```text
al crear
al reactivar
al asignar plan
```

### Pedidos

Un pedido debe:

- pertenecer a un cliente;
- dirigirse a un emprendimiento;
- contener publicaciones válidas;
- utilizar cantidades válidas;
- respetar stock;
- calcular su total.

---

## 6. Estados de pedido

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Transiciones:

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

Presentación:

```text
EN_PROCESO = En camino
COMPLETADO = Entregado
```

---

## 7. Inventario

Al realizar un pedido de producto:

```text
stock -= cantidad
```

Al cancelar:

```text
stock += cantidad
```

Las publicaciones de tipo servicio no utilizan stock como restricción de inventario.

---

## 8. Planes

Cada emprendimiento puede tener un plan.

Planes:

```text
Básico
Estándar
Premium
Max
```

El plan define:

```text
maxProductosActivos
tasaComision
```

Configuración:

```text
Básico    → 10 publicaciones  → 7%
Estándar  → 30 publicaciones  → 5%
Premium   → 60 publicaciones  → 3%
Max       → 100 publicaciones → 1%
```

La aplicación impide superar el máximo de publicaciones activas.

---

## 9. Comisiones

Cuando un pedido pasa a:

```text
COMPLETADO
```

se toma la tasa del plan del emprendimiento.

Cálculo:

```text
montoComision = total × tasaComision
```

Se persisten:

```text
tasaComision
montoComision
fechaCompletado
```

Esto conserva la información histórica aunque el plan sea modificado posteriormente.

---

## 10. Eliminación lógica

Utilizan estado:

```text
activo
```

los módulos:

```text
Usuario
Categoria
Plan
Emprendimiento
Publicacion
```

La eliminación lógica preserva relaciones e historial.

---

## 11. AJAX

Se utiliza:

```xml
<f:ajax>
```

en operaciones de:

- validación;
- formularios;
- edición;
- filtros;
- activación/desactivación;
- estados;
- administración.

---

## 12. Validaciones

La solución utiliza:

```text
JSF
Validadores personalizados
Converters
Services
Bean Validation
Base de datos
```

Validadores activos:

```text
CorreoValidator
ContrasenaValidator
```

Converter activo:

```text
TipoRolConverter
```

---

## 13. Seguridad

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

Los Services también aplican autorización funcional.

---

## 14. Pruebas automatizadas

Regresión:

```text
41 pruebas
0 failures
0 errors
0 skipped
BUILD SUCCESS
```

Comando:

```bash
mvn clean test
```

Build:

```bash
mvn clean package
```

Resultado:

```text
BUILD SUCCESS
```

Artefacto:

```text
target/emprendelink.war
```

---

## 15. Integración continua

GitHub Actions ejecuta:

```text
mvn -B clean test
```

El Pull Request utilizado para integrar el incremento completo de Fase 2 pasó correctamente el pipeline.

---

## 16. Base de datos

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

## 17. Deploy

Servidor:

```text
GlassFish 8.0.4
```

Context path:

```text
/emprendelink
```

Aplicación:

```text
http://localhost:8080/emprendelink/login.xhtml
```

---

## 18. Estado del cierre

Completado:

```text
Arquitectura
JPA
JTA
JSF
CDI
CRUD
AJAX
Validaciones
Roles
Pedidos
Inventario
Planes
Comisiones
41 pruebas automatizadas
Build WAR
CI
Documentación técnica
```

Pendiente antes de generar el documento final:

```text
Smoke test manual final
Actualización definitiva de MATRIZ-PRUEBAS.md
Capturas de evidencias
PDF de Sprint Review
```

---

## 19. Preparación para Fase 3

La siguiente fase podrá reutilizar:

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

Sin duplicar reglas de negocio ni persistencia.