package com.project.tinkoff.service.implementation;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.mapper.CardMapper;
import com.project.tinkoff.repository.CardRepository;
import com.project.tinkoff.repository.ProjectMemberRepository;
import com.project.tinkoff.repository.ProjectRepository;
import com.project.tinkoff.repository.UserRepository;
import com.project.tinkoff.repository.models.*;
import com.project.tinkoff.rest.v1.models.request.CardRequest;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import com.project.tinkoff.service.CardService;
import com.project.tinkoff.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final ProjectRepository projectRepository;
    private final UserContextService userContextService;
    private final CardMapper cardMapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Override
    @Cacheable(cacheNames = "projectCards", key = "#projectId")
    public List<CardResponse> getAllCards(long projectId) {
        return repository.findAllByProjectIdOrderByUpVoteDescCreateAtAsc(projectId).stream()
                .map(card -> {
                    var author = userRepository.findUserById(card.getAuthorId());
                    return cardMapper.fromModel(card, author.getUsername());
                })
                .toList();
    }

    @Override
    public CardResponse getCardById(long projectId, long cardId) {
        Optional<Card> card = repository.findByProjectIdAndId(projectId, cardId);
        if (card.isEmpty()) {
            throw new DataNotFoundException(String.format("Card with id %d doesn't exist in project with id %d", cardId, projectId));
        }
        var author = userRepository.findUserById(card.get().getAuthorId());
        return cardMapper.fromModel(card.get(), author.getUsername());
    }

    @Override
    @Transactional
    public CardResponse createCard(long projectId, CardRequest card) {
        Optional<Project> optProject = projectRepository.findById(projectId);
        if (optProject.isEmpty()) {
            throw new DataNotFoundException("Project doesn't exists");
        }
        Project project = optProject.get();
        UserDto user = userContextService.getCurrentUser();
        Card newCard = Card.builder()
                .title(card.title())
                .summary(card.summary())
                .authorId(user.id())
                .project(project)
                .status(CardStatus.NEW)
                .build();
        Card savedCard = repository.save(newCard);
        return cardMapper.fromModel(savedCard, user.username());
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
        var author = userRepository.findUserById(updatedCard.getAuthorId());
        return cardMapper.fromModel(updatedCard, author.getUsername());
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
        UserDto currentUser = userContextService.getCurrentUser();
        ProjectMember member = projectMemberRepository.findProjectMemberByProjectIdAndUserId(projectId, currentUser.id()).get();

        Optional<Card> optCard = repository.findByProjectIdAndId(projectId, cardId);
        if (member.getVoteCount() == 0) {
            return false;
        }
        if (optCard.isEmpty()) {
            throw new DataNotFoundException(String.format("Card with id %d doesn't exist in project with id %d", cardId, projectId));
        }
        var card = optCard.get();
        if (card.getStatus() != CardStatus.NEW) {
            return false;
        }
        member.setVoteCount(member.getVoteCount() - 1);
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
