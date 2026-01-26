package com.inforgonzalez.todo.crud.list.ui;

import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.Task;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Diálogo para crear o editar una tarea con JavaFX.
 */
public class TaskFormDialog extends Dialog<Task> {
    
    private final TextField tituloField;
    private final TextArea descripcionArea;
    private final ComboBox<Priority> prioridadCombo;
    private final ComboBox<TaskStatus> estadoCombo;
    
    private final Task task;
    
    /**
     * Constructor para crear una nueva tarea.
     */
    public TaskFormDialog(Task task) {
        this.task = task;
        
        // Configurar título del diálogo
        setTitle(task == null ? "Nueva Tarea" : "Editar Tarea");
        setHeaderText(task == null ? "Crear una nueva tarea" : "Editar tarea existente");
        
        // Inicializar componentes
        tituloField = new TextField();
        tituloField.setPromptText("Título de la tarea");
        
        descripcionArea = new TextArea();
        descripcionArea.setPromptText("Descripción detallada (opcional)");
        descripcionArea.setPrefRowCount(4);
        descripcionArea.setWrapText(true);
        
        prioridadCombo = new ComboBox<>();
        prioridadCombo.getItems().addAll(Priority.values());
        prioridadCombo.setValue(Priority.MEDIA);
        
        estadoCombo = new ComboBox<>();
        estadoCombo.getItems().addAll(TaskStatus.values());
        estadoCombo.setValue(TaskStatus.PENDIENTE);
        
        // Configurar layout
        setupLayout();
        
        // Cargar datos si es edición
        if (task != null) {
            loadTaskData();
        }
        
        // Configurar botones
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
        
        // Validación y conversión del resultado
        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                if (validateForm()) {
                    return createTaskFromForm();
                } else {
                    return null;
                }
            }
            return null;
        });
        
        // Botón Guardar siempre habilitado; validar al pulsar
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
            if (!validateForm()) {
                e.consume(); // mantener el diálogo abierto si es inválido
            }
        });
    }
    
    private void setupLayout() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Título
        Label tituloLabel = new Label("Título *:");
        grid.add(tituloLabel, 0, 0);
        grid.add(tituloField, 1, 0);
        GridPane.setHgrow(tituloField, javafx.scene.layout.Priority.ALWAYS);
        
        // Descripción
        Label descripcionLabel = new Label("Descripción:");
        grid.add(descripcionLabel, 0, 1);
        grid.add(descripcionArea, 1, 1);
        GridPane.setHgrow(descripcionArea, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setVgrow(descripcionArea, javafx.scene.layout.Priority.ALWAYS);
        
        // Prioridad
        Label prioridadLabel = new Label("Prioridad *:");
        grid.add(prioridadLabel, 0, 2);
        grid.add(prioridadCombo, 1, 2);
        
        // Estado
        Label estadoLabel = new Label("Estado *:");
        grid.add(estadoLabel, 0, 3);
        grid.add(estadoCombo, 1, 3);
        
        getDialogPane().setContent(grid);
    }
    
    private void loadTaskData() {
        tituloField.setText(task.getTitulo());
        descripcionArea.setText(task.getDescripcion());
        prioridadCombo.setValue(task.getPrioridad());
        estadoCombo.setValue(task.getEstado());
    }
    
    private boolean validateForm() {
        if (!isTitleValid()) {
            showAlert("Validación", "El título debe tener al menos 3 caracteres.");
            return false;
        }
        return true;
    }

    private boolean isTitleValid() {
        String titulo = tituloField.getText() == null ? "" : tituloField.getText().trim();
        return titulo.length() >= 3;
    }
    
    private Task createTaskFromForm() {
        Task newTask;
        
        if (task == null) {
            newTask = new Task();
        } else {
            newTask = task;
        }
        
        newTask.setTitulo(tituloField.getText().trim());
        newTask.setDescripcion(descripcionArea.getText().trim());
        newTask.setPrioridad(prioridadCombo.getValue());
        newTask.setEstado(estadoCombo.getValue());
        
        return newTask;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
