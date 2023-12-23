package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.rest.v1.api.ProjectMemberApi;
import com.project.tinkoff.rest.v1.models.response.ProjectMemberResponse;
import com.project.tinkoff.service.implementation.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectMemberController implements ProjectMemberApi {
    private final ProjectMemberService service;

    @Override
    public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(long projectId) {
        return ResponseEntity.ok(service.getProjectMembers(projectId));
    }

    @Override
    public ResponseEntity<Boolean> deleteMemberFromProject(long projectId, long memberId) {
        return ResponseEntity.ok(service.deleteMemberFromProject(projectId, memberId));
    }
}
