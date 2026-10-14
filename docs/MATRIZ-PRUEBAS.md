# Matriz de pruebas — EmprendeLink DWF

Estados:

```text
PENDIENTE
APROBADA
FALLIDA
BLOQUEADA
```

Una prueba únicamente debe marcarse como `APROBADA` cuando haya sido ejecutada realmente.

---

# 1. Auth / Usuarios

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-001 | Registro válido de cliente | Unit/Manual | APROBADA |
| CP-002 | Correo normalizado | Unit | APROBADA |
| CP-003 | Contraseña almacenada como hash | Unit | APROBADA |
| CP-004 | Registro público como ADMIN rechazado | Unit | APROBADA |
| CP-005 | Correo duplicado rechazado | Unit | APROBADA |
| CP-006 | Correo inválido rechazado | Unit | APROBADA |
| CP-007 | Longitudes máximas validadas | Unit | APROBADA |
| CP-008 | Login válido | Unit/Manual | APROBADA |
| CP-009 | Usuario inactivo no autentica | Unit | APROBADA |
| CP-010 | Hash no queda en usuario autenticado | Unit | APROBADA |
| CP-011 | Admin accede a usuarios | Manual | APROBADA |
| CP-012 | Cliente autenticado accede al sistema | Manual | APROBADA |
| CP-013 | Emprendedor autenticado accede al sistema | Manual | APROBADA |
| CP-014 | Usuario creado desde registro puede iniciar sesión | Manual | APROBADA |

---

# 2. Mappers

| ID | Caso | Estado |
|---|---|---|
| CP-016 | Usuario dominio → entidad | APROBADA |
| CP-017 | Usuario entidad → dominio | APROBADA |
| CP-018 | Categoría dominio ↔ entidad | APROBADA |
| CP-019 | Emprendimiento dominio ↔ entidad | APROBADA |
| CP-020 | Publicación dominio ↔ entidad | APROBADA |
| CP-021 | Pedido y detalles dominio ↔ entidad | APROBADA |

---

# 3. Infraestructura

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-025 | `mvn clean test` | Automatizada | APROBADA |
| CP-026 | `mvn clean package` | Build | APROBADA |
| CP-027 | DataSource responde correctamente | Manual | APROBADA |
| CP-028 | WAR despliega en GlassFish | Manual | APROBADA |
| CP-029 | Persistence Unit JTA inicia con EclipseLink | Integración | APROBADA |

Resultado de regresión conocido:

```text
Tests run: 39
Failures: 0
Errors: 0
```

---

# 4. Categorías

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-030 | Crear categoría | Manual/Service | PENDIENTE |
| CP-031 | Listar categorías | Manual | PENDIENTE |
| CP-032 | Editar categoría | Manual/Service | PENDIENTE |
| CP-033 | Activar/desactivar categoría | Manual/Service | PENDIENTE |
| CP-034 | Nombre vacío rechazado | Unit | APROBADA |
| CP-035 | AJAX en gestión de categorías | Manual | PENDIENTE |

---

# 5. Emprendimientos

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-040 | Crear emprendimiento propio | Unit/Manual | PENDIENTE |
| CP-041 | Listar emprendimientos propios | Manual | PENDIENTE |
| CP-042 | Editar emprendimiento propio | Unit/Manual | PENDIENTE |
| CP-043 | Editar emprendimiento ajeno rechazado | Unit | APROBADA |
| CP-044 | Usuario no emprendedor no puede crear emprendimiento | Unit | APROBADA |
| CP-045 | Admin puede cambiar estado de emprendimiento | Unit | APROBADA |
| CP-046 | Usuario no autorizado no puede cambiar estado | Unit | APROBADA |
| CP-047 | Emprendimiento inactivo no aparece entre opciones activas | Manual | PENDIENTE |

---

# 6. Publicaciones / Catálogo

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-050 | Crear publicación | Unit/Manual | APROBADA |
| CP-051 | Editar publicación propia | Manual | APROBADA |
| CP-052 | Cambiar estado de publicación | Manual | APROBADA |
| CP-053 | Publicación requiere categoría válida | Unit | APROBADA |
| CP-054 | Publicación requiere emprendimiento propio | Unit | APROBADA |
| CP-055 | Producto sin stock suficiente es rechazado donde corresponde | Unit | APROBADA |
| CP-056 | Catálogo lista publicaciones activas | Manual | APROBADA |
| CP-057 | Detalle de publicación funciona | Manual | APROBADA |
| CP-058 | Filtros del catálogo | Manual | APROBADA |
| CP-059 | AJAX en gestión de publicaciones | Manual | APROBADA |

---

# 7. Pedidos

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-060 | Crear pedido con detalle | Unit/Manual | APROBADA |
| CP-061 | Total se calcula desde detalles | Unit | APROBADA |
| CP-062 | Crear pedido descuenta stock | Unit/Manual | APROBADA |
| CP-063 | Stock insuficiente rechazado | Unit | APROBADA |
| CP-064 | Cliente consulta pedidos propios | Manual | APROBADA |
| CP-065 | Cliente consulta detalle propio | Manual | APROBADA |
| CP-066 | Emprendedor consulta pedidos recibidos | Manual | APROBADA |
| CP-067 | Emprendedor no modifica pedido ajeno | Unit | APROBADA |
| CP-068 | PENDIENTE → CONFIRMADO | Unit/Manual | APROBADA |
| CP-069 | CONFIRMADO → EN_PROCESO | Manual | APROBADA |
| CP-070 | EN_PROCESO → COMPLETADO | Manual | APROBADA |
| CP-071 | Transición inválida rechazada | Unit | APROBADA |
| CP-072 | Cancelación cambia estado | Unit/Manual | APROBADA |
| CP-073 | Cancelación restaura stock | Unit/Manual | APROBADA |
| CP-074 | Estado EN_PROCESO se muestra como “En camino” | Manual | APROBADA |
| CP-075 | Estado COMPLETADO se muestra como “Entregado” | Manual | APROBADA |

---

# 8. Seguridad

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-080 | `/admin/*` requiere ROLE_ADMIN | Manual | APROBADA |
| CP-081 | `/inicio.xhtml` requiere sesión | Manual | APROBADA |
| CP-082 | `/publicaciones/*` requiere ROLE_EMPRENDEDOR | Manual | PENDIENTE |
| CP-083 | `/emprendimientos/*` requiere ROLE_EMPRENDEDOR | Manual | PENDIENTE |
| CP-084 | Cliente no puede acceder a administración | Manual | PENDIENTE |
| CP-085 | Emprendedor no puede acceder a administración | Manual | PENDIENTE |
| CP-086 | Ruta privada sin sesión redirige a login | Manual | PENDIENTE |
| CP-087 | Cliente no consulta pedido ajeno | Unit/Manual | PENDIENTE |

---

# 9. Integridad y transacciones

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-090 | Pedido y detalles se persisten relacionados | Integración | APROBADA |
| CP-091 | Publicación conserva relación con categoría | Integración | APROBADA |
| CP-092 | Publicación conserva relación con emprendimiento | Integración | APROBADA |
| CP-093 | Emprendimiento conserva propietario | Integración | APROBADA |
| CP-094 | Pedido conserva cliente y emprendimiento | Integración | APROBADA |
| CP-095 | Rollback completo ante fallo durante creación de pedido | Integración | PENDIENTE |

---

# 10. Regresión obligatoria

Antes de integrar cualquier módulo:

```bash
mvn clean test
mvn clean package
```

También comprobar:

- login;
- registro;
- sesión;
- permisos;
- conexión a MySQL;
- persistencia JPA;
- deploy del WAR.

---

# 11. Pruebas manuales pendientes antes de cerrar Fase 2

Antes del Sprint Review ejecutar y documentar:

```text
CP-030
CP-031
CP-032
CP-033
CP-035

CP-040
CP-041
CP-042
CP-047

CP-082
CP-083
CP-084
CP-085
CP-086
CP-087

CP-095
```

Después de comprobarlas, cambiar su estado a:

```text
APROBADA
```

y utilizar capturas o evidencias en el documento final de la fase.