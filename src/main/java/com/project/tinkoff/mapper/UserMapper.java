package com.project.tinkoff.mapper;

import com.project.tinkoff.repository.models.UserDetailImpl;
import com.project.tinkoff.repository.models.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto fromUserDetails(UserDetailImpl userDetail);
}
