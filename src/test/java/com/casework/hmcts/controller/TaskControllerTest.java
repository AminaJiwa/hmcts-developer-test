package com.casework.hmcts.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.casework.hmcts.entity.Task;
import com.casework.hmcts.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void patchUpdateTask_ShouldReturn200WithUpdatedTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Title");
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Title");
        
        when(taskService.updateTaskPartial(eq(taskId), any())).thenReturn(updatedTask);
        
        // Act & Assert
        mockMvc.perform(patch("/api/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }
    
    @Test
    void patchUpdateTask_ShouldReturn400WhenServiceThrowsException() throws Exception {
        // Arrange
        Long taskId = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Title");
        
        when(taskService.updateTaskPartial(eq(taskId), any()))
            .thenThrow(new RuntimeException("Task not found"));
        
        // Act & Assert
        mockMvc.perform(patch("/api/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Task not found"));
    }
}