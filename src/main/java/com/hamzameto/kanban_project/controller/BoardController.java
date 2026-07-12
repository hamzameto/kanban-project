package com.hamzameto.kanban_project.controller;

import com.hamzameto.kanban_project.dto.BoardRequest;
import com.hamzameto.kanban_project.dto.BoardResponse;
import com.hamzameto.kanban_project.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping
    public ResponseEntity<?> createBoard(@Valid @RequestBody BoardRequest request, Authentication auth) {
        try {
            BoardResponse response = boardService.createBoard(request, auth.getName());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getMyBoards(Authentication auth) {
        return ResponseEntity.ok(boardService.getBoardsForUser(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable Long id, Authentication auth) {
        try {
            return ResponseEntity.ok(boardService.getBoardById(id, auth.getName()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id, Authentication auth) {
        try {
            boardService.deleteBoard(id, auth.getName());
            return ResponseEntity.ok(Map.of("message", "Board deleted successfully"));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}