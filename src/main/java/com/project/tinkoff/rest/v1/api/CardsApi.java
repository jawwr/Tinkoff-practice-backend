package com.project.tinkoff.rest.v1.api;

import com.project.tinkoff.repository.models.VoteType;
import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/projects/{projectId}/cards")
@Tag(name = "Cards")
public interface CardsApi {
    @Operation(summary = "Get all cards")
    @GetMapping("/")
    ResponseEntity<List<CardResponse>> getAllCards(@PathVariable("projectId") long projectId);

    @Operation(summary = "Get card by id")
    @GetMapping("/{cardId}")
    ResponseEntity<CardResponse> getCardById(@PathVariable("projectId") long projectId,
                                             @PathVariable("cardId") long cardId);

    @Operation(summary = "Create card")
    @PostMapping("/")
    ResponseEntity<CardResponse> createCard(@PathVariable("projectId") long projectId,
                                            @RequestBody CardRequest card);

    @Operation(summary = "Update card")
    @PutMapping("/{cardId}")
    ResponseEntity<CardResponse> updateCard(@PathVariable("projectId") long projectId,
                                            @PathVariable("cardId") long cardId,
                                            @RequestBody CardRequest card);

    @Operation(summary = "Delete card")
    @DeleteMapping("/{cardId}")
    ResponseEntity<Boolean> deleteCard(@PathVariable("projectId") long projectId,
                                       @PathVariable("cardId") long cardId);

    @Operation(summary = "Vote for card")
    @PostMapping("/{cardId}/vote")
    ResponseEntity<Boolean> vote(@PathVariable("projectId") long projectId,
                                 @PathVariable("cardId") long cardId,
                                 @RequestParam("voteType") VoteType voteType);
}
