package com.project.tinkoff.mapper;

import com.project.tinkoff.repository.models.ProjectMember;
import com.project.tinkoff.repository.models.UserDetailImpl;
import com.project.tinkoff.repository.models.UserDto;
import com.project.tinkoff.rest.v1.models.response.UserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto fromUserDetails(UserDetailImpl userDetail);

    @Mapping(target = "username", source = "user.username")
    UserInfoResponse fromProjectMember(ProjectMember projectMember);
}
