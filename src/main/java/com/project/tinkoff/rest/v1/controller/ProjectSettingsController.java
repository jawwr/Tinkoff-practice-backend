package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.rest.v1.api.ProjectSettingsApi;
import com.project.tinkoff.rest.v1.models.request.ProjectSettingsRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectSettingsResponse;
import com.project.tinkoff.service.ProjectSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectSettingsController implements ProjectSettingsApi {
    private final ProjectSettingsService service;

    @Override
    @PreAuthorize("hasPermission(#projectId, 'GET_PROJECT_SETTINGS')")
    public ResponseEntity<ProjectSettingsResponse> getProjectSettings(long projectId) {
        return ResponseEntity.ok(service.getProjectSettings(projectId));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'UPDATE_PROJECT_SETTINGS')")
    public ResponseEntity<ProjectSettingsResponse> updateProjectSettings(long projectId,
                                                                         ProjectSettingsRequest projectSettingsRequest) {
        return ResponseEntity.ok(service.updateProjectSettings(projectId, projectSettingsRequest));
    }
}
