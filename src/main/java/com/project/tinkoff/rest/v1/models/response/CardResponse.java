package com.project.tinkoff.rest.v1.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.tinkoff.repository.models.CardStatus;

import java.time.LocalDateTime;

public record CardResponse(
        long id,
        String title,
        String summary,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createAt,
        long authorId,
        String authorName,
        int upVote,
        int downVote,
        CardStatus status
) {
}
