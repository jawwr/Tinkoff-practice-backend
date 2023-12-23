package com.project.tinkoff.service;

import com.project.tinkoff.rest.v1.models.request.ProjectSettingsRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectSettingsResponse;

public interface ProjectSettingsService {
    ProjectSettingsResponse getProjectSettings(long projectId);

    ProjectSettingsResponse updateProjectSettings(long projectId, ProjectSettingsRequest projectSettingsRequest);
}
