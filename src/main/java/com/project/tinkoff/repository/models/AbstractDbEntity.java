package com.project.tinkoff.repository.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class AbstractDbEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator")
    @SequenceGenerator(name = "idGenerator", sequenceName = "hibernate_sequence", allocationSize = 1)
    protected @Setter long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

    @PrePersist
    public void onCreate() {
        var now = LocalDateTime.now();
        createAt = now;
        updateAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        LocalDateTime now = LocalDateTime.now();
        if (createAt == null) {
            createAt = now;
        }
        updateAt = now;
    }
}
