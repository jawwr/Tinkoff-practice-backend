package com.project.tinkoff.rest.v1.api;

import com.project.tinkoff.rest.v1.models.response.ProjectMemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/projects/{projectId}/members")
public interface ProjectMemberApi {
    @GetMapping
    ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(@PathVariable("projectId") long projectId);
}
