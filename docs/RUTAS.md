# Rutas — EmprendeLink DWF

Context path local:

```text
/emprendelink
```

## Rutas implementadas

### Login

```text
/login.xhtml
```

Pública.

### Registro

```text
/registro.xhtml
```

Pública.

Permite crear:

```text
ROLE_CLIENTE
ROLE_EMPRENDEDOR
```

No permite crear `ROLE_ADMIN`.

### Inicio autenticado

```text
/inicio.xhtml
```

Requiere sesión válida.

### Administración de usuarios

```text
/admin/usuarios.xhtml
```

Requiere:

```text
ROLE_ADMIN
```

Protegida por `AdminFilter`.

## Rutas previstas de Fase 2

### Categorías

```text
/admin/categorias.xhtml
```

### Emprendimientos

```text
/emprendimientos/index.xhtml
/emprendimientos/formulario.xhtml
```

### Publicaciones

```text
/publicaciones/index.xhtml
/publicaciones/formulario.xhtml
```

### Catálogo

```text
/catalogo/index.xhtml
/catalogo/detalle.xhtml
```

### Pedidos

```text
/pedidos/index.xhtml
/pedidos/detalle.xhtml
```

Las rutas definitivas podrán ajustarse al implementar cada módulo, pero deben documentarse antes de integrar a `develop`.

## Protección

```text
/admin/* → ROLE_ADMIN
```

Los módulos de emprendedor deben comprobar `ROLE_EMPRENDEDOR` y propiedad del recurso.

Los pedidos privados del cliente deben comprobar `ROLE_CLIENTE` y propiedad del pedido.

## API REST — Fase 3

Ruta base:

```text
/api/v1
```

Previsto:

```text
GET  /api/v1/emprendimientos
GET  /api/v1/emprendimientos/{id}
GET  /api/v1/publicaciones
GET  /api/v1/publicaciones/{id}
POST /api/v1/pedidos
GET  /api/v1/pedidos/{id}
PUT  /api/v1/pedidos/{id}/estado
```

## Errores

```text
/error/403.html
/error/404.html
/error/500.html
```
