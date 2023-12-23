package com.project.tinkoff.mapper;

import com.project.tinkoff.repository.models.ProjectSettings;
import com.project.tinkoff.rest.v1.models.response.ProjectSettingsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectSettingsMapper {
    ProjectSettingsResponse fromModel(ProjectSettings projectSettings);
}
