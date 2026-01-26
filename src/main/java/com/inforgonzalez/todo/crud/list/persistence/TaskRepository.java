package com.inforgonzalez.todo.crud.list.persistence;

import com.inforgonzalez.todo.crud.list.model.Task;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de tareas.
 * Define las operaciones de persistencia.
 */
public interface TaskRepository {
    
    /**
     * Obtiene todas las tareas.
     */
    List<Task> findAll();
    
    /**
     * Busca una tarea por ID.
     */
    Optional<Task> findById(String id);
    
    /**
     * Guarda o actualiza una tarea.
     */
    Task save(Task task);
    
    /**
     * Elimina una tarea por ID.
     */
    boolean deleteById(String id);
    
    /**
     * Guarda todas las tareas en el almacenamiento persistente.
     */
    void saveAll();
    
    /**
     * Recarga las tareas desde el almacenamiento.
     */
    void reload();
}
