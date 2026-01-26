# TODO CRUD List - Aplicación JavaFX con FXML

## Migración a JavaFX

La aplicación ha sido migrada de Swing a JavaFX utilizando **FXML** para la definición de la interfaz de usuario.

### Cambios Realizados

1. **Dependencias actualizadas en `pom.xml`**:
   - Agregadas dependencias de JavaFX (javafx-controls y javafx-fxml)
   - Agregado plugin de JavaFX Maven para ejecutar la aplicación

2. **Clases eliminadas (Swing)**:
   - `MainFrame.java` - Reemplazada por `MainViewController.java`
   - `TaskTableModel.java` - Funcionalidad integrada en `MainViewController.java`

3. **Archivos FXML creados**:
   - `src/main/resources/fxml/MainView.fxml` - Vista principal de la aplicación
   - `src/main/resources/fxml/TaskForm.fxml` - Formulario de tareas

4. **Controladores JavaFX creados**:
   - `MainViewController.java` - Controlador de la vista principal
   - `TaskFormController.java` - Controlador del formulario de tareas

5. **Clases actualizadas**:
   - `App.java` - Ahora extiende `javafx.application.Application` y carga FXML
   - `MainView.java` - Mantenida como versión alternativa sin FXML
   - `TaskFormDialog.java` - Versión sin FXML mantenida

6. **Recursos adicionales**:
   - `src/main/resources/styles/application.css` - Hoja de estilos CSS

### Arquitectura FXML

```
src/
├── main/
│   ├── java/
│   │   └── com/inforgonzalez/todo/crud/list/
│   │       ├── App.java                          (Application principal)
│   │       ├── model/                            (Modelos de datos)
│   │       ├── persistence/                      (Capa de persistencia)
│   │       ├── service/                          (Lógica de negocio)
│   │       └── ui/
│   │           ├── MainViewController.java       (Controlador principal)
│   │           ├── TaskFormController.java       (Controlador formulario)
│   │           ├── MainView.java                 (Versión sin FXML)
│   │           └── TaskFormDialog.java           (Versión sin FXML)
│   └── resources/
│       ├── fxml/
│       │   ├── MainView.fxml                     (Vista principal)
│       │   └── TaskForm.fxml                     (Formulario tareas)
│       └── styles/
│           └── application.css                   (Estilos CSS)
```

### Cómo Ejecutar

#### Opción 1: Desde Eclipse
1. Click derecho en el proyecto
2. Run As → Java Application
3. Seleccionar la clase `App`

#### Opción 2: Desde Maven (si está configurado)
```bash
mvn clean javafx:run
```

#### Opción 3: Ejecutar con exec plugin
```bash
mvn clean compile exec:java
```

### Requisitos

- Java 17 o superior
- JavaFX 21.0.1 (se descarga automáticamente con Maven)

### Estructura UI con FXML

La aplicación JavaFX utiliza FXML para separar la presentación de la lógica:

#### MainView.fxml
- ✅ Panel de filtros (búsqueda, estado, ordenación)
- ✅ Botones de acción (crear, editar, eliminar, etc.)
- ✅ TableView con columnas personalizadas
- ✅ Barra de estado con estadísticas

#### TaskForm.fxml
- ✅ Formulario de entrada de datos
- ✅ Validación de campos
- ✅ ComboBox para prioridad y estado

### Características JavaFX

- ✅ Diseño moderno con JavaFX
- ✅ Separación vista/controlador con FXML
- ✅ Diálogos nativos de JavaFX
- ✅ TableView con columnas personalizadas
- ✅ Binding de propiedades
- ✅ Estilos CSS personalizables
- ✅ Mejor rendimiento en gráficos
- ✅ Colores según estado y prioridad

### Ventajas de usar FXML

1. **Separación de responsabilidades**: La UI está en archivos XML separados
2. **Diseñadores visuales**: Se puede usar Scene Builder para diseñar la interfaz
3. **Mantenibilidad**: Más fácil modificar la UI sin tocar el código Java
4. **Reutilización**: Los componentes FXML se pueden reutilizar
5. **Claridad**: El código del controlador se enfoca solo en la lógica

### Scene Builder (Opcional)

Puedes usar [Gluon Scene Builder](https://gluonhq.com/products/scene-builder/) para editar visualmente los archivos FXML:

1. Descargar e instalar Scene Builder
2. Abrir los archivos `.fxml` con Scene Builder
3. Diseñar la interfaz arrastrando componentes
4. Guardar y ejecutar la aplicación

### Doble Implementación

La aplicación incluye **dos implementaciones**:

1. **Con FXML** (Recomendada):
   - `MainViewController.java` + `MainView.fxml`
   - `TaskFormController.java` + `TaskForm.fxml`

2. **Sin FXML** (Alternativa):
   - `MainView.java` (código Java puro)
   - `TaskFormDialog.java` (código Java puro)

Por defecto, `App.java` usa la versión FXML. Para cambiar a la versión sin FXML, modifica el método `start()` en `App.java`.

