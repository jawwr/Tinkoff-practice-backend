package com.project.tinkoff.mapper;

import com.project.tinkoff.repository.models.Project;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse fromModel(Project project);
}
