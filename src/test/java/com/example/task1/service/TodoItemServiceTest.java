package com.example.task1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.task1.dao.TodoItemRepository;
import com.example.task1.entity.TodoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TodoServiceTest {

    @InjectMocks
    private TodoItemService todoService;

    @Mock
    private TodoItemRepository todoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindAll() {
        // Arrange
        TodoItem todo1 = new TodoItem("Task 1", "Description 1");
        todo1.setId(1L);  // Manually set ID
        TodoItem todo2 = new TodoItem("Task 2", "Description 2");
        todo2.setId(2L);
        List<TodoItem> todos = Arrays.asList(todo1, todo2);
        when(todoRepository.findAll()).thenReturn(todos);

        // Act
        List<TodoItem> result = todoService.getAllTodos();

        // Assert
        assertEquals(2, result.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        TodoItem todo = new TodoItem("Task 1", "Description 1");
        todo.setId(1L);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        // Act
        Optional<TodoItem> result = todoService.getTodoById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().getTitle());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveTodo() {
        // Arrange
        TodoItem todo = new TodoItem("Task 1", "Description 1");
        todo.setId(1L);
        when(todoRepository.save(todo)).thenReturn(todo);

        // Act
        TodoItem result = todoService.createTodoItem(todo);

        // Assert
        assertEquals("Task 1", result.getTitle());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void testDeleteTodo() {
        // Act
        todoService.deleteTodoItem(1L);

        // Assert
        verify(todoRepository, times(1)).deleteById(1L);
    }
    @Test
    void testUpdateTodoItem() {
        // Arrange
        Long id = 1L;
        TodoItem existingTodo = new TodoItem("Old Title", "Old Description");
        existingTodo.setId(id);

        TodoItem updatedTodoDetails = new TodoItem("New Title", "New Description");

        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(TodoItem.class))).thenReturn(existingTodo);

        // Act
        TodoItem result = todoService.updateTodoItem(id, updatedTodoDetails);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).save(existingTodo);
    }

    // Test for the case when the TodoItem is not found
    @Test
    void testUpdateTodoItem_NotFound() {
        // Arrange
        Long id = 1L;
        TodoItem updatedTodoDetails = new TodoItem("New Title", "New Description");

        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            todoService.updateTodoItem(id, updatedTodoDetails);
        });

        assertTrue(exception.getMessage().contains("TodoItem not found with id " + id));
        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, never()).save(any(TodoItem.class));  // Ensure save() is never called
    }
}