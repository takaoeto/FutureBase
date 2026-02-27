package com.example.futurebase.backend.service;

import com.example.futurebase.backend.dto.request.DomainRequest.*;
import com.example.futurebase.backend.entity.Project;
import com.example.futurebase.backend.entity.User;
import com.example.futurebase.backend.exception.AppException;
import com.example.futurebase.backend.repository.ProjectRepository;
import com.example.futurebase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * プロジェクト一覧（ownerのみ）
     */
    @Transactional(readOnly = true)
    public Page<Project> list(Long ownerId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return projectRepository.findByOwnerId(ownerId, pageable);
    }

    /**
     * プロジェクト作成
     */
    @Transactional
    public Project create(Long ownerId, ProjectCreateRequest req) {
        User owner = userRepository.getReferenceById(ownerId);
        Project project = new Project(req.name(), owner);
        return projectRepository.save(project);
    }

    /**
     * プロジェクト更新（ownerのみ）
     */
    @Transactional
    public Project update(Long ownerId, Long projectId, ProjectUpdateRequest req) {
        Project project = findOwnedProjectOrThrow(ownerId, projectId);
        project.setName(req.name());
        return projectRepository.save(project);
    }

    /**
     * プロジェクト削除（ownerのみ）
     */
    @Transactional
    public void delete(Long ownerId, Long projectId) {
        Project project = findOwnedProjectOrThrow(ownerId, projectId);
        projectRepository.delete(project);
    }

    // ---- helpers ----

    /**
     * ownerIdが一致するプロジェクトを取得する
     * 存在しない場合は404、ownerでない場合は403
     */
    public Project findOwnedProjectOrThrow(Long ownerId, Long projectId) {
        // プロジェクト自体が存在しない → 404
        Project project = projectRepository.findById(projectId)
                .orElseThrow(AppException.ResourceNotFoundException::new);

        // ownerでない → 403
        if (!project.getOwner().getId().equals(ownerId)) {
            throw new AppException.ForbiddenException();
        }
        return project;
    }
}
