# Guía Rápida - JavaFX con FXML

## ✅ Archivos Creados

### FXML (Vistas XML)
- `src/main/resources/fxml/MainView.fxml` - Vista principal
- `src/main/resources/fxml/TaskForm.fxml` - Formulario de tareas

### Controladores
- `src/main/java/.../ui/MainViewController.java` - Controlador vista principal
- `src/main/java/.../ui/TaskFormController.java` - Controlador formulario

### Estilos
- `src/main/resources/styles/application.css` - Estilos CSS

### Clases mantenidas (alternativa sin FXML)
- `MainView.java` - Vista sin FXML (código Java puro)
- `TaskFormDialog.java` - Diálogo sin FXML

## 🚀 Cómo Ejecutar

### En Eclipse:
1. Click derecho en `App.java`
2. Run As → Java Application

### Problema con JavaFX?
Si Eclipse no encuentra JavaFX:
1. Click derecho en el proyecto
2. Maven → Update Project
3. Marcar "Force Update of Snapshots/Releases"
4. OK

## 📁 Estructura del Proyecto

```
src/main/
├── java/
│   └── com/inforgonzalez/todo/crud/list/
│       ├── App.java                    ← Punto de entrada
│       ├── model/                      ← Task, Priority, TaskStatus
│       ├── persistence/                ← JsonTaskRepository
│       ├── service/                    ← TaskService
│       └── ui/
│           ├── MainViewController.java ← Controlador FXML
│           ├── TaskFormController.java ← Controlador formulario
│           ├── MainView.java           ← Alternativa sin FXML
│           └── TaskFormDialog.java     ← Alternativa sin FXML
└── resources/
    ├── fxml/
    │   ├── MainView.fxml              ← UI principal (XML)
    │   └── TaskForm.fxml              ← UI formulario (XML)
    └── styles/
        └── application.css            ← Estilos

```

## 🎨 Editar la Interfaz

### Opción 1: Editar FXML directamente
Los archivos `.fxml` son XML legibles. Puedes editarlos con cualquier editor de texto.

### Opción 2: Scene Builder (Visual)
1. Descargar: https://gluonhq.com/products/scene-builder/
2. Abrir archivos `.fxml` con Scene Builder
3. Arrastrar y soltar componentes
4. Guardar y ejecutar

## 🔄 Cambiar entre versiones

### Usar versión FXML (actual):
`App.java` ya está configurado para cargar FXML.

### Usar versión sin FXML:
En `App.java`, reemplazar:
```java
FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
Parent root = loader.load();
MainViewController controller = loader.getController();
controller.setTaskService(taskService);
```

Por:
```java
MainView mainView = new MainView(taskService);
```

Y cambiar:
```java
Scene scene = new Scene(root, 1000, 600);
```

Por:
```java
Scene scene = new Scene(mainView, 1000, 600);
```

## 📝 Características

- ✅ Tabla de tareas con colores por estado
- ✅ Filtros y búsqueda en tiempo real
- ✅ Ordenación por varios criterios
- ✅ CRUD completo (Crear, Leer, Actualizar, Eliminar)
- ✅ Validación de formularios
- ✅ Estadísticas en barra de estado
- ✅ Doble clic para editar
- ✅ Confirmación de eliminación
- ✅ Persistencia en JSON

## ⚡ Comandos Maven

```bash
# Ejecutar aplicación
mvn clean javafx:run

# Compilar proyecto
mvn clean compile

# Ejecutar tests
mvn test

# Empaquetar JAR
mvn package
```

## 🎯 Ventajas FXML

1. **Separación**: UI en XML, lógica en Java
2. **Scene Builder**: Editor visual drag & drop
3. **Mantenibilidad**: Cambios UI sin tocar código
4. **Legibilidad**: Estructura clara y jerárquica
5. **Reutilización**: Componentes modulares

---

¡Aplicación lista para usar! 🎉
