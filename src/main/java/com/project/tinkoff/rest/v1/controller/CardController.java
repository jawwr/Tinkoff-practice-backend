package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.repository.models.VoteType;
import com.project.tinkoff.rest.v1.api.CardsApi;
import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import com.project.tinkoff.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class CardController implements CardsApi {
    private final CardService service;

    @Override
    @PreAuthorize("hasPermission(#projectId, 'GET_ALL_CARDS')")
    public ResponseEntity<List<CardResponse>> getAllCards(long projectId) {
        return ResponseEntity.ok(service.getAllCards(projectId));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'GET_CARD_BY_ID')")
    public ResponseEntity<CardResponse> getCardById(long projectId, long cardId) {
        return ResponseEntity.ok(service.getCardById(projectId, cardId));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'CREATE_CARD')")
    public ResponseEntity<CardResponse> createCard(long projectId, CardRequest card) {
        return ResponseEntity.ok(service.createCard(projectId, card));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'UPDATE_CARD')")
    public ResponseEntity<CardResponse> updateCard(long projectId, long cardId, @Validated CardRequest card) {
        return ResponseEntity.ok(service.updateCard(projectId, cardId, card));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'DELETE_CARD')")
    public ResponseEntity<Boolean> deleteCard(long projectId, long cardId) {
        return ResponseEntity.ok(service.deleteCard(projectId, cardId));
    }

    @Override
    @PreAuthorize("hasPermission(#projectId, 'VOTE_CARD')")
    public ResponseEntity<Boolean> vote(long projectId, long cardId, VoteType voteType) {
        return ResponseEntity.ok(service.vote(projectId, cardId, voteType));
    }
}
