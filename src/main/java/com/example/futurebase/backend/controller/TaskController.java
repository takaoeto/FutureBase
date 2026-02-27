package com.example.futurebase.backend.controller;

import com.example.futurebase.backend.dto.request.DomainRequest.*;
import com.example.futurebase.backend.dto.response.ApiResponse.*;
import com.example.futurebase.backend.entity.Task;
import com.example.futurebase.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * GET /api/projects/{id}/tasks?page=0&size=50
     */
    @GetMapping("/api/projects/{id}/tasks")
    public ResponseEntity<TaskPageResponse> list(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<Task> result = taskService.list(ownerId, id, page, size);
        return ResponseEntity.ok(TaskPageResponse.from(result));
    }

    /**
     * POST /api/projects/{id}/tasks
     */
    @PostMapping("/api/projects/{id}/tasks")
    public ResponseEntity<TaskResponse> create(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long id,
            @Valid @RequestBody TaskCreateRequest req) {
        Task task = taskService.create(ownerId, id, req);
        return ResponseEntity.status(201).body(TaskResponse.from(task));
    }

    /**
     * PUT /api/tasks/{id}
     */
    @PutMapping("/api/tasks/{id}")
    public ResponseEntity<TaskResponse> update(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest req) {
        Task task = taskService.update(ownerId, id, req);
        return ResponseEntity.ok(TaskResponse.from(task));
    }

    /**
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Long ownerId,
            @PathVariable Long id) {
        taskService.delete(ownerId, id);
        return ResponseEntity.noContent().build();
    }
}
