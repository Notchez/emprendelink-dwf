
# AGENTS.md — EmprendeLink DWF

Reglas técnicas obligatorias para desarrolladores y asistentes de IA que trabajen en este repositorio.

> **Estado al inicio de Fase 3 (19/10/2026):** Fase 2 cerrada como incremento funcional; Fase 3 — Servicios REST e Integración de Clientes (Sprint III), en preparación. Fecha académica prevista: 7 u 8 de noviembre de 2026. No marcar funcionalidades nuevas como terminadas hasta integrarlas y verificarlas.

## 1. Arquitectura vigente

La aplicación de Fase 2 sigue operativa con JSF y JPA. La API REST es **una nueva interfaz hacia la misma capa Service**, no una aplicación de negocio paralela.

```text
JSF / XHTML → Managed Beans CDI ──┐
                                   ├→ Services → DAO → JPA → EclipseLink → JTA
Cliente externo → HTTP/JSON → REST Resources → DTO ──┘                      ↓
                                                       GlassFish DataSource → MySQL
```

- El modelo, las reglas de negocio, la persistencia y las transacciones existentes se reutilizan.
- Los REST Resources reciben/validan solicitudes, controlan acceso según el contrato y delegan a Services; **nunca consultan DAO directamente**.
- JSF y la API REST deben poder coexistir sin romper los flujos ya entregados.
- No implementar funcionalidades de Fase 3 directamente en `main` o `develop`.

## 2. Stack y configuración conservados

- Java **25** y Maven, según `pom.xml`.
- Jakarta EE **11**, GlassFish **8.0.4**.
- Jakarta Faces / JSF, CDI, JPA con **EclipseLink**, JTA, MySQL.
- JAX-RS para los nuevos servicios REST (la API de Jakarta EE está declarada con scope `provided`).
- JUnit y GitHub Actions para la regresión y validación continua.
- No reemplazar el proveedor JPA ni introducir Spring en Fase 3 sin una decisión técnica explícita. **Spring corresponde al Sprint IV.**

## 3. Reglas obligatorias heredadas de Fase 2

Estas restricciones siguen vigentes para preservar la arquitectura consolidada:

1. No crear nuevas implementaciones JDBC.
2. No crear Servlets para funcionalidades de Fase 2.
3. No crear vistas JSP.
4. No agregar Hibernate como dependencia.
5. No usar `DriverManager`.
6. No crear `EntityManagerFactory` manualmente.
7. No usar `RESOURCE_LOCAL`.
8. No registrar Services manualmente en `ServletContext`.
9. Usar CDI para inyección de dependencias.
10. Usar `@PersistenceContext` para `EntityManager`.
11. Usar JTA para transacciones.
12. Los Managed Beans llaman Services, nunca DAO directamente.
13. Los Services contienen reglas de negocio.
14. Los DAO contienen acceso a persistencia.
15. Las entidades JPA viven en `persistence/jpa/entity`.
16. Los modelos de dominio viven en `model`.
17. Los mappers convierten entre dominio y entidad.
18. No exponer hashes de contraseña en vistas, sesión, DTO o API.
19. No permitir registro público con `ROLE_ADMIN`.
20. No introducir secretos, como contraseñas, al repositorio.

Reglas adicionales para Fase 3:

21. No repetir lógica de negocio de Services en REST Resources, DTO ni en el cliente externo.
22. No serializar entidades JPA directamente ni exponer relaciones internas sin control.
23. No introducir cambios incompatibles al contrato compartido sin revisar `docs/API.md` y los consumidores.
24. No implementar operaciones protegidas antes de resolver autenticación y autorización para REST.
25. No introducir configuraciones permisivas de CORS o credenciales en el código por conveniencia.
26. Toda nueva funcionalidad debe respetar el alcance del Sprint III; no anticipar Spring ni el despliegue final del Sprint IV.

## 4. Persistencia

```text
Persistence Unit: emprendelinkPU
DataSource JNDI:  jdbc/EmprendeLinkDS
Pool GlassFish:   EmprendeLinkPool
Proveedor JPA:   EclipseLink provisto por GlassFish
Transacciones:   JTA
Base de datos:   emprendelink_dwf
Scripts:         database/schema.sql y database/seed.sql
```

- Toda nueva entidad debe añadirse a `persistence.xml` mientras el archivo mantenga un listado explícito.
- Preservar reglas existentes: negocio único por emprendedor; stock; catálogo según plan; estados de pedidos; historial de comisión y dirección de entrega.
- En particular, el checkout multi-negocio agrupa por emprendimiento y crea **un Pedido por negocio** usando la lógica de `PedidoService`, sin recalcular reglas independientemente desde la API.

## 5. Paquetes vigentes

```text
emprendelink.dao
emprendelink.dao.jpa
emprendelink.dto
emprendelink.exception
emprendelink.model
emprendelink.model.enums
emprendelink.persistence.jpa.entity
emprendelink.persistence.jpa.mapper
emprendelink.security
emprendelink.service
emprendelink.service.impl
emprendelink.web.jsf
emprendelink.web.jsf.converter
emprendelink.web.jsf.validator
emprendelink.web.rest
emprendelink.spring
```

`emprendelink.spring` es un paquete preparatorio; no implica que Spring forme parte del runtime actual.

## 6. Línea base heredada de Fase 2

El incremento JSF se organizó en módulos verticales:

```text
Entity → Mapper → JpaDAO → Service → Bean → XHTML → AJAX/validaciones → tests
```

Según el README y el Product Backlog de `develop`, se implementaron autenticación/usuarios, categorías, emprendimientos, publicaciones/catálogo, carrito/checkout, pedidos y reportes/comisiones. La documentación histórica de evidencias y smoke tests se conserva como registro y no debe reinterpretarse como pruebas ejecutadas de Fase 3.

## 7. Seguridad y permisos

Sesión oficial de la aplicación JSF:

```text
usuarioAutenticado
```

Roles oficiales:

```text
ROLE_ADMIN
ROLE_EMPRENDEDOR
ROLE_CLIENTE
```

- La autorización se verifica **en el backend**; ocultar controles en la interfaz no basta.
- Los filtros JSF existentes protegen sus propias rutas y **no deben asumirse como protección automática de `/api/v1/*`**.
- Para endpoints protegidos, definir y comprobar identidad, usuario activo, rol, propiedad y estado del recurso. El mecanismo concreto de sesión, credenciales o tokens se acuerda y documenta antes de habilitar operaciones privadas.
- Denegar operaciones no autorizadas aunque el consumidor construya manualmente solicitudes HTTP.
- No exponer contraseñas, hashes, tokens, credenciales, trazas, SQL ni información privada de terceros.
- Distinguir apropiadamente respuestas de no autenticado, sin permisos, no encontrado y conflicto, sin filtrar detalles sensibles.

## 8. Convenciones para Fase 3 — REST

- Context path existente: `/emprendelink`.
- Prefijo de API: `/api/v1` mediante `RestApplication` y `@ApplicationPath`.
- Recursos propuestos: `/emprendimientos`, `/publicaciones`, `/pedidos`; su alcance definitivo pertenece al contrato compartido.
- JSON: `application/json` con atributos `camelCase`.
- Usar semántica HTTP adecuada: `GET` para lectura; `POST` para creación; `PUT`/`PATCH` para actualización según el contrato; otras operaciones solo cuando correspondan.
- Definir códigos de éxito y error coherentes: por ejemplo, `200`, `201`, `204`, `400`, `401`, `403`, `404`, `409` y `500` cuando aplique. No devolver siempre `200` ante fallos.
- Los DTO definen las entradas y salidas públicas; no exponen entidades JPA directamente ni contienen reglas de negocio.
- Validar datos entrantes antes de ejecutar operaciones; conservar validaciones y restricciones en Services como fuente de verdad.
- Establecer mensajes JSON de error consistentes, sin detalles internos.
- Mantener ejemplos verificables de solicitudes/respuestas en `docs/API.md` y actualizar la colección de pruebas cuando evolucione el contrato.

**Infraestructura base verificada localmente en la rama de preparación:**

```text
RestApplication.java  → @ApplicationPath("/api/v1")
HealthResource.java   → GET /api/v1/health
```

La prueba manual local del 19/10/2026 devolvió JSON con `status: UP`, `application: EmprendeLink`, `phase: Fase 3` y `apiVersion: v1`. Esto prueba la respuesta del endpoint HTTP, **no** verifica por sí solo la conexión a MySQL, las reglas de negocio ni la seguridad. Confirmar que los archivos estén fusionados en `develop` antes de darlos por disponibles a todo el equipo.

Las clases `EmprendimientoResource`, `PublicacionResource`, `PedidoResource` y los DTO existentes en la línea base de Fase 2 son **scaffolding vacío**, no endpoints implementados.

## 9. Alcance de implementación del Sprint III

El trabajo **pendiente del equipo**, sin adelantarse en la rama base, comprende:

1. Contrato REST definitivo y arquitectura de integración.
2. DTO de entrada, salida y error.
3. Endpoints conectados a Services y a persistencia existente.
4. Seguridad REST, validaciones y manejo consistente de excepciones.
5. Cliente externo consumidor con formularios, estados de carga y errores.
6. Pruebas HTTP positivas, negativas, de límite y de autorización.
7. Documentación de consumo, evidencia técnica y demostración virtual.

Consultar `docs/PRODUCT-BACKLOG.md` y `docs/API.md` para alcance y contratos. **La rama de preparación configura infraestructura y reglas; no implementa las historias del resto del equipo.**

## 10. Git y trabajo colaborativo

- `main`: base estable de liberación; no desarrollar directamente allí.
- `develop`: base de integración del equipo; no hacer commits directos.
- `feature/*`: ramas de desarrollo, creadas desde `develop` actualizado.
- Rama de preparación: `feature/fase3-base-rest` → Pull Request a `develop` tras validación.
- Cada incremento funcional se entrega con Pull Request hacia `develop`, descripción, pruebas y archivos afectados.
- No fusionar ramas con tests fallidos o build roto; no efectuar merges a `main` como parte de la preparación de Fase 3.
- Actualizar README, contrato y demás documentación solo cuando cambien rutas, arquitectura, reglas o instrucciones reales de ejecución.
- Evitar conflictos editando archivos compartidos mediante revisiones y acuerdos previos.

## 11. Build, CI y definición de terminado

Comandos obligatorios antes del Pull Request:

```powershell
mvn clean test
mvn clean package
```

El workflow actual de GitHub Actions ejecuta:

```text
mvn -B clean test
```

`BUILD SUCCESS` verifica compilación/pruebas aplicables; no reemplaza la validación manual del endpoint desplegado ni los casos HTTP de seguridad e integración.

Una historia de Fase 3 solo pasa a **HECHO** cuando:

- cumple sus criterios de aceptación;
- está implementada, revisada e integrada;
- su comportamiento HTTP/JSON se probó con casos pertinentes;
- no rompe JSF ni las reglas de negocio existentes;
- tiene documentación/evidencias proporcionales al cambio.

## 12. Documentos de referencia

```text
docs/API.md                → Contrato preliminar REST; pendiente de cerrar por el equipo
docs/PRODUCT-BACKLOG.md    → Historias, estado y criterios del Sprint III
docs/ARQUITECTURA.md       → Arquitectura heredada y su evolución
docs/ROLES-PERMISOS.md     → Autorización por perfil
docs/MATRIZ-PRUEBAS.md     → Regresión heredada y casos documentados
README.md                  → Instalación y ejecución local
```

Si este archivo contradice un cambio ya integrado en `develop`, actualizarlo en el mismo PR que modifica esa decisión técnica.
