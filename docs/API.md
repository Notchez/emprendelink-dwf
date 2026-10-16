# API REST — EmprendeLink DWF

## Estado

Este documento define el **contrato preliminar** para la Fase 3.

En el estado actual de Fase 2:

```text
NO existe todavía una API REST funcional.
```

Existen clases preparatorias en:

```text
src/main/java/emprendelink/web/rest/
src/main/java/emprendelink/dto/
```

por ejemplo:

```text
EmprendimientoResource
PublicacionResource
PedidoResource

EmprendimientoDTO
PublicacionDTO
CrearPedidoDTO
PedidoDTO
DetallePedidoDTO
ApiErrorDTO
```

pero actualmente son scaffolding y no deben presentarse como endpoints implementados.

---

## 1. Objetivo de Fase 3

La API deberá:

- reutilizar Services existentes;
- exponer operaciones controladas;
- intercambiar JSON;
- usar códigos HTTP correctos;
- validar entradas;
- mantener autorización por rol y propiedad;
- documentar errores;
- ser consumida por un cliente externo.

Arquitectura prevista:

```text
Cliente externo
      ↓ HTTP / JSON
REST Resource
      ↓
DTO
      ↓
Service
      ↓
DAO
      ↓
JPA
      ↓
MySQL
```

---

## 2. Ruta base prevista

```text
/api/v1
```

Ejemplo local:

```text
http://localhost:8080/emprendelink/api/v1
```

---

## 3. Formato

```text
application/json
```

Convención:

```text
camelCase
```

Ejemplos:

```text
idUsuario
idEmprendimiento
idPublicacion
idPedido
fechaPedido
precioUnitario
direccionEntrega
```

---

## 4. Recursos candidatos

Los recursos iniciales previstos son:

```text
emprendimientos
publicaciones
pedidos
```

La definición definitiva debe validarse al iniciar Fase 3.

---

## 5. Emprendimientos — contrato preliminar

### Listar negocios visibles

```http
GET /api/v1/emprendimientos
```

Respuesta esperada:

```text
200 OK
```

### Obtener negocio

```http
GET /api/v1/emprendimientos/{id}
```

Respuestas esperadas:

```text
200 OK
404 Not Found
```

### Publicaciones del negocio

```http
GET /api/v1/emprendimientos/{id}/publicaciones
```

Respuestas esperadas:

```text
200 OK
404 Not Found
```

---

## 6. Publicaciones — contrato preliminar

### Listar publicaciones visibles

```http
GET /api/v1/publicaciones
```

Filtros candidatos:

```text
texto
categoria
emprendimiento
```

La interfaz JSF de Fase 2 ya no expone un filtro manual por tipo.

El campo técnico `tipo` puede seguir formando parte del DTO si resulta necesario para el contrato final.

### Obtener publicación

```http
GET /api/v1/publicaciones/{id}
```

Respuestas esperadas:

```text
200 OK
404 Not Found
```

---

## 7. Pedidos — contrato preliminar

El modelo actual permite un carrito visual con varios negocios, pero persiste:

```text
1 Pedido → 1 Emprendimiento
```

La API de Fase 3 deberá decidir si expone:

```text
A) creación de un pedido por negocio
```

o:

```text
B) endpoint de checkout que reciba varias publicaciones
   y delegue en PedidoService.crearDesdeCarrito(...)
```

La opción B reutiliza mejor el comportamiento de Fase 2.

### Checkout candidato

```http
POST /api/v1/pedidos/checkout
```

Responsabilidad prevista:

- validar cliente;
- validar publicaciones;
- validar stock;
- agrupar por negocio;
- crear un pedido por negocio;
- congelar plan;
- calcular comisión;
- guardar dirección;
- descontar stock.

Respuesta propuesta:

```text
201 Created
```

Errores candidatos:

```text
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
```

### Obtener pedido

```http
GET /api/v1/pedidos/{id}
```

Debe validar propiedad/rol.

### Cambiar estado

```http
PUT /api/v1/pedidos/{id}/estado
```

Debe validar:

- emprendedor autenticado;
- propiedad del negocio;
- transición permitida.

---

## 8. Snapshot del pedido

El DTO final de pedido deberá considerar:

```text
idPlanAplicado
nombrePlanAplicado
tasaComision
montoComision
direccionEntrega
fechaPedido
fechaCompletado
```

Estos valores forman parte del historial comercial.

---

## 9. Seguridad prevista

Los endpoints protegidos deberán verificar:

```text
identidad
rol
usuario activo
propiedad
estado del recurso
```

Roles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

La API deberá reutilizar Services y no duplicar reglas en Resources.

---

## 10. Errores

Contrato previsto:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Recurso no encontrado"
}
```

No devolver:

- stack traces;
- SQL;
- hashes de contraseña;
- secretos;
- información interna innecesaria.

---

## 11. DTO

DTO preparados como scaffolding:

```text
EmprendimientoDTO
PublicacionDTO
CrearPedidoDTO
PedidoDTO
DetallePedidoDTO
ApiErrorDTO
```

En Fase 3 deberán convertirse en contratos reales.

Los DTO:

- no contienen lógica de negocio;
- no exponen entidades JPA;
- no exponen contraseña;
- deben reflejar únicamente datos necesarios para consumo.

---

## 12. Pruebas de Fase 3

Pendiente:

- herramienta de pruebas de API;
- casos positivos;
- casos negativos;
- límites;
- autenticación;
- autorización;
- códigos HTTP;
- JSON;
- validación;
- documentación de consumo.

---

## 13. Estado de implementación

```text
Diseño preliminar        → PREPARADO
Clases scaffolding       → PREPARADAS
DTO funcionales          → PENDIENTE
JAX-RS funcional         → PENDIENTE
Endpoints                → PENDIENTE
Seguridad REST           → PENDIENTE
Cliente consumidor       → PENDIENTE
Pruebas API              → PENDIENTE
Documentación definitiva → PENDIENTE
```

Este documento deberá reemplazarse o ampliarse al implementar Fase 3.