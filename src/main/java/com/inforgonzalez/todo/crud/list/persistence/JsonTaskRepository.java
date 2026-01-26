package com.inforgonzalez.todo.crud.list.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.Task;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;
import com.inforgonzalez.todo.crud.list.model.TaskDuration;
import com.inforgonzalez.todo.crud.list.model.TaskType;
import com.inforgonzalez.todo.crud.list.model.Feasibility;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del repositorio de tareas con persistencia en JSON.
 * Maneja guardado/carga robusto con archivos temporales y backups.
 */
public class JsonTaskRepository implements TaskRepository {
    
    private static final String DEFAULT_FILE = "tasks.json";
    private static final String BACKUP_SUFFIX = ".backup";
    
    private final Path filePath;
    private final Gson gson;
    private final Map<String, Task> tasks;
    
    public JsonTaskRepository() {
        this(DEFAULT_FILE);
    }
    
    public JsonTaskRepository(String fileName) {
        this.filePath = Paths.get(fileName);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.tasks = new LinkedHashMap<>();
        loadFromFile();
    }
    
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }
    
    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }
    
    @Override
    public Task save(Task task) {
        if (task == null || !task.isValid()) {
            throw new IllegalArgumentException("Task inválida o incompleta");
        }
        
        tasks.put(task.getId(), task);
        saveAll();
        return task;
    }
    
    @Override
    public boolean deleteById(String id) {
        boolean removed = tasks.remove(id) != null;
        if (removed) {
            saveAll();
        }
        return removed;
    }
    
    @Override
    public void saveAll() {
        try {
            // Convertir tareas a DTOs
            List<TaskDTO> taskDTOs = tasks.values().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            
            TaskData data = new TaskData(1, taskDTOs);
            String json = gson.toJson(data);
            
            // Guardado atómico: escribir a archivo temporal y luego renombrar
            Path tempFile = Paths.get(filePath.toString() + ".tmp");
            Files.writeString(tempFile, json, StandardOpenOption.CREATE, 
                            StandardOpenOption.TRUNCATE_EXISTING);
            
            // Si existe el archivo original, hacer backup
            if (Files.exists(filePath)) {
                Path backupFile = Paths.get(filePath.toString() + BACKUP_SUFFIX);
                Files.copy(filePath, backupFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Mover archivo temporal al definitivo
            Files.move(tempFile, filePath, StandardCopyOption.REPLACE_EXISTING, 
                      StandardCopyOption.ATOMIC_MOVE);
            
        } catch (IOException e) {
            System.err.println("Error al guardar tareas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void reload() {
        tasks.clear();
        loadFromFile();
    }
    
    /**
     * Carga las tareas desde el archivo JSON.
     * Maneja errores de forma robusta: archivo no existe, JSON corrupto, etc.
     */
    private void loadFromFile() {
        try {
            if (!Files.exists(filePath)) {
                System.out.println("Archivo de tareas no existe. Iniciando con lista vacía.");
                return;
            }
            
            String json = Files.readString(filePath);
            
            if (json.trim().isEmpty()) {
                System.out.println("Archivo de tareas vacío. Iniciando con lista vacía.");
                return;
            }
            
            TaskData data = gson.fromJson(json, TaskData.class);
            
            if (data == null || data.getTasks() == null) {
                System.out.println("Datos de tareas inválidos. Iniciando con lista vacía.");
                return;
            }
            
            // Convertir DTOs a entidades
            for (TaskDTO dto : data.getTasks()) {
                try {
                    Task task = fromDTO(dto);
                    if (task.isValid()) {
                        tasks.put(task.getId(), task);
                    } else {
                        System.err.println("Tarea inválida ignorada: " + dto.getId());
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar tarea: " + e.getMessage());
                }
            }
            
            System.out.println("Cargadas " + tasks.size() + " tareas desde " + filePath);
            
        } catch (JsonSyntaxException e) {
            handleCorruptedJson(e);
        } catch (IOException e) {
            System.err.println("Error al leer archivo de tareas: " + e.getMessage());
            tryRestoreFromBackup();
        }
    }
    
    /**
     * Maneja el caso de JSON corrupto intentando restaurar desde backup.
     */
    private void handleCorruptedJson(JsonSyntaxException e) {
        System.err.println("⚠️  ADVERTENCIA: El archivo JSON está corrupto.");
        System.err.println("Error: " + e.getMessage());
        
        if (tryRestoreFromBackup()) {
            System.out.println("✓ Restaurado desde backup exitosamente.");
        } else {
            System.err.println("✗ No se pudo restaurar. Iniciando con lista vacía.");
            System.err.println("  El archivo corrupto se ha guardado como: " + filePath + ".corrupted");
            
            try {
                // Guardar archivo corrupto para análisis
                Path corruptedFile = Paths.get(filePath.toString() + ".corrupted");
                Files.copy(filePath, corruptedFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                System.err.println("No se pudo guardar copia del archivo corrupto.");
            }
        }
    }
    
    /**
     * Intenta restaurar desde el archivo de backup.
     */
    private boolean tryRestoreFromBackup() {
        Path backupFile = Paths.get(filePath.toString() + BACKUP_SUFFIX);
        
        if (!Files.exists(backupFile)) {
            System.err.println("No existe archivo de backup.");
            return false;
        }
        
        try {
            String json = Files.readString(backupFile);
            TaskData data = gson.fromJson(json, TaskData.class);
            
            if (data != null && data.getTasks() != null) {
                for (TaskDTO dto : data.getTasks()) {
                    Task task = fromDTO(dto);
                    if (task.isValid()) {
                        tasks.put(task.getId(), task);
                    }
                }
                
                // Restaurar el archivo principal desde el backup
                Files.copy(backupFile, filePath, StandardCopyOption.REPLACE_EXISTING);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error al restaurar desde backup: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Convierte una Task a TaskDTO para serialización.
     */
    private TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitulo(),
                task.getDescripcion(),
                task.getPrioridad().name(),
                task.getEstado().name(),
                task.getCreatedAt().toString(),
                task.getUpdatedAt().toString(),
                task.getHorasEstimadas(),
                task.getDuracionTarea() != null ? task.getDuracionTarea().name() : null,
                task.getTipoTarea() != null ? task.getTipoTarea().name() : null,
                task.getFactibilidad() != null ? task.getFactibilidad().name() : null
        );
    }
    
    /**
     * Convierte un TaskDTO a Task (deserialización).
     */
    private Task fromDTO(TaskDTO dto) {
        try {
            Priority prioridad = Priority.valueOf(dto.getPrioridad());
            TaskStatus estado = TaskStatus.valueOf(dto.getEstado());
            Instant createdAt = Instant.parse(dto.getCreatedAt());
            Instant updatedAt = Instant.parse(dto.getUpdatedAt());
            
            // Parsear nuevos campos con valores por defecto si son null
            Double horasEstimadas = dto.getHorasEstimadas() != null ? dto.getHorasEstimadas() : 0.0;
            
            TaskDuration duracionTarea = TaskDuration.PUNTUAL;
            if (dto.getDuracionTarea() != null) {
                try {
                    duracionTarea = TaskDuration.valueOf(dto.getDuracionTarea());
                } catch (IllegalArgumentException e) {
                    // Usar valor por defecto si no se puede parsear
                }
            }
            
            TaskType tipoTarea = TaskType.OTRO;
            if (dto.getTipoTarea() != null) {
                try {
                    tipoTarea = TaskType.valueOf(dto.getTipoTarea());
                } catch (IllegalArgumentException e) {
                    // Usar valor por defecto si no se puede parsear
                }
            }
            
            Feasibility factibilidad = Feasibility.NO_SABE;
            if (dto.getFactibilidad() != null) {
                try {
                    factibilidad = Feasibility.valueOf(dto.getFactibilidad());
                } catch (IllegalArgumentException e) {
                    // Usar valor por defecto si no se puede parsear
                }
            }
            
            return new Task(
                    dto.getId(),
                    dto.getTitulo(),
                    dto.getDescripcion(),
                    prioridad,
                    estado,
                    createdAt,
                    updatedAt,
                    horasEstimadas,
                    duracionTarea,
                    tipoTarea,
                    factibilidad
            );
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new IllegalArgumentException("Error al parsear TaskDTO: " + e.getMessage(), e);
        }
    }
}
