package com.example.rbac.repository;

import com.example.rbac.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserIdOrderByIdDesc(String userId);

    Optional<Task> findByIdAndUserId(Long id, String userId);
    // User-specific tasks sorted by createdAt descending
    List<Task> findByUserIdOrderByCreatedAtDesc(String userId);

    // All tasks sorted by createdAt descending
    List<Task> findAllByOrderByCreatedAtDesc();


}

