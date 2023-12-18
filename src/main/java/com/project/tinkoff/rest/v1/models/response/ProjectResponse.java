package com.project.tinkoff.rest.v1.models.response;

import java.time.LocalDateTime;

public record ProjectResponse(
        long id,
        String title,
        long authorId,
        LocalDateTime createAt
) {
}
