package com.inforgonzalez.todo.crud.list.model;

/**
 * Enumeración para los estados de las tareas.
 */
public enum TaskStatus {
    PENDIENTE("Pendiente", 1),
    EN_PROGRESO("En Progreso", 2),
    HECHA("Hecha", 3);
    
    private final String displayName;
    private final int order;
    
    TaskStatus(String displayName, int order) {
        this.displayName = displayName;
        this.order = order;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getOrder() {
        return order;
    }
    
    /**
     * Obtiene el siguiente estado en el flujo: Pendiente → En progreso → Hecha → Pendiente
     */
    public TaskStatus getNext() {
        return switch (this) {
            case PENDIENTE -> EN_PROGRESO;
            case EN_PROGRESO -> HECHA;
            case HECHA -> PENDIENTE;
        };
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
