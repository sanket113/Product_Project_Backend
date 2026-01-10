package com.example.rbac.service;

import com.example.rbac.dto.TaskRequestDto;
import com.example.rbac.entity.Task;
import com.example.rbac.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    private String getCurrentUser() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // CREATE
    public void createTask(TaskRequestDto request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription() != null ? request.getDescription() : "")
                .status(request.getStatus() != null ? request.getStatus() : "TODO")
                .userId(getCurrentUser())
                .build();

        taskRepository.save(task);
    }

    // READ (Logged-in user)
    public List<Task> getTasks() {
        return taskRepository.findByUserIdOrderByIdDesc(getCurrentUser());
    }

    // UPDATE
    public Task updateTask(Long id, TaskRequestDto request) {
        Task task = taskRepository.findByIdAndUserId(id, getCurrentUser())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        return taskRepository.save(task);
    }

    // DELETE
    public void deleteTask(Long id) {
        Task task = taskRepository.findByIdAndUserId(id, getCurrentUser())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    // READ (Specific user - for MANAGER / ADMIN)
    public List<Task> getTasksByUser(String username) {
        return taskRepository.findByUserIdOrderByIdDesc(username);
    }
}
