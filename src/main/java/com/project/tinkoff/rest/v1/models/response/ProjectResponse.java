package com.project.tinkoff.rest.v1.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProjectResponse(
        long id,
        String title,
        long authorId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createAt
) {
}
