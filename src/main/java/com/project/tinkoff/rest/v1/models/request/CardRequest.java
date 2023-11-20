package com.project.tinkoff.rest.v1.models.request;

import com.project.tinkoff.repository.models.CardStatus;

public record CardRequest(
        String title,
        String summary,
        CardStatus status
) {
}
