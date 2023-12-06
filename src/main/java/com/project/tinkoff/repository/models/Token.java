package com.project.tinkoff.repository.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "token", nullable = false)
    String token;

    @Column(name = "revoked", nullable = false)
    boolean revoked;

    @Column(name = "expired", nullable = false)
    boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
