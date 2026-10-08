# Arquitectura — EmprendeLink DWF

## 1. Estado actual

EmprendeLink se encuentra en Fase 2.

Arquitectura activa:

```text
XHTML / JSF
    ↓
Managed Beans CDI
    ↓
Services
    ↓
Interfaces DAO
    ↓
DAO JPA
    ↓
Entidades JPA
    ↓
EclipseLink
    ↓
JTA
    ↓
GlassFish DataSource
    ↓
MySQL
```

La arquitectura anterior de JDBC + Servlets + JSP ya no forma parte del runtime actual.

## 2. Presentación

Las vistas utilizan XHTML y Jakarta Faces.

Beans:

```text
emprendelink.web.jsf
```

Responsabilidades:

- recibir interacción de la vista;
- invocar Services;
- preparar datos para render;
- mostrar mensajes;
- coordinar AJAX.

No contienen consultas JPA.

## 3. Negocio

Interfaces:

```text
emprendelink.service
```

Implementaciones:

```text
emprendelink.service.impl
```

Responsabilidades:

- reglas de negocio;
- autorización funcional;
- validaciones de negocio;
- coordinación de operaciones;
- límites transaccionales.

## 4. Persistencia

Contratos DAO:

```text
emprendelink.dao
```

Implementaciones:

```text
emprendelink.dao.jpa
```

Acceso JPA:

```java
@PersistenceContext(unitName = "emprendelinkPU")
private EntityManager entityManager;
```

No se crean `EntityManagerFactory` manualmente.

## 5. Transacciones

Las transacciones son administradas con JTA.

Los Services pueden utilizar:

```java
@Transactional
```

No se utiliza `EntityTransaction` manual ni `RESOURCE_LOCAL`.

## 6. DataSource

JNDI oficial:

```text
jdbc/EmprendeLinkDS
```

Pool:

```text
EmprendeLinkPool
```

Las credenciales pertenecen a la configuración del servidor.

## 7. Proveedor JPA

GlassFish utiliza EclipseLink para la Persistence Unit del proyecto.

Hibernate no se empaqueta en el WAR.

## 8. Dominio

Modelos:

```text
Rol
Usuario
Categoria
Emprendimiento
Publicacion
Pedido
DetallePedido
```

Enums:

```text
TipoRol
TipoPublicacion
EstadoPedido
```

El modelo de dominio no depende de JPA.

## 9. Entidades JPA

Ubicación:

```text
emprendelink.persistence.jpa.entity
```

Implementadas:

```text
RolEntity
UsuarioEntity
```

Pendientes:

```text
CategoriaEntity
EmprendimientoEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

## 10. Mappers

Ubicación:

```text
emprendelink.persistence.jpa.mapper
```

Responsabilidad:

```text
Domain Model ↔ JPA Entity
```

No contienen reglas de negocio.

## 11. Relaciones objetivo

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

## 12. Autenticación

```text
login.xhtml
↓
AuthBean
↓
AutenticacionService
↓
UsuarioDAO
↓
JPA
↓
MySQL
```

Sesión:

```text
usuarioAutenticado
```

## 13. Administración de usuarios

```text
admin/usuarios.xhtml
↓
UsuarioBean
↓
UsuarioService
↓
UsuarioDAO
↓
JPA
```

`AdminFilter` protege `/admin/*`.

## 14. Validación

Se utilizan:

- validadores JSF para interacción;
- validación en Service para reglas independientes de la vista;
- Bean Validation en entidades cuando corresponde.

## 15. AJAX

Jakarta Faces AJAX utiliza:

```text
<f:ajax>
```

## 16. Fase 3

La API REST se ubicará en:

```text
emprendelink.web.rest
```

Ruta base:

```text
/api/v1
```

REST reutilizará Services y persistencia existentes.

## 17. Fase 4

`emprendelink.spring` está reservado para la integración posterior con Spring.
