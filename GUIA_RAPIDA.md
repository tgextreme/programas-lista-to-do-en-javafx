# 🚀 Guía Rápida de Uso - TODO CRUD List

## ✨ ¡Tu aplicación está 100% completa!

---

## 📋 ¿Qué tiene tu aplicación?

### ✅ CRUD Completo
- ✅ **Crear** tareas nuevas
- ✅ **Ver** todas las tareas en una tabla
- ✅ **Editar** tareas existentes
- ✅ **Eliminar** tareas con confirmación

### ✅ Funcionalidades Extra
- ✅ **Filtrar** por texto (busca en título y descripción)
- ✅ **Filtrar** por estado (Pendiente, En Progreso, Hecha)
- ✅ **Ordenar** por Prioridad, Estado, Título o Fecha
- ✅ **Cambiar estado rápido** con un botón
- ✅ **Doble clic** para editar
- ✅ **Colores** según estado y prioridad
- ✅ **Estadísticas** en tiempo real
- ✅ **Guardado automático** en JSON

---

## 🎮 Cómo Usar la Aplicación

### 1️⃣ Ejecutar la Aplicación

**⭐ Opción A: Doble Click en launch.vbs (RECOMENDADO)**
```
1. Ve a la carpeta del proyecto: todo.crud.list
2. Busca el archivo "launch.vbs"
3. Doble click en "launch.vbs"
4. ¡La aplicación se abre SIN ventana CMD molesta!

💡 VENTAJA: No aparece ventana CMD negra
```

**Crear Acceso Directo en Escritorio:**
```
1. Ejecuta "crear_acceso_directo.bat"
2. Se creará un acceso directo en tu Escritorio
3. Úsalo para abrir la aplicación rápidamente
```

**Opción B: Desde Eclipse**
```
1. Abre Eclipse
2. Ve al proyecto "todo.crud.list"
3. Navega a: src/main/java/com/inforgonzalez/todo/crud/list/App.java
4. Click derecho en el archivo
5. Selecciona: "Run As → Java Application"
6. ¡La ventana se abrirá!
```

**Opción C: Desde la terminal (si Maven está instalado)**
```bash
cd "C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list"
mvn javafx:run
```

**Opción D: JAR compilado (sin ventana CMD)**
```bash
cd "C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list"
javaw -jar target/todo.crud.list-0.0.1-SNAPSHOT.jar
```

---

### 2️⃣ Crear una Tarea Nueva

1. Haz clic en el botón **"➕ Nueva Tarea"**
2. Se abrirá un formulario
3. Completa los campos:
   - **Título*** (obligatorio, mínimo 3 caracteres)
   - **Descripción** (opcional)
   - **Prioridad*** (Alta, Media, Baja)
   - **Estado*** (Pendiente, En Progreso, Hecha)
4. Haz clic en **"Guardar"**
5. ¡La tarea aparecerá en la tabla!

**💡 Tip**: El botón "Guardar" se habilita automáticamente cuando el título tiene al menos 3 caracteres.

---

### 3️⃣ Ver Tareas

**Todas tus tareas se muestran en la tabla central:**
- **Título**: El nombre de la tarea
- **Prioridad**: Alta, Media o Baja
- **Estado**: Pendiente, En Progreso o Hecha
- **Creado**: Fecha y hora de creación
- **Actualizado**: Fecha y hora de última modificación

**🎨 Colores:**
- 🟠 **Naranja**: Tareas Pendientes
- 🔵 **Azul**: Tareas En Progreso
- 🟢 **Verde**: Tareas Hechas
- **Negrita**: Tareas de Alta Prioridad

---

### 4️⃣ Editar una Tarea

**Opción A: Doble Clic**
1. Haz **doble clic** en cualquier tarea de la tabla
2. Se abrirá el formulario de edición
3. Modifica los campos que desees
4. Haz clic en **"Guardar"**

**Opción B: Botón Editar**
1. Selecciona una tarea haciendo **un clic** en ella
2. Haz clic en el botón **"✏️ Editar"**
3. Modifica los campos
4. Haz clic en **"Guardar"**

**💡 Tip**: La fecha de actualización se modifica automáticamente.

---

### 5️⃣ Eliminar una Tarea

1. Selecciona una tarea haciendo clic en ella
2. Haz clic en el botón **"🗑️ Eliminar"**
3. Aparecerá un diálogo de confirmación
4. Confirma haciendo clic en **"OK"**
5. ¡La tarea se eliminará!

**⚠️ Advertencia**: Esta acción no se puede deshacer.

---

### 6️⃣ Filtrar Tareas

#### 🔍 Filtrar por Texto
1. Escribe en el campo **"Buscar"**
2. La tabla se filtra **automáticamente** en tiempo real
3. Busca en el título y la descripción
4. No distingue mayúsculas/minúsculas

**Ejemplo**: Escribe "importante" para ver solo tareas que contengan esa palabra.

#### 📊 Filtrar por Estado
1. Abre el selector **"Estado"**
2. Selecciona:
   - **Todos**: Muestra todas las tareas
   - **Pendiente**: Solo tareas pendientes
   - **En Progreso**: Solo tareas en progreso
   - **Hecha**: Solo tareas completadas
3. El filtro se aplica instantáneamente

**💡 Tip**: Puedes combinar el filtro de texto con el filtro de estado.

#### 🧹 Limpiar Filtros
- Haz clic en el botón **"Limpiar Filtros"** para resetear todo

---

### 7️⃣ Ordenar Tareas

1. Abre el selector **"Ordenar por"**
2. Selecciona el criterio:
   - **Prioridad**: Alta → Media → Baja (y luego por fecha)
   - **Estado**: Pendiente → En Progreso → Hecha
   - **Título**: Alfabético (A-Z)
   - **Fecha**: Más recientes primero
3. La tabla se reordena automáticamente

---

### 8️⃣ Cambiar Estado Rápido

**Para cambiar el estado de una tarea sin abrir el formulario:**

1. Selecciona una tarea
2. Haz clic en el botón **"🔄 Cambiar Estado"**
3. El estado cambia siguiendo este flujo:
   - Pendiente → En Progreso
   - En Progreso → Hecha
   - Hecha → Pendiente (vuelve al inicio)

**💡 Tip**: Es la forma más rápida de marcar una tarea como completada.

---

### 9️⃣ Recargar Tareas

- Haz clic en el botón **"🔃 Recargar"** para actualizar la lista desde el archivo JSON
- Útil si otro proceso modificó el archivo `tasks.json`

---

## 📊 Barra de Estado

**En la parte inferior de la ventana verás:**
```
Total: X | Pendientes: Y | En Progreso: Z | Completadas: W | Mostrando: N tareas
```

Esta barra se actualiza automáticamente cada vez que:
- Creas una tarea
- Editas una tarea
- Eliminas una tarea
- Cambias el estado
- Aplicas filtros

---

## 💾 Persistencia de Datos

### ¿Dónde se guardan las tareas?

Las tareas se guardan automáticamente en un archivo JSON:
```
C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\tasks.json
```

### ¿Cuándo se guardan?
- ✅ Al crear una tarea
- ✅ Al editar una tarea
- ✅ Al eliminar una tarea
- ✅ Al cambiar el estado

**No necesitas hacer nada manual, todo es automático.**

### Formato del archivo JSON
```json
{
  "version": 1,
  "tasks": [
    {
      "id": "c2b6f7c8-1b2c-4b0a-9a5e-6b6d2f6f3b0a",
      "titulo": "Mi tarea",
      "descripcion": "Descripción de la tarea",
      "prioridad": "ALTA",
      "estado": "PENDIENTE",
      "createdAt": "2026-01-23T18:40:00Z",
      "updatedAt": "2026-01-23T18:40:00Z"
    }
  ]
}
```

---

## 🎨 Personalización

### Modificar Colores

Edita el archivo: `src/main/resources/styles/application.css`

### Modificar la Interfaz

**Con FXML** (recomendado):
1. Abre: `src/main/resources/fxml/MainView.fxml`
2. Edita el XML o usa Scene Builder
3. Los cambios se reflejan al ejecutar

**Con código Java**:
1. Edita: `src/main/java/.../ui/MainViewController.java`
2. Modifica los métodos según necesites

---

## 🐛 Solución de Problemas

### La aplicación no arranca
**Problema**: Error al iniciar
**Solución**: 
1. Verifica que tienes Java 17 o superior
2. Click derecho en el proyecto → Maven → Update Project
3. Limpia el proyecto: Project → Clean

### No se ven mis tareas
**Problema**: La tabla está vacía
**Solución**:
1. Crea una tarea nueva con el botón "➕ Nueva Tarea"
2. Verifica los filtros (puede que esté filtrado)
3. Haz clic en "Limpiar Filtros"
4. Haz clic en "🔃 Recargar"

### Los cambios no se guardan
**Problema**: Al cerrar y abrir, no veo las tareas
**Solución**:
1. Verifica que el archivo `tasks.json` existe
2. Verifica permisos de escritura en la carpeta
3. Revisa la consola de Eclipse por errores

### Error en FXML
**Problema**: "Error loading FXML"
**Solución**:
1. Verifica que los archivos FXML están en `src/main/resources/fxml/`
2. Limpia el proyecto: Project → Clean
3. Maven → Update Project (Force Update)

---

## 📚 Atajos y Tips

### Atajos de Teclado
- **Enter** en el formulario: Guardar (si es válido)
- **Esc** en el formulario: Cancelar
- **Doble clic** en tarea: Editar

### Tips de Productividad
1. **Usa la búsqueda en tiempo real** para encontrar tareas rápido
2. **Doble clic para editar** es más rápido que seleccionar + botón
3. **Cambiar estado rápido** es ideal para marcar tareas completadas
4. **Combina filtros** (texto + estado) para búsquedas precisas
5. **Ordena por fecha** para ver las tareas más recientes

---

## 🎓 Aprende Más

### Documentación del Proyecto
- **README.md**: Documentación técnica completa
- **IMPLEMENTACION_COMPLETA.md**: Detalles de implementación
- **MIGRACION_JAVAFX.md**: Cómo se migró de Swing a JavaFX
- **GUIA_FXML.md**: Guía de uso de FXML
- **GUIA_USO.md**: Manual de usuario detallado

### Conceptos de JavaFX Utilizados
- **FXML**: Separación de vista y lógica
- **TableView**: Tablas de datos
- **Binding**: Actualización automática de datos
- **Listeners**: Eventos en tiempo real
- **CSS**: Personalización de estilos
- **Dialogs**: Ventanas modales

---

## ✅ Checklist de Uso Diario

### Al Empezar tu Día
- [ ] Abre la aplicación
- [ ] Revisa las tareas pendientes (filtro: Pendiente)
- [ ] Identifica las de alta prioridad (están en negrita)
- [ ] Cambia las tareas que vas a trabajar hoy a "En Progreso"

### Durante el Día
- [ ] Crea nuevas tareas según surjan
- [ ] Actualiza el estado de las tareas en progreso
- [ ] Marca como "Hecha" las tareas completadas
- [ ] Usa la búsqueda para encontrar tareas específicas

### Al Finalizar el Día
- [ ] Revisa las tareas "En Progreso"
- [ ] Marca las completadas como "Hecha"
- [ ] Planifica las tareas para mañana
- [ ] Ordena por prioridad para identificar pendientes críticos

---

## 🎉 ¡Disfruta de tu Aplicación!

**Tu aplicación de gestión de tareas está lista para usar.**

Características principales:
- ✅ CRUD completo
- ✅ Filtros avanzados
- ✅ Ordenación múltiple
- ✅ Guardado automático
- ✅ Interfaz moderna
- ✅ Fácil de usar

**¡Comienza a organizar tus tareas ahora mismo!**

---

**Desarrollado por**: InforGonzalez  
**YouTube**: [@inforgonzalez](https://youtube.com/@inforgonzalez)  
**Versión**: 1.0.0  
**Fecha**: Enero 2026
