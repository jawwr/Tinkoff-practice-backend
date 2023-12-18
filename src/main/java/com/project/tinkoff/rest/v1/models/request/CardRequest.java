package com.project.tinkoff.rest.v1.models.request;

import com.project.tinkoff.repository.models.CardStatus;
import jakarta.validation.constraints.Size;

public record CardRequest(
        @Size(min = 2, max = 50) String title,
        @Size(min = 2, max = 1000) String summary,
        CardStatus status
) {
}
