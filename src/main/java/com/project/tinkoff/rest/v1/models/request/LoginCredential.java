package com.project.tinkoff.rest.v1.models.request;

public record LoginCredential(
        String login,
        String password
) {
}
