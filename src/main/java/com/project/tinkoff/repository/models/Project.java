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
@Table(name = "projects")
public class Project extends AbstractDbEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author_id", nullable = false)
    private long authorId;
}
