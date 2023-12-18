package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.mapper.CardMapper;
import com.project.tinkoff.repository.CardRepository;
import com.project.tinkoff.repository.models.*;
import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.CardService;
import com.project.tinkoff.service.ProjectService;
import com.project.tinkoff.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final ProjectService projectService;
    private final UserContextService userContextService;
    private final CardMapper cardMapper;

    @Override
    public List<CardResponse> getAllCards(long projectId) {
        if (!projectService.isProjectExist(projectId)) {
            throw new DataNotFoundException(String.format("Project with id %d doesn't exist", projectId));
        }
        return repository.findAllByProjectId(projectId).stream()
                .sorted(Comparator.comparing(AbstractDbEntity::getCreateAt))
                .map(cardMapper::fromModel)
                .toList();
    }

    @Override
    public CardResponse getCardById(long projectId, long cardId) {
        Optional<Card> card = repository.findByProjectIdAndId(projectId, cardId);
        if (card.isEmpty()) {
            throw new DataNotFoundException(String.format("Card with id %d doesn't exist in project with id %d", cardId, projectId));
        }
        return cardMapper.fromModel(card.get());
    }

    @Override
    @Transactional
    public CardResponse createCard(long projectId, CardRequest card) {
        ProjectResponse projectResponse = projectService.getProjectById(projectId);
        Project project = new Project();
        project.setId(projectResponse.id());
        UserDto user = userContextService.getCurrentUser();
        Card newCard = Card.builder()
                .title(card.title())
                .summary(card.summary())
                .authorId(user.id())
                .project(project)
                .status(CardStatus.NEW)
                .build();
        Card savedCard = repository.save(newCard);
        return cardMapper.fromModel(savedCard);
    }

    @Override
    @Transactional
    public CardResponse updateCard(long projectId, long cardId, CardRequest cardRequest) {
        Optional<Card> optCard = repository.findByProjectIdAndId(projectId, cardId);
        if (optCard.isEmpty()) {
            throw new DataNotFoundException(String.format("Card with id %d doesn't exist in project with id %d", cardId, projectId));
        }
        Card card = optCard.get();
        updateSavedCard(card, cardRequest);
        repository.save(card);
        Card updatedCard = repository.findByProjectIdAndId(projectId, cardId).get();
        return cardMapper.fromModel(updatedCard);
    }

    private void updateSavedCard(Card savedCard, CardRequest request) {
        if (request.status() != null) {
            savedCard.setStatus(request.status());
        }
        if (request.summary() != null) {
            savedCard.setSummary(request.summary());
        }
        if (request.title() != null) {
            savedCard.setTitle(request.title());
        }
    }

    @Override
    @Transactional
    public boolean deleteCard(long projectId, long cardId) {
        repository.deleteByProjectIdAndId(projectId, cardId);
        return true;
    }

    @Override
    @Transactional
    public boolean vote(long projectId, long cardId, VoteType voteType) {
        Optional<Card> optCard = repository.findByProjectIdAndId(projectId, cardId);
        if (optCard.isEmpty()) {
            throw new DataNotFoundException(String.format("Card with id %d doesn't exist in project with id %d", cardId, projectId));
        }
        var card = optCard.get();
        if (card.getStatus() != CardStatus.NEW) {
            return false;
        }
        if (voteType == VoteType.VOTE_FOR) {
            int upVote = card.getUpVote();
            card.setUpVote(upVote + 1);
            repository.save(card);
            return true;
        }
        int downVote = card.getDownVote();
        card.setDownVote(downVote + 1);
        repository.save(card);
        return true;
    }
}
