package com.project.tinkoff.rest.v1.models.request;

public record RegisterCredential(
        String login,
        String password
) {
}
