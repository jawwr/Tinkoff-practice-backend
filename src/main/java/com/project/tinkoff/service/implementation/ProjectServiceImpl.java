package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.mapper.ProjectMapper;
import com.project.tinkoff.repository.*;
import com.project.tinkoff.repository.models.*;
import com.project.tinkoff.rest.v1.models.request.ProjectRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.ProjectService;
import com.project.tinkoff.service.UserContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;
    private final UserContextService userContextService;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectInviteLinkRepository projectInviteLinkRepository;
    private final ProjectSettingsRepository projectSettingsRepository;

    @Value("${app.invite-link.expire-time-minutes}")
    private int inviteLinkExpireTime;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "projects", key = "authentication.name")
    public List<ProjectResponse> getAll() {
        UserDto user = userContextService.getCurrentUser();
        return repository.findAllUsersProjects(user.id())
                .stream()
                .map(projectMapper::fromModel)
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest project) {
        UserDto currentUser = userContextService.getCurrentUser();
        Project newProject = new Project(project.title(), currentUser.id(), Collections.emptyList());
        Project savedProject = repository.save(newProject);

        ProjectSettings projectSettings = ProjectSettings.defaultSettings(savedProject);
        projectSettingsRepository.save(projectSettings);

        User user = userRepository.findUserById(currentUser.id());
        ProjectMember projectMember = new ProjectMember(user, savedProject, ProjectRole.ADMIN, projectSettings.getVoteCount());
        projectMemberRepository.save(projectMember);

        return projectMapper.fromModel(savedProject);
    }

    @Override
    public ProjectResponse getProjectById(long id) {
        Project project = getProjectByIdForCurrenUser(id);
        return projectMapper.fromModel(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(long id, ProjectRequest projectRequest) {
        Project project = getProjectByIdForCurrenUser(id);
        updateProject(project, projectRequest);
        Project updatedProject = repository.save(project);
        return projectMapper.fromModel(updatedProject);
    }

    @Override
    @Transactional
    public boolean deleteProject(long id) {
        projectInviteLinkRepository.deleteAllByProjectId(id);
        projectSettingsRepository.deleteByProjectId(id);
        projectMemberRepository.deleteAllByProjectId(id);
        repository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public String generateInviteLink(long projectId) {
        Project project = getProjectByIdForCurrenUser(projectId);
        String link = RandomStringUtils.randomAlphabetic(10);
        ProjectInviteLink inviteLink = ProjectInviteLink.builder()
                .project(project)
                .link(link)
                .expireTime(LocalDateTime.now().plusMinutes(inviteLinkExpireTime))
                .build();
        projectInviteLinkRepository.save(inviteLink);
        return link;
    }

    @Override
    @Transactional
    public ProjectResponse enterFromInviteLink(String inviteLink) {
        Optional<ProjectInviteLink> optSavedInviteLink = projectInviteLinkRepository.findProjectInviteLinkByLink(inviteLink);
        if (optSavedInviteLink.isEmpty()) {
            throw new DataNotFoundException("Not found valid link");
        }
        var savedLink = optSavedInviteLink.get();
        Project project = savedLink.getProject();

        UserDto userDto = userContextService.getCurrentUser();
        var savedProjectMember = projectMemberRepository.findProjectMemberByProjectIdAndUserId(project.getId(), userDto.id());
        if (savedProjectMember.isPresent()) {
            return projectMapper.fromModel(project);
        }

        ProjectSettings projectSettings = projectSettingsRepository.findByProjectId(project.getId());

        User user = userRepository.findUserById(userDto.id());
        ProjectMember projectMember = new ProjectMember(user, project, ProjectRole.MEMBER, projectSettings.getVoteCount());
        projectMemberRepository.save(projectMember);

        return projectMapper.fromModel(project);
    }

    private Project getProjectByIdForCurrenUser(long projectId) {
        UserDto userCurrent = userContextService.getCurrentUser();
        Optional<ProjectMember> optProjectMember = projectMemberRepository.findProjectMemberByProjectIdAndUserId(projectId, userCurrent.id());
        if (optProjectMember.isEmpty()) {
            throw new DataNotFoundException(String.format("Project with id %d doesn't exist", projectId));
        }
        return optProjectMember.get().getProject();
    }

    private void updateProject(Project savedProject, ProjectRequest newProject) {
        if (!newProject.title().equals(savedProject.getTitle())) {
            savedProject.setTitle(newProject.title());
        }
    }
}
