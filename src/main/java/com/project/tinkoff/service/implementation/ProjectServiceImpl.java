package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.mapper.ProjectMapper;
import com.project.tinkoff.repository.ProjectRepository;
import com.project.tinkoff.repository.models.Project;
import com.project.tinkoff.repository.models.UserDto;
import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.ProjectService;
import com.project.tinkoff.service.UserContextService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectResponse> getAll() {
        return repository.findAll().stream().map(projectMapper::fromModel).toList();
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest project) {
        UserDto user = userContextService.getCurrentUser();
        Project newProject = new Project(project.title(), user.id(), Collections.emptyList());
        Project savedProject = repository.save(newProject);
        return projectMapper.fromModel(savedProject);
    }

    @Override
    public ProjectResponse getProjectById(long id) {
        Project project = getSavedProjectById(id);
        return projectMapper.fromModel(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(long id, ProjectRequest projectRequest) {
        Project project = getSavedProjectById(id);
        updateProject(project, projectRequest);
        Project updatedProject = repository.save(project);
        return projectMapper.fromModel(updatedProject);
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

    private Project getSavedProjectById(long id) {
        Optional<Project> optProject = repository.findById(id);
        if (optProject.isEmpty()) {
            throw new DataNotFoundException(String.format("Project with id %d doesn't exist", id));
        }
        return optProject.get();
    }

    private void updateProject(Project savedProject, ProjectRequest newProject) {
        if (!newProject.title().equals(savedProject.getTitle())) {
            savedProject.setTitle(newProject.title());
        }
    }
}
