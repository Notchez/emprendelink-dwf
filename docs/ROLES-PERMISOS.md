# Roles y permisos — EmprendeLink DWF

Roles oficiales:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

## Visitante

Puede:

- abrir login;
- registrarse;
- crear una cuenta Cliente o Emprendedor.

No puede:

- acceder a administración;
- crear un rol Admin;
- acceder a datos privados.

## ROLE_CLIENTE

Puede:

- iniciar/cerrar sesión;
- acceder al inicio autenticado;
- consultar catálogo cuando esté implementado;
- crear pedidos cuando el módulo esté implementado;
- consultar únicamente sus propios pedidos.

No puede:

- administrar usuarios;
- administrar categorías;
- gestionar emprendimientos;
- gestionar publicaciones de emprendedores;
- consultar pedidos de otros clientes.

## ROLE_EMPRENDEDOR

Puede:

- iniciar/cerrar sesión;
- acceder al inicio autenticado;
- gestionar sus propios emprendimientos;
- gestionar publicaciones de sus propios emprendimientos;
- consultar pedidos dirigidos a sus emprendimientos;
- gestionar estados permitidos de esos pedidos.

No puede:

- editar recursos de otro emprendedor;
- administrar usuarios;
- administrar categorías globales.

## ROLE_ADMIN

Puede:

- iniciar/cerrar sesión;
- consultar usuarios;
- activar/desactivar usuarios;
- administrar categorías;
- supervisar módulos administrativos según se implementen.

No puede desactivar su propia cuenta desde la gestión de usuarios.

## Seguridad de rutas

Actualmente:

```text
/admin/* → ROLE_ADMIN
/inicio.xhtml → sesión válida
```

Los módulos nuevos deben agregar protección equivalente según rol y propiedad.

## Propiedad

El rol no es suficiente para operaciones sobre recursos propios.

Ejemplos:

- un Emprendedor solo modifica sus emprendimientos;
- una publicación solo puede modificarse si pertenece a un emprendimiento del usuario;
- un Cliente solo consulta sus pedidos;
- un Emprendedor solo gestiona pedidos de sus emprendimientos.

## Contraseñas

Nunca deben:

- persistirse en texto plano;
- mostrarse en UI;
- guardarse en sesión;
- incluirse en DTO;
- aparecer en logs.

El sistema almacena hashes PBKDF2.

## Fase 3

La API REST debe aplicar las mismas reglas de autorización.

## Fase 4

Spring Security podrá reutilizar los mismos roles y reglas.
