package com.project.tinkoff.service;

import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;

import java.util.List;

public interface CardService {
    List<CardResponse> getAllCards(long projectId);

    CardResponse getCardById(long projectId, long cardId);

    CardResponse createCard(long projectId, CardRequest card);

    CardResponse updateCard(long projectId, long cardId, CardRequest card);

    boolean deleteCard(long projectId, long cardId);
}
