package com.project.tinkoff.rest.v1.api;

import com.project.tinkoff.rest.v1.models.request.ProjectSettingsRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectSettingsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/projects/{projectId}/settings")
public interface ProjectSettingsApi {
    @GetMapping
    ResponseEntity<ProjectSettingsResponse> getProjectSettings(@PathVariable("projectId") long projectId);

    @PutMapping
    ResponseEntity<ProjectSettingsResponse> updateProjectSettings(@PathVariable("projectId") long projectId,
                                                                  @RequestBody ProjectSettingsRequest projectSettingsRequest);
}
