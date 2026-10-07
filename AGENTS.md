# AGENTS.md — EmprendeLink DWF

Este archivo contiene las instrucciones de trabajo para asistentes de inteligencia artificial que analicen o modifiquen el repositorio EmprendeLink DWF.

El objetivo es mantener consistencia entre el código, la arquitectura, la documentación y los requisitos académicos del proyecto.

---

# 1. Contexto del proyecto

EmprendeLink es un proyecto académico desarrollado para la materia DWF.

El sistema permite gestionar:

- usuarios;
- roles;
- emprendimientos;
- categorías;
- publicaciones;
- catálogo;
- pedidos;
- detalles de pedidos.

El proyecto se encuentra actualmente trabajando en:

```text
FASE 2
```

Enfoque principal:

```text
JPA
Hibernate
Jakarta Faces (JSF)
Managed Beans
AJAX
Validación
Converters
Transacciones
```

La Fase 1 ya estableció una aplicación funcional utilizando:

```text
Servlets
JSP
JDBC
DAO
Services
MySQL
MVC
```

La implementación existente no debe reconstruirse desde cero.

La Fase 2 debe evolucionar sobre la base funcional existente.

---

# 2. Package raíz oficial

El package raíz Java oficial es:

```text
emprendelink
```

La ubicación principal es:

```text
src/main/java/emprendelink/
```

Las pruebas utilizan:

```text
src/test/java/emprendelink/
```

No utilizar:

```text
sv.edu.udb.emprendelink
```

en nuevos packages, imports o documentación.

El `groupId` de Maven no debe utilizarse para inferir el package Java.

---

# 3. Fuente de verdad

Antes de realizar cambios importantes, revisar primero:

```text
README.md
AGENTS.md
docs/ARQUITECTURA.md
docs/CONVENCIONES.md
docs/MODELO-DOMINIO.md
docs/MODELO-DATOS.md
docs/ROLES-PERMISOS.md
docs/RUTAS.md
docs/PRODUCT-BACKLOG.md
docs/MATRIZ-PRUEBAS.md
docs/VERSIONS.md
```

También debe revisarse la estructura real del repositorio.

Si existe una diferencia entre una estructura documentada como futura y el código que ya está implementado, no asumir automáticamente que debe crearse o reemplazarse código.

Analizar primero el estado real.

---

# 4. Arquitectura general

La arquitectura esperada es:

```text
Vista
  ↓
Servlet / JSF Managed Bean
  ↓
Service
  ↓
DAO
  ↓
JDBC / JPA
  ↓
MySQL
```

Responsabilidades:

```text
Presentación
    ↓
Controladores / Beans
    ↓
Servicios
    ↓
DAO
    ↓
Persistencia
```

No invertir estas responsabilidades sin una razón técnica documentada.

---

# 5. Modelo de dominio

El modelo principal se encuentra en:

```text
emprendelink/model/
```

Entidades de dominio principales:

```text
Rol
Usuario
Emprendimiento
Categoria
Publicacion
Pedido
DetallePedido
```

Enums principales:

```text
TipoRol
TipoPublicacion
EstadoPedido
```

El modelo de dominio debe mantenerse independiente de:

- Servlets;
- JSP;
- JSF;
- JPA;
- REST;
- Spring;
- interfaces visuales.

No agregar anotaciones de frameworks al modelo de dominio sin una decisión arquitectónica explícita del equipo.

---

# 6. DAO

Las interfaces DAO se encuentran en:

```text
emprendelink/dao/
```

Implementación JDBC de Fase 1:

```text
emprendelink/dao/jdbc/
```

Implementación JPA de Fase 2:

```text
emprendelink/dao/jpa/
```

Las nuevas implementaciones JPA deben respetar los contratos DAO existentes cuando sea posible.

No modificar una interfaz DAO compartida únicamente para facilitar una implementación concreta sin revisar primero el impacto sobre:

- JDBC;
- Services;
- Servlets;
- otros módulos;
- pruebas.

---

# 7. Services

Las interfaces se encuentran en:

```text
emprendelink/service/
```

Las implementaciones reales utilizan el sufijo:

```text
ServiceImpl
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

No utilizar nombres:

```text
DefaultUsuarioService
DefaultPublicacionService
DefaultPedidoService
```

salvo que el proyecto adopte explícitamente esa convención posteriormente.

Responsabilidades de los Services:

- reglas de negocio;
- validaciones de negocio;
- coordinación entre DAO;
- control de operaciones;
- permisos relacionados con operaciones;
- consistencia de datos.

No mover estas responsabilidades hacia Servlets, Beans o DAO.

---

# 8. Servlets

Los Servlets existentes pertenecen principalmente a la implementación funcional de Fase 1.

Ubicación:

```text
emprendelink/web/servlet/
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

No eliminarlos simplemente porque Fase 2 utiliza JSF.

La migración debe hacerse de manera progresiva y controlada.

Los Servlets no deben:

- ejecutar SQL;
- crear conexiones directamente;
- implementar reglas complejas de negocio.

Flujo esperado:

```text
Servlet
  ↓
Service
  ↓
DAO
  ↓
Persistencia
```

---

# 9. JSF

Los Managed Beans se encuentran en:

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

Durante Fase 2 estos Beans deben convertirse en componentes funcionales.

Los Beans deben utilizar Services.

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

Un Managed Bean no debe:

- ejecutar consultas SQL;
- crear conexiones JDBC;
- duplicar reglas existentes en Services;
- acceder directamente a DAO si existe un Service correspondiente;
- contener lógica de persistencia.

---

# 10. JPA

Las entidades de persistencia de Fase 2 deben ubicarse en:

```text
emprendelink/persistence/jpa/entity/
```

Los mappers deben ubicarse en:

```text
emprendelink/persistence/jpa/mapper/
```

Conversión esperada:

```text
JPA Entity ↔ Domain Model
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

Las relaciones JPA deben representar correctamente las relaciones existentes en:

```text
database/schema.sql
```

y:

```text
docs/MODELO-DATOS.md
```

Utilizar exclusivamente:

```text
jakarta.persistence.*
```

No utilizar:

```text
javax.persistence.*
```

---

# 11. Hibernate

Hibernate forma parte de la persistencia prevista para Fase 2.

Antes de agregar o modificar una dependencia de Hibernate:

1. revisar `pom.xml`;
2. revisar `docs/VERSIONS.md`;
3. utilizar una versión compatible con Jakarta Persistence;
4. verificar compatibilidad con GlassFish;
5. ejecutar `mvn clean package`.

No cambiar versiones individualmente sin actualizar la documentación correspondiente.

---

# 12. Configuración JPA

La configuración de persistencia deberá mantenerse en:

```text
src/main/resources/META-INF/persistence.xml
```

Cuando este archivo sea creado deberá:

- utilizar Jakarta Persistence;
- definir correctamente la unidad de persistencia;
- utilizar la configuración de MySQL acordada;
- evitar credenciales reales;
- mantener compatibilidad con GlassFish;
- registrar o detectar correctamente las entidades JPA.

No hardcodear credenciales personales de ningún integrante.

---

# 13. JSF y AJAX

Fase 2 requiere demostrar interacción mediante JSF y AJAX.

Cuando corresponda utilizar:

```text
<f:ajax>
```

Las operaciones AJAX deben mejorar una funcionalidad real.

Ejemplos válidos:

- filtros de catálogo;
- actualización de listas;
- validación parcial;
- cambio de selección;
- actualización de información sin recargar toda la página.

AJAX no debe saltarse:

- validaciones;
- Services;
- permisos;
- reglas de negocio.

No implementar AJAX únicamente como efecto visual.

---

# 14. Validadores

Los validadores deben utilizarse cuando exista una necesidad real de validar entrada del usuario.

Ejemplos:

- formato;
- longitud;
- rango;
- datos obligatorios;
- reglas específicas del formulario.

Las reglas puramente de negocio deben seguir ubicándose en Services.

No duplicar la misma regla en múltiples capas sin necesidad.

---

# 15. Converters

Los converters deben utilizarse cuando JSF necesite transformar correctamente entre:

```text
valor de vista ↔ objeto Java
```

Ejemplos:

- selección de categoría;
- selección de emprendimiento;
- selección de publicación;
- selección de objetos relacionados.

No crear converters artificiales únicamente para cumplir formalmente el requisito.

---

# 16. Transacciones

Las operaciones que modifiquen múltiples registros relacionados deben mantener integridad transaccional.

Ejemplo principal:

```text
crear pedido
    ↓
guardar pedido
    ↓
guardar detalles
    ↓
confirmar transacción
```

Si una parte crítica falla:

```text
rollback
```

No debe quedar información parcial persistida.

Las transacciones deben gestionarse en la capa apropiada y nunca desde XHTML.

---

# 17. Seguridad

Roles oficiales:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

La autorización debe verificarse en backend.

Ocultar un botón no constituye autorización.

Antes de permitir modificaciones sobre recursos comprobar:

- autenticación;
- rol;
- propiedad del recurso;
- estado del recurso;
- reglas de negocio.

Las contraseñas nunca deben almacenarse en texto plano.

Utilizar las utilidades de seguridad existentes cuando corresponda.

No introducir:

```text
contraseñas reales
tokens
API keys
secretos
credenciales
```

en Git.

---

# 18. Autenticación

La autenticación debe reutilizar la lógica existente siempre que sea posible.

La lógica relacionada con:

- login;
- usuarios activos;
- roles;
- verificación de contraseña;

debe permanecer centralizada.

No duplicar autenticación de manera independiente para Servlets y JSF.

---

# 19. Pedidos

Estados oficiales:

```text
PENDIENTE
CONFIRMADO
EN_PROCESO
COMPLETADO
CANCELADO
```

Los cambios de estado deben validarse como reglas de negocio.

No aceptar cualquier transición simplemente porque ambos valores pertenecen al enum.

Las transiciones permitidas deben mantenerse centralizadas y documentadas.

La creación de pedidos debe proteger la integridad entre:

```text
Pedido
DetallePedido
Publicacion
Emprendimiento
Usuario
```

---

# 20. Base de datos

Base oficial:

```text
emprendelink_dwf
```

Scripts:

```text
database/schema.sql
database/seed.sql
```

El código debe ser compatible con el esquema real.

No modificar nombres de:

- tablas;
- columnas;
- llaves;
- relaciones;

sin revisar todos los componentes afectados.

Cualquier modificación del esquema debe reflejarse también en:

```text
docs/MODELO-DATOS.md
```

---

# 21. Seed

El archivo:

```text
database/seed.sql
```

debe contener datos válidos para el sistema real.

Los valores de contraseña utilizados en datos de prueba deben ser compatibles con el mecanismo real de hashing y verificación utilizado por la aplicación.

No utilizar valores ficticios como:

```text
hash_admin
hash_usuario
hash_cliente
```

si dichos valores impiden autenticar los usuarios de prueba.

---

# 22. Configuración local

La configuración local utiliza:

```text
.env
```

La plantilla compartida es:

```text
.env.example
```

Nunca subir:

```text
.env
```

ni credenciales reales.

La ubicación del archivo puede configurarse mediante:

```text
emprendelink.env
```

No hardcodear rutas locales de un integrante.

---

# 23. GlassFish

El servidor base del proyecto es:

```text
GlassFish 8.0.4
```

El artefacto generado es:

```text
target/emprendelink.war
```

Context path esperado:

```text
/emprendelink
```

Ejemplo local:

```text
http://localhost:8080/emprendelink/
```

La configuración local del servidor dentro de IntelliJ puede variar entre integrantes.

No subir configuraciones privadas del IDE como solución para compartir GlassFish.

---

# 24. Tecnologías fuera de la fase actual

Aunque el repositorio contiene estructuras previstas para futuras fases, durante Fase 2 no debe priorizarse:

```text
Spring
Spring Boot
Spring Security
API REST completa
cliente externo de API
```

Estas tecnologías corresponden principalmente a fases posteriores.

Los packages existentes relacionados con REST o Spring no significan que deban implementarse durante Fase 2.

No ampliar esos módulos salvo instrucción explícita.

---

# 25. Dependencias

Antes de agregar una dependencia:

1. verificar si Jakarta EE o GlassFish ya proporciona la API necesaria;
2. revisar `docs/VERSIONS.md`;
3. comprobar compatibilidad;
4. evitar dependencias innecesarias;
5. actualizar `pom.xml`;
6. ejecutar el build completo.

No agregar frameworks únicamente para simplificar una tarea pequeña.

---

# 26. Maven

El archivo central de dependencias es:

```text
pom.xml
```

Antes de modificarlo revisar el impacto sobre todo el equipo.

Después de cualquier cambio en dependencias ejecutar:

```text
mvn clean package
```

Resultado esperado:

```text
BUILD SUCCESS
```

No asumir que una dependencia es necesaria únicamente porque una clase del proyecto todavía no compile.

Primero revisar:

- imports;
- versión de Jakarta EE;
- APIs proporcionadas por GlassFish;
- configuración del IDE.

---

# 27. Pruebas

Las pruebas se encuentran bajo:

```text
src/test/java/
```

Durante Fase 2 deben incorporarse pruebas reales para funcionalidades relevantes.

Prioridades:

- reglas de negocio;
- CRUD;
- persistencia JPA;
- relaciones;
- permisos;
- transacciones;
- rollback;
- validaciones;
- estados de pedido.

No considerar una prueba válida únicamente porque una clase con nombre:

```text
*Test
```

compile.

Debe existir al menos una prueba ejecutable cuando se afirme cobertura automatizada.

---

# 28. Matriz de pruebas

La matriz manual se encuentra en:

```text
docs/MATRIZ-PRUEBAS.md
```

Debe ampliarse durante Fase 2 para cubrir:

- entidades JPA;
- relaciones;
- persistencia;
- CRUD;
- rollback;
- AJAX;
- validadores;
- converters;
- Managed Beans;
- permisos;
- flujos JSF.

Cuando una prueba sea ejecutada debe registrarse su resultado cuando corresponda.

---

# 29. Build obligatorio

Antes de considerar finalizada una modificación importante ejecutar:

```text
mvn clean package
```

Resultado esperado:

```text
BUILD SUCCESS
```

Una compilación exitosa no significa automáticamente que todas las pruebas funcionales hayan sido realizadas.

Revisar también la salida de Maven.

---

# 30. Git

Ramas permanentes:

```text
main
develop
```

No desarrollar directamente sobre:

```text
main
```

Las tareas deben partir de:

```text
develop
```

Tipos de rama aceptados:

```text
feature/*
fix/*
refactor/*
docs/*
```

Ejemplos:

```text
feature/fase2-categorias
feature/fase2-emprendimientos
feature/fase2-publicaciones
feature/fase2-pedidos
fix/validacion-correo
refactor/package-raiz
docs/fase2-documentacion
```

Los cambios deben integrarse mediante Pull Request hacia:

```text
develop
```

`main` debe recibir únicamente versiones estables.

---

# 31. Integración entre ramas

Antes de crear una rama:

```text
git switch develop
git pull origin develop
```

Después crear la rama correspondiente.

Antes de abrir un Pull Request:

```text
mvn clean package
git status
```

Evitar mezclar cambios de módulos no relacionados dentro del mismo Pull Request.

---

# 32. Cambios compartidos

Tener especial cuidado al modificar archivos utilizados por todo el equipo:

```text
pom.xml
database/schema.sql
database/seed.sql
README.md
AGENTS.md
docs/*
interfaces DAO
interfaces Service
enums
modelos compartidos
configuración JPA
```

Antes de realizar cambios amplios sobre estos archivos revisar el impacto sobre los módulos de los demás integrantes.

Evitar refactors globales innecesarios durante el desarrollo paralelo.

---

# 33. Código existente

No asumir que un archivo está incorrecto simplemente porque existe otra forma de implementarlo.

Antes de reemplazar código:

1. identificar quién lo utiliza;
2. revisar Services relacionados;
3. revisar DAO relacionados;
4. revisar imports;
5. revisar rutas;
6. revisar vistas;
7. revisar pruebas;
8. revisar documentación.

Mantener compatibilidad siempre que sea razonable.

---

# 34. Código generado por IA

Todo código generado debe:

- compilar;
- utilizar clases reales del repositorio;
- respetar nombres existentes;
- respetar interfaces actuales;
- utilizar `jakarta.*`;
- respetar la arquitectura;
- manejar errores;
- evitar credenciales;
- evitar duplicación innecesaria;
- incluir imports correctos;
- considerar el módulo completo afectado.

No inventar:

```text
servicios inexistentes
campos inexistentes
tablas inexistentes
rutas inexistentes
métodos DAO inexistentes
dependencias no declaradas
```

Si una nueva pieza es realmente necesaria, indicarlo explícitamente y explicar qué archivos dependen de ella.

---

# 35. Archivos completos

Cuando una tarea implique cambiar código existente, preferir entregar archivos completos cuando eso reduzca errores de integración.

Evitar instrucciones ambiguas como modificar líneas sueltas cuando el cambio afecta gran parte de una clase.

No eliminar código funcional sin explicar la razón técnica.

Después de cambios estructurales revisar:

```text
imports
packages
referencias
tests
documentación
build
```

---

# 36. Errores y excepciones

El proyecto utiliza excepciones propias.

Entre ellas:

```text
EmprendeLinkException
ValidacionException
ReglaNegocioException
RecursoNoEncontradoException
AccesoNoAutorizadoException
PersistenciaException
```

No mostrar al usuario:

- stack traces;
- errores SQL internos;
- rutas locales;
- credenciales;
- detalles sensibles.

Las excepciones deben convertirse en mensajes apropiados según la capa correspondiente.

---

# 37. Arquitectura por fase

Fase 1:

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

Fase 2:

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

Fase 3:

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

Fase 4:

```text
Spring
  ↓
Services
  ↓
Persistencia
  ↓
MySQL
```

No adelantar una fase si eso perjudica el cumplimiento de la fase actual.

---

# 38. Alcance de Fase 2

El objetivo actual es demostrar correctamente:

```text
Persistencia JPA / Hibernate
Relaciones entre entidades
CRUD
Transacciones
JSF
Managed Beans
AJAX
Validación
Converters
Integridad de datos
Pruebas
```

Cualquier decisión debe priorizar estos requisitos sobre funcionalidades futuras.

---

# 39. Distribución modular

Los módulos de Fase 2 están divididos por áreas funcionales.

Las áreas principales son:

```text
Autenticación y usuarios
Categorías
Emprendimientos
Publicaciones y catálogo
Pedidos
```

Cada módulo debe respetar las mismas reglas arquitectónicas.

Ningún módulo debe crear una arquitectura paralela independiente.

---

# 40. Criterio de finalización

Una tarea de Fase 2 puede considerarse terminada cuando:

1. compila;
2. utiliza el package correcto;
3. respeta la arquitectura;
4. funciona contra MySQL;
5. utiliza JPA cuando corresponde;
6. las relaciones están correctamente mapeadas;
7. los Managed Beans utilizan Services;
8. las reglas de negocio se respetan;
9. los permisos se verifican;
10. los errores se manejan;
11. las transacciones mantienen integridad;
12. las validaciones funcionan;
13. AJAX funciona cuando la tarea lo requiere;
14. existen pruebas o evidencia funcional;
15. `mvn clean package` termina correctamente;
16. la documentación afectada está actualizada;
17. el cambio está listo para Pull Request hacia `develop`.

---

# 41. Principio final

No desarrollar componentes únicamente para completar carpetas o aparentar cumplimiento.

Cada cambio debe aportar funcionalidad verificable y mantener coherencia con:

```text
Código real
Arquitectura
Base de datos
Documentación
Requisitos de la fase
```

La prioridad es entregar una aplicación funcional, consistente y defendible técnicamente.