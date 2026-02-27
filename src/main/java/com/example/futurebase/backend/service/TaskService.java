package com.example.futurebase.backend.service;

import com.example.futurebase.backend.dto.request.DomainRequest.*;
import com.example.futurebase.backend.entity.Project;
import com.example.futurebase.backend.entity.Task;
import com.example.futurebase.backend.exception.AppException;
import com.example.futurebase.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    /**
     * タスク一覧（プロジェクトのownerのみ）
     */
    @Transactional(readOnly = true)
    public Page<Task> list(Long ownerId, Long projectId, int page, int size) {
        // プロジェクトの存在・所有権を確認
        projectService.findOwnedProjectOrThrow(ownerId, projectId);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByProjectId(projectId, pageable);
    }

    /**
     * タスク作成
     */
    @Transactional
    public Task create(Long ownerId, Long projectId, TaskCreateRequest req) {
        Project project = projectService.findOwnedProjectOrThrow(ownerId, projectId);
        Task task = new Task(req.title(), req.status(), project);
        return taskRepository.save(task);
    }

    /**
     * タスク更新（ownerのみ）
     */
    @Transactional
    public Task update(Long ownerId, Long taskId, TaskUpdateRequest req) {
        Task task = findOwnedTaskOrThrow(ownerId, taskId);
        task.setTitle(req.title());
        task.setStatus(req.status());
        return taskRepository.save(task);
    }

    /**
     * タスク削除（ownerのみ）
     */
    @Transactional
    public void delete(Long ownerId, Long taskId) {
        Task task = findOwnedTaskOrThrow(ownerId, taskId);
        taskRepository.delete(task);
    }

    // ---- helpers ----

    private Task findOwnedTaskOrThrow(Long ownerId, Long taskId) {
        // タスク自体が存在しない → 404
        Task task = taskRepository.findById(taskId)
                .orElseThrow(AppException.ResourceNotFoundException::new);

        // ownerでない → 403
        if (!task.getProject().getOwner().getId().equals(ownerId)) {
            throw new AppException.ForbiddenException();
        }
        return task;
    }
}
