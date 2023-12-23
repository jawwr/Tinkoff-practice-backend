package com.project.tinkoff.mapper;

import com.project.tinkoff.repository.models.ProjectMember;
import com.project.tinkoff.rest.v1.models.response.ProjectMemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "accessionDate", source = "createAt")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    ProjectMemberResponse fromModel(ProjectMember member);
}
