package com.example.task1.service;

import com.example.task1.entity.TodoItem;
import com.example.task1.dao.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    // Get all todo items
    public List<TodoItem> getAllTodos() {
        return todoItemRepository.findAll();
    }

    // Get todo item by ID
    public Optional<TodoItem> getTodoById(Long id) {
        return todoItemRepository.findById(id);
    }

    // Create new todo item
    public TodoItem createTodoItem(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }

    // Update existing todo item
    public TodoItem updateTodoItem(Long id, TodoItem todoDetails) {
        return todoItemRepository.findById(id).map(todoItem -> {
            todoItem.setTitle(todoDetails.getTitle());
            todoItem.setDescription(todoDetails.getDescription());
            return todoItemRepository.save(todoItem);
        }).orElseThrow(() -> new RuntimeException("TodoItem not found with id " + id));
    }

    // Delete todo item
    public void deleteTodoItem(Long id) {
        todoItemRepository.deleteById(id);
    }

}