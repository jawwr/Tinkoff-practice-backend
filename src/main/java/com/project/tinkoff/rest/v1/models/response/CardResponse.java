package com.project.tinkoff.rest.v1.models.response;

import com.project.tinkoff.repository.models.Card;
import com.project.tinkoff.repository.models.CardStatus;

import java.time.LocalDateTime;

public record CardResponse(
        long id,
        String title,
        String summary,
        LocalDateTime createAt,
        long authorId,
        int upVote,
        int downVote,
        CardStatus status
) {
    public static CardResponse fromDbModel(Card card) {
        return new CardResponse(
                card.getId(),
                card.getTitle(),
                card.getSummary(),
                card.getCreateAt(),
                card.getAuthorId(),
                card.getUpVote(),
                card.getDownVote(),
                card.getStatus()
        );
    }
}
