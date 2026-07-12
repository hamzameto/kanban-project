package com.hamzameto.kanban_project.controller;

import com.hamzameto.kanban_project.dto.CardRequest;
import com.hamzameto.kanban_project.dto.CardUpdateRequest;
import com.hamzameto.kanban_project.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<?> createCard(@Valid @RequestBody CardRequest request, Authentication auth) {
        try {
            return ResponseEntity.ok(cardService.createCard(request, auth.getName()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/list/{listId}")
    public ResponseEntity<?> getCardsForList(@PathVariable Long listId, Authentication auth) {
        try {
            return ResponseEntity.ok(cardService.getCardsForList(listId, auth.getName()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCard(@PathVariable Long id, @Valid @RequestBody CardUpdateRequest request, Authentication auth) {
        try {
            return ResponseEntity.ok(cardService.updateCard(id, request, auth.getName()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id, Authentication auth) {
        try {
            cardService.deleteCard(id, auth.getName());
            return ResponseEntity.ok(Map.of("message", "Card deleted successfully"));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}