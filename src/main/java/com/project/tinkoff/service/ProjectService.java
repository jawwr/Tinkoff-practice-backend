package com.project.tinkoff.service;

import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getAll();

    ProjectResponse createProject(ProjectRequest project);

    ProjectResponse getProjectById(long id);

    ProjectResponse updateProject(long id, ProjectRequest project);

    boolean deleteProject(long id);

    boolean checkProjectExists(long id);
}
