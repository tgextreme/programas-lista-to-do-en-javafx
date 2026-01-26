package com.inforgonzalez.todo.crud.list.service;

import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.Task;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;
import com.inforgonzalez.todo.crud.list.model.TaskDuration;
import com.inforgonzalez.todo.crud.list.model.TaskType;
import com.inforgonzalez.todo.crud.list.model.Feasibility;
import com.inforgonzalez.todo.crud.list.persistence.TaskRepository;
import com.inforgonzalez.todo.crud.list.persistence.TaskDTO;
import com.inforgonzalez.todo.crud.list.persistence.TaskData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para las tareas.
 * Implementa los casos de uso: CRUD, filtrado, ordenación.
 */
public class TaskService {
    
    private final TaskRepository repository;
    
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Crea una nueva tarea.
     */
    public Task createTask(String titulo, String descripcion, Priority prioridad, TaskStatus estado) {
        Task task = new Task();
        task.setTitulo(titulo);
        task.setDescripcion(descripcion);
        task.setPrioridad(prioridad);
        task.setEstado(estado);
        
        if (!task.isValid()) {
            throw new IllegalArgumentException("La tarea no es válida. El título debe tener al menos 3 caracteres.");
        }
        
        return repository.save(task);
    }
    
    /**
     * Actualiza una tarea existente.
     */
    public Task updateTask(String id, String titulo, String descripcion, Priority prioridad, TaskStatus estado) {
        Optional<Task> existingTask = repository.findById(id);
        
        if (existingTask.isEmpty()) {
            throw new IllegalArgumentException("Tarea no encontrada con ID: " + id);
        }
        
        Task task = existingTask.get();
        task.setTitulo(titulo);
        task.setDescripcion(descripcion);
        task.setPrioridad(prioridad);
        task.setEstado(estado);
        
        if (!task.isValid()) {
            throw new IllegalArgumentException("La tarea no es válida. El título debe tener al menos 3 caracteres.");
        }
        
        return repository.save(task);
    }
    
    /**
     * Elimina una tarea por ID.
     */
    public boolean deleteTask(String id) {
        return repository.deleteById(id);
    }
    
    /**
     * Obtiene todas las tareas.
     */
    public List<Task> getAllTasks() {
        return repository.findAll();
    }
    
    /**
     * Busca una tarea por ID.
     */
    public Optional<Task> getTaskById(String id) {
        return repository.findById(id);
    }
    
    /**
     * Cambia el estado de una tarea al siguiente en el flujo.
     */
    public Task toggleTaskStatus(String id) {
        Optional<Task> taskOpt = repository.findById(id);
        
        if (taskOpt.isEmpty()) {
            throw new IllegalArgumentException("Tarea no encontrada con ID: " + id);
        }
        
        Task task = taskOpt.get();
        task.toggleEstado();
        return repository.save(task);
    }
    
    /**
     * Filtra tareas por texto (busca en título y descripción).
     */
    public List<Task> filterByText(String searchText) {
        return repository.findAll().stream()
                .filter(task -> task.matches(searchText))
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra tareas por estado.
     */
    public List<Task> filterByStatus(TaskStatus status) {
        if (status == null) {
            return repository.findAll();
        }
        
        return repository.findAll().stream()
                .filter(task -> task.getEstado() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra tareas por texto y estado combinados.
     */
    public List<Task> filterTasks(String searchText, TaskStatus status) {
        return repository.findAll().stream()
                .filter(task -> task.matches(searchText))
                .filter(task -> status == null || task.getEstado() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Ordena tareas por prioridad (Alta → Media → Baja) y luego por fecha.
     */
    public List<Task> sortByPriority(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator
                        .comparing((Task t) -> t.getPrioridad().getOrder(), Comparator.reverseOrder())
                        .thenComparing(Task::getCreatedAt))
                .collect(Collectors.toList());
    }
    
    /**
     * Ordena tareas por estado.
     */
    public List<Task> sortByStatus(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator
                        .comparing((Task t) -> t.getEstado().getOrder())
                        .thenComparing(Task::getCreatedAt))
                .collect(Collectors.toList());
    }
    
    /**
     * Ordena tareas por título (alfabético).
     */
    public List<Task> sortByTitle(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getTitulo, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }
    
    /**
     * Ordena tareas por fecha de creación (más reciente primero).
     */
    public List<Task> sortByDate(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * Enum para los criterios de ordenación.
     */
    public enum SortCriteria {
        PRIORITY("Prioridad"),
        STATUS("Estado"),
        TITLE("Título"),
        DATE("Fecha");
        
        private final String displayName;
        
        SortCriteria(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    /**
     * Ordena tareas según el criterio especificado.
     */
    public List<Task> sortTasks(List<Task> tasks, SortCriteria criteria) {
        return switch (criteria) {
            case PRIORITY -> sortByPriority(tasks);
            case STATUS -> sortByStatus(tasks);
            case TITLE -> sortByTitle(tasks);
            case DATE -> sortByDate(tasks);
        };
    }
    
    /**
     * Obtiene estadísticas de las tareas.
     */
    public TaskStatistics getStatistics() {
        List<Task> allTasks = repository.findAll();
        
        long totalTasks = allTasks.size();
        long pendingTasks = allTasks.stream().filter(t -> t.getEstado() == TaskStatus.PENDIENTE).count();
        long inProgressTasks = allTasks.stream().filter(t -> t.getEstado() == TaskStatus.EN_PROGRESO).count();
        long completedTasks = allTasks.stream().filter(t -> t.getEstado() == TaskStatus.HECHA).count();
        
        return new TaskStatistics(totalTasks, pendingTasks, inProgressTasks, completedTasks);
    }
    
    /**
     * Clase para representar estadísticas de tareas.
     */
    public static class TaskStatistics {
        private final long total;
        private final long pending;
        private final long inProgress;
        private final long completed;
        
        public TaskStatistics(long total, long pending, long inProgress, long completed) {
            this.total = total;
            this.pending = pending;
            this.inProgress = inProgress;
            this.completed = completed;
        }
        
        public long getTotal() {
            return total;
        }
        
        public long getPending() {
            return pending;
        }
        
        public long getInProgress() {
            return inProgress;
        }
        
        public long getCompleted() {
            return completed;
        }
        
        @Override
        public String toString() {
            return String.format("Total: %d | Pendientes: %d | En Progreso: %d | Completadas: %d",
                    total, pending, inProgress, completed);
        }
    }

    // =====================
    // Import/Export helpers
    // =====================

    /**
     * Exporta todas las tareas a un archivo JSON.
     */
    public void exportToJson(Path outputFile) throws IOException {
        List<TaskDTO> dtos = getAllTasks().stream()
                .map(this::toDTO)
                .collect(java.util.stream.Collectors.toList());
        TaskData data = new TaskData(1, dtos);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);
        Files.writeString(outputFile, json);
    }

    /**
     * Importa tareas desde un archivo JSON. Si replace=true, reemplaza las existentes.
     * Devuelve el número de tareas importadas.
     */
    public int importFromJson(Path inputFile, boolean replace) throws IOException {
        String json = Files.readString(inputFile);
        Gson gson = new GsonBuilder().create();
        TaskData data = gson.fromJson(json, TaskData.class);
        if (data == null || data.getTasks() == null) return 0;

        List<Task> imported = data.getTasks().stream()
                .map(this::fromDTO)
                .filter(Task::isValid)
                .collect(java.util.stream.Collectors.toList());

        if (replace) {
            // Eliminar todas las tareas existentes
            for (Task t : getAllTasks()) {
                repository.deleteById(t.getId());
            }
        }
        // Guardar importadas
        for (Task t : imported) {
            repository.save(t);
        }
        return imported.size();
    }

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

    private Task fromDTO(TaskDTO dto) {
        Priority prioridad = Priority.valueOf(dto.getPrioridad());
        TaskStatus estado = TaskStatus.valueOf(dto.getEstado());
        Instant createdAt = Instant.parse(dto.getCreatedAt());
        Instant updatedAt = Instant.parse(dto.getUpdatedAt());

        Double horasEstimadas = dto.getHorasEstimadas() != null ? dto.getHorasEstimadas() : 0.0;
        TaskDuration duracion = dto.getDuracionTarea() != null ? TaskDuration.valueOf(dto.getDuracionTarea()) : TaskDuration.PUNTUAL;
        TaskType tipo = dto.getTipoTarea() != null ? TaskType.valueOf(dto.getTipoTarea()) : TaskType.OTRO;
        Feasibility feas = dto.getFactibilidad() != null ? Feasibility.valueOf(dto.getFactibilidad()) : Feasibility.NO_SABE;

        return new Task(
                dto.getId(),
                dto.getTitulo(),
                dto.getDescripcion(),
                prioridad,
                estado,
                createdAt,
                updatedAt,
                horasEstimadas,
                duracion,
                tipo,
                feas
        );
    }
}
