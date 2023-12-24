package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.PermissionDeniedException;
import com.project.tinkoff.mapper.ProjectSettingsMapper;
import com.project.tinkoff.repository.ProjectMemberRepository;
import com.project.tinkoff.repository.ProjectRepository;
import com.project.tinkoff.repository.ProjectSettingsRepository;
import com.project.tinkoff.repository.models.*;
import com.project.tinkoff.rest.v1.models.request.ProjectSettingsRequest;
import com.project.tinkoff.rest.v1.models.response.ProjectSettingsResponse;
import com.project.tinkoff.service.ProjectSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ProjectSettingsServiceImpl implements ProjectSettingsService {
    private final ProjectSettingsRepository projectSettingsRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSettingsMapper projectSettingsMapper;
    private final UserContextServiceImpl userContextService;

    @Override
    public ProjectSettingsResponse getProjectSettings(long projectId) {
        ProjectSettings projectSettings = projectSettingsRepository.findByProjectId(projectId);
        return projectSettingsMapper.fromModel(projectSettings);
    }

    @Override
    @Transactional
    public ProjectSettingsResponse updateProjectSettings(long projectId, ProjectSettingsRequest projectSettingsRequest) {
        UserDto userDto = userContextService.getCurrentUser();
        Optional<ProjectMember> projectMember = projectMemberRepository.findProjectMemberByProjectIdAndUserId(projectId, userDto.id());
        if (projectMember.isEmpty() || projectMember.get().getRole().equals(ProjectRole.MEMBER)) {
            throw new PermissionDeniedException("User don't have permission to update project settings");
        }
        ProjectSettings projectSettings = projectSettingsRepository.findByProjectId(projectId);
        updateSettings(projectSettings, projectSettingsRequest);
        projectSettings = projectSettingsRepository.save(projectSettings);

        List<ProjectMember> members = projectMemberRepository.findAllByProjectId(projectId);
        updateMembers(members, projectSettings);
        projectMemberRepository.saveAll(members);

        return projectSettingsMapper.fromModel(projectSettings);
    }

    private void updateSettings(ProjectSettings projectSettings, ProjectSettingsRequest projectSettingsRequest) {
        if (projectSettingsRequest.voteCount() != projectSettings.getVoteCount()) {
            projectSettings.setVoteCount(projectSettingsRequest.voteCount());
        }
        if (projectSettingsRequest.period() != projectSettings.getPeriod()) {
            projectSettings.setPeriod(projectSettingsRequest.period());
        }
    }

    @Scheduled(cron = "${schedule.project-settings-scheduler}")
    @Transactional
    public void scheduleProjectSettings() {
        log.info("Update projects from settings");
        List<ProjectSettings> settings = projectSettingsRepository.findAll();
        var now = LocalDateTime.now();
        for (ProjectSettings setting : settings) {
            Project project = projectRepository.findById(setting.getProject().getId()).get();
            if (Duration.between(now, project.getCreateAt()).toDays() % setting.getPeriod() != 0) {
                continue;
            }
            var members = projectMemberRepository.findAllByProjectId(setting.getProject().getId());
            updateMembers(members, setting);
            projectMemberRepository.saveAll(members);
        }
    }

    private void updateMembers(List<ProjectMember> members, ProjectSettings settings) {
        for (ProjectMember member : members) {
            member.setVoteCount(settings.getVoteCount());
        }
    }
}
