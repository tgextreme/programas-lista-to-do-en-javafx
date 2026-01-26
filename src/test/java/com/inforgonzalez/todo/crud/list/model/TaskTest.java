package com.inforgonzalez.todo.crud.list.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Task.
 */
class TaskTest {
    
    @Test
    void testCreateTask() {
        Task task = new Task();
        assertNotNull(task.getId());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        assertEquals(Priority.MEDIA, task.getPrioridad());
        assertEquals(TaskStatus.PENDIENTE, task.getEstado());
    }
    
    @Test
    void testTaskValidation() {
        Task task = new Task();
        
        // Task sin título no es válida
        assertFalse(task.isValid());
        
        // Task con título muy corto no es válida
        task.setTitulo("ab");
        assertFalse(task.isValid());
        
        // Task con título válido es válida
        task.setTitulo("Tarea de prueba");
        assertTrue(task.isValid());
    }
    
    @Test
    void testToggleEstado() {
        Task task = new Task();
        task.setTitulo("Test");
        
        assertEquals(TaskStatus.PENDIENTE, task.getEstado());
        
        task.toggleEstado();
        assertEquals(TaskStatus.EN_PROGRESO, task.getEstado());
        
        task.toggleEstado();
        assertEquals(TaskStatus.HECHA, task.getEstado());
        
        task.toggleEstado();
        assertEquals(TaskStatus.PENDIENTE, task.getEstado());
    }
    
    @Test
    void testMatches() {
        Task task = new Task();
        task.setTitulo("Implementar feature X");
        task.setDescripcion("Agregar funcionalidad de búsqueda");
        
        assertTrue(task.matches("feature"));
        assertTrue(task.matches("FEATURE"));
        assertTrue(task.matches("búsqueda"));
        assertFalse(task.matches("test"));
        assertTrue(task.matches("")); // Empty string matches all
        assertTrue(task.matches(null)); // Null matches all
    }
    
    @Test
    void testTituloTrim() {
        Task task = new Task();
        task.setTitulo("  Título con espacios  ");
        assertEquals("Título con espacios", task.getTitulo());
    }
}
