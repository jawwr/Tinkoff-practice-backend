package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByProjectId(long projectId);

    Optional<Card> findByProjectIdAndId(long projectId, long cardId);

    @Modifying
    void deleteByProjectIdAndId(long projectId, long cardId);
}
