package com.project.tinkoff.repository.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_settings")
public class ProjectSettings extends AbstractDbEntity {
    @Column(name = "vote_count", nullable = false)
    private int voteCount;

    @Column(name = "period", nullable = false)
    private int period;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public static ProjectSettings defaultSettings(Project project) {
        return new ProjectSettings(
                1,
                1,
                project
        );
    }
}
