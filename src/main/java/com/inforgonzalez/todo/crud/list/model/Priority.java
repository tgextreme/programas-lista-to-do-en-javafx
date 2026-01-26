package com.inforgonzalez.todo.crud.list.model;

/**
 * Enumeración para los niveles de prioridad de las tareas.
 */
public enum Priority {
    BAJA("Baja", 1),
    MEDIA("Media", 2),
    ALTA("Alta", 3);
    
    private final String displayName;
    private final int order;
    
    Priority(String displayName, int order) {
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
