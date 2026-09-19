# Alcance de Fase 1 — Integrante 2

## Problema

Muchos emprendimientos pequeños no cuentan con un espacio sencillo donde puedan registrar su negocio y mostrar sus productos o servicios a posibles clientes.

## Solución propuesta

EmprendeLink permitirá registrar emprendimientos y publicaciones para que los clientes puedan consultar un catálogo básico. El sistema se desarrollará por fases y en esta primera etapa se utilizará Java Web con arquitectura MVC, JDBC, MySQL, Servlets y JSP/JSTL.

## Usuarios

- Emprendedor: administra la información de su emprendimiento y sus publicaciones.
- Cliente: consulta las publicaciones disponibles.
- Administrador: queda contemplado en el dominio para administración futura.

## Alcance del Sprint I

Para esta fase se trabajará principalmente con:

1. Registro de emprendimientos.
2. Actualización de información de emprendimientos.
3. Registro de publicaciones.
4. Consulta del catálogo.
5. Consulta del detalle de una publicación.

La gestión completa de pedidos queda modelada en el dominio para mantener consistencia con el proyecto, pero no es el flujo principal seleccionado para el Sprint I.

## Requerimientos funcionales

- RF-01: registrar un emprendimiento asociado a un usuario.
- RF-02: actualizar los datos principales de un emprendimiento.
- RF-03: registrar publicaciones de tipo producto o servicio.
- RF-04: listar publicaciones activas.
- RF-05: consultar el detalle de una publicación.
- RF-06: clasificar publicaciones por categoría.
- RF-07: mantener roles de usuario y estados básicos del dominio.

## Requerimientos no funcionales

- RNF-01: utilizar Java y las convenciones definidas por el equipo.
- RNF-02: mantener separación de responsabilidades según MVC.
- RNF-03: no almacenar contraseñas en texto plano.
- RNF-04: utilizar MySQL como base de datos.
- RNF-05: mantener nombres y tipos consistentes entre Java y base de datos.
- RNF-06: validar datos tanto en servidor como en las capas correspondientes.
- RNF-07: mantener el código legible y con encapsulamiento.

## Fuera del alcance del Sprint I

- JPA/Hibernate.
- JSF.
- API REST.
- Spring.
- Seguridad completa.
- Integraciones externas.
