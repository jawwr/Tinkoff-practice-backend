package com.project.tinkoff.rest.v1.models.response;

import com.project.tinkoff.repository.models.ProjectRole;

public record UserInfoResponse(
        long id,
        String username,
        ProjectRole role,
        int voteCount
) {
}
