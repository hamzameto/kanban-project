package com.hamzameto.kanban_project.service;

import com.hamzameto.kanban_project.dto.CardRequest;
import com.hamzameto.kanban_project.dto.CardResponse;
import com.hamzameto.kanban_project.dto.CardUpdateRequest;
import com.hamzameto.kanban_project.entity.Board;
import com.hamzameto.kanban_project.entity.BoardList;
import com.hamzameto.kanban_project.entity.Card;
import com.hamzameto.kanban_project.repository.BoardListRepository;
import com.hamzameto.kanban_project.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BoardListRepository boardListRepository;

    public CardResponse createCard(CardRequest request, String username) {
        BoardList list = boardListRepository.findById(request.getListId())
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        checkOwnership(list, username);

        Card card = new Card();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setPosition(request.getPosition());
        card.setList(list);

        Card saved = cardRepository.save(card);

        return toResponse(saved);
    }

    public List<CardResponse> getCardsForList(Long listId, String username) {
        BoardList list = boardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        checkOwnership(list, username);

        return cardRepository.findByListId(listId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CardResponse updateCard(Long id, CardUpdateRequest request, String username) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        checkOwnership(card.getList(), username);

        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setPosition(request.getPosition());

        Card updated = cardRepository.save(card);

        return toResponse(updated);
    }

    public void deleteCard(Long id, String username) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        checkOwnership(card.getList(), username);

        cardRepository.delete(card);
    }

    private void checkOwnership(BoardList list, String username) {
        Board board = list.getBoard();
        if (!board.getOwner().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to access this card");
        }
    }

    private CardResponse toResponse(Card card) {
        return new CardResponse(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getPosition(),
                card.getList().getId()
        );
    }
}