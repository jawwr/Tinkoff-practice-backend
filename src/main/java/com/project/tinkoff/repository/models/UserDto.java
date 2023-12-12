package com.project.tinkoff.repository.models;

public record UserDto(
        long id,
        String username,
        String login
) {
}
