# Product Backlog — EmprendeLink DWF

Este documento define el Product Backlog de EmprendeLink.

El backlog es un documento vivo y puede evolucionar según:

- requisitos académicos;
- retroalimentación del docente;
- defectos;
- cambios de alcance;
- necesidades de integración.

---

# 1. Prioridades

Se utiliza MoSCoW:

```text
MUST
SHOULD
COULD
WON'T
```

## MUST

Requisito indispensable.

## SHOULD

Importante, pero no bloquea necesariamente la entrega principal.

## COULD

Deseable si existe tiempo.

## WON'T

Fuera del alcance actual.

---

# ÉPICA 1 — Usuarios y autenticación

## HU-01 — Registro de usuario

Como visitante,
quiero registrarme,
para utilizar las funcionalidades privadas de EmprendeLink.

Prioridad:

```text
MUST
```

Criterios:

- correo obligatorio;
- evitar correos duplicados;
- contraseña almacenada de forma segura;
- rol válido;
- usuario activo según reglas del sistema.

---

## HU-02 — Inicio de sesión

Como usuario registrado,
quiero iniciar sesión,
para utilizar las funciones permitidas por mi rol.

Prioridad:

```text
MUST
```

Criterios:

- validar correo;
- verificar contraseña;
- rechazar usuario inactivo;
- identificar rol;
- crear sesión válida.

---

## HU-03 — Cierre de sesión

Como usuario autenticado,
quiero cerrar sesión,
para terminar mi acceso de forma segura.

Prioridad:

```text
MUST
```

---

## HU-04 — Administración de usuarios

Como administrador,
quiero consultar y administrar usuarios,
para controlar el acceso a la plataforma.

Prioridad:

```text
SHOULD
```

---

# ÉPICA 2 — Emprendimientos

## HU-05 — Registrar emprendimiento

Como emprendedor,
quiero registrar un emprendimiento,
para ofrecer productos o servicios.

Prioridad:

```text
MUST
```

---

## HU-06 — Consultar emprendimientos propios

Como emprendedor,
quiero visualizar mis emprendimientos,
para administrarlos.

Prioridad:

```text
MUST
```

---

## HU-07 — Editar emprendimiento propio

Como emprendedor,
quiero actualizar un emprendimiento de mi propiedad.

Prioridad:

```text
MUST
```

Regla:

El usuario debe ser propietario.

---

## HU-08 — Activar o desactivar emprendimiento

Como usuario autorizado,
quiero modificar la disponibilidad de un emprendimiento.

Prioridad:

```text
SHOULD
```

---

## HU-09 — Consultar emprendimientos públicos

Como visitante,
quiero visualizar emprendimientos activos.

Prioridad:

```text
MUST
```

---

# ÉPICA 3 — Categorías

## HU-10 — Crear categoría

Como administrador,
quiero crear categorías,
para organizar las publicaciones.

Prioridad:

```text
MUST
```

---

## HU-11 — Editar categoría

Como administrador,
quiero modificar categorías existentes.

Prioridad:

```text
MUST
```

---

## HU-12 — Activar o desactivar categoría

Como administrador,
quiero controlar qué categorías pueden utilizarse.

Prioridad:

```text
SHOULD
```

---

# ÉPICA 4 — Publicaciones

## HU-13 — Crear publicación

Como emprendedor,
quiero registrar un producto o servicio,
para ofrecerlo dentro de mi emprendimiento.

Prioridad:

```text
MUST
```

---

## HU-14 — Consultar publicaciones propias

Como emprendedor,
quiero consultar las publicaciones de mis emprendimientos.

Prioridad:

```text
MUST
```

---

## HU-15 — Editar publicación propia

Como emprendedor,
quiero modificar una publicación propia.

Prioridad:

```text
MUST
```

---

## HU-16 — Activar o desactivar publicación

Como emprendedor,
quiero controlar si una publicación está disponible.

Prioridad:

```text
SHOULD
```

---

## HU-17 — Consultar catálogo público

Como visitante,
quiero consultar publicaciones activas.

Prioridad:

```text
MUST
```

---

## HU-18 — Consultar detalle

Como usuario,
quiero consultar los detalles de una publicación.

Prioridad:

```text
MUST
```

---

## HU-19 — Filtrar catálogo

Como usuario,
quiero filtrar publicaciones,
para encontrar contenido con mayor facilidad.

Prioridad:

```text
SHOULD
```

Filtros previstos:

```text
categoría
tipo
emprendimiento
```

---

# ÉPICA 5 — Pedidos

## HU-20 — Crear pedido

Como cliente,
quiero realizar un pedido,
para solicitar productos o servicios.

Prioridad:

```text
MUST
```

Criterios:

- cliente válido;
- publicaciones válidas;
- cantidades mayores a cero;
- conservar precio unitario;
- calcular subtotales;
- calcular total;
- crear detalles;
- iniciar en `PENDIENTE`.

---

## HU-21 — Consultar pedidos propios

Como cliente,
quiero consultar mis pedidos.

Prioridad:

```text
MUST
```

---

## HU-22 — Consultar pedidos recibidos

Como emprendedor,
quiero consultar pedidos dirigidos a mis emprendimientos.

Prioridad:

```text
MUST
```

---

## HU-23 — Actualizar estado de pedido

Como emprendedor,
quiero actualizar el estado de un pedido válido de mi emprendimiento.

Prioridad:

```text
MUST
```

Estados:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Las transiciones deberán respetar `docs/ROLES-PERMISOS.md`.

---

# ÉPICA 6 — Administración

## HU-24 — Supervisar emprendimientos

Como administrador,
quiero consultar emprendimientos registrados.

Prioridad:

```text
SHOULD
```

---

## HU-25 — Supervisar publicaciones

Como administrador,
quiero consultar publicaciones registradas.

Prioridad:

```text
SHOULD
```

---

# ÉPICA 7 — Seguridad

## HU-26 — Proteger funciones privadas

Como sistema,
quiero impedir que usuarios no autenticados accedan a funciones privadas.

Prioridad:

```text
MUST
```

---

## HU-27 — Validar roles

Como sistema,
quiero autorizar acciones de acuerdo con el rol del usuario.

Prioridad:

```text
MUST
```

---

## HU-28 — Validar propiedad

Como sistema,
quiero comprobar la propiedad del recurso antes de permitir modificaciones.

Prioridad:

```text
MUST
```

---

## HU-29 — Proteger contraseñas

Como sistema,
quiero almacenar contraseñas mediante hashes seguros.

Prioridad:

```text
MUST
```

---

# ÉPICA 8 — Calidad

## HU-30 — Validar entradas

Como sistema,
quiero rechazar datos inválidos.

Prioridad:

```text
MUST
```

---

## HU-31 — Manejar errores

Como sistema,
quiero mostrar errores controlados sin exponer información interna.

Prioridad:

```text
MUST
```

---

## HU-32 — Ejecutar pruebas

Como equipo,
queremos comprobar las funcionalidades antes de integrarlas.

Prioridad:

```text
MUST
```

---

## HU-33 — Mantener documentación

Como equipo,
queremos mantener la documentación sincronizada con el código.

Prioridad:

```text
MUST
```

---

# ÉPICA 9 — Persistencia JPA — Fase 2

## HU-34 — Configurar JPA

Como equipo,
queremos configurar Jakarta Persistence,
para utilizar JPA durante Fase 2.

Prioridad:

```text
MUST
```

Criterios:

- `persistence.xml`;
- compatibilidad MySQL;
- compatibilidad GlassFish;
- namespace `jakarta.*`;
- build exitoso.

---

## HU-35 — Integrar Hibernate

Como equipo,
queremos utilizar Hibernate como proveedor ORM según la configuración aprobada.

Prioridad:

```text
MUST
```

---

## HU-36 — Mapear entidades JPA

Como equipo,
queremos mapear el modelo persistente,
para representar las relaciones de la base de datos.

Prioridad:

```text
MUST
```

Entidades:

```text
RolEntity
UsuarioEntity
EmprendimientoEntity
CategoriaEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

---

## HU-37 — Mapear relaciones

Como equipo,
queremos representar las relaciones entre entidades,
para mantener la integridad del modelo.

Prioridad:

```text
MUST
```

Relaciones principales:

```text
Rol 1:N Usuario
Usuario 1:N Emprendimiento
Emprendimiento 1:N Publicacion
Categoria 1:N Publicacion
Usuario 1:N Pedido
Emprendimiento 1:N Pedido
Pedido 1:N DetallePedido
Publicacion 1:N DetallePedido
```

---

## HU-38 — Implementar DAO JPA

Como equipo,
queremos implementar los contratos DAO mediante JPA.

Prioridad:

```text
MUST
```

Módulos:

```text
usuarios
categorías
emprendimientos
publicaciones
pedidos
```

---

## HU-39 — Mantener mappers

Como equipo,
queremos transformar entre entidades JPA y dominio,
para conservar la separación arquitectónica.

Prioridad:

```text
MUST
```

Flujo:

```text
Entity ↔ Mapper ↔ Domain
```

---

# ÉPICA 10 — JSF — Fase 2

## HU-40 — Implementar autenticación JSF

Como usuario,
quiero utilizar la autenticación desde una interfaz JSF.

Prioridad:

```text
MUST
```

---

## HU-41 — Implementar gestión JSF de usuarios

Como administrador,
quiero gestionar usuarios mediante JSF.

Prioridad:

```text
MUST
```

---

## HU-42 — Implementar categorías JSF

Como administrador,
quiero gestionar categorías utilizando JSF.

Prioridad:

```text
MUST
```

---

## HU-43 — Implementar emprendimientos JSF

Como emprendedor,
quiero gestionar mis emprendimientos mediante JSF.

Prioridad:

```text
MUST
```

---

## HU-44 — Implementar publicaciones JSF

Como emprendedor,
quiero gestionar publicaciones mediante JSF.

Prioridad:

```text
MUST
```

---

## HU-45 — Implementar catálogo JSF

Como usuario,
quiero consultar y filtrar publicaciones mediante una interfaz JSF.

Prioridad:

```text
MUST
```

---

## HU-46 — Implementar pedidos JSF

Como cliente o emprendedor,
quiero interactuar con pedidos mediante JSF según mis permisos.

Prioridad:

```text
MUST
```

---

# ÉPICA 11 — AJAX, validación y converters — Fase 2

## HU-47 — Incorporar AJAX

Como usuario,
quiero obtener actualizaciones parciales de la interfaz,
para mejorar la interacción con la aplicación.

Prioridad:

```text
MUST
```

AJAX debe utilizarse en funcionalidades reales.

---

## HU-48 — Implementar validadores

Como sistema,
quiero validar correctamente entradas de formularios JSF.

Prioridad:

```text
MUST
```

---

## HU-49 — Implementar converters

Como sistema,
quiero transformar correctamente selecciones de JSF en objetos Java.

Prioridad:

```text
MUST
```

---

# ÉPICA 12 — Transacciones — Fase 2

## HU-50 — Crear pedidos transaccionalmente

Como sistema,
quiero guardar un pedido y sus detalles de manera atómica.

Prioridad:

```text
MUST
```

Resultado:

```text
todo se guarda
```

o:

```text
nada se guarda
```

---

## HU-51 — Ejecutar rollback

Como sistema,
quiero revertir una operación cuando una parte crítica falle.

Prioridad:

```text
MUST
```

---

# ÉPICA 13 — Pruebas — Fase 2

## HU-52 — Probar persistencia JPA

Como equipo,
queremos comprobar CRUD y relaciones JPA.

Prioridad:

```text
MUST
```

---

## HU-53 — Probar reglas de negocio

Como equipo,
queremos comprobar permisos, propiedad y estados.

Prioridad:

```text
MUST
```

---

## HU-54 — Probar transacciones

Como equipo,
queremos comprobar commit y rollback.

Prioridad:

```text
MUST
```

---

## HU-55 — Probar interfaz JSF

Como equipo,
queremos comprobar Beans, validaciones, converters y AJAX.

Prioridad:

```text
MUST
```

---

# Distribución por fases

## Fase 1 — Completada

Tecnologías principales:

```text
Servlets
JSP
JDBC
DAO
Services
MySQL
MVC
```

Objetivo:

Crear la base funcional inicial.

---

## Fase 2 — Actual

Tecnologías principales:

```text
JPA
Hibernate
JSF
Managed Beans
AJAX
Validators
Converters
Transacciones
Pruebas
```

Historias prioritarias:

```text
HU-34 a HU-55
```

además de mantener funcionales las historias de negocio desarrolladas en Fase 1.

---

# Distribución modular de Fase 2

## Módulo 1

```text
Autenticación y usuarios
```

Incluye principalmente:

```text
RolEntity
UsuarioEntity
JpaRolDAO
JpaUsuarioDAO
AuthBean
gestión de usuarios JSF
```

---

## Módulo 2

```text
Categorías
```

Incluye:

```text
CategoriaEntity
CategoriaMapper
JpaCategoriaDAO
CategoriaBean
vistas JSF
validación
AJAX
```

---

## Módulo 3

```text
Emprendimientos
```

Incluye:

```text
EmprendimientoEntity
EmprendimientoMapper
JpaEmprendimientoDAO
EmprendimientoBean
vistas JSF
propiedad de recursos
```

---

## Módulo 4

```text
Publicaciones y catálogo
```

Incluye:

```text
PublicacionEntity
PublicacionMapper
JpaPublicacionDAO
PublicacionBean
CatalogoBean
filtros
AJAX
```

---

## Módulo 5

```text
Pedidos
```

Incluye:

```text
PedidoEntity
DetallePedidoEntity
mappers
JpaPedidoDAO
PedidoBean
transacciones
rollback
estados
```

---

# Fase 3 — Prevista

Objetivo:

```text
API REST
cliente externo
integración
```

No debe adelantarse durante Fase 2 si perjudica el cumplimiento actual.

---

# Fase 4 — Prevista

Objetivo:

```text
Spring
seguridad avanzada
pruebas finales
despliegue
```

---

# Definición de terminado

Una historia puede considerarse terminada cuando:

1. compila;
2. funciona;
3. respeta arquitectura;
4. respeta permisos;
5. maneja errores;
6. mantiene integridad;
7. tiene pruebas o evidencia;
8. no contiene secretos;
9. la documentación afectada está actualizada;
10. `mvn clean package` finaliza correctamente;
11. está lista para Pull Request hacia `develop`.

---

# Regla del backlog

El backlog puede:

- ampliarse;
- dividirse;
- reorganizarse;
- cambiar prioridades;
- recibir nuevos criterios.

Todo cambio importante debe conservar coherencia entre:

```text
requisitos
código
documentación
pruebas
```