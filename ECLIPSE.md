# TODO CRUD List - Instrucciones para Eclipse

## 🚀 Ejecutar la Aplicación en Eclipse

### Paso 1: Importar el Proyecto
1. **File** → **Import** → **Maven** → **Existing Maven Projects**
2. Seleccionar la carpeta del proyecto: `todo.crud.list`
3. Click en **Finish**
4. Eclipse descargará automáticamente las dependencias (Gson)

### Paso 2: Ejecutar la Aplicación
1. En el **Package Explorer**, navegar a:
   ```
   src/main/java → com.inforgonzalez.todo.crud.list → App.java
   ```
2. **Click derecho** en `App.java`
3. **Run As** → **Java Application**
4. ¡La aplicación se iniciará con interfaz gráfica!

### Paso 3: Ejecutar Tests (Opcional)
1. Click derecho en el proyecto `todo.crud.list`
2. **Run As** → **JUnit Test**
3. Eclipse ejecutará todos los tests automáticamente

## 📝 Estructura del Proyecto

```
src/main/java/com/inforgonzalez/todo/crud/list/
├── App.java                    # ⭐ PUNTO DE ENTRADA - Ejecutar este archivo
├── model/
│   ├── Task.java              # Entidad tarea
│   ├── Priority.java          # Enum prioridad (ALTA/MEDIA/BAJA)
│   └── TaskStatus.java        # Enum estado (PENDIENTE/EN_PROGRESO/HECHA)
├── persistence/
│   ├── TaskRepository.java    # Interfaz repositorio
│   ├── JsonTaskRepository.java # Implementación JSON
│   ├── TaskData.java          # Wrapper para JSON
│   └── TaskDTO.java           # Data Transfer Object
├── service/
│   └── TaskService.java       # Lógica de negocio
└── ui/
    ├── MainFrame.java         # Ventana principal
    ├── TaskTableModel.java    # Modelo de tabla
    └── TaskFormDialog.java    # Formulario crear/editar

src/test/java/com/inforgonzalez/todo/crud/list/
├── model/
│   └── TaskTest.java          # Tests del modelo
└── service/
    └── TaskServiceTest.java   # Tests del servicio
```

## ⚡ Atajos de Eclipse Útiles

| Atajo | Acción |
|-------|--------|
| `Ctrl + F11` | Ejecutar última aplicación |
| `Ctrl + Shift + O` | Organizar imports |
| `Ctrl + Shift + F` | Formatear código |
| `Alt + Shift + X, J` | Ejecutar como Java Application |
| `Alt + Shift + X, T` | Ejecutar como JUnit Test |

## 🎯 Primera Ejecución

1. Ejecutar `App.java`
2. Se abrirá la ventana principal
3. Click en **"➕ Nueva Tarea"**
4. Crear tu primera tarea
5. Se guardará automáticamente en `tasks.json`

## 📦 Archivo de Datos

- **Ubicación**: Raíz del proyecto → `tasks.json`
- **Creación**: Automática al guardar la primera tarea
- **Ejemplo**: Ver `tasks.json.example` para formato

## 🔧 Si Eclipse No Reconoce las Dependencias

1. Click derecho en el proyecto
2. **Maven** → **Update Project**
3. Marcar **Force Update of Snapshots/Releases**
4. Click **OK**

## ✅ Verificar que Todo Funciona

### Compilación
- No debe haber errores rojos en el Package Explorer
- Las clases deben compilar sin problemas

### Ejecución
- Al ejecutar `App.java`, debe abrirse una ventana gráfica
- La consola debe mostrar: "TODO CRUD List - Iniciada exitosamente ✓"

### Tests
- Al ejecutar los tests, deben pasar todos (verde en JUnit view)

## 🎨 Características de la Aplicación

✨ **CRUD Completo**: Crear, Leer, Actualizar, Eliminar tareas  
🔍 **Búsqueda en Tiempo Real**: Filtra mientras escribes  
📊 **Filtros**: Por estado (Pendiente/En Progreso/Hecha)  
🔄 **Cambio Rápido de Estado**: Un click para avanzar  
💾 **Persistencia Automática**: Guarda al crear/editar/eliminar  
🎯 **Ordenación**: Por prioridad, estado, título o fecha  
🎨 **Interfaz Intuitiva**: Colores por estado, negrita para alta prioridad  

## 📚 Documentación

- **README.md** - Documentación completa del proyecto
- **GUIA_USO.md** - Guía de usuario detallada
- **CHANGELOG.md** - Historial de cambios y versiones

## 🐛 Solución de Problemas

### "Class not found" o errores de import
- **Solución**: Maven → Update Project → Force Update

### La ventana no se abre
- **Verificar**: Consola de Eclipse para mensajes de error
- **Solución**: Revisar que Java 17 esté configurado

### No guarda las tareas
- **Verificar**: Permisos de escritura en la carpeta del proyecto
- **Ver**: Consola para mensajes de error de guardado

---

**¡Listo para usar!** 🚀

Ejecuta `App.java` y comienza a gestionar tus tareas.
