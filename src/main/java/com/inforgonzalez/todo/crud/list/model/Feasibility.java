package com.inforgonzalez.todo.crud.list.model;

/**
 * Enumeración para la factibilidad de las tareas.
 */
public enum Feasibility {
    BAJO("Bajo", 1),
    MEDIO("Medio", 2),
    ALTO("Alto", 3),
    NO_SABE("N/S", 4);
    
    private final String displayName;
    private final int order;
    
    Feasibility(String displayName, int order) {
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
