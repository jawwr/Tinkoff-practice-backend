package com.project.tinkoff.service.implementation;

import com.project.tinkoff.mapper.ProjectMemberMapper;
import com.project.tinkoff.repository.ProjectMemberRepository;
import com.project.tinkoff.repository.models.ProjectMember;
import com.project.tinkoff.repository.models.UserDto;
import com.project.tinkoff.rest.v1.models.response.ProjectMemberResponse;
import com.project.tinkoff.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberMapper projectMemberMapper;
    private final UserContextService userContextService;

    @Cacheable(cacheNames = "projectMembers", key = "#projectId")
    public List<ProjectMemberResponse> getProjectMembers(long projectId) {
        return projectMemberRepository.findAllByProjectId(projectId)
                .stream()
                .map(projectMemberMapper::fromModel)
                .toList();
    }

    @Transactional
    public boolean deleteMemberFromProject(long projectId, long memberId) {
        UserDto currentUser = userContextService.getCurrentUser();
        ProjectMember projectMember = projectMemberRepository.findProjectMemberByProjectIdAndUserId(projectId, currentUser.id()).get();
        if (projectMember.getUser().getId() == memberId) {
            return false;
        }
        projectMemberRepository.deleteByProjectIdAndUserId(projectId, memberId);
        return true;
    }
}
