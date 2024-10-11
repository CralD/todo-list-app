package com.example.task1.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.example.task1.entity.TodoItem;
import com.example.task1.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TodoItemController.class)
class TodoItemControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private TodoItemService todoItemService;

    @InjectMocks
    private TodoItemController todoItemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoItemController).build();
    }

    // Utility method to convert Java objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test: Get all todos
    @Test
    void testGetAllTodos() throws Exception {
        TodoItem todo1 = new TodoItem("Task 1", "Description 1");
        todo1.setId(1L);
        TodoItem todo2 = new TodoItem("Task 2", "Description 2");
        todo2.setId(2L);

        when(todoItemService.getAllTodos()).thenReturn(Arrays.asList(todo1, todo2));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(todoItemService, times(1)).getAllTodos();
    }

    // Test: Get todo by ID
    @Test
    void testGetTodoById() throws Exception {
        TodoItem todo = new TodoItem("Task 1", "Description 1");
        todo.setId(1L);

        when(todoItemService.getTodoById(1L)).thenReturn(Optional.of(todo));

        mockMvc.perform(get("/api/todos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));

        verify(todoItemService, times(1)).getTodoById(1L);
    }

    // Test: Get todo by ID - not found
    @Test
    void testGetTodoById_NotFound() throws Exception {
        when(todoItemService.getTodoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(todoItemService, times(1)).getTodoById(1L);
    }

    // Test: Create new todo item
    @Test
    void testCreateTodoItem() throws Exception {
        TodoItem todo = new TodoItem("Task 1", "Description 1");
        todo.setId(1L);

        when(todoItemService.createTodoItem(any(TodoItem.class))).thenReturn(todo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));

        verify(todoItemService, times(1)).createTodoItem(any(TodoItem.class));
    }

    // Test: Update todo by ID
    @Test
    void testUpdateTodoItem() throws Exception {
        TodoItem updatedTodo = new TodoItem("Updated Task", "Updated Description");
        updatedTodo.setId(1L);

        when(todoItemService.updateTodoItem(eq(1L), any(TodoItem.class))).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(todoItemService, times(1)).updateTodoItem(eq(1L), any(TodoItem.class));
    }

    // Test: Delete todo by ID
    @Test
    void testDeleteTodoItem() throws Exception {
        doNothing().when(todoItemService).deleteTodoItem(1L);

        mockMvc.perform(delete("/api/todos/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(todoItemService, times(1)).deleteTodoItem(1L);
    }
}