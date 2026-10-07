# EmprendeLink DWF

Proyecto de cátedra de **Desarrollo de Aplicaciones Web con Java (DWF)**.

EmprendeLink es una aplicación web orientada a conectar emprendimientos con clientes, permitiendo administrar usuarios, emprendimientos, categorías, publicaciones y pedidos.

---

## Estado del proyecto

### Fase 1 — Completada

La primera fase implementó la base funcional del sistema utilizando:

- Java
- Jakarta Servlets
- JSP
- JDBC
- MySQL
- Maven
- GlassFish

Actualmente existen:

- Registro e inicio de sesión.
- Gestión de usuarios.
- Gestión de emprendimientos.
- Gestión de categorías.
- Gestión de publicaciones.
- Catálogo.
- Creación y gestión de pedidos.
- Acceso a datos mediante DAO.
- Capa de servicios.
- Validaciones y manejo de excepciones.
- Hash seguro de contraseñas.

### Fase 2 — En desarrollo

La segunda fase corresponde a la integración de:

- JPA.
- Hibernate.
- Jakarta Faces (JSF).
- Managed Beans.
- Relaciones entre entidades.
- CRUD mediante JPA.
- Transacciones.
- AJAX.
- Validadores.
- Converters.
- Pruebas de funcionalidad e integridad.

La implementación JDBC de Fase 1 se mantiene como referencia mientras se desarrolla la nueva capa JPA.

---

# Tecnologías

## Backend

- Java 25
- Jakarta EE 11
- Jakarta Servlet
- Jakarta Faces 4.1
- Jakarta Persistence 3.2
- Jakarta CDI
- Jakarta Validation
- JDBC
- JPA
- Hibernate

## Base de datos

- MySQL 8.4 LTS

## Servidor

- GlassFish 8.0.4

## Build

- Maven 3.9.x

## IDE recomendado

- IntelliJ IDEA

---

# Arquitectura

El proyecto mantiene una arquitectura organizada por responsabilidades.

```text
Vista
  │
  ▼
Servlet / JSF Managed Bean
  │
  ▼
Service
  │
  ▼
DAO
  │
  ▼
JDBC / JPA
  │
  ▼
MySQL
```

Los controladores y Managed Beans no deben contener consultas SQL ni lógica directa de persistencia.

La lógica de negocio pertenece principalmente a la capa `service`.

---

# Estructura principal

```text
emprendelink-dwf/
│
├── database/
│
├── docs/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── emprendelink/
│   │   │       ├── config/
│   │   │       ├── dao/
│   │   │       │   ├── jdbc/
│   │   │       │   └── jpa/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── model/
│   │   │       │   └── enums/
│   │   │       ├── persistence/
│   │   │       │   └── jpa/
│   │   │       │       ├── entity/
│   │   │       │       └── mapper/
│   │   │       ├── security/
│   │   │       ├── service/
│   │   │       │   └── impl/
│   │   │       ├── util/
│   │   │       └── web/
│   │   │           ├── jsf/
│   │   │           ├── rest/
│   │   │           └── servlet/
│   │   │
│   │   ├── resources/
│   │   └── webapp/
│   │
│   └── test/
│       └── java/
│           └── emprendelink/
│
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

El package raíz oficial del proyecto es:

```java
package emprendelink;
```

---

# Modelo principal

El sistema trabaja actualmente con las siguientes entidades de dominio:

- `Usuario`
- `Rol`
- `Emprendimiento`
- `Categoria`
- `Publicacion`
- `Pedido`
- `DetallePedido`

También existen enums para valores controlados como:

- `TipoRol`
- `TipoPublicacion`
- `EstadoPedido`

---

# Persistencia

## JDBC

La implementación original de Fase 1 utiliza:

```text
dao/
└── jdbc/
```

Los DAO JDBC trabajan directamente con MySQL mediante conexiones JDBC.

## JPA

Durante Fase 2 se implementará:

```text
dao/
└── jpa/

persistence/
└── jpa/
    ├── entity/
    └── mapper/
```

Las entidades JPA estarán separadas de los modelos de dominio.

Los mappers serán responsables de convertir entre:

```text
JPA Entity ↔ Domain Model
```

Esto permite mantener separada la persistencia de la lógica del sistema.

---

# Capa de servicios

Las interfaces se encuentran en:

```text
emprendelink/service/
```

Las implementaciones se encuentran en:

```text
emprendelink/service/impl/
```

Ejemplos:

```text
AutenticacionService
CategoriaService
EmprendimientoService
PedidoService
PublicacionService
UsuarioService
```

Implementaciones:

```text
AutenticacionServiceImpl
CategoriaServiceImpl
EmprendimientoServiceImpl
PedidoServiceImpl
PublicacionServiceImpl
UsuarioServiceImpl
```

Los controladores deben consumir servicios y no acceder directamente a la base de datos.

---

# Presentación

## Servlets

La funcionalidad desarrollada durante Fase 1 se encuentra principalmente en:

```text
emprendelink/web/servlet/
```

Entre los controladores existentes están:

```text
AuthServlet
CatalogoServlet
CategoriaServlet
EmprendimientoServlet
PedidoServlet
PublicacionServlet
UsuarioServlet
```

## JSF

Durante Fase 2 se desarrollará la nueva presentación utilizando Jakarta Faces.

Los Managed Beans se encuentran en:

```text
emprendelink/web/jsf/
```

La base actual contempla:

```text
AuthBean
CatalogoBean
CategoriaBean
EmprendimientoBean
PedidoBean
PublicacionBean
```

Los Managed Beans deben comunicarse con la capa de servicios y no directamente con los DAO.

---

# Configuración de base de datos

Crear la base de datos utilizando los scripts disponibles en:

```text
database/
```

El proyecto utiliza:

```text
emprendelink_dwf
```

como nombre de base de datos por defecto.

---

# Variables de entorno

El repositorio contiene:

```text
.env.example
```

Crear una copia llamada:

```text
.env
```

Ejemplo:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=emprendelink_dwf
DB_USER=root
DB_PASSWORD=tu_password
DB_URL=jdbc:mysql://localhost:3306/emprendelink_dwf
APP_CONTEXT_PATH=/emprendelink
```

El archivo `.env` contiene configuración local y **no debe subirse al repositorio**.

---

# Configuración del archivo `.env`

La aplicación puede recibir la ubicación del archivo mediante la propiedad:

```text
emprendelink.env
```

Ejemplo en GlassFish:

```text
-Demprendelink.env=C:\ruta\al\proyecto\.env
```

Cada integrante debe configurar esta ruta según la ubicación del proyecto en su computadora.

---

# Compilar el proyecto

Desde la raíz:

```bash
mvn clean package
```

Si todo está correctamente configurado deberá aparecer:

```text
BUILD SUCCESS
```

El WAR generado estará disponible en:

```text
target/emprendelink.war
```

---

# GlassFish

Cada integrante debe configurar GlassFish localmente.

Versión utilizada por el equipo:

```text
GlassFish 8.0.4
```

El artefacto desplegado es:

```text
emprendelink.war
```

Context path esperado:

```text
/emprendelink
```

Por ejemplo:

```text
http://localhost:8080/emprendelink/
```

La configuración del servidor realizada dentro del IDE puede ser local y no necesariamente se comparte mediante Git.

---

# Flujo Git

Las ramas principales son:

```text
main
develop
```

## `main`

Contiene versiones estables del proyecto.

No se trabaja directamente sobre esta rama.

## `develop`

Es la rama de integración del equipo.

Las nuevas funcionalidades deben partir de `develop`.

## Ramas de trabajo

Formato recomendado:

```text
feature/nombre-funcionalidad
fix/nombre-correccion
refactor/nombre-refactor
```

Ejemplos:

```text
feature/fase2-categorias
feature/fase2-emprendimientos
feature/fase2-publicaciones
feature/fase2-pedidos
refactor/package-raiz
```

---

# Flujo recomendado de trabajo

Antes de crear una nueva rama:

```bash
git switch develop
git pull origin develop
```

Crear rama:

```bash
git switch -c feature/nombre-funcionalidad
```

Después de trabajar:

```bash
git add .
git commit -m "descripcion del cambio"
git push -u origin feature/nombre-funcionalidad
```

Luego se crea un Pull Request hacia:

```text
develop
```

Cuando `develop` tenga una versión estable y validada, podrá integrarse posteriormente a:

```text
main
```

---

# Reglas de desarrollo

Antes de integrar cambios:

- El proyecto debe compilar.
- No deben existir errores de imports.
- No deben subirse credenciales.
- No debe subirse `.env`.
- No debe modificarse directamente `main`.
- Los cambios deben respetar la arquitectura existente.
- Los Beans y Servlets deben utilizar Services.
- Los Services deben utilizar DAO.
- Las consultas SQL no deben colocarse en la capa web.
- Las nuevas implementaciones JPA deben utilizar `jakarta.*`.
- Cada funcionalidad debe respetar permisos y reglas de negocio.
- Los cambios compartidos deben coordinarse antes de modificar archivos centrales.

---

# Pruebas

Antes de realizar un Pull Request ejecutar:

```bash
mvn clean package
```

Además de la compilación, durante Fase 2 se incorporarán pruebas para validar:

- CRUD.
- Reglas de negocio.
- Persistencia JPA.
- Relaciones entre entidades.
- Validaciones.
- Transacciones.
- Rollback ante errores.
- Permisos por rol.
- Flujo de pedidos.

La carpeta utilizada para pruebas es:

```text
src/test/java/
```

---

# Roles

El sistema contempla principalmente:

```text
ADMIN
EMPRENDEDOR
CLIENTE
```

Cada funcionalidad debe respetar los permisos asociados al rol correspondiente.

La definición detallada se encuentra en:

```text
docs/ROLES-PERMISOS.md
```

---

# Documentación

La documentación técnica y funcional se encuentra en:

```text
docs/
```

Los documentos del proyecto deben mantenerse actualizados junto con el código.

Entre ellos se encuentran documentos relacionados con:

- Arquitectura.
- Modelo de datos.
- Roles y permisos.
- Backlog.
- Matriz de pruebas.
- Versiones utilizadas.
- Flujo Git.
- Convenciones.
- Release notes.

---

# Uso de asistentes de IA

El proyecto puede apoyarse en herramientas de inteligencia artificial para:

- Análisis de código.
- Documentación.
- Refactorización.
- Generación de pruebas.
- Detección de errores.
- Apoyo durante el desarrollo.

Sin embargo:

- El código generado debe ser revisado.
- Debe respetar la arquitectura del proyecto.
- No debe inventar clases o dependencias inexistentes.
- No debe introducir frameworks fuera de la fase actual.
- No debe reemplazar decisiones definidas en la documentación.
- Siempre debe verificarse mediante compilación y pruebas.

Las instrucciones específicas para asistentes de IA se encuentran en:

```text
AGENTS.md
```

---

# Alcance actual — Fase 2

Durante la Fase 2 el enfoque es:

```text
JPA + Hibernate + JSF + Managed Beans + AJAX + Validación
```

No corresponde implementar todavía como parte principal de esta fase:

```text
Spring
Spring Boot
Spring Security
API REST completa
```

Las estructuras relacionadas con tecnologías futuras pueden existir como preparación arquitectónica, pero no deben desplazar los objetivos actuales.

---

# Criterio de finalización de una funcionalidad

Una tarea puede considerarse terminada cuando:

1. Compila correctamente.
2. Respeta la arquitectura.
3. Funciona con MySQL.
4. Implementa las reglas de negocio correspondientes.
5. Maneja errores correctamente.
6. Respeta permisos.
7. Cuenta con validaciones.
8. No contiene credenciales.
9. Tiene pruebas o evidencia funcional.
10. `mvn clean package` termina en `BUILD SUCCESS`.
11. Está documentada cuando el cambio lo requiere.
12. Está lista para integrarse mediante Pull Request.

---

# Equipo

Proyecto desarrollado para la materia DWF.

Integrantes:

- Tito Mauricio Nochez Villagran
- Marvin Francisco Pérez Calderón
- Giovanni Manuel Quijano Sosa
- Jeferson Alfredo Romero Rivas
- Rafael Mena Mejia

---

# EmprendeLink

El objetivo del proyecto es evolucionar progresivamente desde una aplicación Jakarta Web basada en Servlets y JDBC hacia una arquitectura empresarial utilizando JPA, Hibernate y JSF, manteniendo separación de responsabilidades, persistencia consistente y una base preparada para las siguientes fases del proyecto.