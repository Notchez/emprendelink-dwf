# Convenciones del proyecto — EmprendeLink DWF

Este documento establece las convenciones oficiales que deberán respetarse durante el desarrollo de EmprendeLink para evitar incompatibilidades entre los módulos desarrollados por distintos integrantes del equipo.

## 1. Identificación del proyecto

- Nombre del proyecto: `EmprendeLink`
- Repositorio: `emprendelink-dwf`
- Base de datos: `emprendelink_dwf`
- Package raíz Java: `sv.edu.udb.emprendelink`
- Context path: `/emprendelink`
- Ruta base de la API: `/api/v1`
- Codificación: `UTF-8`

## 2. Convenciones Java

### Clases e interfaces

Utilizar `PascalCase`.

Ejemplos:

- `Usuario`
- `Publicacion`
- `PedidoService`
- `PublicacionDAO`
- `CatalogoServlet`

### Métodos y atributos

Utilizar `camelCase`.

Ejemplos:

- `nombreUsuario`
- `fechaRegistro`
- `buscarPorId()`
- `listarPublicaciones()`
- `crearPedido()`

### Constantes

Utilizar `UPPER_SNAKE_CASE`.

Ejemplos:

- `MAX_INTENTOS`
- `ESTADO_ACTIVO`

### Packages

Los nombres deberán escribirse completamente en minúsculas.

Ejemplos:

- `sv.edu.udb.emprendelink.model`
- `sv.edu.udb.emprendelink.dao`
- `sv.edu.udb.emprendelink.service`
- `sv.edu.udb.emprendelink.web.servlet`

## 3. Entidades oficiales

Las entidades principales del dominio son:

- `Rol`
- `Usuario`
- `Emprendimiento`
- `Categoria`
- `Publicacion`
- `Pedido`
- `DetallePedido`

No deberán crearse entidades equivalentes con nombres diferentes sin acuerdo previo del equipo.

Por ejemplo, no se deberá crear `Producto` o `Servicio` si la funcionalidad corresponde a `Publicacion`.

## 4. Enumeraciones oficiales

### TipoRol

Valores permitidos:

- `ROLE_ADMIN`
- `ROLE_EMPRENDEDOR`
- `ROLE_CLIENTE`

### TipoPublicacion

Valores permitidos:

- `PRODUCTO`
- `SERVICIO`

### EstadoPedido

Valores permitidos:

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PROCESO`
- `COMPLETADO`
- `CANCELADO`

Estos valores deberán mantenerse iguales en Java, base de datos, API y componentes de seguridad cuando corresponda.

## 5. Convenciones de base de datos

### Tablas

Utilizar nombres en plural y `snake_case`.

Tablas oficiales:

- `roles`
- `usuarios`
- `emprendimientos`
- `categorias`
- `publicaciones`
- `pedidos`
- `detalle_pedido`

### Columnas

Utilizar `snake_case`.

Ejemplos:

- `id_usuario`
- `fecha_registro`
- `id_emprendimiento`
- `precio_unitario`

### Llaves primarias

Utilizar:

`id_<entidad>`

Ejemplos:

- `id_usuario`
- `id_publicacion`
- `id_pedido`

### Llaves foráneas

Utilizar el identificador de la entidad relacionada.

Ejemplos:

- `id_rol`
- `id_propietario`
- `id_cliente`
- `id_emprendimiento`
- `id_publicacion`

## 6. Correspondencia Java — Base de datos

Los atributos Java utilizarán `camelCase`, mientras que las columnas SQL utilizarán `snake_case`.

Ejemplos:

- Java: `idUsuario`
- SQL: `id_usuario`

- Java: `fechaRegistro`
- SQL: `fecha_registro`

- Java: `precioUnitario`
- SQL: `precio_unitario`

Cuando se utilice JPA, esta correspondencia deberá definirse explícitamente mediante el mapeo correspondiente.

## 7. Convenciones HTTP y REST

Las rutas deberán:

- escribirse en minúsculas;
- utilizar sustantivos;
- utilizar nombres en plural para recursos;
- evitar verbos innecesarios en las URLs.

Ejemplos:

- `/api/v1/usuarios`
- `/api/v1/emprendimientos`
- `/api/v1/publicaciones`
- `/api/v1/pedidos`

Ejemplo de recurso individual:

- `/api/v1/publicaciones/{id}`

Los verbos HTTP deberán representar la operación:

- `GET`: consultar
- `POST`: crear
- `PUT`: actualizar
- `DELETE`: eliminar

## 8. Parámetros de formularios

Los nombres enviados desde JSP, JSF o cualquier cliente deberán coincidir con los nombres esperados por el controlador.

Ejemplo:

HTML:

`name="correo"`

Java:

`request.getParameter("correo")`

No deberán utilizarse nombres diferentes para representar el mismo dato sin una razón justificada.

## 9. JSON

Los nombres de propiedades JSON deberán mantenerse consistentes entre backend y cliente.

Ejemplos:

- `idUsuario`
- `idEmprendimiento`
- `idPublicacion`
- `fechaPedido`
- `precioUnitario`

No deberán utilizarse simultáneamente variantes como:

- `publicationId`
- `id_publicacion`
- `idPublicacion`

Para el mismo contrato de API.

## 10. DAO

Las interfaces DAO utilizarán el nombre de la entidad seguido de `DAO`.

Ejemplos:

- `UsuarioDAO`
- `EmprendimientoDAO`
- `CategoriaDAO`
- `PublicacionDAO`
- `PedidoDAO`

Las implementaciones deberán identificar la tecnología utilizada.

Ejemplos:

- `JdbcUsuarioDAO`
- `JdbcPublicacionDAO`
- `JpaUsuarioDAO`
- `JpaPublicacionDAO`

## 11. Servicios

Las interfaces de servicios utilizarán el nombre de la responsabilidad seguido de `Service`.

Ejemplos:

- `UsuarioService`
- `EmprendimientoService`
- `PublicacionService`
- `PedidoService`
- `AutenticacionService`

Las implementaciones estándar podrán utilizar el prefijo `Default`.

Ejemplos:

- `DefaultUsuarioService`
- `DefaultPublicacionService`
- `DefaultPedidoService`

Las reglas de negocio deberán ubicarse principalmente en la capa de servicios y no duplicarse en controladores o DAO.

## 12. Controladores y componentes web

### Servlets

Utilizar el sufijo `Servlet`.

Ejemplos:

- `AuthServlet`
- `CatalogoServlet`
- `PublicacionServlet`
- `PedidoServlet`

### JSF

Los Managed Beans utilizarán el sufijo `Bean`.

Ejemplos:

- `AuthBean`
- `CatalogoBean`
- `PublicacionBean`
- `PedidoBean`

### REST

Los recursos REST utilizarán el sufijo `Resource`.

Ejemplos:

- `EmprendimientoResource`
- `PublicacionResource`
- `PedidoResource`

### Spring

Los controladores Spring utilizarán el sufijo `Controller`.

Ejemplos:

- `PublicacionController`
- `PedidoController`

## 13. DTO

Los objetos destinados al intercambio de información mediante API utilizarán el sufijo `DTO`.

Ejemplos:

- `PublicacionDTO`
- `PedidoDTO`
- `DetallePedidoDTO`
- `CrearPedidoDTO`
- `ApiErrorDTO`

Los DTO no deberán utilizarse para contener lógica de negocio.

## 14. Excepciones

Las excepciones propias del proyecto utilizarán nombres descriptivos.

Base propuesta:

- `EmprendeLinkException`
- `ValidacionException`
- `ReglaNegocioException`
- `RecursoNoEncontradoException`
- `AccesoNoAutorizadoException`
- `PersistenciaException`

## 15. Git

Ramas principales:

- `main`: código estable.
- `develop`: integración del desarrollo.

Ramas de funcionalidades:

`feature/<nombre>`

Ejemplos:

- `feature/usuarios`
- `feature/publicaciones`
- `feature/pedidos`

Ramas de corrección:

`fix/<nombre>`

Ejemplos:

- `fix/login`
- `fix/calculo-total`

Las funcionalidades deberán integrarse mediante Pull Request.

No deberá desarrollarse directamente sobre `main`.

## 16. Commits

Los mensajes deberán ser breves y describir claramente el cambio realizado.

Ejemplos:

- `feat: add publication model`
- `feat: implement user JDBC DAO`
- `fix: correct order total calculation`
- `docs: update database model`
- `refactor: reorganize service layer`

## 17. Archivos sensibles

Nunca deberán subirse al repositorio:

- contraseñas;
- credenciales reales;
- tokens;
- archivos `.env`;
- configuraciones privadas del IDE;
- secretos de servicios externos.

El archivo `.env.example` contendrá únicamente valores de ejemplo.

## 18. Cambios estructurales

No deberán modificarse individualmente sin coordinación:

- nombres de entidades;
- nombres de tablas;
- nombres de columnas;
- packages compartidos;
- rutas;
- roles;
- enumeraciones;
- contratos DAO;
- contratos de servicios;
- contratos de API.

Si un cambio es necesario, deberá discutirse, aplicarse en una rama y revisarse antes de integrarlo a `develop`.

## 19. Regla general

Antes de crear una nueva clase, tabla, ruta o concepto, deberá verificarse primero si ya existe una definición equivalente dentro de la arquitectura del proyecto.

El objetivo es mantener una única convención y evitar duplicación o incompatibilidades entre los módulos.