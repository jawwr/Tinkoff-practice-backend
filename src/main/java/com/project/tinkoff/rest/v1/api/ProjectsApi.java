package com.project.tinkoff.rest.v1.api;

import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/projects")
@Tag(name = "Projects")
public interface ProjectsApi {
    @Operation(summary = "Get all projects")
    @GetMapping("/")
    ResponseEntity<List<ProjectResponse>> getAllProjects();

    @Operation(summary = "Create project")
    @PostMapping("/")
    ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest project);

    @Operation(summary = "Get project by id")
    @GetMapping("/{projectId}")
    ResponseEntity<ProjectResponse> getProjectById(@PathVariable("projectId") long projectId);

    @Operation(summary = "Update project")
    @PutMapping("/{projectId}")
    ResponseEntity<ProjectResponse> updateProject(@PathVariable("projectId") long projectId,
                                                  @RequestBody ProjectRequest project);

    @Operation(summary = "Delete project")
    @DeleteMapping("/{projectId}")
    ResponseEntity<Boolean> deleteProject(@PathVariable("projectId") long projectId);

    @Operation(summary = "Create invite link")
    @PostMapping("/{projectId}/link")
    ResponseEntity<String> generateInviteLink(@PathVariable("projectId") long projectId);

    @Operation(summary = "Enter to project from invite link")
    @PostMapping("/invite/{inviteLink}")
    ResponseEntity<Boolean> enterFromInviteLink(@PathVariable("inviteLink") String inviteLink);
}
