package com.inforgonzalez.todo.crud.list.model;

/**
 * Enumeración para los tipos de tarea.
 */
public enum TaskType {
    STREAMING("Streaming", 1),
    VIDEO_YOUTUBE("Video Normal de YouTube", 2),
    SHORT_YOUTUBE("Short de YouTube", 3),
    REEL_INSTAGRAM("Reel de Instagram", 4),
    POST_REDES("Post en Redes Sociales", 5),
    OTRO("Otro", 6);
    
    private final String displayName;
    private final int order;
    
    TaskType(String displayName, int order) {
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
