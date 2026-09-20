# Evidencia de trabajo - Integrante 5

## 1. Trabajo realizado

Mi parte del proyecto EmprendeLink consiste en desarrollar las pantallas JSP y ayudar con las pruebas.

Trabajé en estos cuatro archivos:

- `emprendimientos/formulario.jsp`: crear y editar emprendimientos.
- `publicaciones/formulario.jsp`: crear y editar publicaciones.
- `catalogo/lista.jsp`: mostrar las publicaciones.
- `catalogo/detalle.jsp`: mostrar la información de una publicación.

Estos archivos ya están subidos a mi rama `feature/jsp-vistas`.

## 2. Revisión de las pantallas

Revisé que los formularios tengan los campos necesarios y que sus nombres coincidan con los que esperan los servlets.

También comprobé en el código que:

- Los campos obligatorios tengan validación.
- El precio y el stock no permitan números negativos.
- El catálogo muestre un mensaje cuando no haya publicaciones.
- Se pueda acceder al detalle y regresar al catálogo.
- Se utilice `c:out` para mostrar los textos de forma segura.

Esta revisión fue del código. Todavía falta probar las pantallas funcionando en el navegador.

## 3. Pruebas pendientes

| Historia | Prueba | Estado |
|---|---|---|
| HU-05 | Crear emprendimiento | BLOQUEADA |
| HU-06 | Editar emprendimiento | BLOQUEADA |
| HU-12 | Crear publicación y validar precio | BLOQUEADA |
| HU-16 | Mostrar catálogo con y sin publicaciones | BLOQUEADA |
| HU-17 | Mostrar detalle y manejar una publicación inexistente | BLOQUEADA |

**Resultado obtenido:** no pude completar las pruebas funcionales porque falta resolver un problema de integración.

## 4. Problemas encontrados

**Problema 1:** al ejecutar la aplicación apareció el error `EmprendimientoService no está configurado`. Revisé GitHub y encontré la interfaz del servicio, pero no logré localizar su implementación ni dónde se conecta con el servlet.

**Problema 2:** cuando el formulario de publicaciones recibe datos incorrectos, el servlet responde con un error 400 en lugar de regresar al formulario con un mensaje.

**Problema 3:** la pantalla de detalle tiene un mensaje para publicaciones inexistentes, pero el servlet responde directamente con un error 404.

Estos problemas quedaron pendientes de revisión con los compañeros encargados del backend y la integración.

## 5. Conclusión

Terminé mis cuatro pantallas JSP y revisé su estructura. También identifiqué los problemas que impiden completar las pruebas.

Cuando mis compañeros terminen de integrar los servicios, podré probar las pantallas en el navegador y registrar los resultados.