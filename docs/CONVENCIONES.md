# Convenciones del proyecto — EmprendeLink DWF

Este documento establece las convenciones oficiales que deberán respetarse durante el desarrollo de EmprendeLink.

Su objetivo es mantener consistencia entre los módulos desarrollados por los diferentes integrantes del equipo y evitar incompatibilidades durante la integración.

---

# 1. Identificación del proyecto

- Nombre del proyecto: `EmprendeLink`
- Repositorio: `emprendelink-dwf`
- Base de datos: `emprendelink_dwf`
- Package raíz Java: `emprendelink`
- Context path: `/emprendelink`
- Ruta base prevista de API: `/api/v1`
- Codificación: `UTF-8`

Estructura Java principal:

```text
src/main/java/emprendelink/
```

Estructura de pruebas:

```text
src/test/java/emprendelink/
```

No utilizar en nuevo código:

```text
sv.edu.udb.emprendelink
```

---

# 2. Convenciones Java

## Clases e interfaces

Utilizar:

```text
PascalCase
```

Ejemplos:

```text
Usuario
Publicacion
PedidoService
PublicacionDAO
CatalogoServlet
CategoriaBean
UsuarioEntity
```

---

## Métodos y atributos

Utilizar:

```text
camelCase
```

Ejemplos:

```text
nombreUsuario
fechaRegistro
buscarPorId()
listarPublicaciones()
crearPedido()
```

---

## Constantes

Utilizar:

```text
UPPER_SNAKE_CASE
```

Ejemplos:

```text
MAX_INTENTOS
ESTADO_ACTIVO
```

---

# 3. Packages

Los packages deberán:

- escribirse en minúsculas;
- representar claramente su responsabilidad;
- mantenerse dentro de `emprendelink`;
- evitar niveles innecesarios.

Ejemplos oficiales:

```text
emprendelink.model
emprendelink.model.enums

emprendelink.dao
emprendelink.dao.jdbc
emprendelink.dao.jpa

emprendelink.service
emprendelink.service.impl

emprendelink.persistence.jpa.entity
emprendelink.persistence.jpa.mapper

emprendelink.security
emprendelink.config
emprendelink.exception
emprendelink.util

emprendelink.web.servlet
emprendelink.web.jsf
emprendelink.web.rest
```

No crear packages equivalentes con nombres diferentes sin necesidad.

Por ejemplo, evitar tener simultáneamente:

```text
emprendelink.services
emprendelink.service
```

para representar la misma responsabilidad.

---

# 4. Namespace Jakarta

El proyecto utiliza:

```text
Jakarta EE
```

Los imports empresariales deberán utilizar:

```text
jakarta.*
```

Ejemplos:

```text
jakarta.servlet.*
jakarta.faces.*
jakarta.persistence.*
jakarta.enterprise.*
jakarta.validation.*
```

No utilizar APIs antiguas:

```text
javax.*
```

salvo que exista una justificación técnica explícita y aprobada.

---

# 5. Entidades oficiales de dominio

Las entidades principales del dominio son:

```text
Rol
Usuario
Emprendimiento
Categoria
Publicacion
Pedido
DetallePedido
```

No deberán crearse entidades equivalentes con nombres diferentes sin acuerdo previo.

Por ejemplo, no crear:

```text
Producto
Servicio
```

si la funcionalidad corresponde al modelo existente:

```text
Publicacion
```

El tipo de publicación se representa mediante:

```text
TipoPublicacion
```

---

# 6. Enumeraciones oficiales

## TipoRol

Valores permitidos:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

## TipoPublicacion

Valores permitidos:

```text
PRODUCTO
SERVICIO
```

## EstadoPedido

Valores permitidos:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Estos valores deberán mantenerse consistentes entre:

- Java;
- base de datos;
- JPA;
- interfaces;
- reglas de negocio;
- API cuando corresponda;
- pruebas.

No crear variantes alternativas para representar el mismo valor.

---

# 7. Convenciones de base de datos

## Tablas

Utilizar nombres en plural y:

```text
snake_case
```

Tablas oficiales:

```text
roles
usuarios
emprendimientos
categorias
publicaciones
pedidos
detalle_pedido
```

---

## Columnas

Utilizar:

```text
snake_case
```

Ejemplos:

```text
id_usuario
fecha_registro
id_emprendimiento
precio_unitario
```

---

## Llaves primarias

Utilizar el formato:

```text
id_<entidad>
```

Ejemplos:

```text
id_usuario
id_publicacion
id_pedido
```

---

## Llaves foráneas

Utilizar el identificador de la entidad o relación correspondiente.

Ejemplos:

```text
id_rol
id_propietario
id_cliente
id_emprendimiento
id_categoria
id_publicacion
id_pedido
```

---

# 8. Correspondencia Java — Base de datos

Los atributos Java utilizarán:

```text
camelCase
```

Las columnas SQL utilizarán:

```text
snake_case
```

Ejemplos:

```text
Java: idUsuario
SQL:  id_usuario
```

```text
Java: fechaRegistro
SQL:  fecha_registro
```

```text
Java: precioUnitario
SQL:  precio_unitario
```

Durante Fase 2, esta correspondencia deberá declararse correctamente mediante JPA cuando los nombres no coincidan automáticamente.

---

# 9. Modelo de dominio y entidades JPA

El modelo de dominio y las entidades JPA representan responsabilidades distintas.

Modelo:

```text
emprendelink/model/
```

Persistencia JPA:

```text
emprendelink/persistence/jpa/entity/
```

Convención:

```text
Usuario
UsuarioEntity

Emprendimiento
EmprendimientoEntity

Categoria
CategoriaEntity

Publicacion
PublicacionEntity

Pedido
PedidoEntity

DetallePedido
DetallePedidoEntity
```

Las entidades JPA utilizarán el sufijo:

```text
Entity
```

El modelo de dominio no utilizará dicho sufijo.

---

# 10. Mappers JPA

Los componentes responsables de convertir entre dominio y persistencia utilizarán el sufijo:

```text
Mapper
```

Ejemplos:

```text
UsuarioMapper
EmprendimientoMapper
CategoriaMapper
PublicacionMapper
PedidoMapper
DetallePedidoMapper
```

Ubicación:

```text
emprendelink/persistence/jpa/mapper/
```

Responsabilidad:

```text
JPA Entity ↔ Domain Model
```

Los mappers no deberán contener reglas de negocio.

---

# 11. DAO

Las interfaces DAO utilizarán:

```text
<Entidad>DAO
```

Ejemplos:

```text
RolDAO
UsuarioDAO
EmprendimientoDAO
CategoriaDAO
PublicacionDAO
PedidoDAO
```

---

## Implementaciones JDBC

Utilizar:

```text
Jdbc<Entidad>DAO
```

Ejemplos:

```text
JdbcUsuarioDAO
JdbcEmprendimientoDAO
JdbcCategoriaDAO
JdbcPublicacionDAO
JdbcPedidoDAO
```

Ubicación:

```text
emprendelink/dao/jdbc/
```

---

## Implementaciones JPA

Utilizar:

```text
Jpa<Entidad>DAO
```

Ejemplos:

```text
JpaUsuarioDAO
JpaEmprendimientoDAO
JpaCategoriaDAO
JpaPublicacionDAO
JpaPedidoDAO
```

Ubicación:

```text
emprendelink/dao/jpa/
```

Las implementaciones JDBC y JPA deberán respetar los contratos definidos por las interfaces DAO cuando corresponda.

---

# 12. Métodos DAO

Los métodos deberán expresar claramente la operación realizada.

## Buscar un registro

Utilizar:

```text
buscarPor...
```

Ejemplos:

```text
buscarPorId()
buscarPorCorreo()
```

---

## Listar registros

Utilizar:

```text
listar...
```

Ejemplos:

```text
listarTodos()
listarTodas()
listarActivas()
listarPorUsuario()
listarPorEmprendimiento()
listarPorCategoria()
```

---

## Crear

Utilizar la convención definida por la interfaz DAO existente.

Ejemplos actualmente válidos según cada contrato:

```text
crear(...)
guardar(...)
```

No renombrar contratos compartidos únicamente por preferencia personal.

---

## Actualizar

Utilizar:

```text
actualizar(...)
```

cuando así esté definido por el contrato correspondiente.

---

## Eliminar

Utilizar nombres descriptivos como:

```text
eliminarPorId(...)
```

No mezclar arbitrariamente dentro del mismo módulo nombres equivalentes como:

```text
find
fetch
get
obtener
cargar
```

si ya existe una convención establecida.

---

# 13. Services

Las interfaces utilizarán:

```text
<Responsabilidad>Service
```

Ejemplos:

```text
AutenticacionService
UsuarioService
EmprendimientoService
CategoriaService
PublicacionService
PedidoService
```

Las implementaciones utilizarán:

```text
<Responsabilidad>ServiceImpl
```

Ejemplos:

```text
AutenticacionServiceImpl
UsuarioServiceImpl
EmprendimientoServiceImpl
CategoriaServiceImpl
PublicacionServiceImpl
PedidoServiceImpl
```

No utilizar:

```text
DefaultUsuarioService
DefaultPublicacionService
DefaultPedidoService
```

mientras la convención oficial siga siendo:

```text
ServiceImpl
```

Las reglas de negocio deberán ubicarse principalmente en esta capa.

---

# 14. Servlets

Los Servlets utilizarán el sufijo:

```text
Servlet
```

Ejemplos:

```text
AuthServlet
CatalogoServlet
UsuarioServlet
EmprendimientoServlet
CategoriaServlet
PublicacionServlet
PedidoServlet
```

Ubicación:

```text
emprendelink/web/servlet/
```

Los Servlets no deberán contener:

- SQL;
- acceso directo a JDBC;
- persistencia JPA;
- reglas complejas de negocio.

Deberán utilizar Services.

---

# 15. Managed Beans JSF

Los Managed Beans utilizarán el sufijo:

```text
Bean
```

Ejemplos:

```text
AuthBean
CatalogoBean
CategoriaBean
EmprendimientoBean
PublicacionBean
PedidoBean
```

Ubicación:

```text
emprendelink/web/jsf/
```

Los Beans deberán:

- coordinar la interacción de la vista;
- llamar Services;
- manejar información de presentación;
- manejar mensajes cuando corresponda.

No deberán ejecutar SQL ni acceder directamente a la persistencia.

---

# 16. Vistas JSF

Las vistas JSF utilizarán:

```text
.xhtml
```

Los nombres de archivos deberán ser:

- descriptivos;
- en minúsculas;
- consistentes dentro de cada módulo.

Ejemplos:

```text
login.xhtml
registro.xhtml
lista.xhtml
detalle.xhtml
formulario.xhtml
```

No mezclar arbitrariamente nombres como:

```text
newProduct.xhtml
nuevo_producto.xhtml
crearProducto.xhtml
```

para representar el mismo estilo dentro del proyecto.

---

# 17. AJAX

Cuando se utilice AJAX en JSF, se utilizarán mecanismos compatibles con Jakarta Faces.

Ejemplo principal:

```text
<f:ajax>
```

AJAX debe estar asociado a una funcionalidad real.

Ejemplos:

- filtros;
- actualizaciones parciales;
- selección dinámica;
- validaciones parciales;
- actualización de tablas o detalles.

No agregar AJAX únicamente como efecto visual.

---

# 18. Validators

Los validadores personalizados utilizarán el sufijo:

```text
Validator
```

Ejemplos:

```text
CorreoValidator
EstadoPedidoValidator
```

Solo deberán crearse cuando exista una necesidad real.

Las reglas complejas de negocio deberán permanecer en Services.

---

# 19. Converters

Los converters utilizarán el sufijo:

```text
Converter
```

Ejemplos:

```text
CategoriaConverter
EmprendimientoConverter
PublicacionConverter
```

Se utilizarán cuando JSF necesite transformar entre:

```text
valor de la vista ↔ objeto Java
```

No crear converters innecesarios únicamente para aumentar la cantidad de componentes.

---

# 20. DTO

Los objetos destinados al intercambio de información utilizarán el sufijo:

```text
DTO
```

Ejemplos:

```text
EmprendimientoDTO
PublicacionDTO
CrearPedidoDTO
PedidoDTO
DetallePedidoDTO
ApiErrorDTO
```

Los DTO no deberán contener lógica de negocio.

Su uso principal corresponde a contratos externos y fases posteriores.

---

# 21. Recursos REST

Los recursos REST utilizarán el sufijo:

```text
Resource
```

Ejemplos:

```text
EmprendimientoResource
PublicacionResource
PedidoResource
```

Ubicación prevista:

```text
emprendelink/web/rest/
```

La implementación completa de REST corresponde principalmente a Fase 3.

No priorizar estos componentes durante Fase 2.

---

# 22. Spring

Los componentes Spring deberán utilizar convenciones apropiadas al framework cuando llegue la fase correspondiente.

Ejemplo para controladores:

```text
PublicacionController
PedidoController
```

Spring corresponde principalmente a Fase 4.

No utilizar Spring para resolver requerimientos de Fase 2.

---

# 23. Excepciones

Las excepciones propias del proyecto utilizarán nombres descriptivos y el sufijo:

```text
Exception
```

Excepciones existentes:

```text
EmprendeLinkException
ValidacionException
ReglaNegocioException
RecursoNoEncontradoException
AccesoNoAutorizadoException
PersistenciaException
```

No crear excepciones duplicadas que representen exactamente el mismo problema.

---

# 24. Parámetros de formularios

Los nombres enviados desde formularios deberán mantenerse consistentes con los valores esperados por el backend.

Ejemplo:

```text
correo
```

No utilizar simultáneamente para el mismo dato:

```text
correo
emailUsuario
userEmail
```

sin una razón técnica.

---

# 25. HTTP y REST

Cuando corresponda implementar la API, las rutas deberán:

- escribirse en minúsculas;
- utilizar sustantivos;
- utilizar recursos en plural;
- evitar verbos innecesarios en las URLs.

Ejemplos:

```text
/api/v1/usuarios
/api/v1/emprendimientos
/api/v1/publicaciones
/api/v1/pedidos
```

Recurso individual:

```text
/api/v1/publicaciones/{id}
```

Métodos HTTP:

```text
GET     consultar
POST    crear
PUT     actualizar
DELETE  eliminar
```

Estas convenciones corresponden principalmente a Fase 3.

---

# 26. JSON

Cuando se implemente intercambio JSON, utilizar propiedades en:

```text
camelCase
```

Ejemplos:

```text
idUsuario
idEmprendimiento
idPublicacion
fechaPedido
precioUnitario
```

No utilizar para el mismo contrato múltiples variantes como:

```text
publicationId
id_publicacion
idPublicacion
```

---

# 27. Git

Ramas permanentes:

```text
main
develop
```

## main

Contiene versiones estables.

No desarrollar directamente sobre esta rama.

## develop

Es la rama principal de integración del equipo.

Las tareas deben crearse desde `develop`.

---

# 28. Tipos de ramas

## Funcionalidades

Formato:

```text
feature/<nombre>
```

Ejemplos:

```text
feature/fase2-auth-usuarios
feature/fase2-categorias
feature/fase2-emprendimientos
feature/fase2-publicaciones
feature/fase2-pedidos
```

---

## Correcciones

Formato:

```text
fix/<nombre>
```

Ejemplos:

```text
fix/login
fix/calculo-total
fix/validacion-correo
```

---

## Refactorización

Formato:

```text
refactor/<nombre>
```

Ejemplos:

```text
refactor/package-raiz
refactor/capa-servicios
```

---

## Documentación

Formato:

```text
docs/<nombre>
```

Ejemplos:

```text
docs/fase2-documentacion
docs/modelo-datos
```

---

# 29. Pull Requests

Las ramas de trabajo deberán integrarse mediante Pull Request hacia:

```text
develop
```

Flujo normal:

```text
feature/*  ─┐
fix/*      ─┤
refactor/* ─┼──> develop ───> main
docs/*     ─┘
```

`main` recibirá cambios cuando exista una versión estable o una entrega.

No utilizar Pull Requests entre ramas de funcionalidades como flujo normal de integración.

---

# 30. Commits

Los mensajes deberán:

- ser breves;
- describir el cambio real;
- evitar mensajes ambiguos;
- mantener consistencia.

Prefijos recomendados:

```text
feat:
fix:
docs:
refactor:
test:
chore:
```

Ejemplos:

```text
feat: implementar persistencia JPA de categorías
feat: agregar gestión JSF de publicaciones
fix: corregir cálculo total de pedidos
docs: actualizar documentación base para Fase 2
refactor: simplificar package raíz a emprendelink
test: agregar pruebas de reglas de pedido
chore: actualizar configuración Maven
```

Evitar commits como:

```text
cambios
update
prueba
arreglo
cosas
final
final2
ahora-si
```

---

# 31. Archivos sensibles

Nunca deberán subirse al repositorio:

```text
contraseñas
credenciales reales
tokens
API keys
claves privadas
.env
configuraciones privadas del IDE
secretos externos
```

El archivo:

```text
.env.example
```

deberá contener únicamente valores de ejemplo.

---

# 32. Configuración local

No hardcodear rutas de una computadora específica.

Ejemplo incorrecto:

```text
C:\Users\nombre\Desktop\proyecto\.env
```

Las configuraciones locales deberán mantenerse fuera del código compartido cuando corresponda.

Cada integrante podrá configurar localmente:

- IntelliJ;
- GlassFish;
- rutas de archivos;
- credenciales de desarrollo.

---

# 33. Dependencias

Antes de agregar una dependencia nueva:

1. revisar `pom.xml`;
2. comprobar si Jakarta EE ya proporciona la API;
3. revisar `docs/VERSIONS.md`;
4. verificar compatibilidad con GlassFish;
5. coordinar si afecta a todo el equipo.

No agregar dependencias simplemente por preferencia personal.

---

# 34. Cambios estructurales

No deberán modificarse sin revisar el impacto:

```text
nombres de entidades
nombres de tablas
nombres de columnas
package raíz
packages compartidos
roles
enums
contratos DAO
contratos Service
rutas generales
dependencias Maven
configuración JPA
```

Los cambios estructurales deberán:

1. realizarse en una rama;
2. probarse;
3. documentarse;
4. integrarse mediante Pull Request.

---

# 35. Pruebas

Las clases de pruebas utilizarán el sufijo:

```text
Test
```

Ejemplos:

```text
UsuarioServiceTest
PedidoServiceTest
JpaCategoriaDAOTest
```

Las pruebas deberán ubicarse bajo:

```text
src/test/java/emprendelink/
```

Una clase llamada `Test` no se considera una prueba automatizada válida si Maven no ejecuta ningún caso de prueba.

---

# 36. Documentación

Los documentos Markdown utilizarán nombres descriptivos.

Documentación general:

```text
README.md
AGENTS.md
```

Documentación técnica:

```text
docs/
```

Cuando un cambio modifique una decisión documentada, deberá actualizarse también el documento correspondiente.

Ejemplos:

```text
Cambio de package
→ CONVENCIONES.md
→ ARQUITECTURA.md
→ README.md

Cambio de tabla
→ MODELO-DATOS.md
→ schema.sql

Cambio de roles
→ ROLES-PERMISOS.md

Cambio de dependencia
→ VERSIONS.md
→ pom.xml
```

---

# 37. Convenciones de Fase 2

Durante Fase 2 se priorizan:

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

No utilizar como solución principal de Fase 2:

```text
Spring
Spring Boot
Spring Security
API REST completa
```

aunque existan packages preparados para fases futuras.

---

# 38. Regla general

Antes de crear una nueva:

```text
clase
entidad
tabla
ruta
package
servicio
DAO
enum
dependencia
```

deberá verificarse primero si existe una definición equivalente dentro del proyecto.

El objetivo es mantener:

- una sola arquitectura;
- una sola nomenclatura;
- una sola interpretación del dominio;
- contratos consistentes;
- integración sencilla entre módulos.

La estructura real del repositorio y la documentación actualizada deberán mantenerse alineadas.