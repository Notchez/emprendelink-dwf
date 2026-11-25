# Sprint IV — Integración Spring y seguridad (Integrante 1)

## Decisión arquitectónica

Se incorpora Spring Framework `7.0.11` mediante `spring-context`. No se migra a Spring Boot ni se sustituye Jakarta EE. GlassFish continúa administrando CDI, JSF, JAX-RS, JPA/EclipseLink, JTA, DataSource y sesiones HTTP. Spring administra componentes de política de acceso y errores que se ejecutan en solicitudes reales, sin crear otro acceso a datos.

```text
HTTP / JSF -> WebAccessFilter ---------+
HTTP / REST -> ApiAuthenticationFilter +-> AccessPolicy (Spring)
                                          -> RouteRules (Spring, inyección por constructor)
                     UsuarioService (CDI/JTA) -> DAO/JPA -> MySQL
Errores REST -> ExceptionMapper / ResponseFilter / ErrorDispatchServlet
                                         -> ApiErrorPolicy (Spring)
```

`SpringRuntime` crea y cierra un `AnnotationConfigApplicationContext` por despliegue WAR mediante un listener registrado en `web.xml`. `SpringConfig` registra los beans y Spring inyecta `RouteRules` en `AccessPolicy`. Los adaptadores Servlet/JAX-RS recuperan los beans del contexto de la aplicación; CDI conserva la administración de `UsuarioService`. No hay escaneo global de componentes, segundo EntityManager ni administrador de transacciones duplicado. No se utiliza Spring Security, Spring MVC ni Spring Data: la autorización por sesión, presentación y persistencia existentes se conservan y se centraliza su política con Spring Context.

## Seguridad

- Rutas JSF: un filtro sustituye los siete filtros por perfil. Antes de permitir una solicitud con sesión, vuelve a consultar la cuenta mediante `UsuarioService`. Si se desactivó, desapareció o cambió de rol, invalida la sesión JSF y exige nuevo inicio. Conserva las redirecciones por perfil del catálogo.
- API: la política Spring define rutas públicas y permisos por método y rol. La sesión se revalida contra la base; los métodos de escritura protegidos conservan `X-CSRF-Token`. El detalle de pedidos y propiedad de recursos siguen siendo responsabilidad de Services, no del filtro.
- Login JSF/REST: sesión anterior invalidada; hash no se expone. Se mantienen los hashes PBKDF2 existentes para compatibilidad con los datos. Se añade límite de longitud al login JSF y se requiere cuenta activa con rol válido.
- JSF conserva el mecanismo ViewState; el filtro rechaza solicitudes no seguras marcadas `Sec-Fetch-Site: cross-site`. Esto es defensa adicional, no prueba exhaustiva de CSRF. API conserva validación de token.
- Cookies: HttpOnly, seguimiento de sesión exclusivamente por cookie y timeout de 30 minutos. El despliegue HTTPS debe configurar Secure a nivel del contenedor/proxy y verificarse por el Integrante 4. No habilitar CORS permisivo.
- Errores: Spring define mensajes públicos comunes. Los mappers preservan errores de negocio controlados; el filtro de respuesta completa errores REST sin cuerpo y el dispatcher de contenedor distingue errores HTML web de errores JSON API. Se conservan códigos HTTP. No se devuelven trazas o SQL al usuario.
- No se agregan secretos. Los datos de `seed.sql` son exclusivamente demostrativos; no deben utilizarse como credenciales de un entorno expuesto. La configuración de BD continúa en GlassFish/JNDI.

## Limpieza y coordinación

Se retiran siete filtros sustituidos por `WebAccessFilter`, `CatalogoBean` vacío y los dos Resources vacíos en `emprendelink.web.rest`, solo tras verificar que no tengan referencias de código. Los Resources activos de catálogo en `src/main/java/rest/` se conservan: su corrección funcional pertenece al Integrante 2. No se cambian contratos de pedido, schema, seed, cliente ni lógica de comisiones. El PR #19 de QA debe revisarse por separado; no se borran evidencias históricas.

## Validación obligatoria antes de integrar

```powershell
mvn -B clean verify
```

La prueba `SpringIntegrationTest` crea un contexto Spring real, recupera beans y verifica permisos, CSRF y mensajes. Las pruebas REST anteriores deben seguir pasando. El workflow CI construye también el WAR. Estos resultados no se consideran aprobados hasta ejecutarlos con JDK 25 y Maven.

En GlassFish, desplegar `target/emprendelink.war` y ejecutar: login y logout de los tres perfiles; acceso directo a rutas privadas sin sesión y con rol incorrecto; desactivar una cuenta con sesión abierta y comprobar revocación; modificar rol y comprobar nueva autenticación JSF; `/api/v1/health`; `/api/v1/auth/me` sin sesión; checkout sin CSRF y con CSRF inválido; pedido ajeno; 404 de recurso inexistente con `Accept: application/json`; error web HTML; revisar cookies y ausencia de secretos en respuestas. Ejecutar regresión de catálogo, carrito, pedido, inventario y comisiones con los responsables. Registrar resultados y evidencias en la matriz, sin marcar casos no ejecutados como aprobados.

Estado del incremento: **código propuesto; compilación, integración y despliegue pendientes de validación en el entorno real**.
