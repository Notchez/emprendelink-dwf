# Roles y permisos — EmprendeLink DWF

Este documento define los roles oficiales del sistema EmprendeLink y las acciones permitidas para cada uno.

Los permisos definidos aquí deberán mantenerse consistentes en:

- lógica de negocio;
- controladores;
- vistas;
- API REST;
- seguridad;
- pruebas.

---

## 1. Roles oficiales

Los roles persistidos dentro del sistema serán:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

Estos valores deberán utilizarse exactamente con la misma escritura en Java, base de datos y componentes de seguridad.

---

## 2. Visitante

Un visitante representa a una persona que accede al sistema sin haber iniciado sesión.

No constituye un rol persistido dentro de la base de datos.

### Acciones permitidas

- consultar el catálogo público;
- visualizar emprendimientos activos;
- visualizar publicaciones activas;
- consultar detalles de una publicación;
- acceder al inicio de sesión;
- acceder al registro de usuario cuando corresponda.

### Acciones no permitidas

- crear pedidos;
- administrar emprendimientos;
- administrar publicaciones;
- consultar información privada;
- acceder a funciones administrativas.

---

## 3. ROLE_CLIENTE

Representa a un usuario registrado que utiliza EmprendeLink para consultar ofertas y realizar pedidos.

### Acciones permitidas

- iniciar y cerrar sesión;
- consultar el catálogo;
- consultar emprendimientos activos;
- consultar publicaciones activas;
- visualizar detalles de publicaciones;
- crear pedidos;
- consultar sus propios pedidos;
- consultar el detalle de sus propios pedidos;
- actualizar los datos permitidos de su perfil.

### Restricciones

Un cliente no podrá:

- administrar emprendimientos;
- crear o modificar publicaciones;
- administrar categorías;
- modificar usuarios de terceros;
- consultar pedidos de otros clientes;
- administrar pedidos pertenecientes a emprendimientos ajenos;
- acceder a funciones administrativas.

---

## 4. ROLE_EMPRENDEDOR

Representa a un usuario autorizado para registrar y gestionar emprendimientos y sus publicaciones.

### Acciones permitidas

- iniciar y cerrar sesión;
- consultar el catálogo;
- gestionar sus propios emprendimientos;
- crear publicaciones para sus emprendimientos;
- modificar sus propias publicaciones;
- activar o desactivar sus propias publicaciones;
- consultar los pedidos recibidos por sus emprendimientos;
- consultar el detalle de dichos pedidos;
- gestionar el estado de pedidos correspondientes a sus emprendimientos;
- actualizar los datos permitidos de su perfil.

### Restricciones

Un emprendedor no podrá:

- modificar emprendimientos pertenecientes a otros usuarios;
- modificar publicaciones de otros emprendimientos;
- consultar información privada de pedidos ajenos a sus emprendimientos;
- administrar categorías globales;
- administrar usuarios;
- acceder a funciones exclusivas del administrador.

---

## 5. ROLE_ADMIN

Representa al usuario con permisos administrativos dentro de EmprendeLink.

### Acciones permitidas

- iniciar y cerrar sesión;
- consultar usuarios;
- activar o desactivar usuarios;
- administrar categorías;
- consultar emprendimientos;
- activar o desactivar emprendimientos cuando corresponda;
- consultar publicaciones;
- activar o desactivar publicaciones cuando corresponda;
- consultar información general necesaria para administración;
- acceder a funciones administrativas del sistema.

### Restricciones generales

Las funciones administrativas deberán utilizarse únicamente para la administración de la plataforma.

Las acciones del administrador también deberán respetar las reglas de integridad y negocio del sistema.

---

## 6. Matriz general de permisos

| Funcionalidad | Visitante | Cliente | Emprendedor | Administrador |
|---|---:|---:|---:|---:|
| Consultar catálogo | Sí | Sí | Sí | Sí |
| Ver emprendimientos activos | Sí | Sí | Sí | Sí |
| Ver publicaciones activas | Sí | Sí | Sí | Sí |
| Iniciar sesión | Sí | Sí | Sí | Sí |
| Crear pedido | No | Sí | No | No |
| Consultar pedidos propios | No | Sí | No | No |
| Gestionar emprendimiento propio | No | No | Sí | No |
| Gestionar publicaciones propias | No | No | Sí | No |
| Consultar pedidos recibidos | No | No | Sí | No |
| Cambiar estado de pedidos recibidos | No | No | Sí | No |
| Administrar categorías | No | No | No | Sí |
| Administrar usuarios | No | No | No | Sí |
| Supervisar emprendimientos | No | No | No | Sí |
| Supervisar publicaciones | No | No | No | Sí |

---

## 7. Propiedad de los recursos

Tener un rol autorizado no será suficiente para modificar cualquier recurso.

También deberá verificarse la propiedad del recurso cuando corresponda.

Ejemplo:

Un usuario con:

`ROLE_EMPRENDEDOR`

podrá modificar un emprendimiento únicamente si dicho emprendimiento pertenece al mismo usuario.

De manera equivalente, una publicación solo podrá ser modificada por el propietario del emprendimiento al que pertenece.

---

## 8. Pedidos

### Cliente

Un cliente podrá:

- crear pedidos;
- consultar únicamente sus propios pedidos.

### Emprendedor

Un emprendedor podrá consultar y gestionar únicamente pedidos dirigidos a emprendimientos de su propiedad.

### Administrador

El acceso administrativo a pedidos deberá limitarse a las funciones necesarias para supervisión o soporte del sistema.

---

## 9. Estados de pedido

Los estados oficiales son:

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PROCESO`
- `COMPLETADO`
- `CANCELADO`

Los cambios de estado deberán implementarse mediante reglas de negocio y no mediante modificaciones arbitrarias desde la interfaz.

Las transiciones permitidas podrán detallarse posteriormente según los requisitos funcionales de la fase correspondiente.

---

## 10. Control de acceso

La autorización deberá verificarse en el backend.

Ocultar un botón o enlace en la interfaz no constituye una medida suficiente de seguridad.

Por ejemplo, aunque un cliente no visualice una opción administrativa, el servidor deberá rechazar igualmente cualquier intento de acceder directamente a una ruta protegida.

---

## 11. Respuestas ante acceso no autorizado

Cuando un usuario intente acceder a un recurso para el cual no posee permisos, la aplicación deberá responder de manera controlada.

Según la tecnología utilizada podrá aplicarse:

- redirección;
- página de acceso denegado;
- respuesta HTTP apropiada;
- excepción de autorización.

Para la API REST se utilizarán códigos HTTP apropiados, por ejemplo:

- `401 Unauthorized` cuando no exista autenticación válida;
- `403 Forbidden` cuando el usuario esté autenticado pero no tenga permiso.

---

## 12. Seguridad de contraseñas

Las contraseñas nunca deberán:

- almacenarse en texto plano;
- mostrarse en respuestas;
- incluirse en logs;
- exponerse mediante la API.

El sistema almacenará únicamente hashes seguros de contraseña.

---

## 13. Evolución hacia Spring Security

Durante la fase correspondiente, los roles oficiales serán reutilizados por Spring Security:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

No se deberán crear nombres alternativos para representar los mismos permisos.

Las reglas de autorización deberán derivarse de este documento y de las reglas de negocio vigentes.

---

## 14. Cambios de permisos

Cualquier modificación en los permisos deberá revisarse antes de implementarse.

Si un permiso cambia, deberán revisarse también:

- controladores;
- servicios;
- vistas;
- API REST;
- configuración de seguridad;
- matriz de pruebas;
- documentación.

El objetivo es evitar que diferentes componentes del sistema apliquen reglas de autorización contradictorias.