# Product Backlog — EmprendeLink DWF

Este documento contiene el Product Backlog inicial de EmprendeLink para la materia Desarrollo de Aplicaciones con Web Frameworks (DWF).

El backlog evolucionará durante el ciclo académico conforme se definan nuevos requisitos, correcciones y criterios indicados por el docente.

---

## 1. Convención de prioridades

Se utilizará la clasificación MoSCoW:

- `MUST`: requisito indispensable.
- `SHOULD`: requisito importante, pero no crítico.
- `COULD`: requisito deseable.
- `WON'T`: fuera del alcance actual.

---

# ÉPICA 1 — Gestión de usuarios y autenticación

## HU-01 — Registro de usuario

Como visitante,
quiero registrarme en EmprendeLink,
para poder utilizar las funcionalidades privadas de la plataforma.

Prioridad:

`MUST`

Criterios iniciales:

- solicitar datos obligatorios;
- validar correo;
- evitar correos duplicados;
- almacenar la contraseña de forma segura;
- asignar un rol válido;
- registrar fecha de creación.

---

## HU-02 — Inicio de sesión

Como usuario registrado,
quiero iniciar sesión,
para acceder a las funcionalidades correspondientes a mi rol.

Prioridad:

`MUST`

Criterios iniciales:

- validar credenciales;
- rechazar usuarios inactivos;
- crear una sesión válida;
- identificar el rol del usuario;
- redirigir según corresponda.

---

## HU-03 — Cierre de sesión

Como usuario autenticado,
quiero cerrar mi sesión,
para finalizar de forma segura mi acceso al sistema.

Prioridad:

`MUST`

---

## HU-04 — Administración de usuarios

Como administrador,
quiero consultar y activar o desactivar usuarios,
para gestionar el acceso a la plataforma.

Prioridad:

`SHOULD`

---

# ÉPICA 2 — Gestión de emprendimientos

## HU-05 — Registrar emprendimiento

Como emprendedor,
quiero registrar un emprendimiento,
para publicar mis productos o servicios.

Prioridad:

`MUST`

Criterios iniciales:

- registrar nombre;
- descripción;
- contacto;
- propietario;
- fecha de registro;
- estado activo.

---

## HU-06 — Consultar emprendimientos propios

Como emprendedor,
quiero consultar mis emprendimientos,
para poder administrarlos.

Prioridad:

`MUST`

---

## HU-07 — Editar emprendimiento

Como emprendedor,
quiero modificar los datos de un emprendimiento propio,
para mantener su información actualizada.

Prioridad:

`MUST`

El sistema deberá verificar que el emprendimiento pertenezca al usuario autenticado.

---

## HU-08 — Activar o desactivar emprendimiento

Como emprendedor,
quiero cambiar el estado de mis emprendimientos,
para controlar su visibilidad.

Prioridad:

`SHOULD`

---

## HU-09 — Consultar emprendimientos públicos

Como visitante o usuario,
quiero consultar emprendimientos activos,
para conocer los negocios disponibles en la plataforma.

Prioridad:

`MUST`

---

# ÉPICA 3 — Categorías

## HU-10 — Administrar categorías

Como administrador,
quiero crear y modificar categorías,
para organizar correctamente las publicaciones.

Prioridad:

`MUST`

---

## HU-11 — Activar o desactivar categorías

Como administrador,
quiero cambiar el estado de las categorías,
para controlar cuáles pueden utilizarse.

Prioridad:

`SHOULD`

---

# ÉPICA 4 — Gestión de publicaciones

## HU-12 — Crear publicación

Como emprendedor,
quiero publicar un producto o servicio,
para ofrecerlo a posibles clientes.

Prioridad:

`MUST`

Criterios iniciales:

- seleccionar emprendimiento;
- seleccionar categoría;
- indicar tipo;
- ingresar nombre;
- descripción;
- precio;
- stock cuando corresponda;
- registrar fecha;
- establecer estado activo.

---

## HU-13 — Consultar publicaciones propias

Como emprendedor,
quiero consultar las publicaciones de mis emprendimientos,
para administrarlas.

Prioridad:

`MUST`

---

## HU-14 — Editar publicación

Como emprendedor,
quiero modificar una publicación propia,
para mantener actualizada la información de mi oferta.

Prioridad:

`MUST`

---

## HU-15 — Activar o desactivar publicación

Como emprendedor,
quiero cambiar la disponibilidad de una publicación,
para controlar si aparece en el catálogo.

Prioridad:

`SHOULD`

---

## HU-16 — Consultar catálogo público

Como visitante o usuario,
quiero consultar las publicaciones activas,
para conocer los productos y servicios disponibles.

Prioridad:

`MUST`

---

## HU-17 — Consultar detalle de publicación

Como visitante o usuario,
quiero consultar el detalle de una publicación,
para conocer información adicional antes de realizar un pedido.

Prioridad:

`MUST`

---

## HU-18 — Filtrar publicaciones

Como usuario,
quiero filtrar publicaciones,
para encontrar más fácilmente productos o servicios de mi interés.

Prioridad:

`COULD`

Filtros previstos:

- categoría;
- tipo;
- emprendimiento.

---

# ÉPICA 5 — Gestión de pedidos

## HU-19 — Crear pedido

Como cliente,
quiero realizar un pedido,
para solicitar productos o servicios de un emprendimiento.

Prioridad:

`MUST`

Criterios iniciales:

- seleccionar publicaciones;
- indicar cantidades;
- validar disponibilidad;
- conservar precio unitario;
- calcular subtotales;
- calcular total;
- generar detalles;
- crear pedido con estado `PENDIENTE`.

---

## HU-20 — Consultar pedidos propios

Como cliente,
quiero consultar los pedidos que he realizado,
para conocer su información y estado.

Prioridad:

`MUST`

---

## HU-21 — Consultar pedidos recibidos

Como emprendedor,
quiero consultar los pedidos recibidos por mis emprendimientos,
para poder gestionarlos.

Prioridad:

`MUST`

---

## HU-22 — Actualizar estado de pedido

Como emprendedor,
quiero cambiar el estado de un pedido recibido,
para reflejar su avance.

Prioridad:

`MUST`

Estados previstos:

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PROCESO`
- `COMPLETADO`
- `CANCELADO`

---

# ÉPICA 6 — Administración

## HU-23 — Supervisar emprendimientos

Como administrador,
quiero consultar los emprendimientos registrados,
para supervisar el contenido de la plataforma.

Prioridad:

`SHOULD`

---

## HU-24 — Supervisar publicaciones

Como administrador,
quiero consultar las publicaciones registradas,
para supervisar el contenido disponible.

Prioridad:

`SHOULD`

---

# ÉPICA 7 — API REST

## HU-25 — Consultar emprendimientos mediante API

Como cliente externo,
quiero obtener emprendimientos mediante una API REST,
para consumir la información desde otra aplicación.

Prioridad:

`MUST`

Fase prevista:

`Fase 3`

---

## HU-26 — Consultar publicaciones mediante API

Como cliente externo,
quiero obtener publicaciones mediante una API REST,
para mostrar el catálogo desde otra aplicación.

Prioridad:

`MUST`

Fase prevista:

`Fase 3`

---

## HU-27 — Crear pedido mediante API

Como cliente autorizado,
quiero crear pedidos mediante la API,
para realizar operaciones desde un cliente externo.

Prioridad:

`MUST`

Fase prevista:

`Fase 3`

---

## HU-28 — Consultar pedido mediante API

Como cliente autorizado,
quiero consultar un pedido mediante la API,
para conocer sus datos y estado.

Prioridad:

`MUST`

Fase prevista:

`Fase 3`

---

## HU-29 — Actualizar estado mediante API

Como emprendedor autorizado,
quiero actualizar el estado de un pedido mediante la API,
para gestionar pedidos desde un cliente externo.

Prioridad:

`SHOULD`

Fase prevista:

`Fase 3`

---

# ÉPICA 8 — Cliente externo

## HU-30 — Consumir catálogo desde cliente externo

Como usuario,
quiero visualizar publicaciones mediante un cliente externo,
para demostrar la integración con la API REST.

Prioridad:

`MUST`

Fase prevista:

`Fase 3`

---

## HU-31 — Consultar detalle desde cliente externo

Como usuario,
quiero visualizar el detalle de una publicación,
para demostrar la consulta de recursos individuales mediante la API.

Prioridad:

`MUST`

Fase prevista:

`Fase 3`

---

## HU-32 — Ejecutar operación transaccional desde cliente externo

Como usuario autorizado,
quiero realizar una operación mediante el cliente externo,
para demostrar una integración completa con el backend.

Prioridad:

`MUST`

Fase prevista:

`Fase 3`

---

# ÉPICA 9 — Seguridad

## HU-33 — Proteger rutas por autenticación

Como sistema,
quiero impedir el acceso no autenticado a funciones privadas,
para proteger la información y operaciones de los usuarios.

Prioridad:

`MUST`

---

## HU-34 — Proteger recursos por rol

Como sistema,
quiero autorizar operaciones según el rol del usuario,
para evitar accesos indebidos.

Prioridad:

`MUST`

---

## HU-35 — Validar propiedad de recursos

Como sistema,
quiero comprobar que un usuario sea propietario del recurso que intenta modificar,
para evitar modificaciones sobre información ajena.

Prioridad:

`MUST`

---

## HU-36 — Integrar Spring Security

Como equipo de desarrollo,
queremos implementar seguridad utilizando Spring Security,
para centralizar autenticación y autorización durante la fase final.

Prioridad:

`MUST`

Fase prevista:

`Fase 4`

---

# ÉPICA 10 — Calidad y liberación

## HU-37 — Validar entradas

Como sistema,
quiero validar los datos ingresados,
para evitar información inválida o inconsistente.

Prioridad:

`MUST`

---

## HU-38 — Manejar errores de forma controlada

Como sistema,
quiero manejar errores de forma consistente,
para evitar fallos sin controlar y exposición de información interna.

Prioridad:

`MUST`

---

## HU-39 — Ejecutar pruebas

Como equipo de desarrollo,
queremos ejecutar pruebas funcionales, de integración y regresión,
para verificar la calidad del sistema.

Prioridad:

`MUST`

---

## HU-40 — Documentar instalación y ejecución

Como integrante del equipo o evaluador,
quiero disponer de instrucciones claras,
para poder ejecutar el proyecto correctamente.

Prioridad:

`MUST`

---

## HU-41 — Preparar versión final

Como equipo de desarrollo,
queremos generar una versión estable del sistema,
para realizar la entrega y demostración final.

Prioridad:

`MUST`

Fase prevista:

`Fase 4`

---

# Distribución inicial por fases

## Fase 1 — Fundamentos Java Web y MVC

Prioridad principal:

- HU-01
- HU-02
- HU-03
- HU-05
- HU-06
- HU-07
- HU-09
- HU-10
- HU-12
- HU-13
- HU-14
- HU-16
- HU-17
- HU-19
- HU-20
- HU-21
- HU-22
- HU-37
- HU-38

Tecnologías principales:

- Java Web
- JDBC
- DAO
- Servlets
- JSP
- JSTL
- MySQL
- MVC

---

## Fase 2 — Persistencia y JSF

Objetivo principal:

Evolucionar las funcionalidades existentes incorporando:

- JPA / Hibernate;
- JSF;
- Managed Beans;
- AJAX;
- validadores;
- convertidores;
- relaciones y transacciones.

No se deberá reconstruir un sistema diferente.

---

## Fase 3 — REST e integración

Historias principales:

- HU-25
- HU-26
- HU-27
- HU-28
- HU-29
- HU-30
- HU-31
- HU-32

Objetivo:

Exponer funcionalidades mediante una API REST y consumirlas desde un cliente externo.

---

## Fase 4 — Spring, seguridad y liberación

Historias principales:

- HU-33
- HU-34
- HU-35
- HU-36
- HU-38
- HU-39
- HU-40
- HU-41

Objetivo:

Integrar componentes Spring, fortalecer seguridad, completar pruebas, documentación y despliegue.

---

# Regla del backlog

El Product Backlog es un documento vivo.

Las historias podrán:

- dividirse;
- ampliarse;
- cambiar de prioridad;
- recibir nuevos criterios;
- reorganizarse entre fases;

según los requerimientos del docente y las necesidades reales del proyecto.

Cualquier cambio relevante deberá mantenerse sincronizado con la planificación y el repositorio.