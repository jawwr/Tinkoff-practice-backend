package com.project.tinkoff.rest.v1.api;

import com.project.tinkoff.rest.v1.models.response.UserInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/projects/{projectId}/me")
public interface ProjectUserApi {
    @GetMapping
    ResponseEntity<UserInfoResponse> getProjectUserInfo(@PathVariable("projectId") long projectId);
}
