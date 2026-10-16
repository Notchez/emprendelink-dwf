# Cierre de Fase 2 — EmprendeLink DWF

## Sprint II — Persistencia Empresarial e Integración JSF

EmprendeLink completa el incremento técnico y funcional de Fase 2 mediante:

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

Se mantienen separadas:

```text
Presentación
Negocio
Persistencia
```

---

## 2. Persistencia

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

## 3. Roles

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

La navegación y las operaciones se separan por perfil.

### Admin

- backoffice;
- usuarios;
- emprendedores;
- categorías;
- planes;
- negocios;
- comisiones.

### Emprendedor

- Pedidos;
- Mi negocio;
- Catálogo;
- Ventas;
- Comisiones.

### Cliente

- Explorar;
- Mis pedidos;
- Mi perfil;
- Carrito.

---

## 4. Creación de cuentas

El registro público crea únicamente:

```text
ROLE_CLIENTE
```

Un emprendedor se crea desde administración.

La operación crea:

```text
Usuario emprendedor
+
Negocio asociado
```

con plan y categoría.

---

## 5. Un negocio por emprendedor

La base aplica:

```text
UNIQUE(id_propietario)
```

Por tanto:

```text
Usuario emprendedor 1:0..1 Emprendimiento
```

La interfaz utiliza:

```text
Mi negocio
```

---

## 6. Categoría del negocio

Cada negocio posee:

```text
Categoria
```

La categoría define:

```text
manejaStock
```

Esto determina el comportamiento técnico de sus publicaciones.

---

## 7. Publicaciones

Implementado:

- creación;
- edición;
- activación/desactivación;
- imagen URL;
- precio;
- stock;
- propiedad;
- límite de plan;
- AJAX.

La categoría y el tipo se derivan del negocio.

No existe selector visual de:

```text
Producto / Servicio
```

para el emprendedor.

---

## 8. Catálogo cliente

Implementado:

- agrupación por negocio;
- búsqueda;
- filtro por categoría;
- detalle;
- imágenes;
- acceso a carrito.

El filtro visual de tipo fue retirado.

---

## 9. Carrito

Implementado mediante:

```text
CarritoBean
@SessionScoped
```

Permite:

```text
Agregar
+
-
Papelera
```

y actualiza mediante AJAX:

- cantidad;
- badge;
- subtotal;
- total;
- estado visual.

Puede contener artículos de distintos negocios.

---

## 10. Checkout multi-negocio

El cliente percibe un único carrito.

Internamente:

```text
Carrito
   ↓
agrupar por negocio
   ↓
Pedido A
Pedido B
...
```

Cada pedido pertenece a un solo emprendimiento.

---

## 11. Snapshot de pedido

Al crear se conservan:

```text
idPlanAplicado
nombrePlanAplicado
tasaComision
montoComision
direccionEntrega
```

Esto protege el historial frente a cambios posteriores de:

- plan;
- tasa;
- dirección del perfil.

Al completar:

```text
fechaCompletado
```

---

## 12. Perfil cliente

Nueva ruta:

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

El logout del cliente se encuentra en Mi perfil.

---

## 13. Panel emprendedor

### Pedidos

Landing del perfil.

### Mi negocio

Muestra:

- plan;
- comisión;
- límite de publicaciones;
- espacios disponibles;
- datos de negocio.

Editable por emprendedor:

```text
descripcion
contacto
logoUrl
```

### Ventas

Muestra:

- ventas totales;
- filtro por mes/año;
- detalle de pedidos completados.

### Comisiones

Muestra comisión histórica asociada a ventas.

---

## 14. Estados de pedido

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

---

## 15. Inventario

Para publicaciones con inventario:

```text
crear pedido:
stock -= cantidad

cancelar:
stock += cantidad
```

La interfaz evita aumentar el carrito por encima del stock conocido y el Service vuelve a validar al confirmar.

---

## 16. Seguridad

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

Los Services también aplican:

- roles;
- propiedad;
- estado;
- disponibilidad;
- integridad.

---

## 17. AJAX

Operaciones AJAX relevantes:

- filtros del catálogo;
- edición y estados;
- contador de publicaciones;
- carrito;
- cantidades;
- subtotales;
- total;
- formularios administrativos.

---

## 18. Validaciones

Capas:

```text
JSF
Validadores personalizados
Converter
Services
Bean Validation
Base de datos
```

Validadores:

```text
CorreoValidator
ContrasenaValidator
```

Converter:

```text
TipoRolConverter
```

---

## 19. Base de datos

Base:

```text
emprendelink_dwf
```

Scripts actuales:

```text
database/schema.sql
database/seed.sql
```

El esquema fue consolidado y las migraciones incrementales anteriores fueron retiradas.

---

## 20. Pruebas automatizadas

Última regresión:

```text
41 tests
0 failures
0 errors
0 skipped
BUILD SUCCESS
```

Comando:

```powershell
mvn clean test
```

Antes del PR final se recomienda:

```powershell
mvn clean package
```

---

## 21. Cumplimiento funcional de Fase 2

La solución incluye:

```text
JSF
Managed Beans
JPA
relaciones
CRUD
AJAX
validaciones
navegación por perfil
reglas de negocio
persistencia
seguridad
pruebas
versionamiento
documentación
```

---

## 22. Pendiente para evidencia final

El código funcional de Fase 2 se considera cerrado.

Pendiente de cierre académico:

```text
ejecutar smoke test manual documentado
cerrar estados pendientes de MATRIZ-PRUEBAS.md
capturar evidencias
generar PDF de Sprint Review
confirmar mvn clean package
integrar PR a develop
```

---

## 23. Preparación para Fase 3

Existen scaffolds en:

```text
emprendelink.web.rest
emprendelink.dto
```

pero no constituyen una API funcional.

Fase 3 deberá implementar:

```text
REST
JSON
DTO reales
códigos HTTP
manejo de errores
seguridad REST
cliente consumidor
pruebas de API
documentación de endpoints
```

reutilizando:

```text
Services
 ↓
DAO
 ↓
JPA
```