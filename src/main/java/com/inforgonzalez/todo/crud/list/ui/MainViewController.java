package com.inforgonzalez.todo.crud.list.ui;

import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.Task;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;
import com.inforgonzalez.todo.crud.list.model.TaskDuration;
import com.inforgonzalez.todo.crud.list.model.TaskType;
import com.inforgonzalez.todo.crud.list.model.Feasibility;
import com.inforgonzalez.todo.crud.list.service.TaskService;
import com.inforgonzalez.todo.crud.list.service.TaskService.SortCriteria;
import com.inforgonzalez.todo.crud.list.service.TaskService.TaskStatistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import javafx.stage.FileChooser;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para la vista principal de la aplicación.
 */
public class MainViewController {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());
    
    @FXML
    private TextField searchField;
    
    @FXML
    private ComboBox<FilterOption> statusFilterCombo;
    
    @FXML
    private ComboBox<SortCriteria> sortCombo;
    
    @FXML
    private TableView<Task> taskTable;
    
    @FXML
    private TableColumn<Task, String> tituloColumn;
    
    @FXML
    private TableColumn<Task, Priority> prioridadColumn;
    
    @FXML
    private TableColumn<Task, TaskStatus> estadoColumn;
    
    @FXML
    private TableColumn<Task, Instant> creadoColumn;
    
    @FXML
    private TableColumn<Task, Instant> actualizadoColumn;
    
    @FXML
    private TableColumn<Task, Double> horasColumn;
    
    @FXML
    private TableColumn<Task, TaskDuration> duracionColumn;
    
    @FXML
    private TableColumn<Task, TaskType> tipoColumn;
    
    @FXML
    private TableColumn<Task, Feasibility> factibilidadColumn;
    
    @FXML
    private Label statusLabel;
    
    private TaskService taskService;
    private ObservableList<Task> taskList;
    
    /**
     * Inicialización automática después de cargar el FXML.
     */
    @FXML
    private void initialize() {
        taskList = FXCollections.observableArrayList();
        taskTable.setItems(taskList);
        
        setupTableColumns();
        setupFilters();
        setupTableRowFactory();
        setupDoubleClickEdit();
    }
    
    /**
     * Establece el servicio de tareas y carga los datos iniciales.
     */
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
        loadTasks();
        updateStatusBar();
    }
    
    private void setupTableColumns() {
        // Columna Título
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        
        // Columna Prioridad
        prioridadColumn.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        prioridadColumn.setCellFactory(col -> new TableCell<Task, Priority>() {
            @Override
            protected void updateItem(Priority priority, boolean empty) {
                super.updateItem(priority, empty);
                if (empty || priority == null) {
                    setText(null);
                } else {
                    setText(priority.getDisplayName());
                }
            }
        });
        
        // Columna Estado
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoColumn.setCellFactory(col -> new TableCell<Task, TaskStatus>() {
            @Override
            protected void updateItem(TaskStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status.getDisplayName());
                    String bgColor = switch (status) {
                        case PENDIENTE -> "-fx-background-color: #fff5e6;";
                        case EN_PROGRESO -> "-fx-background-color: #e6f0ff;";
                        case HECHA -> "-fx-background-color: #e6ffe6;";
                    };
                    setStyle(bgColor);
                }
            }
        });
        
        // Columna Creado
        creadoColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        creadoColumn.setCellFactory(col -> new TableCell<Task, Instant>() {
            @Override
            protected void updateItem(Instant instant, boolean empty) {
                super.updateItem(instant, empty);
                if (empty || instant == null) {
                    setText(null);
                } else {
                    setText(DATE_FORMATTER.format(instant));
                }
            }
        });
        
        // Columna Actualizado
        actualizadoColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        actualizadoColumn.setCellFactory(col -> new TableCell<Task, Instant>() {
            @Override
            protected void updateItem(Instant instant, boolean empty) {
                super.updateItem(instant, empty);
                if (empty || instant == null) {
                    setText(null);
                } else {
                    setText(DATE_FORMATTER.format(instant));
                }
            }
        });
        
        // Columna Horas
        horasColumn.setCellValueFactory(new PropertyValueFactory<>("horasEstimadas"));
        horasColumn.setCellFactory(col -> new TableCell<Task, Double>() {
            @Override
            protected void updateItem(Double horas, boolean empty) {
                super.updateItem(horas, empty);
                if (empty || horas == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f", horas));
                }
            }
        });
        
        // Columna Duración
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracionTarea"));
        duracionColumn.setCellFactory(col -> new TableCell<Task, TaskDuration>() {
            @Override
            protected void updateItem(TaskDuration duracion, boolean empty) {
                super.updateItem(duracion, empty);
                if (empty || duracion == null) {
                    setText(null);
                } else {
                    setText(duracion.getDisplayName());
                }
            }
        });
        
        // Columna Tipo
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoTarea"));
        tipoColumn.setCellFactory(col -> new TableCell<Task, TaskType>() {
            @Override
            protected void updateItem(TaskType tipo, boolean empty) {
                super.updateItem(tipo, empty);
                if (empty || tipo == null) {
                    setText(null);
                } else {
                    setText(tipo.getDisplayName());
                }
            }
        });
        
        // Columna Factibilidad
        factibilidadColumn.setCellValueFactory(new PropertyValueFactory<>("factibilidad"));
        factibilidadColumn.setCellFactory(col -> new TableCell<Task, Feasibility>() {
            @Override
            protected void updateItem(Feasibility factibilidad, boolean empty) {
                super.updateItem(factibilidad, empty);
                if (empty || factibilidad == null) {
                    setText(null);
                } else {
                    setText(factibilidad.getDisplayName());
                }
            }
        });
    }
    
    private void setupFilters() {
        // Inicializar ComboBox de estado
        ObservableList<FilterOption> filterOptions = FXCollections.observableArrayList(FilterOption.values());
        System.out.println("DEBUG: Cargando opciones de filtro: " + filterOptions.size());
        for (FilterOption opt : filterOptions) {
            System.out.println("  - " + opt.toString());
        }
        statusFilterCombo.setItems(filterOptions);
        statusFilterCombo.setValue(FilterOption.ALL);
        statusFilterCombo.setOnAction(e -> applyFilters());
        
        // Configurar cómo se muestran los items en el ComboBox de estado
        statusFilterCombo.setButtonCell(new ListCell<FilterOption>() {
            @Override
            protected void updateItem(FilterOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        
        statusFilterCombo.setCellFactory(lv -> new ListCell<FilterOption>() {
            @Override
            protected void updateItem(FilterOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        
        // Inicializar ComboBox de ordenación
        ObservableList<SortCriteria> sortOptions = FXCollections.observableArrayList(SortCriteria.values());
        System.out.println("DEBUG: Cargando opciones de ordenación: " + sortOptions.size());
        for (SortCriteria opt : sortOptions) {
            System.out.println("  - " + opt.toString());
        }
        sortCombo.setItems(sortOptions);
        sortCombo.setValue(SortCriteria.PRIORITY);
        sortCombo.setOnAction(e -> applyFilters());
        
        // Configurar cómo se muestran los items en el ComboBox de ordenación
        sortCombo.setButtonCell(new ListCell<SortCriteria>() {
            @Override
            protected void updateItem(SortCriteria item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        
        sortCombo.setCellFactory(lv -> new ListCell<SortCriteria>() {
            @Override
            protected void updateItem(SortCriteria item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)  {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        
        // Listener para búsqueda en tiempo real
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }
    
    private void setupTableRowFactory() {
        taskTable.setRowFactory(tv -> new TableRow<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                
                if (empty || task == null) {
                    setStyle("");
                } else {
                    String bgColor = switch (task.getEstado()) {
                        case PENDIENTE -> "-fx-background-color: #fff5e6;";
                        case EN_PROGRESO -> "-fx-background-color: #e6f0ff;";
                        case HECHA -> "-fx-background-color: #e6ffe6;";
                    };
                    
                    String fontWeight = task.getPrioridad() == Priority.ALTA ? 
                            "-fx-font-weight: bold;" : "";
                    
                    setStyle(bgColor + fontWeight);
                }
            }
        });
    }
    
    private void setupDoubleClickEdit() {
        taskTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onEditTask();
            }
        });
    }
    
    private void loadTasks() {
        applyFilters();
    }
    
    private void applyFilters() {
        if (taskService == null) return;
        
        String searchText = searchField.getText().trim();
        FilterOption filterOption = statusFilterCombo.getValue();
        TaskStatus status = filterOption != null ? filterOption.getStatus() : null;
        SortCriteria sortCriteria = sortCombo.getValue();
        
        // Filtrar tareas
        List<Task> tasks = taskService.filterTasks(searchText, status);
        
        // Ordenar tareas
        if (sortCriteria != null) {
            tasks = taskService.sortTasks(tasks, sortCriteria);
        }
        
        // Actualizar tabla
        taskList.clear();
        taskList.addAll(tasks);
        updateStatusBar();
    }
    
    @FXML
    private void onClearFilters() {
        searchField.clear();
        statusFilterCombo.setValue(FilterOption.ALL);
        sortCombo.setValue(SortCriteria.PRIORITY);
    }
    
    @FXML
    private void onCreateTask() {
        try {
            TaskFormController controller = new TaskFormController();
            // Obtener la ventana actual desde cualquier control de la interfaz
            Window owner = taskTable.getScene().getWindow();
            Optional<Task> result = controller.showDialog(null, owner);
            
            result.ifPresent(newTask -> {
                try {
                    taskService.createTask(
                            newTask.getTitulo(),
                            newTask.getDescripcion(),
                            newTask.getPrioridad(),
                            newTask.getEstado()
                    );
                    loadTasks();
                    showMessage("Éxito", "Tarea creada exitosamente", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showMessage("Error", "Error al crear tarea: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        } catch (Exception e) {
            showMessage("Error", "Error al abrir el formulario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void onEditTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        
        if (selectedTask == null) {
            showMessage("Aviso", "Por favor, seleccione una tarea para editar.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            TaskFormController controller = new TaskFormController();
            // Obtener la ventana actual desde cualquier control de la interfaz
            Window owner = taskTable.getScene().getWindow();
            Optional<Task> result = controller.showDialog(selectedTask, owner);
            
            result.ifPresent(editedTask -> {
                try {
                    taskService.updateTask(
                            editedTask.getId(),
                            editedTask.getTitulo(),
                            editedTask.getDescripcion(),
                            editedTask.getPrioridad(),
                            editedTask.getEstado()
                    );
                    loadTasks();
                    showMessage("Éxito", "Tarea actualizada exitosamente", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showMessage("Error", "Error al actualizar tarea: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        } catch (Exception e) {
            showMessage("Error", "Error al abrir el formulario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void onDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        
        if (selectedTask == null) {
            showMessage("Aviso", "Por favor, seleccione una tarea para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar Eliminación");
        confirmDialog.setHeaderText("¿Está seguro de que desea eliminar la tarea?");
        confirmDialog.setContentText("\"" + selectedTask.getTitulo() + "\"");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                taskService.deleteTask(selectedTask.getId());
                loadTasks();
                showMessage("Éxito", "Tarea eliminada exitosamente", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showMessage("Error", "Error al eliminar tarea: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void onToggleStatus() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        
        if (selectedTask == null) {
            showMessage("Aviso", "Por favor, seleccione una tarea.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            taskService.toggleTaskStatus(selectedTask.getId());
            loadTasks();
        } catch (Exception e) {
            showMessage("Error", "Error al cambiar estado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void onRefresh() {
        loadTasks();
    }

    @FXML
    private void onExport() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Exportar tareas a JSON");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            chooser.setInitialFileName("tasks.json");
            java.io.File file = chooser.showSaveDialog(taskTable.getScene().getWindow());
            if (file != null) {
                taskService.exportToJson(file.toPath());
                showMessage("Éxito", "Tareas exportadas a: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showMessage("Error", "No se pudo exportar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onImport() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Importar tareas desde JSON");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            java.io.File file = chooser.showOpenDialog(taskTable.getScene().getWindow());
            if (file != null) {
                int count = taskService.importFromJson(file.toPath(), true); // reemplazar existentes
                loadTasks();
                showMessage("Éxito", "Importadas " + count + " tareas desde: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showMessage("Error", "No se pudo importar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void updateStatusBar() {
        if (taskService == null) return;
        
        TaskStatistics stats = taskService.getStatistics();
        statusLabel.setText(String.format(
                "%s | Mostrando: %d tareas",
                stats.toString(),
                taskList.size()
        ));
    }
    
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

    @FXML
    private void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("TODO CRUD List");
        alert.setContentText("Tomas Gonzalez — Copyright 2026\n"
                + "Si estás interesado en un técnico, escríbeme a tgextreme89@gmail.com");

        if (taskTable != null && taskTable.getScene() != null) {
            alert.initOwner(taskTable.getScene().getWindow());
        }

        alert.showAndWait();
    }
    
    /**
     * Opciones de filtro por estado.
     */
    public enum FilterOption {
        ALL("Todos", null),
        PENDING("Pendiente", TaskStatus.PENDIENTE),
        IN_PROGRESS("En Progreso", TaskStatus.EN_PROGRESO),
        DONE("Hecha", TaskStatus.HECHA);
        
        private final String displayName;
        private final TaskStatus status;
        
        FilterOption(String displayName, TaskStatus status) {
            this.displayName = displayName;
            this.status = status;
        }
        
        public TaskStatus getStatus() {
            return status;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
}
