# Arquitectura del proyecto — EmprendeLink DWF

Este documento define la arquitectura oficial de EmprendeLink para la materia DWF.

El proyecto debe evolucionar entre las diferentes fases sin reconstruir la aplicación desde cero.

La arquitectura prioriza:

- separación de responsabilidades;
- reutilización de lógica;
- mantenibilidad;
- integración entre módulos;
- evolución progresiva de tecnologías.

---

# 1. Estado actual

## Fase 1 — Implementada

La aplicación cuenta con una base funcional utilizando:

```text
JSP
Servlets
Services
DAO
JDBC
MySQL
```

Flujo principal:

```text
JSP
 ↓
Servlet
 ↓
Service
 ↓
DAO
 ↓
JDBC
 ↓
MySQL
```

---

## Fase 2 — En desarrollo

La Fase 2 incorpora:

```text
Jakarta Faces
Managed Beans
AJAX
JPA
Hibernate
Validadores
Converters
Transacciones
Pruebas
```

Flujo esperado:

```text
XHTML / JSF
     ↓
Managed Bean
     ↓
Service
     ↓
DAO
     ↓
JPA / Hibernate
     ↓
MySQL
```

La Fase 2 debe reutilizar la lógica existente y no crear una aplicación independiente.

---

# 2. Principio general

La arquitectura se divide principalmente en:

```text
Presentación
Controladores
Servicios
Acceso a datos
Persistencia
Modelo de dominio
Seguridad
Configuración
```

El flujo general es:

```text
Usuario
  ↓
Vista
  ↓
Controlador / Bean
  ↓
Service
  ↓
DAO
  ↓
Persistencia
  ↓
Base de datos
```

Cada capa debe limitarse a su responsabilidad.

---

# 3. Package raíz

El package raíz oficial es:

```text
emprendelink
```

Ubicación:

```text
src/main/java/emprendelink/
```

Pruebas:

```text
src/test/java/emprendelink/
```

No utilizar en nuevo código:

```text
sv.edu.udb.emprendelink
```

---

# 4. Estructura Java

La estructura principal es:

```text
emprendelink/
│
├── config/
│
├── dao/
│   ├── jdbc/
│   └── jpa/
│
├── dto/
├── exception/
│
├── model/
│   └── enums/
│
├── persistence/
│   └── jpa/
│       ├── entity/
│       └── mapper/
│
├── security/
│
├── service/
│   └── impl/
│
├── spring/
│
├── util/
│
└── web/
    ├── jsf/
    ├── rest/
    └── servlet/
```

No todas las estructuras representan funcionalidad actualmente activa.

Los packages de tecnologías futuras pueden existir como preparación, pero no deben confundirse con funcionalidad implementada.

---

# 5. Modelo de dominio

El modelo se encuentra en:

```text
emprendelink/model/
```

Entidades principales:

```text
Rol
Usuario
Emprendimiento
Categoria
Publicacion
Pedido
DetallePedido
```

Enumeraciones:

```text
TipoRol
TipoPublicacion
EstadoPedido
```

El modelo representa conceptos del negocio.

No debe depender directamente de:

```text
Servlets
JSP
JSF
JPA
REST
Spring
```

El objetivo es mantener el dominio reutilizable independientemente de la tecnología utilizada para presentación o persistencia.

---

# 6. Relaciones principales del dominio

Las relaciones conceptuales son:

```text
Rol
 └── Usuario

Usuario
 ├── Emprendimiento
 └── Pedido

Emprendimiento
 ├── Publicacion
 └── Pedido

Categoria
 └── Publicacion

Pedido
 └── DetallePedido

Publicacion
 └── DetallePedido
```

Representación general:

```text
Rol 1 ─── N Usuario

Usuario 1 ─── N Emprendimiento

Emprendimiento 1 ─── N Publicacion

Categoria 1 ─── N Publicacion

Usuario 1 ─── N Pedido

Emprendimiento 1 ─── N Pedido

Pedido 1 ─── N DetallePedido

Publicacion 1 ─── N DetallePedido
```

Estas relaciones deberán reflejarse correctamente durante el mapeo JPA.

---

# 7. DAO

Las interfaces DAO se encuentran en:

```text
emprendelink/dao/
```

Interfaces principales:

```text
RolDAO
UsuarioDAO
EmprendimientoDAO
CategoriaDAO
PublicacionDAO
PedidoDAO
```

Los DAO definen contratos de acceso a datos.

Las capas superiores deben depender principalmente de las interfaces y no de una tecnología concreta.

---

# 8. JDBC

La implementación utilizada durante Fase 1 se encuentra en:

```text
emprendelink/dao/jdbc/
```

Ejemplos:

```text
JdbcRolDAO
JdbcUsuarioDAO
JdbcEmprendimientoDAO
JdbcCategoriaDAO
JdbcPublicacionDAO
JdbcPedidoDAO
```

Flujo:

```text
Service
 ↓
DAO
 ↓
JdbcDAO
 ↓
JDBC
 ↓
MySQL
```

La implementación JDBC se mantiene durante Fase 2 como parte de la funcionalidad existente y como referencia durante la migración.

No debe eliminarse antes de comprobar que la nueva persistencia JPA cubre correctamente las funciones necesarias.

---

# 9. JPA

La implementación JPA de Fase 2 se ubicará en:

```text
emprendelink/dao/jpa/
```

Ejemplos previstos:

```text
JpaRolDAO
JpaUsuarioDAO
JpaEmprendimientoDAO
JpaCategoriaDAO
JpaPublicacionDAO
JpaPedidoDAO
```

Flujo esperado:

```text
Service
 ↓
DAO
 ↓
JpaDAO
 ↓
JPA / Hibernate
 ↓
MySQL
```

Los DAO JPA deberán respetar los contratos existentes cuando sea razonable.

No debe modificarse un contrato DAO compartido únicamente para facilitar una implementación JPA concreta sin analizar primero el impacto.

---

# 10. Entidades JPA

Las entidades de persistencia se ubicarán en:

```text
emprendelink/persistence/jpa/entity/
```

Entidades previstas:

```text
RolEntity
UsuarioEntity
EmprendimientoEntity
CategoriaEntity
PublicacionEntity
PedidoEntity
DetallePedidoEntity
```

Las entidades deberán utilizar:

```text
jakarta.persistence.*
```

No utilizar:

```text
javax.persistence.*
```

Las entidades JPA serán responsables de representar la persistencia relacional.

No deberán contener lógica compleja de negocio.

---

# 11. Separación dominio — JPA

El proyecto mantiene separados:

```text
Modelo de dominio
```

y:

```text
Entidades JPA
```

Ejemplo:

```text
Usuario
   ↕
UsuarioMapper
   ↕
UsuarioEntity
```

Esto permite evitar que el dominio dependa directamente de JPA.

Flujo general:

```text
Domain Model
    ↕
Mapper
    ↕
JPA Entity
```

---

# 12. Mappers

Los mappers se ubicarán en:

```text
emprendelink/persistence/jpa/mapper/
```

Ejemplos:

```text
RolMapper
UsuarioMapper
EmprendimientoMapper
CategoriaMapper
PublicacionMapper
PedidoMapper
DetallePedidoMapper
```

Responsabilidad:

```text
JPA Entity ↔ Domain Model
```

Los mappers pueden convertir:

```text
Entity → Domain
Domain → Entity
```

No deben contener:

- consultas;
- manejo de transacciones;
- reglas de negocio;
- lógica de presentación.

---

# 13. Services

Las interfaces de servicio se encuentran en:

```text
emprendelink/service/
```

Servicios principales:

```text
AutenticacionService
UsuarioService
EmprendimientoService
CategoriaService
PublicacionService
PedidoService
```

Implementaciones:

```text
emprendelink/service/impl/
```

Clases existentes:

```text
AutenticacionServiceImpl
UsuarioServiceImpl
EmprendimientoServiceImpl
CategoriaServiceImpl
PublicacionServiceImpl
PedidoServiceImpl
```

La convención oficial es:

```text
ServiceImpl
```

No:

```text
DefaultService
```

---

# 14. Responsabilidades de Services

La capa Service contiene principalmente:

- reglas de negocio;
- validaciones de negocio;
- permisos;
- coordinación entre operaciones;
- coordinación entre DAO;
- control de estados;
- integridad lógica.

Ejemplo:

```text
PedidoBean
   ↓
PedidoService
   ↓
PedidoDAO
```

Un Service puede utilizar varios DAO cuando una operación lo requiera.

Ejemplo:

```text
PedidoService
 ├── PedidoDAO
 ├── PublicacionDAO
 └── UsuarioDAO
```

---

# 15. Regla de acceso a datos

Los componentes de presentación no deben acceder directamente a la base de datos.

Incorrecto:

```text
Bean
 ↓
EntityManager
```

o:

```text
Servlet
 ↓
Connection
```

Flujo correcto:

```text
Bean / Servlet
      ↓
   Service
      ↓
     DAO
      ↓
Persistencia
```

---

# 16. Servlets

Los Servlets se encuentran en:

```text
emprendelink/web/servlet/
```

Controladores existentes:

```text
AuthServlet
CatalogoServlet
UsuarioServlet
EmprendimientoServlet
CategoriaServlet
PublicacionServlet
PedidoServlet
```

Estos forman parte principalmente de la implementación funcional de Fase 1.

Responsabilidades:

- recibir solicitudes;
- obtener parámetros;
- realizar validaciones básicas;
- invocar Services;
- gestionar navegación;
- preparar información para la vista;
- manejar mensajes.

No deben:

- ejecutar SQL;
- contener lógica compleja de negocio;
- gestionar directamente persistencia JPA.

---

# 17. JSP

Las vistas de Fase 1 utilizan JSP.

Ubicación principal:

```text
src/main/webapp/WEB-INF/views/
```

Las vistas deben limitarse principalmente a:

- presentación;
- formularios;
- renderización de datos.

No deben contener consultas o lógica de persistencia.

---

# 18. Jakarta Faces

Durante Fase 2 la capa de presentación evoluciona a Jakarta Faces.

Las vistas utilizarán:

```text
.xhtml
```

Los componentes JSF deberán interactuar con Managed Beans.

Flujo:

```text
XHTML
 ↓
Managed Bean
 ↓
Service
 ↓
DAO
 ↓
JPA
```

---

# 19. Managed Beans

Los Beans se encuentran en:

```text
emprendelink/web/jsf/
```

Beans previstos:

```text
AuthBean
CatalogoBean
CategoriaBean
EmprendimientoBean
PublicacionBean
PedidoBean
```

Los Beans son responsables de:

- datos de formularios;
- acciones de interfaz;
- navegación;
- mensajes;
- interacción con Services;
- actualización de vistas.

No deberán contener:

- SQL;
- consultas JPA;
- reglas de negocio complejas;
- manejo directo de conexiones.

---

# 20. AJAX

Fase 2 requiere demostrar AJAX mediante Jakarta Faces.

Mecanismo principal:

```text
<f:ajax>
```

AJAX podrá utilizarse para:

- filtros;
- actualización parcial de vistas;
- selección dinámica;
- formularios;
- validaciones;
- tablas;
- detalles.

Flujo conceptual:

```text
Acción usuario
     ↓
   AJAX
     ↓
Managed Bean
     ↓
Service
     ↓
Resultado
     ↓
Actualización parcial
```

AJAX no debe reemplazar la lógica de negocio ni la seguridad del backend.

---

# 21. Validación

La validación puede existir en diferentes niveles.

## Vista

Responsable principalmente de:

```text
required
formatos
longitudes
rangos
```

## Managed Bean

Puede coordinar errores y mensajes asociados a la interacción.

## Service

Debe validar:

```text
reglas de negocio
permisos
propiedad
estados
consistencia
```

## Base de datos

Debe mantener:

```text
PK
FK
UNIQUE
NOT NULL
restricciones
```

Una sola capa no debe considerarse suficiente para toda validación.

---

# 22. Validators

Los validators específicos de JSF podrán utilizarse cuando exista una necesidad concreta de validación de entrada.

Ejemplo conceptual:

```text
Formulario
 ↓
Validator
 ↓
Managed Bean
```

No deben utilizarse para reemplazar reglas de negocio propias de Services.

---

# 23. Converters

Los converters se utilizarán cuando JSF necesite transformar entre:

```text
valor recibido desde la vista
```

y:

```text
objeto Java
```

Ejemplo:

```text
ID de categoría
      ↓
CategoriaConverter
      ↓
Categoria
```

Su uso debe responder a una necesidad funcional real.

---

# 24. Transacciones

Las operaciones que afectan múltiples registros deberán mantener consistencia transaccional.

Ejemplo:

```text
Crear pedido
    ↓
Crear cabecera
    ↓
Crear detalles
    ↓
Actualizar información relacionada
    ↓
Commit
```

Si ocurre un error crítico:

```text
Rollback
```

Resultado requerido:

```text
todo se guarda
```

o:

```text
nada se guarda
```

No debe persistirse información parcial.

---

# 25. Pedido como operación crítica

El módulo de pedidos requiere especial atención porque relaciona múltiples conceptos:

```text
Usuario
Emprendimiento
Pedido
DetallePedido
Publicacion
```

El flujo de creación debe comprobar como mínimo:

- cliente válido;
- emprendimiento válido;
- publicaciones válidas;
- cantidades válidas;
- precios;
- subtotales;
- total;
- integridad de detalles.

El estado inicial esperado es:

```text
PENDIENTE
```

---

# 26. Estados de pedido

Estados oficiales:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Las transiciones deben validarse mediante reglas de negocio.

No deberá aceptarse arbitrariamente cualquier cambio entre dos valores del enum.

La definición oficial de transiciones deberá mantenerse en:

```text
docs/ROLES-PERMISOS.md
```

y reflejarse en la implementación de Service.

---

# 27. Seguridad

La seguridad se aplica principalmente mediante:

- autenticación;
- roles;
- propiedad de recursos;
- reglas de acceso.

Roles:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

Ejemplo:

```text
Usuario autenticado
      ↓
Validar rol
      ↓
Validar propiedad
      ↓
Service
      ↓
Operación
```

La interfaz puede ocultar acciones, pero la autorización debe verificarse siempre en backend.

---

# 28. Contraseñas

Las contraseñas nunca deben persistirse en texto plano.

El proyecto contiene utilidades de seguridad para:

```text
hash
verificación
```

La autenticación debe reutilizar este mecanismo.

No crear sistemas paralelos de contraseñas para JSF.

---

# 29. Configuración

Los componentes de configuración se encuentran principalmente en:

```text
emprendelink/config/
```

Actualmente existe configuración para la conexión utilizada por JDBC y registro de servicios.

La configuración JPA será incorporada durante Fase 2.

La configuración específica de persistencia deberá mantenerse separada del código de presentación.

---

# 30. persistence.xml

La configuración JPA utilizará:

```text
src/main/resources/META-INF/persistence.xml
```

Este archivo deberá definir la unidad de persistencia utilizada por el proyecto.

No deberá incluir credenciales personales reales.

La configuración deberá ser compatible con:

```text
Jakarta Persistence
Hibernate
MySQL
GlassFish
```

---

# 31. Inyección y registro de servicios

Durante Fase 1 los Services son registrados para ser utilizados por los controladores existentes.

La evolución hacia JSF deberá reutilizar los mismos contratos y responsabilidades.

No debe crearse un segundo conjunto independiente de Services únicamente para JSF.

Conceptualmente:

```text
Servlet ──────┐
              ↓
           Service
              ↑
JSF Bean ─────┘
```

Esto permite compartir reglas de negocio entre diferentes tecnologías de presentación.

---

# 32. Manejo de errores

Excepciones existentes:

```text
EmprendeLinkException
ValidacionException
ReglaNegocioException
RecursoNoEncontradoException
AccesoNoAutorizadoException
PersistenciaException
```

Flujo conceptual:

```text
Persistencia
     ↓
PersistenciaException
     ↓
Service
     ↓
Regla de negocio / validación
     ↓
Controlador / Bean
     ↓
Mensaje apropiado
```

No mostrar directamente al usuario:

```text
stack traces
SQL interno
contraseñas
rutas locales
datos sensibles
```

---

# 33. Base de datos

La base oficial es:

```text
emprendelink_dwf
```

La definición SQL real se mantiene en:

```text
database/schema.sql
```

Los datos de prueba se mantienen en:

```text
database/seed.sql
```

Las entidades JPA deben ser compatibles con el esquema existente.

No modificar la base únicamente para facilitar un mapeo JPA sin analizar primero el impacto.

---

# 34. Fuente de verdad de datos

Para estructura física:

```text
database/schema.sql
```

Para documentación:

```text
docs/MODELO-DATOS.md
```

Para conceptos de negocio:

```text
docs/MODELO-DOMINIO.md
```

Estos elementos deberán mantenerse consistentes.

---

# 35. API REST

La estructura prevista para REST se encuentra en:

```text
emprendelink/web/rest/
```

La implementación formal corresponde principalmente a:

```text
Fase 3
```

Flujo previsto:

```text
Cliente externo
      ↓
REST Resource
      ↓
Service
      ↓
DAO
      ↓
JPA
      ↓
MySQL
```

Los recursos REST deberán reutilizar la misma capa Service.

No desarrollar una segunda lógica de negocio para la API.

---

# 36. DTO

Los DTO se encuentran en:

```text
emprendelink/dto/
```

Su función principal será transportar información hacia interfaces externas cuando corresponda.

Ejemplos:

```text
EmprendimientoDTO
PublicacionDTO
CrearPedidoDTO
PedidoDTO
DetallePedidoDTO
ApiErrorDTO
```

Los DTO no representan el modelo de dominio ni deben contener reglas de negocio.

---

# 37. Spring

El proyecto contiene estructura preparada para futuras fases:

```text
emprendelink/spring/
```

Spring corresponde principalmente a:

```text
Fase 4
```

No debe utilizarse actualmente para sustituir:

```text
JSF
JPA
Managed Beans
DAO
```

durante Fase 2.

La presencia del package no implica que Spring esté implementado actualmente.

---

# 38. Evolución por fases

## Fase 1

```text
JSP
 ↓
Servlet
 ↓
Service
 ↓
DAO
 ↓
JDBC
 ↓
MySQL
```

---

## Fase 2

```text
JSF / XHTML
     ↓
Managed Bean
     ↓
Service
     ↓
DAO
     ↓
JPA / Hibernate
     ↓
MySQL
```

---

## Fase 3

```text
Cliente externo
      ↓
REST API
      ↓
Service
      ↓
DAO
      ↓
JPA / Hibernate
      ↓
MySQL
```

---

## Fase 4

```text
Spring
  ↓
Service
  ↓
Persistencia
  ↓
MySQL
```

La arquitectura podrá adaptarse según los requisitos exactos de cada fase, pero deberá conservar las responsabilidades ya establecidas siempre que sea razonable.

---

# 39. Regla de dependencias

Dependencia esperada:

```text
Presentación
     ↓
Controlador / Bean
     ↓
Service
     ↓
DAO
     ↓
Persistencia
```

Reglas:

- una vista no accede a la base de datos;
- un Bean no ejecuta SQL;
- un Servlet no implementa persistencia;
- un DAO no maneja navegación;
- una Entity no maneja presentación;
- un Mapper no contiene reglas de negocio;
- un Service no contiene código visual;
- el dominio no depende directamente de frameworks web.

---

# 40. Dependencias entre módulos

Cada módulo funcional debe utilizar las mismas capas.

Ejemplo categorías:

```text
categoria.xhtml
      ↓
CategoriaBean
      ↓
CategoriaService
      ↓
CategoriaDAO
      ↓
JpaCategoriaDAO
      ↓
CategoriaEntity
```

Ejemplo publicaciones:

```text
publicaciones.xhtml
       ↓
PublicacionBean
       ↓
PublicacionService
       ↓
PublicacionDAO
       ↓
JpaPublicacionDAO
       ↓
PublicacionEntity
```

Ejemplo pedidos:

```text
pedidos.xhtml
      ↓
PedidoBean
      ↓
PedidoService
      ↓
PedidoDAO
      ↓
JpaPedidoDAO
      ↓
PedidoEntity
```

---

# 41. Archivos compartidos

Cambios sobre los siguientes elementos pueden afectar varios módulos:

```text
pom.xml
database/schema.sql
database/seed.sql
interfaces DAO
interfaces Service
modelos
enums
persistence.xml
configuración
```

Estos cambios deben realizarse con especial cuidado durante el trabajo paralelo.

---

# 42. Pruebas

La arquitectura debe permitir probar componentes independientemente cuando sea posible.

Las pruebas se ubican en:

```text
src/test/java/emprendelink/
```

Durante Fase 2 deberán cubrirse especialmente:

- Services;
- reglas de negocio;
- DAO JPA;
- relaciones;
- persistencia;
- transacciones;
- rollback;
- permisos;
- estados.

---

# 43. Build

El proyecto debe validarse mediante:

```text
mvn clean package
```

Un módulo no deberá considerarse listo para integración si rompe el build general.

Resultado esperado:

```text
BUILD SUCCESS
```

---

# 44. Compatibilidad con Fase 1

La incorporación de JPA y JSF no implica eliminar inmediatamente:

```text
JdbcDAO
Servlets
JSP
```

La transición deberá hacerse de forma progresiva.

Antes de retirar una implementación anterior deberá existir una alternativa funcional equivalente y haber sido probada.

---

# 45. Componentes futuros

Las estructuras preparadas para fases futuras no deben condicionar innecesariamente la Fase 2.

No priorizar actualmente:

```text
REST completo
Spring
Spring Security
cliente externo
```

La prioridad actual es:

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

---

# 46. Objetivo arquitectónico

EmprendeLink debe mantenerse como una sola aplicación que evoluciona progresivamente.

La arquitectura debe permitir que varios integrantes trabajen en módulos separados sin crear implementaciones incompatibles.

Toda modificación importante debe conservar, cuando sea posible:

```text
Vista
 ↓
Controlador / Bean
 ↓
Service
 ↓
DAO
 ↓
Persistencia
```

El objetivo final es mantener un proyecto:

- funcional;
- modular;
- comprensible;
- mantenible;
- integrable;
- defendible técnicamente.