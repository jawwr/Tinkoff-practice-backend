package com.project.tinkoff.service;

import com.project.tinkoff.repository.ProjectMemberRepository;
import com.project.tinkoff.repository.UserPermission;
import com.project.tinkoff.repository.models.ProjectMember;
import com.project.tinkoff.repository.models.ProjectRole;
import com.project.tinkoff.repository.models.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PermissionEvaluatorImpl implements PermissionEvaluator {
    private final ProjectMemberRepository projectMemberRepository;
    private final UserContextService userContextService;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        UserPermission userPermission = UserPermission.valueOf(permission.toString());
        return hasPermission(userPermission, (long) targetDomainObject);
    }

    private boolean hasPermission(UserPermission userPermission, long projectId) {
        UserDto currentUser = userContextService.getCurrentUser();
        Optional<ProjectMember> projectMember =
                projectMemberRepository.findProjectMemberByProjectIdAndUserId(projectId, currentUser.id());
        return switch (userPermission) {
            case GET_PROJECT_BY_ID, GET_CARD_BY_ID, VOTE_CARD, GENERATE_LINK, GET_ALL_CARDS, CREATE_CARD, GET_PROJECT_SETTINGS, GET_MEMBERS ->
                    projectMember.isPresent();
            case UPDATE_PROJECT, DELETE_PROJECT, UPDATE_CARD, DELETE_CARD, UPDATE_PROJECT_SETTINGS, DELETE_MEMBERS ->
                    projectMember.isPresent() && projectMember.get().getRole().equals(ProjectRole.ADMIN);
        };
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return false;
    }
}
