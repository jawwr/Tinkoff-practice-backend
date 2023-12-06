package com.project.tinkoff.rest.v1.models.response;

import com.project.tinkoff.repository.models.User;

public record UserDto(
        long id,
        String username,
        String login
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getLogin()
        );
    }
}
