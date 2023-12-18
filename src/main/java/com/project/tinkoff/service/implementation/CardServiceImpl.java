package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.repository.CardRepository;
import com.project.tinkoff.repository.models.*;
import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.CardService;
import com.project.tinkoff.service.ProjectService;
import com.project.tinkoff.service.UserContextService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final ProjectService projectService;
    private final UserContextService userContextService;

    @Override
    public List<CardResponse> getAllCards(long projectId) {
        if (!projectService.isProjectExist(projectId)) {
            throw new DataNotFoundException(String.format("Project with id %d doesn't exist", projectId));
        }
        return repository.findAllByProjectId(projectId).stream()
                .sorted(Comparator.comparing(AbstractDbEntity::getCreateAt))
                .map(CardResponse::fromDbModel)
                .toList();
    }

    @Override
    public CardResponse getCardById(long projectId, long cardId) {
        Optional<Card> card = repository.findByProjectIdAndId(projectId, cardId);
        if (card.isEmpty()) {
            throw new DataNotFoundException(String.format("Card with id %d doesn't exist in project with id %d", cardId, projectId));
        }
        return CardResponse.fromDbModel(card.get());
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
        return CardResponse.fromDbModel(savedCard);
    }

    @Override
    @Transactional
    public CardResponse updateCard(long projectId, long cardId, CardRequest card) {
        ProjectResponse projectResponse = projectService.getProjectById(projectId);
        Project project = new Project();
        project.setId(projectResponse.id());
        Optional<Card> savedCard = repository.findByProjectIdAndId(projectId, cardId);
        if (savedCard.isEmpty()) {
            throw new DataNotFoundException(String.format("Card with id %d doesn't exist in project with id %d", cardId, projectId));
        }
        UserDto user = userContextService.getCurrentUser();
        Card newCard = Card.builder()
                .title(card.title())
                .summary(card.summary())
                .authorId(user.id())
                .downVote(savedCard.get().getDownVote())
                .upVote(savedCard.get().getUpVote())
                .project(project)
                .status(card.status() == null ? savedCard.get().getStatus() : card.status())
                .build();
        newCard.setId(cardId);
        Card updatedCard = repository.save(newCard);
        return CardResponse.fromDbModel(updatedCard);
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
