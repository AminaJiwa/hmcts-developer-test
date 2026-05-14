package com.casework.hmcts.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.casework.hmcts.entity.Task;
import com.casework.hmcts.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void updateTaskPartial_ShouldUpdateTitleOnly() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Title");
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        
        // Act
        Task updated = taskService.updateTaskPartial(taskId, updates);
        
        // Assert
        assertEquals("New Title", updated.getTitle());
        assertEquals("Old Description", updated.getDescription()); // Unchanged
        verify(taskRepository).save(existingTask);
    }
    
    @Test
    void updateTaskPartial_ShouldUpdateMultipleFields() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setStatus("PENDING");
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Title");
        updates.put("status", "IN_PROGRESS");
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        
        // Act
        Task updated = taskService.updateTaskPartial(taskId, updates);
        
        // Assert
        assertEquals("New Title", updated.getTitle());
        assertEquals("IN_PROGRESS", updated.getStatus());
    }
    
    @Test
    void updateTaskPartial_ShouldThrowExceptionWhenTaskNotFound() {
        // Arrange
        Long taskId = 999L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Title");
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        
        // Act & Assert
        try {
            taskService.updateTaskPartial(taskId, updates);
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            // Assert that the exception is of the expected type
            assertTrue(RuntimeException.class.isInstance(e));
        }
    }
    
    @Test
    void updateTaskPartial_ShouldUpdateDueDateTime() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setDueDateTime(LocalDateTime.now().plusDays(1));
        
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(5);
        Map<String, Object> updates = new HashMap<>();
        updates.put("dueDateTime", newDueDate.toString());
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        
        // Act
        Task updated = taskService.updateTaskPartial(taskId, updates);
        
        // Assert
        assertEquals(newDueDate, updated.getDueDateTime());
    }
}