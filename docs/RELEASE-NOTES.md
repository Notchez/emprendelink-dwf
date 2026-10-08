# Release Notes — EmprendeLink DWF

## Fase 1 — Sprint I

Se construyó el primer incremento funcional con arquitectura MVC, JDBC, Servlets, JSP/JSTL, Services, DAO y MySQL.

Esta implementación permitió validar el dominio, la base de datos y los flujos iniciales.

## Fase 2 — Sprint II

### Arquitectura

La aplicación evolucionó a una arquitectura Jakarta EE administrada por GlassFish:

```text
JSF
↓
CDI
↓
Services
↓
DAO JPA
↓
EclipseLink
↓
JTA
↓
GlassFish DataSource
↓
MySQL
```

Se retiraron del runtime:

- implementaciones JDBC;
- Servlets de Fase 1;
- vistas JSP;
- conexión directa con `DriverManager`;
- registro manual de Services;
- `EntityManagerFactory` manual;
- Hibernate empaquetado en el WAR.

### Persistencia

Implementado:

- `RolEntity`;
- `UsuarioEntity`;
- `RolMapper`;
- `UsuarioMapper`;
- `JpaRolDAO`;
- `JpaUsuarioDAO`;
- Persistence Unit `emprendelinkPU`;
- DataSource `jdbc/EmprendeLinkDS`;
- transacciones JTA.

### JSF

Implementado:

- `login.xhtml`;
- `registro.xhtml`;
- `inicio.xhtml`;
- `admin/usuarios.xhtml`;
- `AuthBean`;
- `UsuarioBean`;
- validadores;
- converter de rol;
- AJAX.

### Seguridad

Implementado:

- PBKDF2 para contraseñas;
- sesión `usuarioAutenticado`;
- filtro para área administrativa;
- filtro de autenticación;
- registro público limitado a Cliente y Emprendedor;
- protección contra auto-desactivación del administrador;
- hash retirado del objeto autenticado.

### Pruebas

Pruebas JUnit para:

- registro;
- normalización;
- hash;
- roles;
- correo duplicado;
- correo inválido;
- límites de campos;
- login válido;
- usuario inactivo;
- mappers.

### Pendiente de Fase 2

- categorías;
- emprendimientos;
- publicaciones;
- catálogo;
- pedidos y detalle;
- relaciones JPA restantes;
- cobertura de integración de módulos restantes.
