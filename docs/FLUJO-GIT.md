# Flujo de trabajo con Git — EmprendeLink DWF

Este documento define el flujo oficial de Git que debe utilizar todo el equipo durante el desarrollo de EmprendeLink DWF.

Estas reglas también deben ser tomadas como referencia por cualquier asistente de IA utilizado para trabajar con este repositorio.

---

## 1. Ramas principales

El proyecto utiliza dos ramas permanentes:

### `main`

Contiene únicamente versiones estables del proyecto.

No se debe desarrollar directamente sobre esta rama.

Los cambios llegan a `main` únicamente desde `develop` cuando existe una versión estable o una entrega.

### `develop`

Es la rama de integración del equipo.

Todas las nuevas funcionalidades, correcciones y cambios deben integrarse primero en `develop`.

---

## 2. Ramas de trabajo

Cada tarea debe desarrollarse en su propia rama.

### Nuevas funcionalidades

Formato:

```text
feature/nombre-de-la-tarea
```

Ejemplos:

```text
feature/crud-categorias
feature/registro-usuarios
feature/gestion-emprendimientos
```

### Correcciones

Formato:

```text
fix/nombre-del-problema
```

Ejemplo:

```text
fix/validacion-correo
```

Las ramas `feature/*` y `fix/*` deben crearse desde `develop`.

---

## 3. Primera vez trabajando con el repositorio

Clonar el proyecto:

```bash
git clone https://github.com/Notchez/emprendelink-dwf.git
```

Entrar al proyecto:

```bash
cd emprendelink-dwf
```

Cambiar a `develop`:

```bash
git switch develop
```

---

## 4. Antes de comenzar una tarea

Siempre actualizar `develop`:

```bash
git switch develop
git pull origin develop
```

Después crear una rama para la tarea:

```bash
git switch -c feature/nombre-de-la-tarea
```

Ejemplo:

```bash
git switch -c feature/crud-categorias
```

---

## 5. Guardar cambios

Agregar los archivos modificados:

```bash
git add -A
```

Crear el commit:

```bash
git commit -m "feat: implementar gestión de categorías"
```

Subir la rama por primera vez:

```bash
git push -u origin feature/crud-categorias
```

---

## 6. Pull Request

Cuando una tarea esté terminada, crear un Pull Request en GitHub.

La dirección correcta es:

```text
feature/* → develop
```

o:

```text
fix/* → develop
```

Ejemplo:

```text
feature/crud-categorias → develop
```

Nunca enviar directamente una rama `feature/*` hacia `main`.

---

## 7. Después del Merge

Cuando el Pull Request haya sido aprobado e integrado en `develop`, la rama de trabajo puede eliminarse.

Para comenzar otra tarea:

```bash
git switch develop
git pull origin develop
git switch -c feature/nueva-tarea
```

---

## 8. Flujo general

```text
feature/* ──────┐
                ├────► develop ─────► main
fix/* ──────────┘
```

- `feature/*`: nuevas funcionalidades.
- `fix/*`: correcciones.
- `develop`: integración del trabajo del equipo.
- `main`: versiones estables y entregables.

---

## 9. Reglas importantes

- No desarrollar directamente sobre `main`.
- Evitar desarrollar directamente sobre `develop`.
- Cada tarea debe tener su propia rama.
- Actualizar `develop` antes de crear una rama nueva.
- Los Pull Requests de desarrollo deben apuntar a `develop`.
- No subir archivos generados como `target/`.
- No subir configuraciones de IntelliJ como `.idea/`.
- No subir contraseñas, credenciales ni archivos `.env`.
- Respetar la estructura y convenciones definidas en `docs/CONVENCIONES.md`.
- Revisar `docs/ARQUITECTURA.md` antes de realizar cambios estructurales importantes.

---

## 10. Instrucción para asistentes de IA

Si este repositorio está siendo analizado por un asistente de IA:

1. Utilizar `develop` como rama base para nuevas tareas.
2. No recomendar cambios directos sobre `main`.
3. Crear ramas `feature/*` o `fix/*` según corresponda.
4. Mantener la arquitectura y convenciones existentes.
5. Consultar primero la documentación ubicada en `/docs`.
6. No modificar innecesariamente contratos, paquetes, nombres o estructura compartida.
7. Considerar `pom.xml` como fuente de verdad para las dependencias y versiones utilizadas por el proyecto.