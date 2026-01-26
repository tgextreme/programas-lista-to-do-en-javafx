package com.inforgonzalez.todo.crud.list.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que representa una tarea en el sistema TODO.
 */
public class Task {
    private String id;
    private String titulo;
    private String descripcion;
    private Priority prioridad;
    private TaskStatus estado;
    private Instant createdAt;
    private Instant updatedAt;
    
    // Nuevos campos
    private Double horasEstimadas;
    private TaskDuration duracionTarea;
    private TaskType tipoTarea;
    private Feasibility factibilidad;
    
    /**
     * Constructor para crear una nueva tarea con valores por defecto.
     */
    public Task() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.prioridad = Priority.MEDIA;
        this.estado = TaskStatus.PENDIENTE;
        this.horasEstimadas = 0.0;
        this.duracionTarea = TaskDuration.PUNTUAL;
        this.tipoTarea = TaskType.OTRO;
        this.factibilidad = Feasibility.NO_SABE;
    }
    
    /**
     * Constructor completo (usado principalmente para deserialización).
     */
    public Task(String id, String titulo, String descripcion, Priority prioridad, 
                TaskStatus estado, Instant createdAt, Instant updatedAt,
                Double horasEstimadas, TaskDuration duracionTarea, 
                TaskType tipoTarea, Feasibility factibilidad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.horasEstimadas = horasEstimadas;
        this.duracionTarea = duracionTarea;
        this.tipoTarea = tipoTarea;
        this.factibilidad = factibilidad;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public Priority getPrioridad() {
        return prioridad;
    }
    
    public TaskStatus getEstado() {
        return estado;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public Double getHorasEstimadas() {
        return horasEstimadas;
    }
    
    public TaskDuration getDuracionTarea() {
        return duracionTarea;
    }
    
    public TaskType getTipoTarea() {
        return tipoTarea;
    }
    
    public Feasibility getFactibilidad() {
        return factibilidad;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo != null ? titulo.trim() : null;
        updateTimestamp();
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion != null ? descripcion.trim() : null;
        updateTimestamp();
    }
    
    public void setPrioridad(Priority prioridad) {
        this.prioridad = prioridad;
        updateTimestamp();
    }
    
    public void setEstado(TaskStatus estado) {
        this.estado = estado;
        updateTimestamp();
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setHorasEstimadas(Double horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
        updateTimestamp();
    }
    
    public void setDuracionTarea(TaskDuration duracionTarea) {
        this.duracionTarea = duracionTarea;
        updateTimestamp();
    }
    
    public void setTipoTarea(TaskType tipoTarea) {
        this.tipoTarea = tipoTarea;
        updateTimestamp();
    }
    
    public void setFactibilidad(Feasibility factibilidad) {
        this.factibilidad = factibilidad;
        updateTimestamp();
    }
    
    /**
     * Actualiza el timestamp de última modificación.
     */
    private void updateTimestamp() {
        this.updatedAt = Instant.now();
    }
    
    /**
     * Valida que la tarea tenga todos los campos obligatorios.
     */
    public boolean isValid() {
        return titulo != null && !titulo.trim().isEmpty() && titulo.trim().length() >= 3
                && prioridad != null && estado != null;
    }
    
    /**
     * Cambia el estado al siguiente en el flujo.
     */
    public void toggleEstado() {
        this.estado = estado.getNext();
        updateTimestamp();
    }
    
    /**
     * Verifica si la tarea coincide con un texto de búsqueda.
     */
    public boolean matches(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return true;
        }
        
        String search = searchText.toLowerCase();
        boolean matchesTitulo = titulo != null && titulo.toLowerCase().contains(search);
        boolean matchesDescripcion = descripcion != null && descripcion.toLowerCase().contains(search);
        
        return matchesTitulo || matchesDescripcion;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", prioridad=" + prioridad +
                ", estado=" + estado +
                '}';
    }
}
