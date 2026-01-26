# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [2.0.0] - 2026-01-23

### 🎨 Migración a JavaFX con FXML

Migración completa de Swing a JavaFX con arquitectura FXML para separación de vista y lógica.

### ✨ Nuevas Características

#### Interfaz JavaFX
- **FXML Architecture**: Separación completa de vista (XML) y lógica (Controladores)
- **MainView.fxml**: Vista principal declarativa con filtros, tabla y acciones
- **TaskForm.fxml**: Formulario modal para crear/editar tareas
- **MainViewController**: Controlador principal con anotaciones @FXML
- **TaskFormController**: Controlador del formulario con validación
- **Estilos CSS**: `application.css` para personalización visual
- **Scene Builder Compatible**: Archivos FXML editables visualmente

#### Mejoras Visuales
- 🟠 **Pendiente**: Fondo naranja claro (#fff5e6)
- 🔵 **En Progreso**: Fondo azul claro (#e6f0ff)
- 🟢 **Hecha**: Fondo verde claro (#e6ffe6)
- **Alta Prioridad**: Texto en negrita automático
- **Hover effects**: Efectos visuales en botones
- **Focus states**: Bordes destacados en campos activos

#### Doble Implementación
- **Versión FXML** (recomendada): Con archivos XML y controladores
- **Versión Java puro** (alternativa): `MainView.java` y `TaskFormDialog.java`
- Fácil cambio entre versiones modificando `App.java`

### 🔧 Cambios Técnicos

#### Dependencias Actualizadas
```xml
<!-- Agregadas -->
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
```

#### Plugins Maven
- Agregado `javafx-maven-plugin` v0.0.8
- Configurado para ejecutar con `mvn javafx:run`

#### Archivos Eliminados
- ❌ `MainFrame.java` (Swing) → ✅ `MainViewController.java` (JavaFX)
- ❌ `TaskTableModel.java` (Swing) → Integrado en `MainViewController`

#### Archivos Creados
- ✅ `src/main/resources/fxml/MainView.fxml`
- ✅ `src/main/resources/fxml/TaskForm.fxml`
- ✅ `src/main/resources/styles/application.css`
- ✅ `src/main/java/.../ui/MainViewController.java`
- ✅ `src/main/java/.../ui/TaskFormController.java`
- ✅ `src/main/java/.../ui/MainView.java` (alternativa sin FXML)
- ✅ `MIGRACION_JAVAFX.md`
- ✅ `GUIA_FXML.md`

### 📚 Documentación
- README.md actualizado con JavaFX y FXML
- Guía de migración completa
- Guía rápida de uso con FXML
- Instrucciones de Scene Builder
- Comandos Maven útiles
- Sección de troubleshooting

### 🎯 Ventajas de la Migración
- Diseño moderno y responsive
- Mejor rendimiento gráfico
- Estilos CSS personalizables
- Separación clara vista/controlador
- Compatible con herramientas visuales (Scene Builder)
- Binding de propiedades reactivo
- Animaciones nativas (preparado para futuras mejoras)

---

## [1.0.0] - 2026-01-23

### 🎉 Lanzamiento Inicial

Primera versión completa de TODO CRUD List - Aplicación de Gestión de Tareas con Swing.

### ✨ Características Implementadas

#### Requisitos Funcionales (MVP)

- **RF-01: Crear tarea**
  - Formulario completo con validaciones
  - Campos obligatorios: título, prioridad, estado
  - Campos opcionales: descripción
  - Timestamps automáticos (creación y actualización)

- **RF-02: Listar tareas**
  - Tabla visual con 5 columnas: Título, Prioridad, Estado, Creado, Actualizado
  - Mensaje "No hay tareas" cuando la lista está vacía
  - Colores distintivos por estado

- **RF-03: Editar tarea**
  - Edición mediante doble click o botón
  - Mantiene el ID original
  - Actualiza automáticamente el timestamp
  - Validaciones en tiempo real

- **RF-04: Eliminar tarea**
  - Confirmación antes de eliminar
  - Eliminación permanente e inmediata
  - Guardado automático

- **RF-05: Cambiar estado rápidamente**
  - Botón "Cambiar Estado" para ciclo rápido
  - Flujo: Pendiente → En progreso → Hecha → Pendiente
  - Un solo click para actualizar

- **RF-06: Filtrar por texto**
  - Búsqueda en tiempo real (al escribir)
  - Case-insensitive
  - Busca en título y descripción
  - Coincidencia parcial

- **RF-07: Filtrar por estado**
  - Dropdown con opciones: Todos, Pendiente, En Progreso, Hecha
  - Combinable con búsqueda por texto

- **RF-08: Ordenación**
  - Ordenar por Prioridad (Alta → Media → Baja)
  - Ordenar por Estado
  - Ordenar por Título (alfabético)
  - Ordenar por Fecha (más recientes primero)
  - Ordenación secundaria por fecha de creación

- **RF-09: Guardado en JSON**
  - Persistencia automática al crear/editar/eliminar
  - Carga automática al iniciar
  - Formato JSON estructurado con versionado

#### Requisitos No Funcionales

- **RNF-01: Robustez**
  - Manejo de JSON corrupto sin caída
  - Restauración automática desde backup
  - Mensajes informativos de error
  - Guardado del archivo corrupto para análisis

- **RNF-02: Consistencia**
  - IDs únicos UUID v4
  - IDs inmutables durante toda la vida de la tarea
  - Validación de integridad en carga

- **RNF-03: Rendimiento**
  - Filtrado instantáneo
  - Sin lag en búsqueda en tiempo real
  - Soporta cientos de tareas sin problemas

- **RNF-04: Usabilidad**
  - Atajos de teclado (Enter para guardar, Esc para cancelar)
  - Doble click para editar
  - Interfaz intuitiva
  - Look and Feel del sistema operativo

- **RNF-05: Estructura**
  - Arquitectura modular en capas:
    - `model`: Entidades y enums
    - `persistence`: Repositorio y serialización JSON
    - `service`: Lógica de negocio
    - `ui`: Interfaz gráfica Swing
  - Separación clara de responsabilidades
  - Código mantenible y extensible

### 🏗️ Arquitectura

#### Modelo de Datos
- **Task**: Entidad principal con ID, título, descripción, prioridad, estado, timestamps
- **Priority**: Enum (ALTA, MEDIA, BAJA)
- **TaskStatus**: Enum (PENDIENTE, EN_PROGRESO, HECHA)

#### Capa de Persistencia
- **TaskRepository**: Interfaz del repositorio
- **JsonTaskRepository**: Implementación con Gson
- **TaskData**: Wrapper para JSON con versionado
- **TaskDTO**: Data Transfer Object para serialización

#### Capa de Servicio
- **TaskService**: Lógica de negocio completa
  - CRUD operations
  - Filtrado combinado
  - Ordenación múltiple
  - Estadísticas
  - Validaciones

#### Capa de UI
- **MainFrame**: Ventana principal con tabla y controles
- **TaskFormDialog**: Formulario modal para crear/editar
- **TaskTableModel**: Modelo personalizado para JTable
- **TaskCellRenderer**: Renderizador con colores por estado

### 🎨 Interfaz de Usuario

- **Panel de Filtros**:
  - Búsqueda por texto
  - Filtro por estado
  - Selector de ordenación
  - Botón limpiar filtros

- **Panel de Acciones**:
  - Nueva Tarea (➕)
  - Editar (✏️)
  - Eliminar (🗑️)
  - Cambiar Estado (🔄)
  - Recargar (🔃)

- **Tabla de Tareas**:
  - Colores por estado (naranja/azul/verde)
  - Negrita para prioridad alta
  - Doble click para editar
  - Selección simple

- **Barra de Estado**:
  - Estadísticas: Total, Pendientes, En Progreso, Completadas
  - Contador de tareas mostradas

### 📊 Formato JSON

```json
{
  "version": 1,
  "tasks": [
    {
      "id": "uuid",
      "titulo": "string",
      "descripcion": "string",
      "prioridad": "ALTA|MEDIA|BAJA",
      "estado": "PENDIENTE|EN_PROGRESO|HECHA",
      "createdAt": "ISO-8601",
      "updatedAt": "ISO-8601"
    }
  ]
}
```

### 🔒 Seguridad en Persistencia

- Guardado atómico con archivo temporal
- Backup automático antes de sobrescribir
- Detección y recuperación de JSON corrupto
- Preservación de archivo corrupto para análisis

### 🧪 Testing

- Tests unitarios para modelo (TaskTest)
- Tests unitarios para servicio (TaskServiceTest)
- Mock repository para testing aislado
- Cobertura de casos principales

### 📦 Dependencias

- **Java 17**: Lenguaje base
- **Maven**: Gestión de dependencias y build
- **Gson 2.10.1**: Serialización/deserialización JSON
- **JUnit 5.11.0**: Framework de testing
- **Swing**: UI (incluido en JDK)

### 📝 Documentación

- **README.md**: Documentación principal del proyecto
- **GUIA_USO.md**: Guía completa de uso para usuarios
- **CHANGELOG.md**: Historial de cambios (este archivo)
- **tasks.json.example**: Ejemplo de archivo de datos
- Javadoc en todas las clases públicas

### 🚀 Instalación y Ejecución

```bash
# Clonar/descargar el proyecto
# Compilar
mvn clean compile

# Ejecutar
mvn exec:java

# Empaquetar
mvn package
java -jar target/todo.crud.list-0.0.1-SNAPSHOT.jar
```

### ✅ Definition of Done

- [x] CRUD completo funciona sin errores
- [x] Filtros por texto y estado (combinados)
- [x] Persistencia en JSON robusta
- [x] Manejo de archivo inexistente
- [x] Código modular y bien estructurado
- [x] Ordenación múltiple implementada
- [x] Cambio rápido de estado
- [x] Confirmación de eliminación
- [x] Manejo de JSON corrupto
- [x] Tests unitarios básicos
- [x] Documentación completa

### 🎯 Características Destacadas

- **UX Pulida**: Colores, iconos, mensajes claros
- **Robustez**: No se rompe con datos corruptos
- **Rendimiento**: Búsqueda instantánea
- **Productividad**: Atajos y acciones rápidas
- **Mantenibilidad**: Código limpio y documentado

---

## [Unreleased] - Futuras Mejoras

### Planificado para Versiones Futuras

#### P1 (Alta Prioridad)
- [ ] Exportar tareas a CSV
- [ ] Importar tareas desde CSV
- [ ] Deshacer última acción
- [ ] Temas: Claro / Oscuro

#### P2 (Media Prioridad)
- [ ] Tags/Etiquetas personalizadas
- [ ] Filtro por tags
- [ ] Fecha límite (deadline)
- [ ] Notificaciones de tareas vencidas
- [ ] Búsqueda avanzada con operadores

#### P3 (Baja Prioridad)
- [ ] Adjuntar archivos a tareas
- [ ] Comentarios en tareas
- [ ] Historial de cambios por tarea
- [ ] Categorías personalizables
- [ ] Múltiples listas/proyectos

#### Mejoras Técnicas
- [ ] Persistencia en base de datos (SQLite)
- [ ] API REST
- [ ] Sincronización en la nube
- [ ] Versión web
- [ ] App móvil

---

## Tipos de Cambios

- **✨ Added** - Para nuevas características
- **🔧 Changed** - Para cambios en funcionalidad existente
- **⚠️ Deprecated** - Para características que serán eliminadas
- **🗑️ Removed** - Para características eliminadas
- **🐛 Fixed** - Para corrección de bugs
- **🔒 Security** - Para correcciones de seguridad

---

**Mantenido por**: InforGonzalez  
**Canal**: [YouTube @inforgonzalez](https://youtube.com/@inforgonzalez)
