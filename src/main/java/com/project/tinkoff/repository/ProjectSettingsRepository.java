package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.ProjectSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectSettingsRepository extends JpaRepository<ProjectSettings, Long> {
    ProjectSettings findByProjectId(long projectId);

    void deleteByProjectId(long projectId);
}
