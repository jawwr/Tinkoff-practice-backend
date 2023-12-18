package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.rest.v1.api.ProjectsApi;
import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class ProjectsController implements ProjectsApi {
    private final ProjectService service;

    @Autowired
    public ProjectsController(ProjectService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<ProjectResponse> createProject(ProjectRequest project) {
        return ResponseEntity.ok(service.createProject(project));
    }

    @Override
    public ResponseEntity<ProjectResponse> getProjectById(long projectId) {
        return ResponseEntity.ok(service.getProjectById(projectId));
    }

    @Override
    public ResponseEntity<ProjectResponse> updateProject(long projectId, ProjectRequest project) {
        return ResponseEntity.ok(service.updateProject(projectId, project));
    }

    @Override
    public ResponseEntity<Boolean> deleteProject(long projectId) {
        return ResponseEntity.ok(service.deleteProject(projectId));
    }
}
