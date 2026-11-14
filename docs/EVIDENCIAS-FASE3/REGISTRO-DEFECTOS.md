# Registro de defectos — QA Fase 3

## DEF-001 — Respuesta 404 HTML en endpoint REST (confirmado)

- **Fecha:** 14 de noviembre de 2026
- **Severidad sugerida:** Media — incompatibilidad del contrato de errores JSON; la respuesta HTTP 404 es correcta
- **Endpoints:** `GET /api/v1/emprendimientos/999999` y `GET /api/v1/publicaciones/999999`
- **Resultado esperado:** HTTP 404 con `Content-Type: application/json` y cuerpo estructurado de error (`status`, `error`, `message`) según `docs/API.md`
- **Resultado real:** HTTP 404, `Content-Type: text/html`, página `404 - Recurso no encontrado`
- **Reproducción:**

```powershell
curl.exe -i -H "Accept: application/json" http://localhost:8080/emprendelink/api/v1/emprendimientos/999999
```

- **Repetibilidad:** Confirmado en dos ejecuciones del recurso emprendimientos (`curl.exe -i` sin y con encabezado Accept) y en el recurso publicaciones (CP-204)
- **Responsables para revisión:** Integrante 1 (manejo compartido de errores REST) e Integrante 2 (recursos de catálogo)
- **Estado:** Abierto, pendiente de revisión/corrección
- **Evidencias:** `api/CP-202-emprendimiento-inexistente-404-html.png`, `api/CP-202-confirmacion-accept-json-404-html.png`, `api/CP-204-publicacion-inexistente-404-html.png`

El origen técnico exacto no se ha diagnosticado todavía.


## DEF-002 — Filtro `texto` no aplicado en catálogo de publicaciones (confirmado en API desplegada)

- **Fecha:** 14 de noviembre de 2026
- **Severidad sugerida:** Media — incumplimiento funcional del filtrado acordado
- **Endpoint:** `GET /api/v1/publicaciones?texto=cafe`
- **Resultado esperado:** HTTP 200 y resultados filtrados de acuerdo con `texto`; dado el contenido observado en el listado general, no deberían devolverse publicaciones sin coincidencia textual
- **Resultado real:** HTTP 200, `Content-Type: application/json`, mismos tres registros (`id`: 3, 2, 1) que la consulta sin filtros CP-203
- **Reproducción:**

```powershell
curl.exe -i -H "Accept: application/json" "http://localhost:8080/emprendelink/api/v1/publicaciones?texto=cafe"
```

- **Responsable para revisión:** Integrante 2 (API de catálogo); confirmar contra el contrato definitivo con Integrante 1
- **Estado:** Abierto, pendiente de diagnóstico y corrección
- **Evidencias:** `api/CP-203-publicaciones-200.png`, `api/CP-205-filtro-texto-ignorado-200.png`

La evidencia confirma la ausencia de filtrado observable, no determina la causa interna (parámetro ignorado, ruta desactualizada, regla distinta, etc.).
