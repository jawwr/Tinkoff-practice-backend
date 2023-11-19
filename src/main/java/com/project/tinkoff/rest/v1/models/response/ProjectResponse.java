package com.project.tinkoff.rest.v1.models.response;

import com.project.tinkoff.repository.models.Project;

import java.time.LocalDateTime;

public record ProjectResponse(
        long id,
        String title,
        long authorId,
        LocalDateTime createAt
) {
    public static ProjectResponse fromDbModel(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getAuthorId(),
                project.getCreateAt()
        );
    }
}
