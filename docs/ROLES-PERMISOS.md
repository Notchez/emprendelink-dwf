# Roles y permisos — EmprendeLink DWF

Este documento define los roles oficiales, permisos, propiedad de recursos y reglas de autorización de EmprendeLink.

Estas reglas deben mantenerse consistentes en:

- Services;
- Servlets;
- Managed Beans;
- vistas;
- persistencia;
- pruebas;
- API cuando corresponda;
- seguridad futura.

---

# 1. Roles oficiales

Roles persistidos:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

Los nombres deben mantenerse exactamente iguales en:

- Java;
- base de datos;
- JPA;
- seguridad;
- pruebas.

---

# 2. Visitante

Un visitante no representa un rol persistido.

Es una persona que todavía no ha iniciado sesión.

## Permitido

- acceder al login;
- acceder al registro;
- consultar catálogo público;
- consultar emprendimientos activos;
- consultar publicaciones activas;
- consultar detalles públicos.

## No permitido

- crear pedidos;
- consultar pedidos privados;
- gestionar emprendimientos;
- gestionar publicaciones;
- administrar categorías;
- administrar usuarios;
- modificar información privada.

---

# 3. ROLE_CLIENTE

Representa al usuario que utiliza EmprendeLink principalmente para consultar ofertas y realizar pedidos.

## Permitido

- iniciar sesión;
- cerrar sesión;
- consultar catálogo;
- consultar publicaciones;
- consultar emprendimientos;
- crear pedidos;
- consultar sus propios pedidos;
- consultar el detalle de sus propios pedidos;
- actualizar información personal permitida.

## No permitido

- administrar usuarios;
- administrar categorías;
- gestionar emprendimientos;
- gestionar publicaciones;
- consultar pedidos de otros clientes;
- modificar pedidos dirigidos a emprendimientos;
- acceder a recursos administrativos.

---

# 4. ROLE_EMPRENDEDOR

Representa al usuario autorizado para gestionar sus emprendimientos.

## Permitido

- iniciar sesión;
- cerrar sesión;
- consultar catálogo;
- consultar sus emprendimientos;
- crear emprendimientos;
- editar sus emprendimientos;
- gestionar publicaciones de sus emprendimientos;
- consultar pedidos recibidos;
- consultar detalle de pedidos recibidos;
- modificar el estado de pedidos válidos recibidos;
- actualizar información personal permitida.

## No permitido

- modificar emprendimientos ajenos;
- modificar publicaciones ajenas;
- consultar pedidos de emprendimientos ajenos;
- administrar usuarios;
- administrar categorías globales;
- utilizar funciones exclusivas de administrador.

---

# 5. ROLE_ADMIN

Representa al usuario administrativo de la plataforma.

## Permitido

- iniciar sesión;
- cerrar sesión;
- consultar usuarios;
- activar o desactivar usuarios cuando corresponda;
- gestionar categorías;
- supervisar emprendimientos;
- supervisar publicaciones;
- acceder a funciones administrativas;
- consultar información necesaria para administración.

## Restricción

El rol administrador no elimina las reglas de integridad.

Una operación administrativa también debe respetar:

- llaves;
- relaciones;
- reglas de negocio;
- consistencia.

---

# 6. Matriz general

| Funcionalidad | Visitante | Cliente | Emprendedor | Admin |
|---|---:|---:|---:|---:|
| Consultar catálogo | Sí | Sí | Sí | Sí |
| Ver publicaciones activas | Sí | Sí | Sí | Sí |
| Ver emprendimientos activos | Sí | Sí | Sí | Sí |
| Registrarse | Sí | No aplica | No aplica | No aplica |
| Iniciar sesión | Sí | Sí | Sí | Sí |
| Crear pedido | No | Sí | No | No |
| Consultar pedidos propios | No | Sí | No | No |
| Gestionar emprendimiento propio | No | No | Sí | No |
| Gestionar publicación propia | No | No | Sí | No |
| Consultar pedidos recibidos | No | No | Sí | No |
| Cambiar estado de pedido recibido | No | No | Sí | No |
| Administrar categorías | No | No | No | Sí |
| Administrar usuarios | No | No | No | Sí |
| Supervisar emprendimientos | No | No | No | Sí |
| Supervisar publicaciones | No | No | No | Sí |

---

# 7. Autenticación

Una operación privada requiere una sesión válida.

El sistema debe identificar al menos:

```text
usuario
rol
estado activo
```

Un usuario inactivo no debe autenticarse exitosamente.

---

# 8. Autorización

Autenticación y autorización no son lo mismo.

```text
Autenticación
= saber quién es el usuario
```

```text
Autorización
= determinar qué puede hacer
```

Después de autenticar debe verificarse el permiso correspondiente.

---

# 9. Propiedad de recursos

Tener el rol adecuado no permite modificar cualquier recurso.

Ejemplo:

```text
ROLE_EMPRENDEDOR
```

puede modificar un emprendimiento únicamente si:

```text
emprendimiento.idPropietario
==
usuarioAutenticado.idUsuario
```

La misma regla aplica indirectamente a publicaciones y pedidos relacionados.

---

# 10. Emprendimientos

Un emprendedor puede modificar únicamente emprendimientos propios.

Debe verificarse:

```text
usuario autenticado
rol emprendedor
propiedad
estado permitido
```

No confiar en un ID enviado por formulario como prueba de propiedad.

---

# 11. Publicaciones

Una publicación pertenece a un emprendimiento.

Para modificarla debe comprobarse que:

```text
publicación
  ↓
emprendimiento
  ↓
propietario
  ↓
usuario autenticado
```

El usuario no puede obtener permiso simplemente cambiando el ID de la URL o formulario.

---

# 12. Categorías

La administración global de categorías corresponde a:

```text
ROLE_ADMIN
```

Los emprendedores pueden seleccionar categorías válidas para sus publicaciones, pero no administrarlas globalmente.

---

# 13. Pedidos

## Cliente

Puede:

- crear pedidos;
- consultar sus pedidos;
- consultar sus detalles.

No puede:

- consultar pedidos de terceros;
- cambiar el estado operativo del pedido.

---

## Emprendedor

Puede:

- consultar pedidos dirigidos a emprendimientos propios;
- consultar detalles;
- actualizar estados permitidos.

Debe verificarse propiedad del emprendimiento antes de permitir cualquier modificación.

---

## Administrador

Puede realizar funciones de supervisión cuando exista una necesidad administrativa.

No se considera operador normal del flujo comercial del pedido.

---

# 14. Estados oficiales de pedido

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

El estado inicial de un pedido es:

```text
PENDIENTE
```

---

# 15. Transiciones permitidas

Las transiciones normales son:

```text
PENDIENTE
   ├──► CONFIRMADO
   └──► CANCELADO

CONFIRMADO
   ├──► EN_PROCESO
   └──► CANCELADO

EN_PROCESO
   ├──► COMPLETADO
   └──► CANCELADO

COMPLETADO
   └──► estado terminal

CANCELADO
   └──► estado terminal
```

Tabla:

| Estado actual | Estado permitido |
|---|---|
| `PENDIENTE` | `CONFIRMADO`, `CANCELADO` |
| `CONFIRMADO` | `EN_PROCESO`, `CANCELADO` |
| `EN_PROCESO` | `COMPLETADO`, `CANCELADO` |
| `COMPLETADO` | Ninguno |
| `CANCELADO` | Ninguno |

---

# 16. Transiciones inválidas

Ejemplos que deben rechazarse:

```text
PENDIENTE → COMPLETADO
PENDIENTE → EN_PROCESO
CONFIRMADO → PENDIENTE
EN_PROCESO → CONFIRMADO
COMPLETADO → PENDIENTE
CANCELADO → CONFIRMADO
```

No es suficiente comprobar que ambos estados existen dentro de `EstadoPedido`.

Debe verificarse la transición.

---

# 17. Lugar de validación de estados

La regla debe encontrarse principalmente en:

```text
PedidoService
```

o su implementación correspondiente.

No debe existir únicamente en:

- XHTML;
- JavaScript;
- Managed Bean;
- Servlet.

La interfaz puede limitar opciones visuales, pero el backend debe volver a validar.

---

# 18. Seguridad en JSF

Los Managed Beans deben verificar los mismos permisos que la implementación web anterior.

Ejemplo:

```text
PedidoBean
   ↓
PedidoService
   ↓
validar usuario
validar rol
validar propiedad
validar transición
```

No considerar:

```text
rendered="false"
```

como mecanismo suficiente de seguridad.

---

# 19. Acceso directo

Un usuario puede intentar introducir directamente:

```text
URL
ID
parámetro
```

aunque la interfaz no muestre la acción.

El backend debe rechazar la operación igualmente.

---

# 20. Contraseñas

Las contraseñas nunca deben:

- almacenarse en texto plano;
- registrarse en logs;
- mostrarse en errores;
- incluirse en respuestas;
- exponerse a otros usuarios.

La aplicación debe guardar únicamente representaciones seguras compatibles con el mecanismo de hashing utilizado.

---

# 21. Usuarios inactivos

Un usuario inactivo debe ser rechazado durante autenticación.

Si el estado cambia durante una sesión, las operaciones sensibles deberán aplicar la política definida por el sistema.

---

# 22. Errores de autorización

Las operaciones no autorizadas deben producir una respuesta controlada.

Según la tecnología:

```text
mensaje JSF
redirect
página 403
excepción de autorización
respuesta HTTP
```

No mostrar:

```text
stack trace
SQL
información sensible
```

---

# 23. Excepciones

Cuando corresponda puede utilizarse:

```text
AccesoNoAutorizadoException
```

u otra excepción del dominio apropiada.

El mensaje debe ser comprensible sin revelar detalles internos.

---

# 24. Persistencia JPA

Las relaciones JPA no sustituyen las reglas de autorización.

Una relación correcta entre:

```text
UsuarioEntity
EmprendimientoEntity
```

no significa automáticamente que el usuario tenga permiso para modificar cualquier `EmprendimientoEntity`.

La autorización sigue perteneciendo a la lógica del sistema.

---

# 25. Pruebas obligatorias de seguridad

Durante Fase 2 deben comprobarse al menos:

- cliente intentando leer pedido ajeno;
- emprendedor intentando modificar emprendimiento ajeno;
- emprendedor intentando modificar publicación ajena;
- emprendedor intentando cambiar pedido ajeno;
- usuario sin rol administrativo intentando administrar categorías;
- transición inválida de pedido;
- modificación de pedido terminal.

---

# 26. Spring Security

Los roles actuales deberán reutilizarse en la fase correspondiente:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

No crear roles alternativos simplemente por incorporar Spring.

La integración de Spring Security corresponde a una fase posterior.

---

# 27. Cambios

Si un permiso cambia deben revisarse también:

```text
Service
Servlet
Managed Bean
XHTML
pruebas
API
documentación
```

No modificar un permiso únicamente en la interfaz.

---

# 28. Principio final

Toda operación sensible debe responder estas preguntas:

```text
¿Está autenticado?
¿Está activo?
¿Tiene el rol correcto?
¿Es propietario cuando corresponde?
¿El recurso está en un estado válido?
¿La operación está permitida?
```

Solo si las validaciones necesarias son correctas debe ejecutarse la operación.