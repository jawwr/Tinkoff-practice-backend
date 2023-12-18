package com.project.tinkoff.rest.v1.models.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ProjectRequest(@Min(2) @Max(50) String title) {
}
