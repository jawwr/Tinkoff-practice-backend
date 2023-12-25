package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.rest.v1.api.ProjectUserApi;
import com.project.tinkoff.rest.v1.models.response.UserInfoResponse;
import com.project.tinkoff.service.implementation.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectUserController implements ProjectUserApi {
    private final UserProjectService service;

    @Override
    @PreAuthorize("hasPermission(#projectId, 'GET_PROJECT_BY_ID')")
    public ResponseEntity<UserInfoResponse> getProjectUserInfo(long projectId) {
        return ResponseEntity.ok(service.getProjectUserInfo(projectId));
    }
}
