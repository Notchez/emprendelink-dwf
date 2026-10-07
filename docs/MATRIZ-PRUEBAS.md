# Matriz de pruebas — EmprendeLink DWF

Este documento define las pruebas funcionales, de integración, persistencia, seguridad y regresión de EmprendeLink.

Durante Fase 2 la prioridad es comprobar:

```text
JPA
Hibernate
JSF
Managed Beans
AJAX
Validators
Converters
Transacciones
Permisos
Reglas de negocio
```

---

# 1. Estados

Cada prueba puede encontrarse en:

```text
PENDIENTE
APROBADA
FALLIDA
BLOQUEADA
```

---

# 2. Evidencia

Cada prueba ejecutada debe poder respaldarse mediante:

- captura;
- resultado Maven;
- log controlado;
- consulta de base de datos;
- resultado automatizado;
- evidencia visual;
- descripción reproducible.

No registrar:

- contraseñas;
- tokens;
- secretos;
- credenciales.

---

# 3. Pruebas de Fase 1 a conservar

## CP-001 — Registro válido

Módulo:

```text
Usuarios
```

Resultado esperado:

- crea usuario;
- correo único;
- contraseña segura;
- rol válido.

Estado:

```text
PENDIENTE
```

---

## CP-002 — Correo duplicado

Resultado esperado:

- rechaza registro;
- no duplica usuario;
- muestra error controlado.

Estado:

```text
PENDIENTE
```

---

## CP-003 — Login válido

Resultado esperado:

- autentica usuario activo;
- identifica rol;
- establece sesión.

Estado:

```text
PENDIENTE
```

---

## CP-004 — Login inválido

Resultado esperado:

- rechaza autenticación;
- no establece sesión válida.

Estado:

```text
PENDIENTE
```

---

## CP-005 — Usuario inactivo

Resultado esperado:

- autenticación rechazada.

Estado:

```text
PENDIENTE
```

---

## CP-006 — Crear emprendimiento propio

Precondición:

```text
ROLE_EMPRENDEDOR
```

Resultado esperado:

- crea emprendimiento;
- propietario correcto.

Estado:

```text
PENDIENTE
```

---

## CP-007 — Editar emprendimiento ajeno

Resultado esperado:

- operación rechazada;
- registro sin cambios.

Estado:

```text
PENDIENTE
```

---

## CP-008 — Crear categoría

Precondición:

```text
ROLE_ADMIN
```

Resultado esperado:

- categoría creada correctamente.

Estado:

```text
PENDIENTE
```

---

## CP-009 — Crear publicación válida

Resultado esperado:

- publicación persistida;
- emprendimiento correcto;
- categoría correcta.

Estado:

```text
PENDIENTE
```

---

## CP-010 — Precio negativo

Resultado esperado:

- valor rechazado;
- no persiste publicación inválida.

Estado:

```text
PENDIENTE
```

---

## CP-011 — Catálogo público

Resultado esperado:

- muestra publicaciones permitidas;
- no requiere login para contenido público.

Estado:

```text
PENDIENTE
```

---

## CP-012 — Crear pedido válido

Resultado esperado:

- estado `PENDIENTE`;
- detalles correctos;
- precios guardados;
- subtotales correctos;
- total correcto.

Estado:

```text
PENDIENTE
```

---

## CP-013 — Cantidad inválida

Resultado esperado:

- rechazo;
- no crea detalle inválido.

Estado:

```text
PENDIENTE
```

---

## CP-014 — Cliente consulta pedido ajeno

Resultado esperado:

- acceso rechazado;
- información no expuesta.

Estado:

```text
PENDIENTE
```

---

## CP-015 — Emprendedor consulta pedido propio

Resultado esperado:

- pedido visible si corresponde a su emprendimiento.

Estado:

```text
PENDIENTE
```

---

## CP-016 — Emprendedor modifica pedido ajeno

Resultado esperado:

- operación rechazada;
- estado original conservado.

Estado:

```text
PENDIENTE
```

---

# 4. Persistencia JPA — Fase 2

## CP-017 — Arranque de unidad de persistencia

Objetivo:

Comprobar que la configuración JPA inicia correctamente.

Resultado esperado:

- unidad de persistencia válida;
- sin errores de mapeo;
- conexión funcional.

Estado:

```text
PENDIENTE
```

---

## CP-018 — Persistencia de RolEntity

Resultado esperado:

- crear;
- consultar;
- actualizar cuando corresponda;
- relación válida con usuario.

Estado:

```text
PENDIENTE
```

---

## CP-019 — Persistencia de UsuarioEntity

Resultado esperado:

- usuario persistido;
- rol relacionado;
- correo único;
- datos recuperables.

Estado:

```text
PENDIENTE
```

---

## CP-020 — Relación Usuario — Rol

Resultado esperado:

```text
Rol 1:N Usuario
```

sin referencias inválidas.

Estado:

```text
PENDIENTE
```

---

## CP-021 — Persistencia de CategoriaEntity

Resultado esperado:

- crear;
- consultar;
- actualizar;
- listar.

Estado:

```text
PENDIENTE
```

---

## CP-022 — Persistencia de EmprendimientoEntity

Resultado esperado:

- propietario válido;
- relación persistida;
- recuperación correcta.

Estado:

```text
PENDIENTE
```

---

## CP-023 — Relación Usuario — Emprendimiento

Resultado esperado:

```text
Usuario 1:N Emprendimiento
```

Estado:

```text
PENDIENTE
```

---

## CP-024 — Persistencia de PublicacionEntity

Resultado esperado:

- emprendimiento relacionado;
- categoría relacionada;
- tipo válido;
- precio válido.

Estado:

```text
PENDIENTE
```

---

## CP-025 — Relaciones de publicación

Comprobar:

```text
Emprendimiento 1:N Publicacion
Categoria 1:N Publicacion
```

Estado:

```text
PENDIENTE
```

---

## CP-026 — Persistencia de PedidoEntity

Resultado esperado:

- cliente válido;
- emprendimiento válido;
- estado válido;
- total válido.

Estado:

```text
PENDIENTE
```

---

## CP-027 — Persistencia de DetallePedidoEntity

Resultado esperado:

- pedido relacionado;
- publicación relacionada;
- cantidad válida;
- precio unitario persistido;
- subtotal válido.

Estado:

```text
PENDIENTE
```

---

## CP-028 — Relación Pedido — Detalle

Resultado esperado:

```text
Pedido 1:N DetallePedido
```

Estado:

```text
PENDIENTE
```

---

# 5. DAO JPA

## CP-029 — CRUD JPA de categorías

Verificar:

- crear;
- buscar;
- listar;
- actualizar;
- operación de eliminación o cambio de estado según contrato.

Estado:

```text
PENDIENTE
```

---

## CP-030 — CRUD JPA de emprendimientos

Resultado esperado:

- operaciones correctas;
- propiedad conservada.

Estado:

```text
PENDIENTE
```

---

## CP-031 — CRUD JPA de publicaciones

Resultado esperado:

- operaciones correctas;
- relaciones conservadas.

Estado:

```text
PENDIENTE
```

---

## CP-032 — Consultas JPA de pedidos

Verificar:

- pedidos por cliente;
- pedidos por emprendimiento;
- detalle por ID;
- permisos aplicados por capa Service.

Estado:

```text
PENDIENTE
```

---

# 6. Mappers

## CP-033 — Entity → Domain

Resultado esperado:

- IDs conservados;
- atributos conservados;
- relaciones necesarias convertidas correctamente.

Estado:

```text
PENDIENTE
```

---

## CP-034 — Domain → Entity

Resultado esperado:

- atributos correctamente mapeados;
- sin pérdida de información necesaria.

Estado:

```text
PENDIENTE
```

---

# 7. JSF

## CP-035 — Render de página JSF

Resultado esperado:

- XHTML carga;
- Bean disponible;
- no ocurre error de resolución.

Estado:

```text
PENDIENTE
```

---

## CP-036 — Login mediante AuthBean

Resultado esperado:

- login válido;
- rechazo de credenciales incorrectas;
- rol disponible;
- sesión controlada.

Estado:

```text
PENDIENTE
```

---

## CP-037 — CRUD JSF de categorías

Resultado esperado:

- lista;
- crea;
- modifica;
- muestra mensajes;
- utiliza Service.

Estado:

```text
PENDIENTE
```

---

## CP-038 — Gestión JSF de emprendimientos

Resultado esperado:

- lista únicamente recursos permitidos;
- crea;
- modifica propios;
- rechaza recursos ajenos.

Estado:

```text
PENDIENTE
```

---

## CP-039 — Gestión JSF de publicaciones

Resultado esperado:

- lista;
- crea;
- modifica;
- valida propietario;
- relaciones correctas.

Estado:

```text
PENDIENTE
```

---

## CP-040 — Gestión JSF de pedidos

Resultado esperado:

- cliente consulta sus pedidos;
- emprendedor consulta pedidos recibidos;
- permisos correctos.

Estado:

```text
PENDIENTE
```

---

# 8. AJAX

## CP-041 — Actualización parcial

Objetivo:

Comprobar una interacción real mediante:

```text
<f:ajax>
```

Resultado esperado:

- no recarga toda la página;
- Bean procesa el evento;
- componente correcto se actualiza.

Estado:

```text
PENDIENTE
```

---

## CP-042 — Filtro de catálogo con AJAX

Resultado esperado:

- filtro aplicado;
- resultados actualizados;
- datos consistentes.

Estado:

```text
PENDIENTE
```

---

# 9. Validators

## CP-043 — Campo obligatorio

Resultado esperado:

- formulario no continúa;
- mensaje visible;
- registro no persiste.

Estado:

```text
PENDIENTE
```

---

## CP-044 — Valor numérico inválido

Ejemplo:

```text
precio negativo
cantidad <= 0
```

Resultado esperado:

- rechazo;
- mensaje;
- sin persistencia inválida.

Estado:

```text
PENDIENTE
```

---

## CP-045 — Validación de correo

Resultado esperado:

- formato inválido rechazado;
- duplicado rechazado cuando corresponda.

Estado:

```text
PENDIENTE
```

---

# 10. Converters

## CP-046 — Converter de relación

Ejemplo:

```text
selección de categoría
```

Resultado esperado:

```text
valor de vista → Categoria
```

correctamente.

Estado:

```text
PENDIENTE
```

---

## CP-047 — Converter con ID inexistente

Resultado esperado:

- error controlado;
- no provoca persistencia incorrecta.

Estado:

```text
PENDIENTE
```

---

# 11. Transacciones

## CP-048 — Creación transaccional de pedido

Resultado esperado:

- pedido creado;
- todos los detalles creados;
- datos consistentes.

Estado:

```text
PENDIENTE
```

---

## CP-049 — Rollback al fallar detalle

Escenario:

1. iniciar creación de pedido;
2. provocar un fallo crítico en un detalle;
3. ejecutar operación.

Resultado esperado:

```text
rollback
```

No debe quedar:

- pedido parcial;
- detalles parciales;
- datos inconsistentes.

Estado:

```text
PENDIENTE
```

---

# 12. Estados de pedido

## CP-050 — PENDIENTE → CONFIRMADO

Resultado esperado:

- transición permitida.

Estado:

```text
PENDIENTE
```

---

## CP-051 — PENDIENTE → CANCELADO

Resultado esperado:

- transición permitida.

Estado:

```text
PENDIENTE
```

---

## CP-052 — CONFIRMADO → EN_PROCESO

Resultado esperado:

- transición permitida.

Estado:

```text
PENDIENTE
```

---

## CP-053 — EN_PROCESO → COMPLETADO

Resultado esperado:

- transición permitida.

Estado:

```text
PENDIENTE
```

---

## CP-054 — Transición inválida

Ejemplo:

```text
PENDIENTE → COMPLETADO
```

Resultado esperado:

- operación rechazada;
- estado original conservado.

Estado:

```text
PENDIENTE
```

---

## CP-055 — Modificar pedido completado

Resultado esperado:

- rechazo;
- `COMPLETADO` permanece terminal.

Estado:

```text
PENDIENTE
```

---

## CP-056 — Modificar pedido cancelado

Resultado esperado:

- rechazo;
- `CANCELADO` permanece terminal.

Estado:

```text
PENDIENTE
```

---

# 13. Seguridad

## CP-057 — Cliente accede a pedido ajeno

Resultado esperado:

- acceso rechazado.

Estado:

```text
PENDIENTE
```

---

## CP-058 — Emprendedor edita emprendimiento ajeno

Resultado esperado:

- rechazo;
- datos sin cambios.

Estado:

```text
PENDIENTE
```

---

## CP-059 — Emprendedor edita publicación ajena

Resultado esperado:

- rechazo;
- datos sin cambios.

Estado:

```text
PENDIENTE
```

---

## CP-060 — Emprendedor modifica pedido ajeno

Resultado esperado:

- rechazo;
- estado conservado.

Estado:

```text
PENDIENTE
```

---

## CP-061 — Cliente intenta administrar categoría

Resultado esperado:

- acceso rechazado.

Estado:

```text
PENDIENTE
```

---

# 14. Build y pruebas automatizadas

## CP-062 — Build Maven

Ejecutar:

```bash
mvn clean package
```

Resultado esperado:

```text
BUILD SUCCESS
```

Estado:

```text
PENDIENTE
```

---

## CP-063 — Ejecución de tests Maven

Resultado esperado:

- Maven ejecuta casos reales;
- no únicamente clases vacías;
- cantidad de tests mayor que cero cuando ya exista suite automatizada.

Estado:

```text
PENDIENTE
```

---

# 15. Regresión

## CP-064 — Login después de integración JPA

Resultado esperado:

- autenticación continúa funcionando.

Estado:

```text
PENDIENTE
```

---

## CP-065 — Catálogo después de integración JSF

Resultado esperado:

- datos disponibles;
- relaciones correctas;
- filtros funcionales.

Estado:

```text
PENDIENTE
```

---

## CP-066 — Pedidos después de integración completa

Resultado esperado:

- crear;
- consultar;
- gestionar estados;
- mantener integridad.

Estado:

```text
PENDIENTE
```

---

# 16. Registro de defectos

Cuando una prueba falle debe registrarse:

```text
ID de prueba
fecha
resultado obtenido
resultado esperado
módulo
rama
defecto encontrado
corrección
estado
```

Estados posibles del defecto:

```text
ABIERTO
EN_CORRECCION
CORREGIDO
REVALIDAR
CERRADO
```

---

# 17. Sprint Review de Fase 2

Antes de la entrega deben revisarse:

```text
JPA configurado
entidades mapeadas
relaciones funcionales
CRUD funcional
JSF funcional
Managed Beans funcionales
AJAX demostrado
validaciones demostradas
converters demostrados
transacción demostrada
rollback demostrado
permisos demostrados
build exitoso
defectos críticos cerrados
```

---

# 18. Evidencias recomendadas

Para la entrega pueden conservarse evidencias de:

- pantallas JSF;
- formularios;
- AJAX;
- datos persistidos;
- relaciones;
- mensajes de validación;
- permisos;
- cambios de estado;
- Maven;
- pruebas;
- rollback.

---

# 19. Regla de aprobación

Una prueba solo debe marcarse:

```text
APROBADA
```

cuando haya sido ejecutada realmente.

No marcar una prueba como aprobada únicamente porque el código compile.

---

# 20. Mantenimiento

Actualizar esta matriz cuando:

- cambie una regla;
- cambie un permiso;
- se agregue funcionalidad;
- se corrija un defecto;
- cambie persistencia;
- cambie una transición;
- se integre una rama;
- se prepare una entrega.

Las pruebas de regresión deben repetirse cuando un cambio posterior pueda afectar una funcionalidad previamente validada.