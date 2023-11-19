package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.repository.ProjectRepository;
import com.project.tinkoff.repository.models.Project;
import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.ProjectService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;
    private static final long MOCK_AUTHOR_ID = 1000;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProjectResponse> getAll() {
        return repository.findAll().stream().map(ProjectResponse::fromDbModel).toList();
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest project) {
        Project newProject = new Project(project.title(), MOCK_AUTHOR_ID);
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
        Project updateProject = new Project(project.title(), MOCK_AUTHOR_ID);
        updateProject.setId(id);
        Project updatedProject = repository.save(updateProject);
        return ProjectResponse.fromDbModel(updatedProject);
    }

    @Override
    @Transactional
    public boolean deleteProject(long id) {
        repository.deleteById(id);
        return true;
    }
}
