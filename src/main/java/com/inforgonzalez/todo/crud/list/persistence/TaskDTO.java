package com.inforgonzalez.todo.crud.list.persistence;

import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;

/**
 * DTO (Data Transfer Object) para la serialización JSON de tareas.
 * Usa strings para dates para formato ISO-8601.
 */
public class TaskDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private String prioridad;  // Enum as String
    private String estado;     // Enum as String
    private String createdAt;  // ISO-8601 String
    private String updatedAt;  // ISO-8601 String
    
    // Nuevos campos
    private Double horasEstimadas;
    private String duracionTarea;  // Enum as String
    private String tipoTarea;      // Enum as String
    private String factibilidad;   // Enum as String
    
    public TaskDTO() {
    }
    
    public TaskDTO(String id, String titulo, String descripcion, String prioridad,
                   String estado, String createdAt, String updatedAt,
                   Double horasEstimadas, String duracionTarea, String tipoTarea, String factibilidad) {
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
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getPrioridad() {
        return prioridad;
    }
    
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Double getHorasEstimadas() {
        return horasEstimadas;
    }
    
    public void setHorasEstimadas(Double horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
    }
    
    public String getDuracionTarea() {
        return duracionTarea;
    }
    
    public void setDuracionTarea(String duracionTarea) {
        this.duracionTarea = duracionTarea;
    }
    
    public String getTipoTarea() {
        return tipoTarea;
    }
    
    public void setTipoTarea(String tipoTarea) {
        this.tipoTarea = tipoTarea;
    }
    
    public String getFactibilidad() {
        return factibilidad;
    }
    
    public void setFactibilidad(String factibilidad) {
        this.factibilidad = factibilidad;
    }
}
