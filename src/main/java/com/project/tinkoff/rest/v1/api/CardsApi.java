package com.project.tinkoff.rest.v1.api;

import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/projects/{projectId}/cards")
public interface CardsApi {
    @GetMapping("/")
    ResponseEntity<List<CardResponse>> getAllCards(@PathVariable("projectId") long projectId);

    @GetMapping("/{cardId}")
    ResponseEntity<CardResponse> getCardById(@PathVariable("projectId") long projectId,
                                             @PathVariable("cardId") long cardId);

    @PostMapping("/")
    ResponseEntity<CardResponse> createCard(@PathVariable("projectId") long projectId,
                                            @RequestBody CardRequest card);

    @PutMapping("/{cardId}")
    ResponseEntity<CardResponse> updateCard(@PathVariable("projectId") long projectId,
                                            @PathVariable("cardId") long cardId,
                                            @RequestBody CardRequest card);

    @DeleteMapping("/{cardId}")
    ResponseEntity<Boolean> deleteCard(@PathVariable("projectId") long projectId,
                                       @PathVariable("cardId") long cardId);
}
