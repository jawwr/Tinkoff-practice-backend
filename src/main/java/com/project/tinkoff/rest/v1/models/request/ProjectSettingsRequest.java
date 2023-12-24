package com.project.tinkoff.rest.v1.models.request;

public record ProjectSettingsRequest(
        int voteCount,
        int period,
        String projectTitle
) {
}
