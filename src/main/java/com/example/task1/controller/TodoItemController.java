package com.example.task1.controller;

import com.example.task1.entity.TodoItem;
import com.example.task1.service.TodoItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoItemController {

    @Autowired
    private TodoItemService todoItemService;

    // Get all todos
    @GetMapping
    public List<TodoItem> getAllTodos() {
        return todoItemService.getAllTodos();
    }

    // Get todo by ID
    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoById(@PathVariable Long id) {
        return todoItemService.getTodoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new todo
    @PostMapping
    public TodoItem createTodoItem(@Valid @RequestBody TodoItem todoItem) {
        return todoItemService.createTodoItem(todoItem);
    }

    // Update todo by ID
    @PutMapping("/{id}")
    public ResponseEntity<TodoItem> updateTodoItem(
            @PathVariable Long id,
            @Valid @RequestBody TodoItem todoDetails) {
        return ResponseEntity.ok(todoItemService.updateTodoItem(id, todoDetails));
    }

    // Delete todo by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        todoItemService.deleteTodoItem(id);
        return ResponseEntity.noContent().build();
    }
}
