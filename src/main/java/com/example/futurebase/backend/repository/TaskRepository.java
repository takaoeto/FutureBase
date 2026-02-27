package com.example.futurebase.backend.repository;

import com.example.futurebase.backend.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Optional<Task> findByIdAndProjectOwnerId(Long id, Long ownerId);
}
