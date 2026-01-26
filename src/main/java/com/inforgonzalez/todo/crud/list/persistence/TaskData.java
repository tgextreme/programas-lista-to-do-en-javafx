package com.inforgonzalez.todo.crud.list.persistence;

import java.util.List;

/**
 * Clase wrapper para la serialización JSON.
 * Contiene la versión del formato y la lista de tareas.
 */
public class TaskData {
    private int version;
    private List<TaskDTO> tasks;
    
    public TaskData() {
        this.version = 1;
    }
    
    public TaskData(int version, List<TaskDTO> tasks) {
        this.version = version;
        this.tasks = tasks;
    }
    
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    public List<TaskDTO> getTasks() {
        return tasks;
    }
    
    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
