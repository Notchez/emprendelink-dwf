# Product Backlog — EmprendeLink

## Historias consideradas

| ID | Historia | Prioridad | Sprint I | Responsable principal | Estado |
|---|---|---|---|---|---|
| HU-05 | Como emprendedor quiero registrar mi emprendimiento para poder publicar mi oferta. | Alta | Sí | Equipo | Pendiente integración |
| HU-06 | Como emprendedor quiero actualizar los datos de mi emprendimiento para mantener la información vigente. | Alta | Sí | Equipo | Pendiente integración |
| HU-12 | Como emprendedor quiero registrar una publicación para ofrecer un producto o servicio. | Alta | Sí | Equipo | Pendiente integración |
| HU-16 | Como cliente quiero consultar el catálogo para conocer las publicaciones disponibles. | Alta | Sí | Equipo | Pendiente integración |
| HU-17 | Como cliente quiero ver el detalle de una publicación para conocer su información antes de contactar al emprendimiento. | Alta | Sí | Equipo | Pendiente integración |
| HU-37 | Como cliente quiero realizar un pedido para solicitar productos de un emprendimiento. | Media | No | Equipo | Backlog |

## Criterios de aceptación

### HU-05 — Registrar emprendimiento

- Debe existir un propietario válido.
- El nombre es obligatorio.
- El emprendimiento queda activo al registrarse.
- Se registra la fecha de creación.

### HU-06 — Actualizar emprendimiento

- Solo se actualiza un emprendimiento existente.
- El nombre no puede quedar vacío.
- La descripción y el contacto pueden modificarse.
- El identificador del emprendimiento no cambia.

### HU-12 — Registrar publicación

- Debe pertenecer a un emprendimiento.
- Debe tener categoría y tipo.
- El nombre y el precio son obligatorios.
- El precio no puede ser negativo.
- El stock no puede ser negativo cuando aplique.
- La publicación queda activa inicialmente.

### HU-16 — Consultar catálogo

- Solo se muestran publicaciones disponibles según el flujo definido por el equipo.
- Una lista sin resultados debe manejarse sin generar error.
- Cada resultado debe permitir identificar nombre, tipo, categoría y precio.

### HU-17 — Ver detalle

- Debe consultarse una publicación existente.
- Se muestra su información principal.
- Si no existe, el sistema debe mostrar un mensaje controlado.

## Trazabilidad del Sprint I

| Historia | Entidades principales | Validaciones principales |
|---|---|---|
| HU-05 | Usuario, Rol, Emprendimiento | propietario, nombre, estado inicial |
| HU-06 | Emprendimiento | existencia, nombre, contacto |
| HU-12 | Emprendimiento, Categoria, Publicacion | tipo, precio, stock, relaciones |
| HU-16 | Publicacion, Categoria, Emprendimiento | lista vacía, publicaciones válidas |
| HU-17 | Publicacion, Categoria, Emprendimiento | existencia e identificador |
