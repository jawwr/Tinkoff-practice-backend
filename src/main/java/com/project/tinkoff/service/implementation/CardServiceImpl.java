package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.repository.CardRepository;
import com.project.tinkoff.repository.models.AbstractDbEntity;
import com.project.tinkoff.repository.models.Card;
import com.project.tinkoff.repository.models.Project;
import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import com.project.tinkoff.rest.v1.models.response.ProjectResponse;
import com.project.tinkoff.service.CardService;
import com.project.tinkoff.service.ProjectService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final ProjectService projectService;
    private static final long MOCK_AUTHOR_ID = 1000;

    @Autowired
    public CardServiceImpl(CardRepository repository, ProjectService projectService) {
        this.repository = repository;
        this.projectService = projectService;
    }

    @Override
    @Transactional
    public List<CardResponse> getAllCards(long projectId) {
        projectService.getProjectById(projectId);
        return repository.findAllByProjectId(projectId).stream()
                .sorted(Comparator.comparing(AbstractDbEntity::getUpdateAt))
                .map(CardResponse::fromDbModel)
                .toList();
    }

    @Override
    public CardResponse getCardById(long projectId, long cardId) {
        Optional<Card> card = repository.findByProjectIdAndId(projectId, cardId);
        if (card.isEmpty()) {
            throw new DataNotFoundException("Card with id " + cardId + " doesn't exist in project with id " + projectId);
        }
        return CardResponse.fromDbModel(card.get());
    }

    @Override
    @Transactional
    public CardResponse createCard(long projectId, CardRequest card) {
        ProjectResponse projectResponse = projectService.getProjectById(projectId);
        Project project = new Project();
        project.setId(projectResponse.id());
        Card newCard = new Card(card.title(), card.summary(), MOCK_AUTHOR_ID, 0, 0, project);
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
            throw new DataNotFoundException("Card with id " + cardId + " doesn't exist in project with id " + projectId);
        }
        Card newCard = new Card(
                card.title(),
                card.summary(),
                MOCK_AUTHOR_ID,
                savedCard.get().getUpVote(),
                savedCard.get().getDownVote(),
                project
        );
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
}
