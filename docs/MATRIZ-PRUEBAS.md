# Matriz de pruebas — EmprendeLink DWF

Estados:

```text
PENDIENTE
APROBADA
FALLIDA
BLOQUEADA
```

Una prueba manual solo debe marcarse `APROBADA` cuando haya sido ejecutada realmente.

---

## 1. Regresión automatizada

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-001 | `CategoriaMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-002 | `EmprendimientoMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-003 | `PedidoMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-004 | `PublicacionMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-005 | `UsuarioMapper` dominio ↔ entidad | Unit | APROBADA |
| CP-006 | Auth / usuarios | Unit | APROBADA |
| CP-007 | Categoría Service | Unit | APROBADA |
| CP-008 | Emprendimiento Service | Unit | APROBADA |
| CP-009 | Pedido Service | Unit | APROBADA |
| CP-010 | Publicación Service | Unit | APROBADA |
| CP-011 | `mvn clean test` | Build/Test | APROBADA |

Última ejecución:

```text
Tests run: 41
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

---

## 2. Infraestructura

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-020 | DataSource `jdbc/EmprendeLinkDS` conecta | Manual | APROBADA |
| CP-021 | JPA inicia con EclipseLink/JTA | Integración | APROBADA |
| CP-022 | Aplicación despliega en GlassFish | Manual | APROBADA |
| CP-023 | Context path `/emprendelink` responde | Manual | APROBADA |
| CP-024 | `mvn clean package` después del último incremento | Build | PENDIENTE |

---

## 3. Administración

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-030 | Admin accede a `/admin/*` | Manual | APROBADA |
| CP-031 | Crear categoría | Manual | APROBADA |
| CP-032 | Editar categoría | Manual | PENDIENTE |
| CP-033 | Activar/desactivar categoría | Manual | PENDIENTE |
| CP-034 | Configurar `manejaStock` | Manual | APROBADA |
| CP-035 | Crear emprendedor desde Admin | Manual | APROBADA |
| CP-036 | Crear negocio junto al emprendedor | Manual | APROBADA |
| CP-037 | Rechazar correo duplicado de emprendedor | Manual | PENDIENTE |
| CP-038 | Rechazar plan inactivo | Manual/Service | PENDIENTE |
| CP-039 | Rechazar categoría inactiva | Manual/Service | PENDIENTE |

---

## 4. Regla un emprendedor = un negocio

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-040 | Emprendedor creado recibe un negocio | Manual | APROBADA |
| CP-041 | `id_propietario` es único en BD | Integración | APROBADA |
| CP-042 | No existe flujo UI para crear segundo negocio | Manual | APROBADA |
| CP-043 | Segundo negocio para mismo propietario es rechazado por integridad | Integración | PENDIENTE |

---

## 5. Mi negocio

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-050 | Emprendedor ve su negocio | Manual | APROBADA |
| CP-051 | Muestra plan y comisión | Manual | APROBADA |
| CP-052 | Muestra publicaciones activas / máximo | Manual | APROBADA |
| CP-053 | Emprendedor edita descripción | Manual | PENDIENTE |
| CP-054 | Emprendedor edita contacto | Manual | PENDIENTE |
| CP-055 | Emprendedor edita logo URL | Manual | PENDIENTE |
| CP-056 | Nombre no se edita desde Mi negocio | Manual | APROBADA |
| CP-057 | Categoría no se edita desde Mi negocio | Manual | APROBADA |

---

## 6. Publicaciones

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-060 | Crear publicación propia | Manual/Service | APROBADA |
| CP-061 | Editar publicación actualiza el mismo registro | Manual | APROBADA |
| CP-062 | Editar no crea duplicado | Manual | APROBADA |
| CP-063 | Activar/desactivar publicación | Manual | APROBADA |
| CP-064 | Contador del plan cambia con AJAX | Manual | APROBADA |
| CP-065 | Espacios disponibles cambian con AJAX | Manual | APROBADA |
| CP-066 | Imagen URL se conserva | Manual | APROBADA |
| CP-067 | Precio con entrada de centavos | Manual | APROBADA |
| CP-068 | Publicación requiere negocio propio | Unit | APROBADA |
| CP-069 | Límite de plan al crear | Unit | APROBADA |
| CP-070 | Límite de plan al reactivar | Unit | APROBADA |
| CP-071 | Stock negativo rechazado | Unit | APROBADA |
| CP-072 | Categoría/tipo se derivan del negocio | Manual/Service | PENDIENTE |

---

## 7. Catálogo cliente

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-080 | Catálogo lista publicaciones visibles | Manual | APROBADA |
| CP-081 | Búsqueda AJAX | Manual | APROBADA |
| CP-082 | Filtro por categoría | Manual | APROBADA |
| CP-083 | Filtro visual Tipo no existe | Manual | APROBADA |
| CP-084 | Detalle de publicación funciona | Manual | APROBADA |
| CP-085 | Emprendedor autenticado no entra al catálogo público | Manual | PENDIENTE |
| CP-086 | Admin autenticado no entra al catálogo público | Manual | PENDIENTE |

---

## 8. Carrito

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-090 | Agregar artículo cambia a stepper | Manual | APROBADA |
| CP-091 | `+` incrementa en catálogo por AJAX | Manual | APROBADA |
| CP-092 | `-` disminuye en catálogo por AJAX | Manual | APROBADA |
| CP-093 | Cantidad 1 muestra papelera | Manual | APROBADA |
| CP-094 | Papelera elimina artículo | Manual | APROBADA |
| CP-095 | Badge del carrito se actualiza | Manual | APROBADA |
| CP-096 | `+/-` se actualiza inmediatamente dentro del carrito | Manual | APROBADA |
| CP-097 | Subtotales/totales cambian por AJAX | Manual | APROBADA |
| CP-098 | No permite superar stock | Manual | PENDIENTE |
| CP-099 | Carrito conserva estado entre vistas | Manual | PENDIENTE |
| CP-100 | Carrito admite artículos de dos negocios | Manual | PENDIENTE |

---

## 9. Checkout y pedidos

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-110 | Checkout usa dirección sugerida del perfil | Manual | PENDIENTE |
| CP-111 | Dirección obligatoria validada | Manual/Service | PENDIENTE |
| CP-112 | Carrito multi-negocio crea un pedido por negocio | Manual/Service | PENDIENTE |
| CP-113 | Total se calcula desde detalles | Unit | APROBADA |
| CP-114 | Pedido descuenta stock | Unit | APROBADA |
| CP-115 | Stock insuficiente rechazado | Unit | APROBADA |
| CP-116 | Pedido congela plan aplicado | Unit | APROBADA |
| CP-117 | Pedido congela tasa de comisión | Unit | APROBADA |
| CP-118 | Pedido calcula monto de comisión | Unit | APROBADA |
| CP-119 | Cancelar restaura stock | Unit | APROBADA |
| CP-120 | PENDIENTE → CONFIRMADO | Unit | APROBADA |
| CP-121 | Salto directo a COMPLETADO rechazado | Unit | APROBADA |
| CP-122 | Otro emprendedor no cambia pedido ajeno | Unit | APROBADA |
| CP-123 | Cliente ve sus pedidos | Manual | APROBADA |
| CP-124 | Estados se muestran correctamente en móvil | Manual | APROBADA |
| CP-125 | Detalle muestra líneas del pedido | Manual | APROBADA |
| CP-126 | Cambio posterior del perfil no altera dirección histórica | Manual | PENDIENTE |

---

## 10. Perfil cliente

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-130 | `/perfil/*` requiere cliente | Manual | PENDIENTE |
| CP-131 | Nombre es solo lectura | Manual | APROBADA |
| CP-132 | Correo es solo lectura | Manual | APROBADA |
| CP-133 | Teléfono se puede actualizar | Manual | PENDIENTE |
| CP-134 | Dirección se puede actualizar | Manual | PENDIENTE |
| CP-135 | Cerrar sesión está dentro de Mi perfil | Manual | APROBADA |

---

## 11. Seguridad

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-140 | `/admin/*` requiere Admin | Manual | APROBADA |
| CP-141 | `/publicaciones/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-142 | `/emprendimientos/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-143 | `/ventas/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-144 | `/comisiones/*` requiere Emprendedor | Manual | PENDIENTE |
| CP-145 | `/carrito/*` requiere Cliente | Manual | PENDIENTE |
| CP-146 | `/perfil/*` requiere Cliente | Manual | PENDIENTE |
| CP-147 | Ruta privada sin sesión redirige a login | Manual | PENDIENTE |
| CP-148 | Cliente no consulta pedido ajeno | Unit/Manual | PENDIENTE |
| CP-149 | Emprendedor no gestiona publicación ajena | Unit | APROBADA |

---

## 12. Persistencia e integridad

| ID | Caso | Tipo | Estado |
|---|---|---|---|
| CP-160 | Pedido y detalles se persisten relacionados | Integración | APROBADA |
| CP-161 | Emprendimiento conserva propietario | Integración | APROBADA |
| CP-162 | Emprendimiento conserva plan | Integración | APROBADA |
| CP-163 | Emprendimiento conserva categoría | Integración | APROBADA |
| CP-164 | Usuario conserva dirección | Mapper/Integración | APROBADA |
| CP-165 | Pedido conserva dirección entrega | Mapper/Integración | APROBADA |
| CP-166 | Pedido conserva snapshot del plan | Mapper/Unit | APROBADA |
| CP-167 | Rollback total si falla checkout multi-negocio | Integración | PENDIENTE |

---

## 13. Antes del PR final de Fase 2

Ejecutar:

```powershell
mvn clean test
mvn clean package
```

Registrar evidencia de los casos manuales pendientes más importantes:

```text
CP-085
CP-086
CP-098
CP-100
CP-110
CP-112
CP-126
CP-133
CP-134
CP-141
CP-142
CP-143
CP-144
CP-145
CP-146
CP-147
CP-167
```

No es obligatorio que todos los casos pendientes se automaticen en Fase 2, pero sí deben ejecutarse y documentarse si se desean presentar como evidencia de cierre.