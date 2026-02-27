package com.example.futurebase.backend.controller;

import com.example.futurebase.backend.dto.request.DomainRequest.*;
import com.example.futurebase.backend.dto.response.ApiResponse.*;
import com.example.futurebase.backend.entity.Project;
import com.example.futurebase.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * GET /api/projects?page=0&size=20
     */
    @GetMapping
    public ResponseEntity<ProjectPageResponse> list(
            @AuthenticationPrincipal Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Project> result = projectService.list(ownerId, page, size);
        return ResponseEntity.ok(ProjectPageResponse.from(result));
    }

    /**
     * POST /api/projects
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> create(
            @AuthenticationPrincipal Long ownerId,
            @Valid @RequestBody ProjectCreateRequest req) {
        Project project = projectService.create(ownerId, req);
        return ResponseEntity.status(201).body(ProjectResponse.from(project));
    }

    /**
     * PUT /api/projects/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateRequest req) {
        Project project = projectService.update(ownerId, id, req);
        return ResponseEntity.ok(ProjectResponse.from(project));
    }

    /**
     * DELETE /api/projects/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long id) {
        projectService.delete(ownerId, id);
        return ResponseEntity.noContent().build();
    }
}
