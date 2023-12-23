package com.project.tinkoff.rest.v1.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProjectMemberResponse(
        long userId,
        String username,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime accessionDate
) {
}
