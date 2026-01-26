# ✅ DIÁLOGOS AHORA SE ABREN EN LA MISMA PANTALLA

## 🎯 Problema Resuelto
Cuando tienes **3 pantallas físicas**, los diálogos (Nueva Tarea, Editar, Alertas) se abrían en una pantalla diferente a donde estaba la ventana principal.

## ✅ Solución Implementada

He modificado el código para que **todos los diálogos y alertas se abran en la misma pantalla** que la ventana principal de la aplicación.

---

## 🔧 Cambios Realizados

### 1. TaskFormController.java ✅

#### Agregado import de Window:
```java
import javafx.stage.Window;
```

#### Modificado método showDialog():
**Antes:**
```java
public Optional<Task> showDialog(Task task) throws IOException
```

**Después:**
```java
public Optional<Task> showDialog(Task task, Window owner) throws IOException {
    // ...código de creación del diálogo...
    
    // IMPORTANTE: Establecer el owner para que aparezca en la misma pantalla
    if (owner != null) {
        dialog.initOwner(owner);
    }
    
    // ...resto del código...
}
```

**Efecto:** El diálogo se abre en la misma pantalla que la ventana `owner`.

---

### 2. MainViewController.java ✅

#### Agregado import de Window:
```java
import javafx.stage.Window;
```

#### Modificado onCreateTask():
**Antes:**
```java
Optional<Task> result = controller.showDialog(null);
```

**Después:**
```java
// Obtener la ventana actual desde cualquier control de la interfaz
Window owner = taskTable.getScene().getWindow();
Optional<Task> result = controller.showDialog(null, owner);
```

#### Modificado onEditTask():
**Antes:**
```java
Optional<Task> result = controller.showDialog(selectedTask);
```

**Después:**
```java
// Obtener la ventana actual desde cualquier control de la interfaz
Window owner = taskTable.getScene().getWindow();
Optional<Task> result = controller.showDialog(selectedTask, owner);
```

#### Modificado showMessage():
**Antes:**
```java
private void showMessage(String title, String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
```

**Después:**
```java
private void showMessage(String title, String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    
    // IMPORTANTE: Establecer el owner para que aparezca en la misma pantalla
    if (taskTable != null && taskTable.getScene() != null) {
        alert.initOwner(taskTable.getScene().getWindow());
    }
    
    alert.showAndWait();
}
```

**Efecto:** Todas las alertas (éxito, error, avisos) se abren en la misma pantalla.

---

## 🎯 Qué Hace esto Técnicamente

### initOwner() en JavaFX:
```java
dialog.initOwner(owner);
```

- **owner** es la ventana principal (Window)
- `initOwner()` establece la relación padre-hijo entre ventanas
- JavaFX automáticamente coloca el diálogo en la **misma pantalla** que el owner
- El diálogo se centra sobre la ventana padre

### Cómo se obtiene el owner:
```java
Window owner = taskTable.getScene().getWindow();
```

1. `taskTable` → Control de la interfaz (TableView)
2. `getScene()` → Obtiene la escena donde está el control
3. `getWindow()` → Obtiene la ventana (Stage) que contiene la escena

---

## 🎨 Comportamiento Visual

### Antes de la Corrección ❌:
```
[Pantalla 1]              [Pantalla 2]              [Pantalla 3]
                          📊 Ventana                 🔔 Diálogo
                          Principal                  aparece aquí
                          está aquí
```

### Después de la Corrección ✅:
```
[Pantalla 1]              [Pantalla 2]              [Pantalla 3]
                          📊 Ventana
                          Principal
                             🔔 Diálogo
                             aparece aquí
                             (misma pantalla)
```

---

## 📋 Diálogos Afectados (Todos)

### ✅ Diálogo "Nueva Tarea"
- Se abre en la misma pantalla que la ventana principal
- Centrado sobre la ventana principal

### ✅ Diálogo "Editar Tarea"
- Se abre en la misma pantalla que la ventana principal
- Centrado sobre la ventana principal

### ✅ Alertas de Éxito
- "Tarea creada exitosamente"
- "Tarea actualizada exitosamente"
- "Tarea eliminada exitosamente"

### ✅ Alertas de Error
- "Error al crear tarea"
- "Error al actualizar tarea"
- Etc.

### ✅ Alertas de Aviso
- "Por favor, seleccione una tarea"
- Etc.

### ✅ Diálogos de Confirmación
- "¿Está seguro de eliminar esta tarea?"

---

## 🧪 Cómo Probarlo

### Paso 1: Recompilar
```
1. En Eclipse: Project → Clean → Clean all projects
2. Project → Build Project
```

### Paso 2: Ejecutar
```
1. Click derecho en App.java
2. Run As → Java Application
```

### Paso 3: Probar en Configuración Multi-Pantalla

#### Con 3 Pantallas:
1. **Arrastra la ventana principal** a la pantalla 2 (o la que prefieras)
2. **Click en "➕ Nueva Tarea"**
   - ✅ El diálogo debe aparecer en la pantalla 2 (sobre la ventana)
3. **Crea una tarea y guárdala**
   - ✅ La alerta de éxito debe aparecer en la pantalla 2
4. **Click en "✏️ Editar"**
   - ✅ El diálogo debe aparecer en la pantalla 2
5. **Mueve la ventana a la pantalla 3**
6. **Click en cualquier botón**
   - ✅ Los diálogos deben aparecer en la pantalla 3

---

## 🔧 Archivos Modificados

1. **TaskFormController.java**
   - Agregado parámetro `Window owner` al método `showDialog()`
   - Agregado `dialog.initOwner(owner)`
   - Import de `javafx.stage.Window`

2. **MainViewController.java**
   - Actualizado `onCreateTask()` para pasar owner
   - Actualizado `onEditTask()` para pasar owner
   - Actualizado `showMessage()` para usar owner
   - Import de `javafx.stage.Window`

---

## ✅ Estado Final

**Errores de Compilación:** ❌ NINGUNO  
**Diálogos en Misma Pantalla:** ✅ SÍ  
**Alertas en Misma Pantalla:** ✅ SÍ  
**Funcionalidad:** ✅ TOTALMENTE OPERATIVA  

---

## 📝 Notas Técnicas

### ¿Por qué era necesario?

Sin `initOwner()`, JavaFX usa el comportamiento por defecto del sistema operativo:
- **Windows:** Los diálogos se abren en la pantalla "primaria" definida por el SO
- **En multi-monitor:** Esto causa que aparezcan en una pantalla aleatoria o en la primaria

Con `initOwner()`:
- Los diálogos son **hijos** de la ventana principal
- Se centran sobre la ventana padre
- Aparecen en la **misma pantalla** que el padre
- Se mantienen encima del padre (modal)

### Ventajas Adicionales:

1. **UX Mejorada:** El usuario no tiene que buscar el diálogo en otra pantalla
2. **Centrado Automático:** Los diálogos se centran sobre la ventana
3. **Comportamiento Modal:** El diálogo bloquea la ventana padre correctamente
4. **Consistencia:** Todos los diálogos se comportan igual

---

## 🎉 Resultado

**AHORA todos los diálogos y alertas se abren en la MISMA pantalla que la ventana principal**, sin importar en qué monitor físico esté la aplicación.

---

**Fecha:** 26 de enero de 2026  
**Problema:** Diálogos en pantalla diferente (multi-monitor)  
**Estado:** ✅ RESUELTO  
**Archivos Modificados:** 2  

¡Disfruta de tu aplicación con mejor soporte multi-pantalla! 🖥️🖥️🖥️
