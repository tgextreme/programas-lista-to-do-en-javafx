package com.inforgonzalez.todo.crud.list.ui;

import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.Task;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;
import com.inforgonzalez.todo.crud.list.service.TaskService;
import com.inforgonzalez.todo.crud.list.service.TaskService.SortCriteria;
import com.inforgonzalez.todo.crud.list.service.TaskService.TaskStatistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Vista principal de la aplicación TODO CRUD List con JavaFX.
 */
public class MainView extends BorderPane {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());
    
    private final TaskService taskService;
    
    // Componentes UI
    private TextField searchField;
    private ComboBox<FilterOption> statusFilterCombo;
    private ComboBox<SortCriteria> sortCombo;
    private TableView<Task> taskTable;
    private Label statusLabel;
    
    private ObservableList<Task> taskList;
    
    public MainView(TaskService taskService) {
        this.taskService = taskService;
        this.taskList = FXCollections.observableArrayList();
        
        initializeUI();
        loadTasks();
        updateStatusBar();
    }
    
    private void initializeUI() {
        setPadding(new Insets(10));
        
        // Zona superior: filtros y controles
        setTop(createFilterPanel());
        
        // Zona central: tabla de tareas
        setCenter(createTableView());
        
        // Zona inferior: barra de estado
        setBottom(createStatusBar());
    }
    
    private VBox createFilterPanel() {
        VBox filterPanel = new VBox(10);
        filterPanel.setPadding(new Insets(10));
        filterPanel.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
        
        Label titleLabel = new Label("Filtros y Ordenación");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Panel de filtros
        HBox filtersTop = new HBox(15);
        filtersTop.setAlignment(Pos.CENTER_LEFT);
        
        // Búsqueda por texto
        Label searchLabel = new Label("Buscar:");
        searchField = new TextField();
        searchField.setPromptText("Buscar en título y descripción");
        searchField.setPrefWidth(200);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        
        // Filtro por estado
        Label statusLabel = new Label("Estado:");
        statusFilterCombo = new ComboBox<>();
        statusFilterCombo.getItems().addAll(FilterOption.values());
        statusFilterCombo.setValue(FilterOption.ALL);
        statusFilterCombo.setOnAction(e -> applyFilters());
        
        // Ordenación
        Label sortLabel = new Label("Ordenar por:");
        sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll(SortCriteria.values());
        sortCombo.setValue(SortCriteria.PRIORITY);
        sortCombo.setOnAction(e -> applyFilters());
        
        // Botón limpiar filtros
        Button clearButton = new Button("Limpiar Filtros");
        clearButton.setOnAction(e -> clearFilters());
        
        filtersTop.getChildren().addAll(
                searchLabel, searchField,
                statusLabel, statusFilterCombo,
                sortLabel, sortCombo,
                clearButton
        );
        
        // Panel de botones de acción
        HBox actionPanel = new HBox(10);
        actionPanel.setAlignment(Pos.CENTER_LEFT);
        
        Button newButton = new Button("➕ Nueva Tarea");
        newButton.setStyle("-fx-font-weight: bold;");
        newButton.setOnAction(e -> createTask());
        
        Button editButton = new Button("✏️ Editar");
        editButton.setOnAction(e -> editSelectedTask());
        
        Button deleteButton = new Button("🗑️ Eliminar");
        deleteButton.setOnAction(e -> deleteSelectedTask());
        
        Button toggleButton = new Button("🔄 Cambiar Estado");
        toggleButton.setOnAction(e -> toggleSelectedTaskStatus());
        
        Button refreshButton = new Button("🔃 Recargar");
        refreshButton.setOnAction(e -> loadTasks());
        
        actionPanel.getChildren().addAll(
                newButton, editButton, deleteButton, toggleButton, refreshButton
        );
        
        filterPanel.getChildren().addAll(titleLabel, filtersTop, actionPanel);
        
        return filterPanel;
    }
    
    private VBox createTableView() {
        VBox tablePanel = new VBox(10);
        tablePanel.setPadding(new Insets(10, 0, 10, 0));
        
        Label titleLabel = new Label("Lista de Tareas");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        taskTable = new TableView<>();
        taskTable.setItems(taskList);
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Columna Título
        TableColumn<Task, String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tituloCol.setPrefWidth(300);
        
        // Columna Prioridad
        TableColumn<Task, Priority> prioridadCol = new TableColumn<>("Prioridad");
        prioridadCol.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        prioridadCol.setPrefWidth(100);
        prioridadCol.setCellFactory(col -> new TableCell<Task, Priority>() {
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
        TableColumn<Task, TaskStatus> estadoCol = new TableColumn<>("Estado");
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoCol.setPrefWidth(120);
        estadoCol.setCellFactory(col -> new TableCell<Task, TaskStatus>() {
            @Override
            protected void updateItem(TaskStatus status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status.getDisplayName());
                    // Colorear según estado
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
        TableColumn<Task, Instant> creadoCol = new TableColumn<>("Creado");
        creadoCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        creadoCol.setPrefWidth(140);
        creadoCol.setCellFactory(col -> new TableCell<Task, Instant>() {
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
        TableColumn<Task, Instant> actualizadoCol = new TableColumn<>("Actualizado");
        actualizadoCol.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        actualizadoCol.setPrefWidth(140);
        actualizadoCol.setCellFactory(col -> new TableCell<Task, Instant>() {
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
        
        taskTable.getColumns().addAll(tituloCol, prioridadCol, estadoCol, creadoCol, actualizadoCol);
        
        // Doble clic para editar
        taskTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editSelectedTask();
            }
        });
        
        // Colorear filas según prioridad y estado
        taskTable.setRowFactory(tv -> new TableRow<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                
                if (empty || task == null) {
                    setStyle("");
                } else {
                    // Color según estado
                    String bgColor = switch (task.getEstado()) {
                        case PENDIENTE -> "-fx-background-color: #fff5e6;";
                        case EN_PROGRESO -> "-fx-background-color: #e6f0ff;";
                        case HECHA -> "-fx-background-color: #e6ffe6;";
                    };
                    
                    // Negrita para alta prioridad
                    String fontWeight = task.getPrioridad() == Priority.ALTA ? 
                            "-fx-font-weight: bold;" : "";
                    
                    setStyle(bgColor + fontWeight);
                }
            }
        });
        
        VBox.setVgrow(taskTable, javafx.scene.layout.Priority.ALWAYS);
        tablePanel.getChildren().addAll(titleLabel, taskTable);
        
        return tablePanel;
    }
    
    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-border-color: lightgray; -fx-border-width: 1 0 0 0;");
        
        statusLabel = new Label("Listo");
        statusBar.getChildren().add(statusLabel);
        
        return statusBar;
    }
    
    private void loadTasks() {
        applyFilters();
    }
    
    private void applyFilters() {
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
    
    private void clearFilters() {
        searchField.clear();
        statusFilterCombo.setValue(FilterOption.ALL);
        sortCombo.setValue(SortCriteria.PRIORITY);
    }
    
    private void createTask() {
        TaskFormDialog dialog = new TaskFormDialog(null);
        Optional<Task> result = dialog.showAndWait();
        
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
    }
    
    private void editSelectedTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        
        if (selectedTask == null) {
            showMessage("Aviso", "Por favor, seleccione una tarea para editar.", Alert.AlertType.WARNING);
            return;
        }
        
        TaskFormDialog dialog = new TaskFormDialog(selectedTask);
        Optional<Task> result = dialog.showAndWait();
        
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
    }
    
    private void deleteSelectedTask() {
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
    
    private void toggleSelectedTaskStatus() {
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
    
    private void updateStatusBar() {
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
        alert.showAndWait();
    }
    
    /**
     * Opciones de filtro por estado.
     */
    private enum FilterOption {
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
