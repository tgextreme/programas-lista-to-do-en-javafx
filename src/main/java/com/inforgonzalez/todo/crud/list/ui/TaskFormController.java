package com.inforgonzalez.todo.crud.list.ui;

import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.Task;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;
import com.inforgonzalez.todo.crud.list.model.TaskDuration;
import com.inforgonzalez.todo.crud.list.model.TaskType;
import com.inforgonzalez.todo.crud.list.model.Feasibility;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Optional;

/**
 * Controlador para el formulario de tareas.
 */
public class TaskFormController {
    
    @FXML
    private TextField tituloField;
    
    @FXML
    private TextArea descripcionArea;
    
    @FXML
    private ComboBox<Priority> prioridadCombo;
    
    @FXML
    private ComboBox<TaskStatus> estadoCombo;
    
    @FXML
    private TextField horasEstimadasField;
    
    @FXML
    private ComboBox<TaskDuration> duracionCombo;
    
    @FXML
    private ComboBox<TaskType> tipoCombo;
    
    @FXML
    private ComboBox<Feasibility> factibilidadCombo;
    
    private Task task;
    private Dialog<Task> dialog;
    
    /**
     * Inicialización automática después de cargar el FXML.
     */
    @FXML
    private void initialize() {
        // Inicializar ComboBoxes
        prioridadCombo.getItems().addAll(Priority.values());
        prioridadCombo.setValue(Priority.MEDIA);
        
        estadoCombo.getItems().addAll(TaskStatus.values());
        estadoCombo.setValue(TaskStatus.PENDIENTE);
        
        duracionCombo.getItems().addAll(TaskDuration.values());
        duracionCombo.setValue(TaskDuration.PUNTUAL);
        
        tipoCombo.getItems().addAll(TaskType.values());
        tipoCombo.setValue(TaskType.OTRO);
        
        factibilidadCombo.getItems().addAll(Feasibility.values());
        factibilidadCombo.setValue(Feasibility.NO_SABE);
        
        // Validar que el campo de horas solo acepte números
        horasEstimadasField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                horasEstimadasField.setText(oldValue);
            }
        });
    }
    
    /**
     * Muestra el diálogo de formulario y retorna la tarea si fue confirmada.
     * @param task La tarea a editar, o null para crear una nueva
     * @param owner La ventana padre para que el diálogo aparezca en la misma pantalla
     */
    public Optional<Task> showDialog(Task task, Window owner) throws IOException {
        this.task = task;
        
        // Cargar el FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskForm.fxml"));
        loader.setController(this);
        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(loader.load());
        
        // Crear el diálogo
        dialog = new Dialog<>();
        dialog.setTitle(task == null ? "Nueva Tarea" : "Editar Tarea");
        dialog.setHeaderText(task == null ? "Crear una nueva tarea" : "Editar tarea existente");
        dialog.setDialogPane(dialogPane);
        
        // IMPORTANTE: Establecer el owner para que aparezca en la misma pantalla
        if (owner != null) {
            dialog.initOwner(owner);
        }
        
        // Agregar botones
        ButtonType saveButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
        
        // Cargar datos si es edición
        if (task != null) {
            loadTaskData();
        }
        
        // Botón Guardar siempre habilitado; validar al pulsar
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(false);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
            if (!validateForm()) {
                e.consume(); // mantener el diálogo abierto si es inválido
            }
        });
        
        // Configurar el resultado del diálogo
        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                if (validateForm()) {
                    return createTaskFromForm();
                }
            }
            return null;
        });
        
        // Mostrar el diálogo y retornar el resultado
        return dialog.showAndWait();
    }
    
    private void loadTaskData() {
        tituloField.setText(task.getTitulo());
        descripcionArea.setText(task.getDescripcion());
        prioridadCombo.setValue(task.getPrioridad());
        estadoCombo.setValue(task.getEstado());
        
        // Cargar nuevos campos
        if (task.getHorasEstimadas() != null) {
            horasEstimadasField.setText(task.getHorasEstimadas().toString());
        }
        if (task.getDuracionTarea() != null) {
            duracionCombo.setValue(task.getDuracionTarea());
        }
        if (task.getTipoTarea() != null) {
            tipoCombo.setValue(task.getTipoTarea());
        }
        if (task.getFactibilidad() != null) {
            factibilidadCombo.setValue(task.getFactibilidad());
        }
    }
    
    private boolean validateForm() {
        String titulo = tituloField.getText().trim();
        
        if (titulo.isEmpty()) {
            showAlert("Validación", "El título es obligatorio.");
            return false;
        }
        
        if (titulo.length() < 3) {
            showAlert("Validación", "El título debe tener al menos 3 caracteres.");
            return false;
        }
        
        return true;
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
        
        // Guardar nuevos campos
        String horasText = horasEstimadasField.getText().trim();
        if (!horasText.isEmpty()) {
            try {
                newTask.setHorasEstimadas(Double.parseDouble(horasText));
            } catch (NumberFormatException e) {
                newTask.setHorasEstimadas(0.0);
            }
        } else {
            newTask.setHorasEstimadas(0.0);
        }
        
        newTask.setDuracionTarea(duracionCombo.getValue());
        newTask.setTipoTarea(tipoCombo.getValue());
        newTask.setFactibilidad(factibilidadCombo.getValue());
        
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
