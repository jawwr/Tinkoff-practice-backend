package com.project.tinkoff.service.implementation;

import com.project.tinkoff.mapper.ProjectMemberMapper;
import com.project.tinkoff.repository.ProjectMemberRepository;
import com.project.tinkoff.rest.v1.models.response.ProjectMemberResponse;
import com.project.tinkoff.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectService projectService;
    private final ProjectMemberMapper projectMemberMapper;

    public List<ProjectMemberResponse> getProjectMembers(long projectId) {
        projectService.checkProjectExists(projectId);
        return projectMemberRepository.findAllByProjectId(projectId).stream().map(projectMemberMapper::fromModel).toList();
    }
}
