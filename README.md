# TODO CRUD List - Aplicación de Gestión de Tareas

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.1-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Educational-green.svg)](LICENSE)

## 📋 Descripción
Aplicación completa de gestión de tareas (TODO) con persistencia en JSON, filtros avanzados y interfaz gráfica moderna en **JavaFX con FXML**.

### ✨ Características Destacadas
- 🎨 **Interfaz moderna** con JavaFX y FXML
- 💾 **Persistencia JSON** automática y robusta
- 🔍 **Filtros avanzados** por texto y estado en tiempo real
- 📊 **Ordenación múltiple** por prioridad, fecha, título y estado
- 🎯 **Validación** de formularios en tiempo real
- 🌈 **Colores visuales** según estado y prioridad
- 🏗️ **Arquitectura limpia** en capas (MVC)
- 🧪 **Tests unitarios** con JUnit 5
- 📱 **Responsive** y escalable
- 🎭 **Doble implementación**: Con FXML y sin FXML

## 📑 Tabla de Contenidos

- [Características Principales](#-características-principales)
  - [Requisitos Funcionales](#requisitos-funcionales-mvp)
  - [Requisitos No Funcionales](#requisitos-no-funcionales)
- [Arquitectura](#-arquitectura)
- [Modelo de Datos](#-modelo-de-datos)
- [Uso](#-uso)
- [Troubleshooting](#-troubleshooting)
- [Validaciones](#-validaciones)
- [Tecnologías](#-tecnologías)
- [Características de JavaFX](#-características-de-javafx)
- [Documentación Adicional](#-documentación-adicional)
- [Herramientas de Desarrollo](#-herramientas-de-desarrollo)
- [Comandos Maven Útiles](#-comandos-maven-útiles)
- [Contribuir](#-contribuir)
- [Próximas Mejoras](#-próximas-mejoras)
- [Checklist](#-checklist-definition-of-done)

## ✨ Características Principales

### Requisitos Funcionales (MVP)

#### RF-01. Crear tarea
- Campos obligatorios: título, prioridad, estado
- Campos opcionales: descripción, fechaCreacion, fechaActualizacion
- Validación de campos requeridos

#### RF-02. Listar tareas
- Vista en tabla con: título, prioridad, estado, fechas
- Mensaje cuando no hay tareas: "No hay tareas"
- Información clara y legible

#### RF-03. Editar tarea
- Modificar todos los campos de la tarea
- Mantener el mismo ID
- Actualizar fecha de modificación automáticamente

#### RF-04. Eliminar tarea
- Confirmación antes de eliminar
- Eliminación por ID

#### RF-05. Cambiar estado rápidamente
- Botón de acción rápida para cambiar estado
- Flujo: Pendiente → En progreso → Hecha

#### RF-06. Filtrar por texto
- Búsqueda en tiempo real
- Case-insensitive
- Busca en título y descripción

#### RF-07. Filtrar por estado
- Selector: Todos / Pendiente / En progreso / Hecha
- Combinable con filtro de texto

#### RF-08. Ordenación
- Por prioridad (Alta → Media → Baja)
- Ordenación secundaria por fecha de creación
- Selector de criterio de ordenación

#### RF-09. Guardado en JSON
- Persistencia automática al crear/editar/eliminar
- Carga automática al iniciar
- Manejo robusto de archivos inexistentes

### Requisitos No Funcionales

- **RNF-01 Robustez**: JSON corrupto no rompe la aplicación
- **RNF-02 Consistencia**: IDs únicos e inmutables (UUID)
- **RNF-03 Rendimiento**: Filtrado instantáneo
- **RNF-04 Usabilidad**: Interfaz intuitiva con atajos de teclado
- **RNF-05 Estructura**: Arquitectura modular (Modelo / Persistencia / UI)

## 🏗️ Arquitectura

### Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/inforgonzalez/todo/crud/list/
│   │   ├── App.java                          # Punto de entrada (JavaFX Application)
│   │   ├── model/
│   │   │   ├── Task.java                    # Entidad tarea
│   │   │   ├── Priority.java                # Enum prioridad
│   │   │   └── TaskStatus.java              # Enum estado
│   │   ├── persistence/
│   │   │   ├── TaskRepository.java          # Interfaz repositorio
│   │   │   ├── JsonTaskRepository.java      # Implementación JSON
│   │   │   ├── TaskData.java                # Wrapper para JSON
│   │   │   └── TaskDTO.java                 # DTO para transferencia
│   │   ├── service/
│   │   │   └── TaskService.java             # Lógica de negocio
│   │   └── ui/
│   │       ├── MainViewController.java      # Controlador FXML principal
│   │       ├── TaskFormController.java      # Controlador FXML formulario
│   │       ├── MainView.java                # Vista alternativa (sin FXML)
│   │       └── TaskFormDialog.java          # Diálogo alternativo (sin FXML)
│   └── resources/
│       ├── fxml/
│       │   ├── MainView.fxml                # Vista principal (XML)
│       │   └── TaskForm.fxml                # Formulario (XML)
│       └── styles/
│           └── application.css              # Estilos CSS
└── test/
    └── java/com/inforgonzalez/todo/crud/list/
        ├── AppTest.java
        ├── model/TaskTest.java
        └── service/TaskServiceTest.java
```

### Arquitectura en Capas

- **Capa de Presentación** (UI): JavaFX con FXML + Controladores
- **Capa de Lógica de Negocio**: TaskService
- **Capa de Persistencia**: TaskRepository + JsonTaskRepository
- **Capa de Modelo**: Entidades y Enums

## 📊 Modelo de Datos

### Enumeraciones

**Prioridad**: 
- `BAJA` - Prioridad baja
- `MEDIA` - Prioridad media
- `ALTA` - Prioridad alta

**Estado**:
- `PENDIENTE` - Tarea pendiente de iniciar
- `EN_PROGRESO` - Tarea en progreso
- `HECHA` - Tarea completada

### Entidad Task

```java
{
    "id": "UUID",                    // Identificador único
    "titulo": "String",              // Título (requerido)
    "descripcion": "String",         // Descripción (opcional)
    "prioridad": "ALTA|MEDIA|BAJA", // Prioridad (requerido)
    "estado": "PENDIENTE|EN_PROGRESO|HECHA", // Estado (requerido)
    "createdAt": "ISO-8601",         // Fecha de creación
    "updatedAt": "ISO-8601"          // Fecha de actualización
}
```

### Formato JSON

```json
{
  "version": 1,
  "tasks": [
    {
      "id": "c2b6f7c8-1b2c-4b0a-9a5e-6b6d2f6f3b0a",
      "titulo": "Preparar miniatura del directo",
      "descripcion": "Texto claro + contraste",
      "prioridad": "ALTA",
      "estado": "PENDIENTE",
      "createdAt": "2026-01-23T18:40:00Z",
      "updatedAt": "2026-01-23T18:40:00Z"
    }
  ]
}
```

## 🚀 Uso

### Ejecutar la aplicación

#### ⭐ Opción 1: Launch VBS (RECOMENDADO - Sin ventana CMD)
1. **Doble click** en `launch.vbs` (en la raíz del proyecto)
2. La aplicación se ejecuta **sin mostrar ventana CMD molesta**
3. Para crear acceso directo en Escritorio: ejecuta `crear_acceso_directo.bat`

#### Opción 2: Desde Eclipse/IDE
1. Click derecho en `App.java`
2. Run As → Java Application

#### Opción 3: Con Maven (JavaFX Plugin)
```bash
# Compilar y ejecutar con JavaFX
mvn clean javafx:run
```

#### Opción 4: Con Maven (Exec Plugin)
```bash
# Compilar
mvn clean compile

# Ejecutar
mvn exec:java
```

#### Opción 5: JAR Ejecutable con javaw (sin CMD)
```bash
# Empaquetar
mvn package

# Ejecutar SIN ventana CMD (javaw en lugar de java)
javaw -jar target/todo.crud.list-0.0.1-SNAPSHOT.jar

# O ejecutar CON ventana CMD (para debugging)
java -jar target/todo.crud.list-0.0.1-SNAPSHOT.jar
```

> **💡 Nota:** `javaw` ejecuta aplicaciones GUI sin mostrar ventana de consola, mientras que `java` sí la muestra.

### Interfaz de Usuario (JavaFX)

#### Zona Superior (Filtros)
- **Buscar**: Campo de texto para búsqueda en tiempo real
- **Estado**: Selector (Todos/Pendiente/En progreso/Hecha)
- **Ordenar por**: Selector (Prioridad/Estado/Título/Fecha)

#### Zona Central (Lista)
- Tabla con columnas: Título | Prioridad | Estado | Creado | Actualizado
- Colores por estado: 🟠 Pendiente | 🔵 En Progreso | 🟢 Hecha
- Negrita para tareas de alta prioridad
- Doble clic para editar
- Acciones: Nueva | Editar | Eliminar | Cambiar Estado | Recargar

#### Formulario (Diálogo JavaFX)
- Título (obligatorio, mínimo 3 caracteres)
- Descripción (opcional, multilínea)
- Prioridad (selector: Alta/Media/Baja)
- Estado (selector: Pendiente/En Progreso/Hecha)
- Botones: Guardar / Cancelar
- Validación en tiempo real

## 🐛 Troubleshooting

### ❌ Problema: Aparece ventana CMD molesta al ejecutar
**Causa**: Usar `java` en lugar de `javaw` para aplicaciones GUI

**Solución 1 - VBS Launcher (Recomendado)**:
- Usa `launch.vbs` en lugar de `.bat` o `.exe`
- No muestra ventana CMD
- Doble click en `launch.vbs` en la raíz del proyecto

**Solución 2 - Crear Acceso Directo**:
1. Ejecuta `crear_acceso_directo.bat`
2. Se creará un acceso directo en tu Escritorio
3. Usa ese acceso directo para ejecutar sin CMD

**Solución 3 - Comando Manual**:
```bash
# En lugar de: java -jar archivo.jar
# Usa: javaw -jar archivo.jar
javaw -jar target/todo.crud.list-0.0.1-SNAPSHOT.jar
```

**Más información**: Ver `SOLUCION_CMD.md`

### Error: "javafx.* no se puede resolver"
**Solución**: Actualizar dependencias Maven
```bash
# En Eclipse/IDE:
Click derecho en proyecto → Maven → Update Project → Force Update
```

### Error: "Error loading FXML"
**Solución**: Verificar que los archivos FXML están en `src/main/resources/fxml/`

### Error: "No se encuentra el Main-Class"
**Solución**: Verificar que `App.java` extiende `Application` y tiene el método `main()`

### La aplicación no guarda las tareas
**Solución**: Verificar permisos de escritura en el directorio donde se ejecuta (archivo `tasks.json`)

### Problema con colores o estilos
**Solución**: Verificar que `application.css` está en `src/main/resources/styles/`

## 📸 Capturas de Pantalla

### Vista Principal
- Tabla con todas las tareas
- Panel de filtros en la parte superior
- Botones de acción
- Barra de estado con estadísticas

### Formulario de Tarea
- Campos de entrada validados
- ComboBox para prioridad y estado
- Botones Guardar/Cancelar

### Características Visuales
- 🟠 **Pendiente**: Fondo naranja claro (#fff5e6)
- 🔵 **En Progreso**: Fondo azul claro (#e6f0ff)
- 🟢 **Hecha**: Fondo verde claro (#e6ffe6)
- **Alta Prioridad**: Texto en negrita

## 📝 Validaciones

- **Título**: No vacío, longitud mínima 3 caracteres, trim automático
- **IDs**: UUID únicos e inmutables
- **Fechas**: Actualización automática de `updatedAt`
- **Persistencia**: Guardado atómico (archivo temporal + renombrado)

## 🔧 Tecnologías

- **Java 17** (LTS)
- **Maven** (gestión de dependencias y build)
- **JavaFX 21.0.1** (interfaz gráfica moderna)
  - javafx-controls (componentes UI)
  - javafx-fxml (separación vista/lógica)
- **FXML** (definición declarativa de UI)
- **CSS** (estilos personalizados)
- **Gson 2.10.1** (serialización JSON)
- **JUnit 5** (testing unitario)

### Dependencias Maven

```xml
<!-- JavaFX -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.1</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.1</version>
</dependency>

<!-- JSON -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

## 🎨 Características de JavaFX

### Ventajas sobre Swing
- ✅ Diseño moderno y responsive
- ✅ Separación vista/controlador con FXML
- ✅ Estilos CSS personalizables
- ✅ Mejor rendimiento gráfico
- ✅ Animaciones y efectos nativos
- ✅ Scene Builder para diseño visual
- ✅ Binding de propiedades reactivo

### Componentes Utilizados
- **BorderPane**: Layout principal
- **TableView**: Tabla de tareas con columnas personalizadas
- **Dialog**: Formularios modales
- **ComboBox**: Selectores de filtro, prioridad y estado
- **TextField/TextArea**: Entrada de texto
- **Button**: Acciones
- **Label**: Etiquetas y barra de estado

## 📚 Documentación Adicional

- **[MIGRACION_JAVAFX.md](MIGRACION_JAVAFX.md)** - Detalles de la migración de Swing a JavaFX
- **[GUIA_FXML.md](GUIA_FXML.md)** - Guía rápida de uso con FXML
- **[GUIA_USO.md](GUIA_USO.md)** - Manual de usuario detallado

## 📌 Checklist "Definition of Done"

### Funcionalidades Core
- [x] Crear/Editar/Eliminar funciona sin errores
- [x] Filtro por texto y por estado funciona combinado
- [x] Se guarda en JSON y al reiniciar se recupera igual
- [x] No se rompe si el archivo no existe
- [x] Código modular (modelo, almacenamiento, servicio, UI)
- [x] Ordenación por prioridad, fecha, título y estado
- [x] Cambio rápido de estado (Pendiente → En Progreso → Hecha)
- [x] Confirmación de eliminación
- [x] Manejo robusto de JSON corrupto

### Interfaz JavaFX
- [x] Migración completa de Swing a JavaFX
- [x] Arquitectura FXML (separación vista/controlador)
- [x] Archivos FXML creados (MainView.fxml, TaskForm.fxml)
- [x] Controladores implementados (MainViewController, TaskFormController)
- [x] Estilos CSS aplicados
- [x] Colores por estado y prioridad
- [x] TableView con columnas personalizadas
- [x] Validación de formularios en tiempo real
- [x] Búsqueda en tiempo real (listener en TextField)
- [x] Doble clic para editar tareas

### Testing y Calidad
- [x] Tests unitarios (TaskTest, TaskServiceTest)
- [x] Manejo de errores robusto
- [x] Código limpio y documentado
- [x] Arquitectura en capas clara

### Documentación
- [x] README.md completo y actualizado
- [x] Guía de migración JavaFX
- [x] Guía rápida FXML
- [x] Javadoc en clases principales

## 🛠️ Herramientas de Desarrollo

### Scene Builder (Opcional)
Para editar visualmente los archivos FXML:

1. **Descargar**: [Gluon Scene Builder](https://gluonhq.com/products/scene-builder/)
2. **Instalar** Scene Builder en tu sistema
3. **Abrir** archivos `.fxml` con Scene Builder
4. **Diseñar** la interfaz arrastrando componentes
5. **Guardar** y ejecutar la aplicación

### Estructura FXML
Los archivos FXML son XML que definen la estructura de la UI:

```xml
<!-- Ejemplo simplificado de MainView.fxml -->
<BorderPane xmlns:fx="http://javafx.com/fxml/1" 
            fx:controller="...MainViewController">
    <top>
        <!-- Panel de filtros -->
    </top>
    <center>
        <TableView fx:id="taskTable">
            <!-- Columnas -->
        </TableView>
    </center>
</BorderPane>
```

### Doble Implementación
El proyecto incluye **dos versiones**:

1. **Con FXML** (actual, recomendada):
   - `MainViewController.java` + `MainView.fxml`
   - `TaskFormController.java` + `TaskForm.fxml`
   - Separación clara vista/lógica
   - Compatible con Scene Builder

2. **Sin FXML** (alternativa):
   - `MainView.java` (código Java puro)
   - `TaskFormDialog.java` (código Java puro)
   - Todo en código Java
   - Más control programático

Para cambiar entre versiones, modifica el método `start()` en `App.java`.

## ⚙️ Comandos Maven Útiles

```bash
# Limpiar el proyecto
mvn clean

# Compilar sin ejecutar tests
mvn compile -DskipTests

# Ejecutar tests
mvn test

# Ejecutar tests de una clase específica
mvn test -Dtest=TaskServiceTest

# Empaquetar sin tests
mvn package -DskipTests

# Ver dependencias
mvn dependency:tree

# Verificar actualizaciones de dependencias
mvn versions:display-dependency-updates

# Ejecutar con JavaFX plugin
mvn javafx:run

# Compilar, testear y empaquetar (ciclo completo)
mvn clean install
```

## 🤝 Contribuir

### Estructura de Commits
```
tipo(alcance): descripción corta

Descripción detallada (opcional)

Ejemplos:
- feat(ui): agregar filtro por fecha
- fix(persistence): corregir serialización JSON
- docs(readme): actualizar instrucciones de instalación
- refactor(service): simplificar lógica de ordenación
```

### Agregar Nueva Funcionalidad

1. **Modelo**: Agregar campos a `Task.java` si es necesario
2. **Persistencia**: Actualizar `TaskDTO.java` para serialización
3. **Servicio**: Implementar lógica en `TaskService.java`
4. **UI**: 
   - Actualizar `MainView.fxml` o crear nuevo FXML
   - Implementar en controlador correspondiente
   - Actualizar estilos en `application.css`
5. **Tests**: Agregar tests unitarios
6. **Docs**: Actualizar README.md

## 🚀 Próximas Mejoras

- [ ] Exportar tareas a PDF
- [ ] Filtro por fecha (creación/actualización)
- [ ] Estadísticas visuales con gráficos
- [ ] Drag & drop para reordenar tareas
- [ ] Temas personalizables (claro/oscuro)
- [ ] Notificaciones de tareas pendientes
- [ ] Búsqueda avanzada con operadores
- [ ] Categorías/etiquetas para tareas

## 📄 Licencia

Este proyecto es de código abierto y está disponible para uso educativo.

## 👨‍💻 Autor

InforGonzalez - [YouTube](https://youtube.com/@inforgonzalez)

---

**Versión**: 1.0.0  
**Fecha**: Enero 2026
