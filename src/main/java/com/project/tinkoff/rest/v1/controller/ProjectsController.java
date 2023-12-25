package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.rest.v1.api.ProjectsApi;
import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class ProjectsController implements ProjectsApi {
    private final ProjectService service;

    @Override
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<ProjectResponse> createProject(ProjectRequest project) {
        return ResponseEntity.ok(service.createProject(project));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'GET_PROJECT_BY_ID')")
    public ResponseEntity<ProjectResponse> getProjectById(long projectId) {
        return ResponseEntity.ok(service.getProjectById(projectId));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'UPDATE_PROJECT')")
    public ResponseEntity<ProjectResponse> updateProject(long projectId, ProjectRequest project) {
        return ResponseEntity.ok(service.updateProject(projectId, project));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'DELETE_PROJECT')")
    public ResponseEntity<Boolean> deleteProject(long projectId) {
        return ResponseEntity.ok(service.deleteProject(projectId));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'GENERATE_LINK')")
    public ResponseEntity<String> generateInviteLink(long projectId) {
        return ResponseEntity.ok(service.generateInviteLink(projectId));
    }

    @Override
    public ResponseEntity<ProjectResponse> enterFromInviteLink(String inviteLink) {
        return ResponseEntity.ok(service.enterFromInviteLink(inviteLink));
    }
}
