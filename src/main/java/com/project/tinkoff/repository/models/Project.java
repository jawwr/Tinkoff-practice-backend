package com.project.tinkoff.repository.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project extends AbstractDbEntity {
    @Column(name = "title", nullable = false)
    @Size(min = 2)
    private String title;

    @Column(name = "author_id", nullable = false)
    private long authorId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", fetch = FetchType.LAZY)
    private List<Card> cards;
}
