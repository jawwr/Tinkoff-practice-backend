package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.repository.ProjectRepository;
import com.project.tinkoff.repository.models.Project;
import com.project.tinkoff.repository.models.UserDetailImpl;
import com.project.tinkoff.repository.models.UserDto;
import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.ProjectService;
import com.project.tinkoff.service.UserContextService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;
    private final UserContextService userContextService;

    @Override
    public List<ProjectResponse> getAll() {
        return repository.findAll().stream().map(ProjectResponse::fromDbModel).toList();
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest project) {
        UserDto user = userContextService.getCurrentUser();
        Project newProject = new Project(project.title(), user.id(), Collections.emptyList());
        Project savedProject = repository.save(newProject);
        return ProjectResponse.fromDbModel(savedProject);
    }

    @Override
    public ProjectResponse getProjectById(long id) {
        Optional<Project> project = repository.findById(id);
        if (project.isEmpty()) {
            throw new DataNotFoundException(String.format("Project with id %d doesn't exist", id));
        }
        return ProjectResponse.fromDbModel(project.get());
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(long id, ProjectRequest project) {
        UserDto user = userContextService.getCurrentUser();
        Project updateProject = new Project(project.title(), user.id(), Collections.emptyList());
        Project updatedProject = repository.save(updateProject);
        return ProjectResponse.fromDbModel(updatedProject);
    }

    @Override
    @Transactional
    public boolean deleteProject(long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean isProjectExist(long id) {
        return repository.isProjectExist(id);
    }
}
