package com.example.futurebase.backend.repository;

import com.example.futurebase.backend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);

    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);
}
