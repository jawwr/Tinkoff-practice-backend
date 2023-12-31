package com.project.tinkoff.repository.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "cards")
public class Card extends AbstractDbEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "author_id", nullable = false)
    private long authorId;

    @Column(name = "up_vote", nullable = false)
    private int upVote;

    @Column(name = "down_vote", nullable = false)
    private int downVote;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardStatus status;
}
