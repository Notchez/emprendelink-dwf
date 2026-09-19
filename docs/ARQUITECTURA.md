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

```text
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
```

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

Por ejemplo, `PublicacionDAO` podrá definir operaciones para:

- buscar una publicación por ID;
- listar publicaciones;
- listar publicaciones activas;
- listar publicaciones por emprendimiento;
- guardar;
- actualizar;
- eliminar.

La tecnología utilizada para realizar estas operaciones podrá cambiar sin modificar el contrato principal.

---

## 5. Convención de nombres y firmas DAO

Para mantener consistencia entre los diferentes módulos del proyecto, las interfaces DAO deberán seguir una convención común para nombrar sus métodos.

Los nombres deberán escribirse en español y utilizar `camelCase`.

### 5.1. Consultas que devuelven un único registro

Se utilizará el prefijo:

`buscarPor...`

Ejemplos:

```java
Publicacion buscarPorId(Integer id);
Usuario buscarPorId(Integer id);
Usuario buscarPorCorreo(String correo);
Emprendimiento buscarPorId(Integer id);
```

El nombre deberá indicar claramente el criterio utilizado para realizar la búsqueda.

---

### 5.2. Consultas que devuelven colecciones

Se utilizará el prefijo:

`listar...`

Ejemplos:

```java
List<Publicacion> listarTodas();
List<Publicacion> listarActivas();
List<Publicacion> listarPorEmprendimiento(Integer idEmprendimiento);
List<Pedido> listarPorUsuario(Integer idUsuario);
List<Categoria> listarTodas();
```

Cuando corresponda, el nombre deberá expresar claramente el filtro aplicado.

Ejemplos:

- `listarTodas()`
- `listarActivas()`
- `listarPendientes()`
- `listarPorUsuario(...)`
- `listarPorEmprendimiento(...)`
- `listarPorCategoria(...)`

La terminación deberá mantener concordancia con la entidad cuando resulte natural.

Ejemplos:

```java
List<Publicacion> listarTodas();
List<Categoria> listarTodas();
List<Usuario> listarTodos();
List<Pedido> listarTodos();
```

---

### 5.3. Operaciones de creación

Para crear o persistir un nuevo registro se utilizará:

`guardar(...)`

Ejemplos:

```java
void guardar(Publicacion publicacion);
void guardar(Usuario usuario);
void guardar(Pedido pedido);
```

---

### 5.4. Operaciones de actualización

Para modificar un registro existente se utilizará:

`actualizar(...)`

Ejemplos:

```java
void actualizar(Publicacion publicacion);
void actualizar(Usuario usuario);
void actualizar(Pedido pedido);
```

---

### 5.5. Operaciones de eliminación

Para eliminar registros se utilizará el prefijo:

`eliminarPor...`

Ejemplos:

```java
void eliminarPorId(Integer id);
```

Si posteriormente existen otros criterios de eliminación, el método deberá expresar claramente dicho criterio.

---

### 5.6. Ejemplo de contrato DAO

Una interfaz como `PublicacionDAO` deberá mantener una nomenclatura consistente.

```java
public interface PublicacionDAO {

    Publicacion buscarPorId(Integer id);

    List<Publicacion> listarTodas();

    List<Publicacion> listarActivas();

    List<Publicacion> listarPorEmprendimiento(Integer idEmprendimiento);

    void guardar(Publicacion publicacion);

    void actualizar(Publicacion publicacion);

    void eliminarPorId(Integer id);
}
```

Las implementaciones concretas, como `JdbcPublicacionDAO` o `JpaPublicacionDAO`, deberán respetar exactamente el contrato definido por la interfaz correspondiente.

No deberán utilizarse nombres alternativos para una misma operación, como:

- `get`
- `find`
- `fetch`
- `obtener`
- `cargar`

salvo que una tecnología o framework lo requiera explícitamente.

El objetivo de esta convención es que cualquier integrante pueda identificar el propósito de un método DAO únicamente por su nombre, independientemente del módulo en el que esté trabajando.

---

## 6. Implementaciones DAO

Durante la evolución del proyecto existirán diferentes implementaciones de las interfaces DAO.

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

Ejemplo:

```text
PublicacionService
        ↓
 PublicacionDAO
      ↙     ↘
Jdbc...     Jpa...
```

---

## 7. Capa de servicios

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

Los servicios no deberán contener código relacionado directamente con JSP, Servlets, JSF o componentes visuales.

---

## 8. Capa de presentación

La tecnología utilizada para presentar información cambiará durante las diferentes fases de la materia.

### Fase 1

Se utilizarán:

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
- validadores
- convertidores

### Fase 3

Se incorporará un cliente externo que consumirá la API REST.

La tecnología del cliente será definida según los requisitos de la fase.

### Fase 4

Spring MVC podrá proporcionar nuevos controladores y mecanismos de presentación cuando corresponda.

---

## 9. Controladores

Los controladores recibirán solicitudes del usuario y coordinarán la interacción con la capa de servicios.

### Servlets

Durante la Fase 1 se utilizarán inicialmente:

- `AuthServlet`
- `CatalogoServlet`
- `UsuarioServlet`
- `EmprendimientoServlet`
- `CategoriaServlet`
- `PublicacionServlet`
- `PedidoServlet`

Los Servlets no deberán acceder directamente a la base de datos.

Flujo esperado:

```text
Servlet
   ↓
Service
   ↓
 DAO
   ↓
Base de datos
```

Los Servlets serán responsables principalmente de:

- recibir parámetros HTTP;
- diferenciar solicitudes GET y POST;
- realizar validaciones básicas de entrada;
- invocar la capa Service;
- colocar información en request o session cuando corresponda;
- realizar `forward`;
- realizar `redirect`;
- controlar el flujo de navegación;
- presentar mensajes de éxito o error.

Las reglas de negocio no deberán implementarse directamente dentro de los Servlets.

---

## 10. JSF

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

La incorporación de JSF deberá sustituir o complementar la capa de presentación sin obligar a reconstruir la lógica de negocio.

---

## 11. Persistencia JPA

Cuando se incorpore JPA / Hibernate, la persistencia se mantendrá separada del resto de la aplicación.

Estructura prevista:

```text
persistence/jpa/entity
```

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

```text
persistence/jpa/mapper
```

Contendrá los componentes responsables de convertir entre:

```text
Entidad JPA ↔ Modelo de dominio
```

Esta estrategia permitirá mantener el modelo de dominio desacoplado de la tecnología de persistencia.

Si durante el desarrollo se determina que la ruta académica requiere utilizar directamente anotaciones JPA sobre los POJO del dominio, esta arquitectura podrá simplificarse mediante una decisión coordinada del equipo.

Cualquier modificación de este tipo deberá quedar documentada.

---

## 12. API REST

Durante la Fase 3 se incorporará una API REST.

Ruta base prevista:

```text
/api/v1
```

Recursos previstos:

- `EmprendimientoResource`
- `PublicacionResource`
- `PedidoResource`

Ejemplos de rutas:

```text
GET  /api/v1/emprendimientos
GET  /api/v1/emprendimientos/{id}

GET  /api/v1/publicaciones
GET  /api/v1/publicaciones/{id}
GET  /api/v1/emprendimientos/{id}/publicaciones

POST /api/v1/pedidos
GET  /api/v1/pedidos/{id}
PUT  /api/v1/pedidos/{id}/estado
```

Estas rutas son una definición inicial y podrán ampliarse según los requisitos funcionales del proyecto.

La API deberá reutilizar la lógica de negocio existente mediante la capa Service.

Flujo esperado:

```text
Cliente externo
      ↓
 REST Resource
      ↓
    Service
      ↓
     DAO
      ↓
Base de datos
```

---

## 13. DTO

La API utilizará objetos específicos para transportar datos cuando sea necesario.

DTO previstos:

- `EmprendimientoDTO`
- `PublicacionDTO`
- `CrearPedidoDTO`
- `PedidoDTO`
- `DetallePedidoDTO`
- `ApiErrorDTO`

Los DTO permitirán separar el contrato externo de la API del modelo interno de la aplicación.

Esto permitirá evitar la exposición innecesaria de información interna de las entidades.

---

## 14. Spring

Durante la Fase 4 se incorporarán los módulos de Spring requeridos por el proyecto.

Podrán utilizarse componentes relacionados con:

- Spring MVC
- Spring Security
- Spring Data / JPA
- Spring REST
- Dependency Injection
- otros módulos autorizados por la materia

Spring deberá integrarse sobre las responsabilidades existentes.

La incorporación de Spring no implica obligatoriamente reconstruir desde cero las capas de:

- dominio;
- servicios;
- acceso a datos;
- persistencia.

Cuando sea posible, Spring deberá aprovechar las interfaces y responsabilidades establecidas durante las fases anteriores.

---

## 15. Seguridad

La seguridad se incorporará progresivamente.

Roles oficiales:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

La autorización deberá realizarse según las responsabilidades de cada rol.

Durante las diferentes fases podrán existir mecanismos básicos de control de acceso.

Durante la Fase 4 se implementará protección de rutas y recursos utilizando los mecanismos de seguridad correspondientes.

Las contraseñas nunca deberán almacenarse como texto plano.

Los secretos y credenciales reales nunca deberán almacenarse en Git.

Ejemplos de información que no deberá subirse al repositorio:

- contraseñas de bases de datos;
- tokens;
- claves privadas;
- API keys;
- credenciales reales;
- archivos locales que contengan secretos.

---

## 16. Manejo de errores

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

Cada capa deberá manejar únicamente los errores que correspondan a su responsabilidad.

Por ejemplo:

```text
DAO
↓
PersistenciaException
↓
Service
↓
ReglaNegocioException / RecursoNoEncontradoException
↓
Controlador
↓
Mensaje o respuesta apropiada
```

No deberán mostrarse directamente al usuario detalles internos de excepciones SQL, stack traces o información sensible.

---

## 17. Estructura Java prevista

La estructura base del código será:

```text
src/main/java/sv/edu/udb/emprendelink/
```

Paquetes previstos:

```text
model
model/enums

dao
dao/jdbc
dao/jpa

service
service/impl

persistence/jpa/entity
persistence/jpa/mapper

web/servlet
web/jsf
web/rest

dto

spring
security
config
exception
util
```

Las carpetas se crearán progresivamente según sean necesarias.

No se deberán crear componentes vacíos únicamente para completar la estructura.

La estructura real del repositorio será la fuente de verdad sobre los componentes que ya hayan sido implementados.

---

## 18. Vistas previstas

La ubicación base de las vistas JSP será:

```text
src/main/webapp/WEB-INF/views/
```

Estructura inicial prevista:

```text
auth/
catalogo/
emprendimientos/
publicaciones/
pedidos/
admin/
error/
```

Ejemplos:

```text
auth/login.jsp

catalogo/lista.jsp
catalogo/detalle.jsp

emprendimientos/lista.jsp
emprendimientos/formulario.jsp

publicaciones/lista.jsp
publicaciones/formulario.jsp

pedidos/lista.jsp
pedidos/detalle.jsp

admin/usuarios.jsp
admin/categorias.jsp

error/403.jsp
error/404.jsp
error/500.jsp
```

Las JSP estarán ubicadas dentro de `WEB-INF` para evitar su acceso directo cuando corresponda.

El acceso normal deberá realizarse mediante los controladores.

---

## 19. Evolución por fases

La arquitectura deberá permitir la siguiente evolución.

### Fase 1

```text
JSP / Servlets
      ↓
   Services
      ↓
     DAO
      ↓
    JDBC
      ↓
    MySQL
```

### Fase 2

```text
JSF / Managed Beans
        ↓
     Services
        ↓
       DAO
        ↓
 JPA / Hibernate
        ↓
      MySQL
```

### Fase 3

```text
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
```

### Fase 4

```text
Spring MVC / REST / Security
            ↓
         Services
            ↓
DAO / Spring Data cuando corresponda
            ↓
      JPA / Hibernate
            ↓
          MySQL
```

El proyecto deberá evolucionar sobre la misma base funcional.

Las nuevas tecnologías deberán integrarse sobre el trabajo existente y no convertirse en aplicaciones independientes por fase.

---

## 20. Regla de dependencias

Como principio general:

- las vistas no acceden directamente a la base de datos;
- los controladores no contienen consultas SQL;
- los controladores no implementan reglas de negocio complejas;
- los DAO no contienen lógica de presentación;
- los DAO se encargan del acceso a datos;
- los servicios contienen las principales reglas de negocio;
- el modelo no deberá depender de tecnologías web;
- los controladores reutilizarán los servicios existentes;
- las diferentes implementaciones DAO respetarán sus interfaces;
- las tecnologías específicas deberán mantenerse lo más aisladas posible;
- JDBC, JPA, JSF, REST y Spring no deberán provocar duplicación innecesaria de la lógica del sistema.

Dependencia general esperada:

```text
Presentación
     ↓
Controladores
     ↓
  Servicios
     ↓
    DAO
     ↓
Persistencia
```

No deberá invertirse este flujo sin una razón técnica documentada.

---

## 21. Objetivo arquitectónico

La arquitectura de EmprendeLink busca que diferentes integrantes puedan trabajar en módulos separados sin generar dependencias innecesarias.

También busca que el proyecto pueda evolucionar de manera progresiva durante las cuatro fases de DWF:

1. Java Web, Servlets, JSP, JDBC y MVC.
2. JSF, AJAX y JPA / Hibernate.
3. Servicios REST y cliente consumidor.
4. Spring, seguridad, pruebas y despliegue.

Las decisiones estructurales deberán mantenerse consistentes.

Cualquier cambio importante que afecte:

- contratos DAO;
- modelo de dominio;
- servicios;
- organización de paquetes;
- rutas generales;
- persistencia;
- seguridad;
- arquitectura entre capas;

deberá revisarse antes de integrarse a `develop`.

Esta arquitectura podrá evolucionar durante el proyecto, pero los cambios deberán quedar documentados en este archivo.

El objetivo final es mantener una solución:

- comprensible;
- modular;
- mantenible;
- reproducible;
- extensible;
- preparada para evolucionar durante las siguientes fases de la materia.
