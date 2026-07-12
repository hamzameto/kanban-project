package com.hamzameto.kanban_project.controller;

import com.hamzameto.kanban_project.dto.BoardListRequest;
import com.hamzameto.kanban_project.service.BoardListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/boardlists")
public class BoardListController {

    @Autowired
    private BoardListService boardListService;

    @PostMapping
    public ResponseEntity<?> createBoardList(@Valid @RequestBody BoardListRequest request, Authentication auth) {
        try {
            return ResponseEntity.ok(boardListService.createBoardList(request, auth.getName()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getListsForBoard(@PathVariable Long boardId, Authentication auth) {
        try {
            return ResponseEntity.ok(boardListService.getListsForBoard(boardId, auth.getName()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoardList(@PathVariable Long id, @Valid @RequestBody BoardListRequest request, Authentication auth) {
        try {
            return ResponseEntity.ok(boardListService.updateBoardList(id, request, auth.getName()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoardList(@PathVariable Long id, Authentication auth) {
        try {
            boardListService.deleteBoardList(id, auth.getName());
            return ResponseEntity.ok(Map.of("message", "List deleted successfully"));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}