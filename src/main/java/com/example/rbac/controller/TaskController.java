package com.example.rbac.controller;

import com.example.rbac.dto.TaskRequestDto;
import com.example.rbac.entity.Task;
import com.example.rbac.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Task Board Controller
 *
 * USER → sees only own tasks
 * MANAGER → sees all tasks
 * SUPER_ADMIN → sees all tasks
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_MANAGER','ROLE_SUPER_ADMIN')")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // ================= CREATE TASK =================
    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> createTask(@RequestBody TaskRequestDto request) {
        taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
    }

    // ================= GET TASKS =================
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestParam(required = false) String username) {
        List<Task> tasks;

        // USER role → only own tasks
        if (username != null) {
            tasks = taskService.getTasksByUser(username);
        } else {
            tasks = taskService.getTasks(); // MANAGER / SUPER_ADMIN → all tasks
        }

        return ResponseEntity.ok(tasks);
    }

    // ================= UPDATE TASK =================
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody TaskRequestDto request) {
        Task updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    // ================= DELETE TASK =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
