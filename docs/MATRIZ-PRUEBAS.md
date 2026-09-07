# Matriz de pruebas — EmprendeLink DWF

Este documento contiene la matriz inicial de pruebas del proyecto EmprendeLink.

La matriz deberá ampliarse conforme se implementen nuevas funcionalidades durante las diferentes fases del proyecto.

---

## 1. Objetivo

Verificar que las funcionalidades principales del sistema se comporten correctamente y que las reglas de negocio, seguridad, persistencia e integración funcionen según lo esperado.

---

## 2. Tipos de prueba

Durante el proyecto podrán utilizarse:

- pruebas funcionales;
- pruebas de integración;
- pruebas de persistencia;
- pruebas de seguridad;
- pruebas de API;
- pruebas de regresión;
- pruebas de aceptación.

---

## 3. Estados de prueba

Cada caso podrá utilizar uno de los siguientes estados:

- `PENDIENTE`
- `APROBADA`
- `FALLIDA`
- `BLOQUEADA`

---

## 4. Formato de casos

Cada caso deberá incluir como mínimo:

- ID;
- módulo;
- descripción;
- precondiciones;
- pasos;
- resultado esperado;
- resultado obtenido;
- estado;
- evidencia.

---

# Casos iniciales

## CP-001 — Registro exitoso de usuario

Módulo:

`Usuarios`

Tipo:

`Funcional`

Precondiciones:

- El correo ingresado no existe previamente.
- La base de datos se encuentra disponible.

Pasos:

1. Acceder al registro.
2. Ingresar datos válidos.
3. Enviar el formulario.

Resultado esperado:

- El usuario se registra correctamente.
- La contraseña no se almacena en texto plano.
- Se asigna el rol correspondiente.
- Se registra la fecha de creación.

Estado:

`PENDIENTE`

---

## CP-002 — Registro con correo duplicado

Módulo:

`Usuarios`

Tipo:

`Funcional`

Precondiciones:

- Existe un usuario con el mismo correo.

Pasos:

1. Acceder al registro.
2. Ingresar un correo existente.
3. Completar los demás datos.
4. Enviar el formulario.

Resultado esperado:

- El sistema rechaza el registro.
- Se muestra un mensaje controlado.
- No se crea un usuario duplicado.

Estado:

`PENDIENTE`

---

## CP-003 — Inicio de sesión válido

Módulo:

`Autenticación`

Tipo:

`Funcional`

Precondiciones:

- Existe un usuario activo con credenciales válidas.

Pasos:

1. Acceder al formulario de inicio de sesión.
2. Ingresar correo y contraseña correctos.
3. Enviar el formulario.

Resultado esperado:

- La autenticación es exitosa.
- Se crea una sesión válida.
- El sistema identifica correctamente el rol del usuario.

Estado:

`PENDIENTE`

---

## CP-004 — Inicio de sesión inválido

Módulo:

`Autenticación`

Tipo:

`Funcional`

Pasos:

1. Acceder al inicio de sesión.
2. Ingresar credenciales incorrectas.
3. Enviar el formulario.

Resultado esperado:

- El sistema rechaza la autenticación.
- No se crea una sesión válida.
- Se muestra un mensaje controlado.

Estado:

`PENDIENTE`

---

## CP-005 — Acceso administrativo sin permisos

Módulo:

`Seguridad`

Tipo:

`Seguridad`

Precondiciones:

- Usuario autenticado con rol diferente a `ROLE_ADMIN`.

Pasos:

1. Intentar acceder a `/admin/usuarios`.

Resultado esperado:

- El servidor rechaza el acceso.
- El usuario no puede ejecutar operaciones administrativas.

Estado:

`PENDIENTE`

---

## CP-006 — Crear emprendimiento

Módulo:

`Emprendimientos`

Tipo:

`Funcional`

Precondiciones:

- Usuario autenticado con `ROLE_EMPRENDEDOR`.

Pasos:

1. Acceder al formulario de creación.
2. Ingresar datos válidos.
3. Guardar.

Resultado esperado:

- Se crea el emprendimiento.
- El propietario corresponde al usuario autenticado.
- El emprendimiento queda asociado correctamente en la base de datos.

Estado:

`PENDIENTE`

---

## CP-007 — Modificar emprendimiento ajeno

Módulo:

`Emprendimientos`

Tipo:

`Seguridad`

Precondiciones:

- Usuario autenticado con `ROLE_EMPRENDEDOR`.
- Existe un emprendimiento perteneciente a otro usuario.

Pasos:

1. Intentar editar el emprendimiento ajeno mediante su ID.

Resultado esperado:

- El sistema rechaza la operación.
- El registro no se modifica.

Estado:

`PENDIENTE`

---

## CP-008 — Crear publicación válida

Módulo:

`Publicaciones`

Tipo:

`Funcional`

Precondiciones:

- Usuario autenticado con `ROLE_EMPRENDEDOR`.
- Existe un emprendimiento propio.
- Existe una categoría activa.

Pasos:

1. Acceder al formulario de publicación.
2. Seleccionar emprendimiento y categoría.
3. Ingresar datos válidos.
4. Guardar.

Resultado esperado:

- La publicación se registra correctamente.
- Queda asociada al emprendimiento y categoría seleccionados.

Estado:

`PENDIENTE`

---

## CP-009 — Precio negativo en publicación

Módulo:

`Publicaciones`

Tipo:

`Validación`

Pasos:

1. Intentar crear o modificar una publicación.
2. Ingresar un precio menor que cero.
3. Guardar.

Resultado esperado:

- El sistema rechaza el valor.
- La publicación no se guarda con un precio inválido.

Estado:

`PENDIENTE`

---

## CP-010 — Consultar catálogo público

Módulo:

`Catálogo`

Tipo:

`Funcional`

Pasos:

1. Acceder al catálogo sin iniciar sesión.

Resultado esperado:

- Se muestran únicamente publicaciones visibles o activas.
- No se requiere autenticación.

Estado:

`PENDIENTE`

---

## CP-011 — Crear pedido válido

Módulo:

`Pedidos`

Tipo:

`Funcional`

Precondiciones:

- Usuario autenticado con `ROLE_CLIENTE`.
- Existen publicaciones válidas.

Pasos:

1. Seleccionar publicaciones.
2. Indicar cantidades válidas.
3. Crear el pedido.

Resultado esperado:

- Se crea un pedido con estado `PENDIENTE`.
- Se crean los detalles correspondientes.
- Los precios unitarios quedan almacenados.
- Los subtotales se calculan correctamente.
- El total corresponde a la suma de subtotales.

Estado:

`PENDIENTE`

---

## CP-012 — Cantidad inválida en pedido

Módulo:

`Pedidos`

Tipo:

`Validación`

Pasos:

1. Intentar crear un pedido.
2. Ingresar cantidad igual o menor que cero.

Resultado esperado:

- El sistema rechaza la solicitud.
- No se crea un detalle inválido.

Estado:

`PENDIENTE`

---

## CP-013 — Cliente consulta pedido ajeno

Módulo:

`Pedidos`

Tipo:

`Seguridad`

Precondiciones:

- Usuario autenticado con `ROLE_CLIENTE`.
- Existe un pedido perteneciente a otro cliente.

Pasos:

1. Intentar consultar directamente el pedido ajeno.

Resultado esperado:

- El acceso es rechazado.
- No se expone información del pedido.

Estado:

`PENDIENTE`

---

## CP-014 — Emprendedor consulta pedido recibido

Módulo:

`Pedidos`

Tipo:

`Funcional`

Precondiciones:

- Usuario autenticado con `ROLE_EMPRENDEDOR`.
- Existe un pedido dirigido a uno de sus emprendimientos.

Pasos:

1. Acceder a pedidos recibidos.
2. Consultar el pedido.

Resultado esperado:

- El pedido se muestra correctamente.

Estado:

`PENDIENTE`

---

## CP-015 — Emprendedor intenta modificar pedido ajeno

Módulo:

`Pedidos`

Tipo:

`Seguridad`

Precondiciones:

- Existe un pedido correspondiente a un emprendimiento de otro usuario.

Pasos:

1. Intentar modificar el estado del pedido.

Resultado esperado:

- El sistema rechaza la operación.
- El estado original no cambia.

Estado:

`PENDIENTE`

---

## CP-016 — Persistencia de relaciones

Módulo:

`Persistencia`

Tipo:

`Integración`

Objetivo:

Verificar relaciones entre:

- usuario y rol;
- usuario y emprendimiento;
- emprendimiento y publicación;
- categoría y publicación;
- pedido y detalles.

Resultado esperado:

- Las relaciones se almacenan y recuperan correctamente.
- No se generan referencias inválidas.

Estado:

`PENDIENTE`

---

## CP-017 — API consulta publicaciones

Módulo:

`API REST`

Tipo:

`Integración`

Fase prevista:

`Fase 3`

Solicitud:

`GET /api/v1/publicaciones`

Resultado esperado:

- Código `200 OK`.
- Respuesta en JSON.
- Estructura compatible con el contrato definido.

Estado:

`PENDIENTE`

---

## CP-018 — API recurso inexistente

Módulo:

`API REST`

Tipo:

`Integración`

Fase prevista:

`Fase 3`

Solicitud conceptual:

`GET /api/v1/publicaciones/999999`

Resultado esperado:

- Código `404 Not Found`.
- Respuesta de error controlada.
- No se devuelve stack trace.

Estado:

`PENDIENTE`

---

## CP-019 — API acceso sin autorización

Módulo:

`API REST`

Tipo:

`Seguridad`

Fase prevista:

`Fase 3`

Resultado esperado:

Según el caso:

- `401 Unauthorized`; o
- `403 Forbidden`.

No deberá devolverse información protegida.

Estado:

`PENDIENTE`

---

## CP-020 — Regresión de funcionalidades principales

Módulo:

`General`

Tipo:

`Regresión`

Fase prevista:

`Fase 4`

Objetivo:

Verificar que después de integrar Spring y los componentes finales continúen funcionando:

- autenticación;
- usuarios;
- emprendimientos;
- publicaciones;
- pedidos;
- persistencia;
- API;
- seguridad.

Estado:

`PENDIENTE`

---

## 5. Evidencias

Cada prueba ejecutada deberá poder respaldarse mediante una evidencia apropiada.

Ejemplos:

- captura de pantalla;
- respuesta HTTP;
- resultado de herramienta de prueba;
- registro controlado;
- resultado automatizado;
- evidencia de base de datos.

Las evidencias deberán evitar mostrar:

- contraseñas;
- tokens;
- credenciales;
- información sensible.

---

## 6. Mantenimiento

Esta matriz deberá actualizarse cuando:

- se agregue una funcionalidad;
- cambie una regla de negocio;
- se modifique un endpoint;
- se corrija un defecto;
- cambien permisos;
- se modifique la persistencia;
- se realice una entrega.

Una prueba marcada como aprobada deberá volver a ejecutarse cuando un cambio posterior pueda afectar su comportamiento.