package com.inforgonzalez.todo.crud.list.service;

import com.inforgonzalez.todo.crud.list.model.Priority;
import com.inforgonzalez.todo.crud.list.model.Task;
import com.inforgonzalez.todo.crud.list.model.TaskStatus;
import com.inforgonzalez.todo.crud.list.persistence.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para TaskService.
 */
class TaskServiceTest {
    
    private TaskService service;
    private MockTaskRepository repository;
    
    @BeforeEach
    void setUp() {
        repository = new MockTaskRepository();
        service = new TaskService(repository);
    }
    
    @Test
    void testCreateTask() {
        Task task = service.createTask(
                "Nueva tarea",
                "Descripción",
                Priority.ALTA,
                TaskStatus.PENDIENTE
        );
        
        assertNotNull(task);
        assertEquals("Nueva tarea", task.getTitulo());
        assertEquals(Priority.ALTA, task.getPrioridad());
        assertEquals(1, repository.findAll().size());
    }
    
    @Test
    void testCreateInvalidTask() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createTask("ab", "Desc", Priority.MEDIA, TaskStatus.PENDIENTE);
        });
    }
    
    @Test
    void testUpdateTask() {
        Task task = service.createTask("Original", "Desc", Priority.MEDIA, TaskStatus.PENDIENTE);
        
        Task updated = service.updateTask(
                task.getId(),
                "Actualizado",
                "Nueva desc",
                Priority.ALTA,
                TaskStatus.EN_PROGRESO
        );
        
        assertEquals("Actualizado", updated.getTitulo());
        assertEquals(Priority.ALTA, updated.getPrioridad());
        assertEquals(TaskStatus.EN_PROGRESO, updated.getEstado());
    }
    
    @Test
    void testDeleteTask() {
        Task task = service.createTask("Tarea", "Desc", Priority.MEDIA, TaskStatus.PENDIENTE);
        
        assertTrue(service.deleteTask(task.getId()));
        assertEquals(0, repository.findAll().size());
    }
    
    @Test
    void testToggleTaskStatus() {
        Task task = service.createTask("Tarea", "Desc", Priority.MEDIA, TaskStatus.PENDIENTE);
        
        Task updated = service.toggleTaskStatus(task.getId());
        assertEquals(TaskStatus.EN_PROGRESO, updated.getEstado());
    }
    
    @Test
    void testFilterByText() {
        service.createTask("Tarea importante", "Desc", Priority.ALTA, TaskStatus.PENDIENTE);
        service.createTask("Otra tarea", "Desc", Priority.MEDIA, TaskStatus.PENDIENTE);
        service.createTask("Tarea normal", "Desc", Priority.BAJA, TaskStatus.PENDIENTE);
        
        List<Task> filtered = service.filterByText("importante");
        assertEquals(1, filtered.size());
        assertEquals("Tarea importante", filtered.get(0).getTitulo());
    }
    
    @Test
    void testFilterByStatus() {
        service.createTask("Tarea 1", "Desc", Priority.ALTA, TaskStatus.PENDIENTE);
        service.createTask("Tarea 2", "Desc", Priority.MEDIA, TaskStatus.EN_PROGRESO);
        service.createTask("Tarea 3", "Desc", Priority.BAJA, TaskStatus.HECHA);
        
        List<Task> pending = service.filterByStatus(TaskStatus.PENDIENTE);
        assertEquals(1, pending.size());
        
        List<Task> all = service.filterByStatus(null);
        assertEquals(3, all.size());
    }
    
    @Test
    void testSortByPriority() {
        Task t1 = service.createTask("Baja", "Desc", Priority.BAJA, TaskStatus.PENDIENTE);
        Task t2 = service.createTask("Alta", "Desc", Priority.ALTA, TaskStatus.PENDIENTE);
        Task t3 = service.createTask("Media", "Desc", Priority.MEDIA, TaskStatus.PENDIENTE);
        
        List<Task> tasks = service.getAllTasks();
        List<Task> sorted = service.sortByPriority(tasks);
        
        assertEquals(Priority.ALTA, sorted.get(0).getPrioridad());
        assertEquals(Priority.MEDIA, sorted.get(1).getPrioridad());
        assertEquals(Priority.BAJA, sorted.get(2).getPrioridad());
    }
    
    @Test
    void testGetStatistics() {
        service.createTask("T1", "Desc", Priority.ALTA, TaskStatus.PENDIENTE);
        service.createTask("T2", "Desc", Priority.MEDIA, TaskStatus.EN_PROGRESO);
        service.createTask("T3", "Desc", Priority.BAJA, TaskStatus.HECHA);
        service.createTask("T4", "Desc", Priority.MEDIA, TaskStatus.PENDIENTE);
        
        TaskService.TaskStatistics stats = service.getStatistics();
        
        assertEquals(4, stats.getTotal());
        assertEquals(2, stats.getPending());
        assertEquals(1, stats.getInProgress());
        assertEquals(1, stats.getCompleted());
    }
    
    /**
     * Mock implementation of TaskRepository for testing.
     */
    private static class MockTaskRepository implements TaskRepository {
        private final List<Task> tasks = new ArrayList<>();
        
        @Override
        public List<Task> findAll() {
            return new ArrayList<>(tasks);
        }
        
        @Override
        public Optional<Task> findById(String id) {
            return tasks.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst();
        }
        
        @Override
        public Task save(Task task) {
            tasks.removeIf(t -> t.getId().equals(task.getId()));
            tasks.add(task);
            return task;
        }
        
        @Override
        public boolean deleteById(String id) {
            return tasks.removeIf(t -> t.getId().equals(id));
        }
        
        @Override
        public void saveAll() {
            // No-op for mock
        }
        
        @Override
        public void reload() {
            // No-op for mock
        }
    }
}
