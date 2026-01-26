package com.inforgonzalez.todo.crud.list.model;

/**
 * Enumeración para la duración de las tareas.
 */
public enum TaskDuration {
    PUNTUAL("Puntual", 1),
    LARGO("Largo", 2);
    
    private final String displayName;
    private final int order;
    
    TaskDuration(String displayName, int order) {
        this.displayName = displayName;
        this.order = order;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getOrder() {
        return order;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
