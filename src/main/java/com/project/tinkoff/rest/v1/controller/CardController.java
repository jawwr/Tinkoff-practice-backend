package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.repository.models.VoteType;
import com.project.tinkoff.rest.v1.api.CardsApi;
import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import com.project.tinkoff.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class CardController implements CardsApi {
    private final CardService service;

    @Autowired
    public CardController(CardService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<CardResponse>> getAllCards(long projectId) {
        return ResponseEntity.ok(service.getAllCards(projectId));
    }

    @Override
    public ResponseEntity<CardResponse> getCardById(long projectId, long cardId) {
        return ResponseEntity.ok(service.getCardById(projectId, cardId));
    }

    @Override
    public ResponseEntity<CardResponse> createCard(long projectId, CardRequest card) {
        return ResponseEntity.ok(service.createCard(projectId, card));
    }

    @Override
    public ResponseEntity<CardResponse> updateCard(long projectId, long cardId, @Validated CardRequest card) {
        return ResponseEntity.ok(service.updateCard(projectId, cardId, card));
    }

    @Override
    public ResponseEntity<Boolean> deleteCard(long projectId, long cardId) {
        return ResponseEntity.ok(service.deleteCard(projectId, cardId));
    }

    @Override
    public ResponseEntity<Boolean> vote(long projectId, long cardId, VoteType voteType) {
        return ResponseEntity.ok(service.vote(projectId, cardId, voteType));
    }
}
