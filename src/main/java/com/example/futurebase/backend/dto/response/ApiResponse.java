package com.example.futurebase.backend.dto.response;

import com.example.futurebase.backend.entity.Project;
import com.example.futurebase.backend.entity.Task;
import com.example.futurebase.backend.entity.TaskStatus;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApiResponse {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    // ---- Common ----

    public record MessageResponse(String message) {
    }

    public record ErrorDetail(String field, String reason) {
    }

    public record ErrorResponse(
            String code,
            String message,
            List<ErrorDetail> details,
            String traceId) {
    }

    // ---- Auth ----

    public record TokenResponse(
            String token,
            String tokenType,
            int expiresIn) {
    }

    // ---- Domain ----

    public record ProjectResponse(
            Long id,
            String name,
            Long ownerId,
            String createdAt) {
        public static ProjectResponse from(Project p) {
            return new ProjectResponse(
                    p.getId(),
                    p.getName(),
                    p.getOwner().getId(),
                    p.getCreatedAt().format(FORMATTER));
        }
    }

    public record TaskResponse(
            Long id,
            String title,
            TaskStatus status,
            Long projectId,
            String createdAt) {
        public static TaskResponse from(Task t) {
            return new TaskResponse(
                    t.getId(),
                    t.getTitle(),
                    t.getStatus(),
                    t.getProject().getId(),
                    t.getCreatedAt().format(FORMATTER));
        }
    }

    // ---- Pagination ----

    public record ProjectPageResponse(
            List<ProjectResponse> items,
            int page,
            int size,
            long total) {
        public static ProjectPageResponse from(Page<Project> page) {
            return new ProjectPageResponse(
                    page.getContent().stream().map(ProjectResponse::from).toList(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements());
        }
    }

    public record TaskPageResponse(
            List<TaskResponse> items,
            int page,
            int size,
            long total) {
        public static TaskPageResponse from(Page<Task> page) {
            return new TaskPageResponse(
                    page.getContent().stream().map(TaskResponse::from).toList(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements());
        }
    }
}
