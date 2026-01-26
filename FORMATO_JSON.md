# Formato del archivo JSON de datos (tasks.json)

Este documento describe con detalle la estructura y significado de cada campo que puede contener el archivo de datos en formato JSON usado por la aplicación (por defecto `tasks.json`).

## Estructura General

El archivo es un objeto JSON con dos claves de nivel superior:

```json
{
  "version": 1,
  "tasks": [ /* arreglo de tareas */ ]
}
```

- version: número entero que indica la versión del formato de datos.
- tasks: arreglo de objetos de tarea (cada uno sigue el esquema `TaskDTO`).

## Campos de Nivel Superior

- version: entero. Obligatorio. Actualmente `1`. Permite compatibilidad futura en migraciones del formato.
- tasks: arreglo de objetos. Obligatorio. Si está vacío, la aplicación inicia sin tareas.

## Esquema de una Tarea (TaskDTO)

Cada elemento dentro de `tasks` representa una tarea con los siguientes campos:

- id: cadena. Obligatorio. Identificador único (UUID). Ejemplo: "c2b6f7c8-1b2c-4b0a-9a5e-6b6d2f6f3b0a".
- titulo: cadena. Obligatorio. Mínimo 3 caracteres.
- descripcion: cadena. Opcional. Texto libre descriptivo.
- prioridad: cadena (enum). Obligatorio. Valores permitidos:
  - "BAJA"
  - "MEDIA"
  - "ALTA"
- estado: cadena (enum). Obligatorio. Valores permitidos:
  - "PENDIENTE"
  - "EN_PROGRESO"
  - "HECHA"
- createdAt: cadena. Obligatorio. Fecha/hora en formato ISO-8601 UTC. Ejemplo: "2026-01-23T18:40:00Z".
- updatedAt: cadena. Obligatorio. Fecha/hora en formato ISO-8601 UTC.

Campos adicionales (opcionales) para enriquecer la tarea:

- horasEstimadas: número (double). Opcional. Si falta, se interpreta como `0.0`.
- duracionTarea: cadena (enum). Opcional. Si falta o es inválido, se usa `"PUNTUAL"`.
  - Valores: "PUNTUAL", "LARGO".
- tipoTarea: cadena (enum). Opcional. Si falta o es inválido, se usa `"OTRO"`.
  - Valores: "STREAMING", "VIDEO_YOUTUBE", "SHORT_YOUTUBE", "REEL_INSTAGRAM", "POST_REDES", "OTRO".
- factibilidad: cadena (enum). Opcional. Si falta o es inválido, se usa `"NO_SABE"`.
  - Valores: "BAJO", "MEDIO", "ALTO", "NO_SABE".

## Reglas de Validación Importantes

- `titulo`, `prioridad` y `estado` son obligatorios; si faltan o son inválidos, la tarea se ignora.
- `titulo` debe tener al menos 3 caracteres.
- `createdAt` y `updatedAt` deben ser fechas válidas ISO-8601 (UTC), por ejemplo `YYYY-MM-DDTHH:mm:ssZ`.
- Las enumeraciones se almacenan como cadenas en mayúsculas (tal cual los nombres del enum).
- Los campos opcionales (nuevos) admiten ausencia y tienen valores por defecto (ver arriba).

## Ejemplo Mínimo Válido

```json
{
  "version": 1,
  "tasks": [
    {
      "id": "c2b6f7c8-1b2c-4b0a-9a5e-6b6d2f6f3b0a",
      "titulo": "Preparar miniatura",
      "descripcion": "Crear miniatura con buen contraste",
      "prioridad": "ALTA",
      "estado": "PENDIENTE",
      "createdAt": "2026-01-23T18:40:00Z",
      "updatedAt": "2026-01-23T18:40:00Z"
    }
  ]
}
```

## Ejemplo Completo con Campos Opcionales

```json
{
  "version": 1,
  "tasks": [
    {
      "id": "a1111111-2222-3333-4444-555555555555",
      "titulo": "Documentar API REST",
      "descripcion": "Completar la documentación de los endpoints",
      "prioridad": "MEDIA",
      "estado": "EN_PROGRESO",
      "createdAt": "2026-01-22T14:20:00Z",
      "updatedAt": "2026-01-23T16:45:00Z",
      "horasEstimadas": 3.5,
      "duracionTarea": "LARGO",
      "tipoTarea": "POST_REDES",
      "factibilidad": "ALTO"
    }
  ]
}
```

## Ubicación y Gestión del Archivo

- Archivo principal: `tasks.json` (en el directorio de ejecución de la app).
- Ejemplo de referencia: `tasks.json.example` (incluye una muestra del formato).
- Backup automático: `tasks.json.backup` (se crea/actualiza antes de escribir cambios).
- Escritura atómica: se usa un archivo temporal `tasks.json.tmp` y luego se reemplaza el principal.

## Compatibilidad y Migraciones

- La clave `version` permite evolucionar el formato. Actualmente es `1`.
- Si faltan los campos opcionales, la aplicación aplica valores por defecto al cargar.
- Si el JSON está corrupto, la aplicación intenta restaurar desde `tasks.json.backup` y mantiene una copia `tasks.json.corrupted` para diagnóstico.

## Notas para Integraciones Externas

- Las fechas deben ser UTC y formateadas como ISO-8601 con sufijo `Z`.
- Los valores de `prioridad`, `estado`, `duracionTarea`, `tipoTarea` y `factibilidad` deben coincidir exactamente con los nombres listados (sensible a mayúsculas).
- Evita agregar campos no listados; la app ignorará claves desconocidas, pero no las usará.
