# Arquitectura del proyecto — EmprendeLink DWF

Este documento define la arquitectura base de EmprendeLink para la materia Desarrollo de Aplicaciones con Web Frameworks (DWF).

La arquitectura está diseñada para permitir que el mismo proyecto evolucione durante las cuatro fases de la materia sin reconstruir completamente el sistema cada vez que se incorpore una nueva tecnología.

---

## 1. Principio general

EmprendeLink seguirá una arquitectura organizada por responsabilidades.

Las principales capas serán:

- Presentación
- Controladores
- Servicios
- Acceso a datos
- Persistencia
- Modelo de dominio
- Configuración
- Seguridad

El objetivo es evitar que la lógica de negocio, el acceso a datos y la presentación queden mezclados.

---

## 2. Flujo general

La comunicación principal del sistema seguirá el siguiente flujo:

Usuario / Cliente
↓
Vista
↓
Controlador
↓
Servicio
↓
DAO
↓
Base de datos

Cada capa deberá comunicarse principalmente con la capa inmediatamente inferior.

---

## 3. Modelo de dominio

El modelo de dominio representa los conceptos principales de EmprendeLink.

Entidades iniciales:

- `Rol`
- `Usuario`
- `Emprendimiento`
- `Categoria`
- `Publicacion`
- `Pedido`
- `DetallePedido`

Enumeraciones:

- `TipoRol`
- `TipoPublicacion`
- `EstadoPedido`

Estas clases representan el negocio y deberán evitar depender directamente de tecnologías de presentación como Servlets, JSF, REST o Spring MVC.

---

## 4. Capa DAO

La capa DAO será responsable de acceder a la información persistida.

Las interfaces principales serán:

- `RolDAO`
- `UsuarioDAO`
- `EmprendimientoDAO`
- `CategoriaDAO`
- `PublicacionDAO`
- `PedidoDAO`

Estas interfaces definirán los contratos de acceso a datos.

Ejemplo conceptual:

`PublicacionDAO`

podrá definir operaciones como:

- buscar por ID;
- listar publicaciones;
- listar publicaciones activas;
- listar por emprendimiento;
- guardar;
- actualizar;
- eliminar.

La tecnología utilizada para realizar estas operaciones podrá cambiar sin modificar el contrato principal.

---

## 5. Implementaciones DAO

Durante la evolución del proyecto existirán diferentes implementaciones.

### Fase 1

Persistencia mediante JDBC.

Ejemplos:

- `JdbcUsuarioDAO`
- `JdbcEmprendimientoDAO`
- `JdbcPublicacionDAO`
- `JdbcPedidoDAO`

### Fase 2 y posteriores

Persistencia mediante JPA / Hibernate.

Ejemplos:

- `JpaUsuarioDAO`
- `JpaEmprendimientoDAO`
- `JpaPublicacionDAO`
- `JpaPedidoDAO`

Ambas implementaciones deberán respetar las interfaces DAO existentes.

De esta manera, las capas superiores no dependerán directamente de JDBC o JPA.

---

## 6. Capa de servicios

La capa de servicios contendrá las reglas de negocio.

Servicios principales:

- `AutenticacionService`
- `UsuarioService`
- `EmprendimientoService`
- `CategoriaService`
- `PublicacionService`
- `PedidoService`

Implementaciones estándar:

- `DefaultAutenticacionService`
- `DefaultUsuarioService`
- `DefaultEmprendimientoService`
- `DefaultCategoriaService`
- `DefaultPublicacionService`
- `DefaultPedidoService`

Responsabilidades de esta capa:

- validar reglas de negocio;
- coordinar operaciones entre entidades;
- utilizar los DAO;
- evitar que los controladores contengan lógica de negocio;
- permitir reutilizar la misma lógica desde Servlets, JSF, REST y Spring.

---

## 7. Capa de presentación

La tecnología utilizada para presentar información cambiará durante las diferentes fases de la materia.

### Fase 1

- JSP
- JSTL
- HTML
- CSS

Las vistas deberán encargarse principalmente de mostrar información y recibir datos del usuario.

No deberán contener lógica de negocio compleja.

### Fase 2

Se incorporará:

- Jakarta Faces
- Managed Beans
- AJAX
- validadores;
- convertidores.

### Fase 3

Se incorporará un cliente externo que consumirá la API REST.

La tecnología del cliente será definida según los requisitos de la fase.

### Fase 4

Spring MVC podrá proporcionar nuevos controladores y mecanismos de presentación cuando corresponda.

---

## 8. Controladores

Los controladores recibirán solicitudes del usuario y coordinarán la interacción con la capa de servicios.

### Servlets

Fase 1:

- `AuthServlet`
- `CatalogoServlet`
- `UsuarioServlet`
- `EmprendimientoServlet`
- `CategoriaServlet`
- `PublicacionServlet`
- `PedidoServlet`

Los Servlets no deberán acceder directamente a la base de datos.

Flujo esperado:

Servlet
↓
Service
↓
DAO
↓
Base de datos

---

## 9. JSF

Durante la Fase 2 se incorporarán Managed Beans.

Nombres previstos:

- `AuthBean`
- `CatalogoBean`
- `EmprendimientoBean`
- `CategoriaBean`
- `PublicacionBean`
- `PedidoBean`

Los Beans utilizarán los servicios existentes.

No deberán duplicar reglas de negocio que ya existan en la capa Service.

---

## 10. Persistencia JPA

Cuando se incorpore JPA / Hibernate, la persistencia se mantendrá separada del resto de la aplicación.

Estructura prevista:

`persistence/jpa/entity`

Contendrá entidades JPA.

Ejemplos:

- `RolEntity`
- `UsuarioEntity`
- `EmprendimientoEntity`
- `CategoriaEntity`
- `PublicacionEntity`
- `PedidoEntity`
- `DetallePedidoEntity`

Estructura:

`persistence/jpa/mapper`

Contendrá los componentes responsables de convertir entre:

Entidad JPA ↔ Modelo de dominio

Esta estrategia permitirá mantener el modelo de dominio desacoplado de la tecnología de persistencia.

Si durante el desarrollo se determina que la ruta académica requiere utilizar directamente anotaciones JPA sobre los POJO del dominio, esta arquitectura podrá simplificarse mediante una decisión coordinada del equipo.

---

## 11. API REST

Durante la Fase 3 se incorporará una API REST.

Ruta base:

`/api/v1`

Recursos previstos:

- `EmprendimientoResource`
- `PublicacionResource`
- `PedidoResource`

Ejemplos de rutas:

- `GET /api/v1/emprendimientos`
- `GET /api/v1/emprendimientos/{id}`
- `GET /api/v1/publicaciones`
- `GET /api/v1/publicaciones/{id}`
- `GET /api/v1/emprendimientos/{id}/publicaciones`
- `POST /api/v1/pedidos`
- `GET /api/v1/pedidos/{id}`
- `PUT /api/v1/pedidos/{id}/estado`

Estas rutas son una definición inicial y podrán ampliarse según los requisitos funcionales del proyecto.

---

## 12. DTO

La API utilizará objetos específicos para transportar datos cuando sea necesario.

DTO previstos:

- `EmprendimientoDTO`
- `PublicacionDTO`
- `CrearPedidoDTO`
- `PedidoDTO`
- `DetallePedidoDTO`
- `ApiErrorDTO`

Los DTO permitirán separar el contrato externo de la API del modelo interno de la aplicación.

---

## 13. Spring

Durante la Fase 4 se incorporarán los módulos de Spring requeridos por el proyecto.

Podrán utilizarse componentes relacionados con:

- Spring MVC
- Spring Security
- Spring Data / JPA
- Spring REST
- Dependency Injection
- otros módulos autorizados por la materia.

Spring deberá integrarse sobre las responsabilidades existentes.

La incorporación de Spring no implica obligatoriamente reconstruir desde cero las capas de dominio, servicios o acceso a datos.

---

## 14. Seguridad

La seguridad se incorporará progresivamente.

Roles oficiales:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

La autorización deberá realizarse según las responsabilidades de cada rol.

Durante la Fase 4 se implementará protección de rutas y recursos utilizando los mecanismos de seguridad correspondientes.

Las contraseñas nunca deberán almacenarse como texto plano.

Los secretos y credenciales reales nunca deberán almacenarse en Git.

---

## 15. Manejo de errores

Se utilizarán excepciones propias del proyecto para representar diferentes tipos de problemas.

Base prevista:

- `EmprendeLinkException`
- `ValidacionException`
- `ReglaNegocioException`
- `RecursoNoEncontradoException`
- `AccesoNoAutorizadoException`
- `PersistenciaException`

Esto permitirá implementar posteriormente manejo centralizado de errores para:

- Servlets;
- JSF;
- API REST;
- Spring.

---

## 16. Estructura Java prevista

La estructura base del código será:

src/main/java/sv/edu/udb/emprendelink/

- `model`
- `model/enums`
- `dao`
- `dao/jdbc`
- `dao/jpa`
- `service`
- `service/impl`
- `persistence/jpa/entity`
- `persistence/jpa/mapper`
- `web/servlet`
- `web/jsf`
- `web/rest`
- `dto`
- `spring`
- `security`
- `config`
- `exception`
- `util`

Las carpetas se crearán progresivamente según sean necesarias.

No se deberán crear componentes vacíos únicamente para completar la estructura.

---

## 17. Vistas previstas

La ubicación base de las vistas JSP será:

`src/main/webapp/WEB-INF/views/`

Estructura inicial prevista:

- `auth/`
- `catalogo/`
- `emprendimientos/`
- `publicaciones/`
- `pedidos/`
- `admin/`
- `error/`

Ejemplos:

- `auth/login.jsp`
- `catalogo/lista.jsp`
- `catalogo/detalle.jsp`
- `emprendimientos/lista.jsp`
- `emprendimientos/formulario.jsp`
- `publicaciones/lista.jsp`
- `publicaciones/formulario.jsp`
- `pedidos/lista.jsp`
- `pedidos/detalle.jsp`
- `admin/usuarios.jsp`
- `admin/categorias.jsp`
- `error/403.jsp`
- `error/404.jsp`
- `error/500.jsp`

---

## 18. Evolución por fases

La arquitectura deberá permitir la siguiente evolución:

Fase 1:

JSP / Servlets
↓
Services
↓
DAO
↓
JDBC
↓
MySQL

Fase 2:

JSF / Managed Beans
↓
Services
↓
DAO
↓
JPA / Hibernate
↓
MySQL

Fase 3:

Cliente externo
↓
REST API
↓
Services
↓
DAO
↓
JPA / Hibernate
↓
MySQL

Fase 4:

Spring MVC / REST / Security
↓
Services
↓
DAO / Spring Data cuando corresponda
↓
JPA / Hibernate
↓
MySQL

El proyecto deberá evolucionar sobre la misma base funcional.

---

## 19. Regla de dependencias

Como principio general:

- las vistas no acceden directamente a la base de datos;
- los controladores no contienen consultas SQL;
- los DAO no contienen lógica de presentación;
- los servicios contienen las principales reglas de negocio;
- el modelo no deberá depender de tecnologías web;
- los controladores reutilizarán los servicios existentes;
- las tecnologías específicas deberán mantenerse lo más aisladas posible.

---

## 20. Objetivo arquitectónico

La arquitectura de EmprendeLink busca que diferentes integrantes puedan trabajar en módulos separados sin generar dependencias innecesarias.

Las decisiones estructurales deberán mantenerse consistentes y cualquier cambio importante deberá ser revisado antes de integrarse a `develop`.

Esta arquitectura podrá evolucionar durante el proyecto, pero los cambios deberán quedar documentados en este archivo.