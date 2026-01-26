# Guía de Uso - TODO CRUD List

## 📖 Tabla de Contenidos
1. [Inicio Rápido](#inicio-rápido)
2. [Gestión de Tareas](#gestión-de-tareas)
3. [Filtrado y Búsqueda](#filtrado-y-búsqueda)
4. [Ordenación](#ordenación)
5. [Atajos de Teclado](#atajos-de-teclado)
6. [Persistencia de Datos](#persistencia-de-datos)
7. [Solución de Problemas](#solución-de-problemas)

---

## 🚀 Inicio Rápido

### Ejecutar la Aplicación

#### Desde Eclipse
1. Importar el proyecto como proyecto Maven existente
2. Esperar a que Maven descargue las dependencias
3. Click derecho en `App.java` → Run As → Java Application

#### Desde Línea de Comandos
```bash
# Compilar
mvn clean compile

# Ejecutar
mvn exec:java

# O crear JAR ejecutable
mvn package
java -jar target/todo.crud.list-0.0.1-SNAPSHOT.jar
```

### Primera Ejecución
- La aplicación iniciará con una lista vacía
- Se creará automáticamente un archivo `tasks.json` al guardar la primera tarea
- Puedes importar el archivo de ejemplo `tasks.json.example` renombrándolo a `tasks.json`

---

## 📝 Gestión de Tareas

### Crear una Nueva Tarea

1. **Click en el botón "➕ Nueva Tarea"**
2. **Completar el formulario:**
   - **Título*** (obligatorio): Mínimo 3 caracteres
   - **Descripción**: Opcional, puede contener detalles adicionales
   - **Prioridad*** (obligatorio): Alta, Media o Baja
   - **Estado*** (obligatorio): Pendiente, En Progreso o Hecha

3. **Validaciones:**
   - El título no puede estar vacío
   - Debe tener al menos 3 caracteres
   - Los espacios al inicio y final se eliminan automáticamente

4. **Click en "Guardar"** o presiona **Enter**
5. **Click en "Cancelar"** o presiona **Esc** para descartar

### Editar una Tarea

**Opción 1: Doble Click**
- Hacer doble click sobre cualquier tarea en la tabla

**Opción 2: Botón Editar**
1. Seleccionar una tarea en la tabla
2. Click en el botón "✏️ Editar"

**En el formulario de edición:**
- Modificar los campos deseados
- El ID de la tarea se mantiene
- La fecha de actualización se actualiza automáticamente
- Click en "Guardar" para confirmar

### Eliminar una Tarea

1. Seleccionar una tarea en la tabla
2. Click en el botón "🗑️ Eliminar"
3. Confirmar la eliminación en el diálogo
4. La tarea se eliminará permanentemente

⚠️ **Importante**: La eliminación es permanente y no se puede deshacer.

### Cambiar Estado Rápidamente

1. Seleccionar una tarea
2. Click en el botón "🔄 Cambiar Estado"
3. El estado cambiará automáticamente siguiendo el flujo:
   - Pendiente → En Progreso → Hecha → Pendiente

Esta es la forma más rápida de actualizar el progreso de una tarea.

---

## 🔍 Filtrado y Búsqueda

### Búsqueda por Texto

**Campo de búsqueda** (zona superior izquierda):
- Escribe cualquier texto
- La búsqueda es **en tiempo real** (al escribir)
- **Case-insensitive** (no distingue mayúsculas/minúsculas)
- Busca en:
  - Título de la tarea
  - Descripción de la tarea

**Ejemplos:**
- Buscar "feature" → encontrará "Feature X", "nueva feature"
- Buscar "urgente" → encontrará tareas con "urgente" en título o descripción

### Filtro por Estado

**Selector de Estado**:
- **Todos**: Muestra todas las tareas (sin filtro)
- **Pendiente**: Solo tareas pendientes de iniciar
- **En Progreso**: Solo tareas en desarrollo
- **Hecha**: Solo tareas completadas

### Filtros Combinados

Puedes combinar la búsqueda por texto con el filtro por estado:
- **Ejemplo**: Buscar "API" + Estado "En Progreso"
- Resultado: Solo tareas en progreso que contengan "API"

### Limpiar Filtros

Click en **"Limpiar Filtros"** para:
- Borrar el texto de búsqueda
- Restablecer el filtro de estado a "Todos"
- Restablecer la ordenación a "Prioridad"

---

## 📊 Ordenación

### Criterios de Ordenación

**Selector "Ordenar por"** (zona superior):

1. **Prioridad** (por defecto)
   - Alta → Media → Baja
   - Ordenación secundaria por fecha de creación

2. **Estado**
   - Pendiente → En Progreso → Hecha
   - Ordenación secundaria por fecha de creación

3. **Título**
   - Orden alfabético (A-Z)
   - No distingue mayúsculas/minúsculas

4. **Fecha**
   - Más recientes primero
   - Basado en fecha de creación

### Ordenación con Filtros

La ordenación se aplica **después** de los filtros:
1. Primero se filtran las tareas (búsqueda + estado)
2. Luego se ordenan según el criterio seleccionado

---

## ⌨️ Atajos de Teclado

### En el Formulario de Tarea

| Atajo | Acción |
|-------|--------|
| `Enter` | Guardar tarea |
| `Esc` | Cancelar y cerrar |
| `Tab` | Navegar entre campos |

### En la Tabla

| Acción | Método |
|--------|--------|
| Editar | Doble click en la tarea |
| Seleccionar | Click simple |

---

## 💾 Persistencia de Datos

### Archivo JSON

**Ubicación**: `tasks.json` (en el directorio de la aplicación)

**Formato**:
```json
{
  "version": 1,
  "tasks": [
    {
      "id": "uuid-único",
      "titulo": "Título de la tarea",
      "descripcion": "Descripción opcional",
      "prioridad": "ALTA|MEDIA|BAJA",
      "estado": "PENDIENTE|EN_PROGRESO|HECHA",
      "createdAt": "2026-01-23T18:40:00Z",
      "updatedAt": "2026-01-23T18:40:00Z"
    }
  ]
}
```

### Guardado Automático

- **Guardado inmediato** al:
  - Crear una tarea
  - Editar una tarea
  - Eliminar una tarea
  - Cambiar el estado

- **Guardado robusto**:
  - Se usa un archivo temporal durante el guardado
  - Se crea un backup antes de sobrescribir
  - Guardado atómico para prevenir corrupción

### Carga Automática

- Al iniciar la aplicación, se carga `tasks.json`
- Si el archivo no existe, se inicia con lista vacía (sin error)
- Si el archivo está corrupto, se intenta restaurar desde backup

### Archivos de Respaldo

| Archivo | Propósito |
|---------|-----------|
| `tasks.json` | Archivo principal |
| `tasks.json.backup` | Backup automático |
| `tasks.json.tmp` | Temporal durante guardado |
| `tasks.json.corrupted` | Copia del archivo corrupto (si ocurre error) |

---

## 🔧 Solución de Problemas

### Archivo JSON Corrupto

**Síntomas:**
- Mensaje de error al iniciar
- Lista vacía cuando debería tener datos

**Solución automática:**
1. La aplicación intenta restaurar desde `tasks.json.backup`
2. Si tiene éxito, muestra mensaje de confirmación
3. Si falla, guarda el archivo corrupto como `tasks.json.corrupted`

**Solución manual:**
1. Cerrar la aplicación
2. Revisar `tasks.json.backup` (debería tener la última versión válida)
3. Copiar `tasks.json.backup` → `tasks.json`
4. Reiniciar la aplicación

### La Aplicación No Guarda

**Verificar:**
- Permisos de escritura en el directorio
- Espacio disponible en disco
- Revisar la consola para mensajes de error

### No Aparecen las Tareas

**Verificar:**
1. **Filtros activos**: Click en "Limpiar Filtros"
2. **Archivo JSON**: Verificar que `tasks.json` existe y contiene datos
3. **Formato**: Validar el JSON con un editor o validador online

### La Búsqueda No Funciona

**Verificar:**
- La búsqueda es por coincidencia parcial
- Es case-insensitive
- Busca en título Y descripción
- Prueba con un texto más corto o diferente

### Errores de Validación

**"El título es obligatorio"**
- El campo título no puede estar vacío

**"El título debe tener al menos 3 caracteres"**
- Escribir un título más largo

---

## 📈 Barra de Estado

En la parte inferior de la ventana se muestra:

```
Total: 10 | Pendientes: 5 | En Progreso: 3 | Completadas: 2 | Mostrando: 8 tareas
```

- **Total**: Cantidad total de tareas en el sistema
- **Pendientes**: Tareas con estado "Pendiente"
- **En Progreso**: Tareas con estado "En Progreso"
- **Completadas**: Tareas con estado "Hecha"
- **Mostrando**: Cantidad de tareas visibles después de aplicar filtros

---

## 🎨 Códigos de Color

Las tareas se colorean según su estado:

| Estado | Color de Fondo |
|--------|----------------|
| Pendiente | 🟠 Naranja claro |
| En Progreso | 🔵 Azul claro |
| Hecha | 🟢 Verde claro |

**Prioridad Alta**: Se muestra en **negrita**

---

## 💡 Consejos y Mejores Prácticas

1. **Usa prioridades de forma consistente**
   - Alta: Tareas urgentes o críticas
   - Media: Tareas importantes pero no urgentes
   - Baja: Tareas que pueden esperar

2. **Mantén descripciones claras**
   - Añade contexto útil en la descripción
   - Incluye enlaces o referencias si es necesario

3. **Actualiza el estado regularmente**
   - Usa el botón de cambio rápido de estado
   - Mantén sincronizado el estado con el progreso real

4. **Aprovecha los filtros**
   - Usa "En Progreso" para ver tu trabajo actual
   - Usa "Pendiente" para planificar próximas tareas
   - Filtra por texto para encontrar tareas específicas

5. **Haz backups periódicos**
   - Copia `tasks.json` a un lugar seguro
   - Especialmente antes de hacer cambios masivos

---

## 📞 Soporte

Para reportar problemas o sugerencias:
- **Canal**: YouTube @inforgonzalez
- **Repositorio**: (si aplica)

---

**Versión de la Guía**: 1.0.0  
**Última actualización**: Enero 2026
