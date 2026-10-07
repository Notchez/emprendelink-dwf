# Flujo de trabajo con Git — EmprendeLink DWF

Este documento define el flujo oficial de Git para el desarrollo de EmprendeLink DWF.

El objetivo es permitir que varios integrantes trabajen simultáneamente sin modificar directamente las ramas estables ni mezclar tareas independientes.

---

# 1. Ramas permanentes

El proyecto utiliza dos ramas permanentes:

```text
main
develop
```

## main

Contiene versiones estables, entregables o releases del proyecto.

No se debe desarrollar directamente sobre esta rama.

Los cambios normalmente llegan a `main` desde:

```text
develop
```

cuando la versión integrada ha sido validada.

---

## develop

Es la rama principal de integración del equipo.

Todas las nuevas funcionalidades, correcciones, refactors y cambios de documentación deben integrarse primero en:

```text
develop
```

Las ramas de trabajo deben crearse tomando como base un `develop` actualizado.

---

# 2. Tipos de ramas

Cada tarea debe desarrollarse en una rama independiente.

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
fix/validacion-correo
fix/calculo-total
fix/permisos-pedido
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

# 3. Primera vez trabajando con el repositorio

Clonar:

```bash
git clone https://github.com/Notchez/emprendelink-dwf.git
```

Entrar:

```bash
cd emprendelink-dwf
```

Cambiar a `develop`:

```bash
git switch develop
```

Actualizar:

```bash
git pull origin develop
```

---

# 4. Antes de comenzar una tarea

Siempre partir de un `develop` actualizado.

```bash
git switch develop
git pull origin develop
```

Después crear la rama correspondiente.

Ejemplo:

```bash
git switch -c feature/fase2-categorias
```

Verificar:

```bash
git branch --show-current
```

---

# 5. Durante el trabajo

Consultar cambios:

```bash
git status
```

Revisar diferencias:

```bash
git diff
```

Agregar cambios:

```bash
git add -A
```

Crear commit:

```bash
git commit -m "feat: implementar persistencia JPA de categorías"
```

---

# 6. Convención de commits

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
feat: implementar gestión JSF de publicaciones
feat: agregar persistencia JPA de pedidos
fix: validar transición de estado de pedido
docs: actualizar documentación base para Fase 2
refactor: simplificar package raiz a emprendelink
test: agregar pruebas de reglas de pedido
chore: actualizar configuración Maven
```

Evitar mensajes como:

```text
cambios
update
arreglo
prueba
final
final2
cosas
```

---

# 7. Validación antes de subir

Antes de enviar una rama debe comprobarse al menos:

```bash
mvn clean package
```

Resultado esperado:

```text
BUILD SUCCESS
```

También revisar:

```bash
git status
```

La rama no debe contener:

- archivos `.env`;
- credenciales;
- configuraciones privadas de IntelliJ;
- `target/`;
- secretos;
- archivos generados innecesarios.

---

# 8. Push

Primera subida de una rama:

```bash
git push -u origin nombre-de-la-rama
```

Ejemplo:

```bash
git push -u origin feature/fase2-categorias
```

Después de establecer upstream puede utilizarse:

```bash
git push
```

---

# 9. Pull Request

Las ramas de trabajo deben integrarse mediante Pull Request hacia:

```text
develop
```

Ejemplos:

```text
feature/fase2-categorias → develop

fix/permisos-pedido → develop

refactor/package-raiz → develop

docs/fase2-documentacion → develop
```

No utilizar normalmente:

```text
feature/* → main
```

---

# 10. Revisión antes del Merge

Antes de realizar Merge comprobar:

- el objetivo de la rama está completo;
- no existen cambios ajenos a la tarea;
- el proyecto compila;
- no existen credenciales;
- no existen conflictos;
- la arquitectura se respeta;
- la documentación afectada está actualizada;
- las pruebas correspondientes fueron ejecutadas.

---

# 11. Después del Merge

Después de integrar el Pull Request:

```bash
git switch develop
git pull origin develop
```

Verificar:

```bash
mvn clean package
```

Después puede eliminarse la rama integrada.

---

# 12. Verificar ramas integradas

Para comprobar qué ramas locales ya están incluidas en `develop`:

```bash
git branch --merged develop
```

Si una rama aparece en esa lista, puede eliminarse normalmente con:

```bash
git branch -d nombre-de-la-rama
```

Ejemplo:

```bash
git branch -d feature/fase2-categorias
```

Utilizar preferentemente:

```text
-d
```

en lugar de:

```text
-D
```

porque `-d` protege contra eliminar accidentalmente trabajo no integrado.

---

# 13. Eliminar rama remota

Cuando una rama ya fue integrada y no será reutilizada:

```bash
git push origin --delete nombre-de-la-rama
```

Ejemplo:

```bash
git push origin --delete feature/fase2-categorias
```

GitHub también puede eliminar automáticamente la rama después del Merge si esa opción está habilitada.

---

# 14. Limpiar referencias remotas

Después de borrar ramas remotas:

```bash
git fetch --prune
```

Consultar todas las ramas:

```bash
git branch -a
```

---

# 15. Flujo general

```text
feature/*  ─┐
fix/*      ─┤
refactor/* ─┼────► develop ─────► main
docs/*     ─┘
```

Responsabilidades:

```text
feature/*   nuevas funcionalidades
fix/*       correcciones
refactor/*  cambios estructurales
docs/*      documentación
develop     integración
main        versiones estables
```

---

# 16. Trabajo paralelo durante Fase 2

La Fase 2 se divide principalmente en:

```text
Autenticación y usuarios
Categorías
Emprendimientos
Publicaciones y catálogo
Pedidos
```

Cada módulo debe trabajar en una rama propia.

Ejemplos:

```text
feature/fase2-auth-usuarios
feature/fase2-categorias
feature/fase2-emprendimientos
feature/fase2-publicaciones
feature/fase2-pedidos
```

No utilizar una sola rama para desarrollar toda la Fase 2.

---

# 17. Archivos compartidos

Los siguientes archivos requieren especial coordinación:

```text
pom.xml
database/schema.sql
database/seed.sql
README.md
AGENTS.md
docs/*
interfaces DAO
interfaces Service
modelos
enums
persistence.xml
```

Si dos ramas necesitan modificar el mismo archivo compartido, debe reducirse al mínimo el cambio para evitar conflictos.

---

# 18. Sincronizar una rama con develop

Si `develop` recibe cambios mientras se trabaja en otra rama, primero actualizar:

```bash
git switch develop
git pull origin develop
```

Después regresar a la rama:

```bash
git switch nombre-de-la-rama
```

La integración de los cambios recientes puede realizarse mediante el mecanismo acordado por el equipo.

Antes de resolver conflictos, revisar cuidadosamente ambos cambios.

No aceptar automáticamente una versión sin entender qué se está reemplazando.

---

# 19. Conflictos

Si ocurre un conflicto:

1. identificar los archivos afectados;
2. revisar ambos cambios;
3. conservar la funcionalidad necesaria;
4. eliminar los marcadores de conflicto;
5. compilar;
6. ejecutar pruebas;
7. realizar el commit de resolución.

Marcadores que nunca deben permanecer:

```text
<<<<<<<
=======
>>>>>>>
```

---

# 20. Reglas importantes

- No desarrollar directamente sobre `main`.
- Evitar desarrollar directamente sobre `develop`.
- Crear una rama por tarea.
- Actualizar `develop` antes de crear una rama.
- Los Pull Requests de trabajo deben apuntar a `develop`.
- No mezclar módulos no relacionados en un mismo PR.
- No subir `target/`.
- No subir `.idea/`.
- No subir `.env`.
- No subir credenciales.
- Ejecutar `mvn clean package` antes de integrar.
- Revisar la arquitectura antes de realizar cambios estructurales.
- Mantener commits descriptivos.

---

# 21. Fuente de verdad

Convenciones:

```text
docs/CONVENCIONES.md
```

Arquitectura:

```text
docs/ARQUITECTURA.md
```

Instrucciones para asistentes:

```text
AGENTS.md
```

Dependencias:

```text
pom.xml
```

Baseline tecnológica:

```text
docs/VERSIONS.md
```

---

# 22. Objetivo

El flujo Git debe permitir:

- desarrollo paralelo;
- cambios aislados;
- revisión antes de integración;
- historial comprensible;
- eliminación segura de ramas;
- protección de `main`;
- integración estable mediante `develop`.

La prioridad es evitar que una tarea incompleta o defectuosa afecte directamente la versión integrada del equipo.