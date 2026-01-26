package com.inforgonzalez.todo.crud.list;

import com.inforgonzalez.todo.crud.list.persistence.JsonTaskRepository;
import com.inforgonzalez.todo.crud.list.persistence.TaskRepository;
import com.inforgonzalez.todo.crud.list.service.TaskService;
import com.inforgonzalez.todo.crud.list.ui.MainViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * TODO CRUD List - Aplicación de Gestión de Tareas
 * 
 * @author InforGonzalez
 * @version 1.0.0
 */
public class App extends Application {
    
    private TaskService taskService;
    
    @Override
    public void init() throws Exception {
        // Inicializar servicios
        TaskRepository repository = new JsonTaskRepository("tasks.json");
        taskService = new TaskService(repository);
        
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║   TODO CRUD List - Aplicación de Gestión de Tareas  ║");
        System.out.println("║              Inicializando...                        ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();
            
            // Obtener el controlador y configurar el servicio
            MainViewController controller = loader.getController();
            controller.setTaskService(taskService);
            
            // Configurar la escena
            Scene scene = new Scene(root, 1000, 600);
            
            // Cargar hoja de estilos CSS (opcional)
            try {
                String css = getClass().getResource("/styles/application.css").toExternalForm();
                scene.getStylesheets().add(css);
            } catch (Exception e) {
                System.out.println("No se pudo cargar el CSS (opcional): " + e.getMessage());
            }
            
            // Configurar el stage
            primaryStage.setTitle("TODO CRUD List - Gestor de Tareas");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(500);
            // Establecer icono de la aplicación para la barra de tareas
            try {
                Image icon = null;
                try {
                    // Intentar icono empaquetado en recursos
                    icon = new Image(getClass().getResourceAsStream("/icon.png"));
                } catch (Exception ignore) {}

                if (icon == null) {
                    // Fallbacks: buscar archivo en diferentes ubicaciones del app-image
                    String userDir = System.getProperty("user.dir");
                    java.io.File dir = new java.io.File(userDir);
                    java.io.File parent = dir.getParentFile();
                    java.io.File[] candidates = new java.io.File[] {
                        new java.io.File(dir, "icon.png"),
                        new java.io.File(dir, "logo.png"),
                        parent != null ? new java.io.File(parent, "icon.png") : null,
                        parent != null ? new java.io.File(parent, "logo.png") : null,
                        parent != null ? new java.io.File(new java.io.File(parent, "app"), "icon.png") : null,
                        parent != null ? new java.io.File(new java.io.File(parent, "app"), "logo.png") : null
                    };
                    for (java.io.File f : candidates) {
                        if (f != null && f.exists()) {
                            icon = new Image(f.toURI().toString());
                            break;
                        }
                    }
                }

                if (icon != null) {
                    primaryStage.getIcons().add(icon);
                }
            } catch (Exception e) {
                System.out.println("No se pudo establecer el icono: " + e.getMessage());
            }
            primaryStage.show();
            
            System.out.println("╔══════════════════════════════════════════════════════╗");
            System.out.println("║              Iniciada exitosamente ✓                 ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

