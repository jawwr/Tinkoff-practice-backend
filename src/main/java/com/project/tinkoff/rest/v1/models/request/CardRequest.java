package com.project.tinkoff.rest.v1.models.request;

import com.project.tinkoff.repository.models.CardStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CardRequest(
        @Min(2) @Max(50) String title,
        @Min(2) @Max(1000) String summary,
        CardStatus status
) {
}
