package com.project.tinkoff.service.implementation;

import com.project.tinkoff.mapper.UserMapper;
import com.project.tinkoff.repository.ProjectMemberRepository;
import com.project.tinkoff.repository.models.ProjectMember;
import com.project.tinkoff.repository.models.UserDto;
import com.project.tinkoff.rest.v1.models.response.UserInfoResponse;
import com.project.tinkoff.service.ProjectService;
import com.project.tinkoff.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProjectService {
    private final UserContextService contextService;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectService projectService;
    private final UserMapper userMapper;

    public UserInfoResponse getProjectUserInfo(long projectId) {
        projectService.checkProjectExists(projectId);
        UserDto contextUser = contextService.getCurrentUser();
        ProjectMember projectMember = projectMemberRepository.findProjectMemberByProjectIdAndUserId(projectId, contextUser.id()).get();
        return userMapper.fromProjectMember(projectMember);
    }
}
