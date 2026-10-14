# Cierre de Fase 2 — EmprendeLink DWF

## Sprint II — Persistencia Empresarial e Integración JSF

Este documento resume el incremento desarrollado durante la Fase 2 del proyecto EmprendeLink.

---

# 1. Objetivo alcanzado

La aplicación evolucionó desde la arquitectura utilizada en Fase 1 hacia una solución empresarial basada en:

```text
Jakarta Faces
Jakarta CDI
Jakarta Persistence
Jakarta Transactions
EclipseLink
GlassFish
MySQL
```

La aplicación integra:

```text
Interfaz
↓
Lógica de negocio
↓
Persistencia
↓
Base de datos
```

manteniendo separación de responsabilidades.

---

# 2. Arquitectura final de Fase 2

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
GlassFish
      ↓
MySQL
```

---

# 3. Módulos entregados

## Autenticación

Incluye:

- login;
- logout;
- sesión;
- registro;
- roles;
- contraseña segura.

---

## Usuarios

Incluye:

- listado;
- roles;
- activación/desactivación;
- restricciones de administración.

---

## Categorías

Incluye:

- crear;
- listar;
- editar;
- activar/desactivar.

---

## Emprendimientos

Incluye:

- crear;
- listar propios;
- editar;
- validación de propietario;
- activación/desactivación administrativa.

---

## Publicaciones

Incluye:

- productos;
- servicios;
- categorías;
- emprendimiento;
- precio;
- stock;
- creación;
- edición;
- estado.

---

## Catálogo

Incluye:

- listado público;
- búsqueda;
- filtros;
- detalle.

---

## Pedidos

Incluye:

- creación;
- detalle;
- cálculo de total;
- validación de stock;
- descuento de inventario;
- restauración de inventario;
- consulta por cliente;
- consulta por emprendedor;
- cambios de estado.

---

# 4. Persistencia JPA

Entidades:

```text
RolEntity
UsuarioEntity
CategoriaEntity
EmprendimientoEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

Proveedor:

```text
EclipseLink
```

Persistence Unit:

```text
emprendelinkPU
```

DataSource:

```text
jdbc/EmprendeLinkDS
```

---

# 5. Relaciones principales

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

---

# 6. Reglas principales

## Usuarios

Un usuario inactivo no puede utilizar normalmente las funciones protegidas del sistema.

---

## Emprendimientos

Solo el propietario puede editar sus emprendimientos.

La activación/desactivación administrativa corresponde al administrador.

---

## Publicaciones

Una publicación debe:

- pertenecer a un emprendimiento válido;
- utilizar una categoría válida;
- tener precio válido;
- respetar stock cuando sea producto.

---

## Pedidos

Un pedido debe:

- pertenecer a un cliente;
- dirigirse a un emprendimiento;
- incluir publicaciones del emprendimiento correspondiente;
- utilizar cantidades válidas;
- respetar stock;
- calcular correctamente su total.

---

# 7. Estados de pedido

```text
PENDIENTE
    ↓
CONFIRMADO
    ↓
EN_PROCESO
    ↓
COMPLETADO
```

Cancelación posible desde:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
```

Estados terminales:

```text
COMPLETADO
CANCELADO
```

Presentación al cliente:

```text
EN_PROCESO = En camino
COMPLETADO = Entregado
```

---

# 8. Inventario

Al realizar un pedido de producto:

```text
stock -= cantidad
```

Al cancelar:

```text
stock += cantidad
```

El comportamiento fue probado manualmente durante la integración del módulo.

---

# 9. Eliminación lógica

Se utiliza eliminación lógica mediante:

```text
activo
```

en:

```text
Usuario
Categoria
Emprendimiento
Publicacion
```

Esta decisión permite preservar:

- integridad referencial;
- historial;
- pedidos existentes;
- relaciones.

---

# 10. AJAX

Se utilizan operaciones AJAX mediante:

```xml
<f:ajax>
```

Ejemplos:

- validaciones;
- edición;
- filtros;
- activación/desactivación;
- cambios de estado.

---

# 11. Validación

La solución utiliza:

- validación JSF;
- validadores personalizados;
- converters;
- validación en Services;
- Bean Validation;
- reglas de base de datos.

---

# 12. Seguridad

Roles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

Protección:

```text
AdminFilter
AutenticacionFilter
EmprendedorFilter
PedidosFilter
```

Los Services también aplican autorización funcional.

---

# 13. Pruebas automatizadas

La línea estable ejecuta:

```bash
mvn clean test
```

Total actual:

```text
39 pruebas automatizadas
```

Objetivo:

```text
Failures: 0
Errors: 0
```

---

# 14. Build

Comando:

```bash
mvn clean package
```

Artefacto generado:

```text
target/emprendelink.war
```

---

# 15. Deploy

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
http://localhost:8080/emprendelink/
```

---

# 16. Preparación para Fase 3

La Fase 3 podrá utilizar:

```text
REST
 ↓
Services existentes
 ↓
DAO JPA
 ↓
MySQL
```

Esto evita duplicar:

- reglas de negocio;
- validaciones;
- autorización funcional;
- persistencia.