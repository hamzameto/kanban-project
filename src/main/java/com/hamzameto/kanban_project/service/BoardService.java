package com.hamzameto.kanban_project.service;

import com.hamzameto.kanban_project.dto.BoardRequest;
import com.hamzameto.kanban_project.dto.BoardResponse;
import com.hamzameto.kanban_project.entity.Board;
import com.hamzameto.kanban_project.entity.User;
import com.hamzameto.kanban_project.repository.BoardRepository;
import com.hamzameto.kanban_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public BoardResponse createBoard(BoardRequest request, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setOwner(owner);
        board.setCreatedAt(LocalDateTime.now());

        Board saved = boardRepository.save(board);

        return toResponse(saved);
    }

    public List<BoardResponse> getBoardsForUser(String username) {
        return boardRepository.findByOwnerUsername(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BoardResponse getBoardById(Long id, String username) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        checkOwnership(board, username);

        return toResponse(board);
    }

    public void deleteBoard(Long id, String username) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        checkOwnership(board, username);

        boardRepository.delete(board);
    }

    private void checkOwnership(Board board, String username) {
        if (!board.getOwner().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to access this board");
        }
    }

    private BoardResponse toResponse(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getOwner().getUsername(),
                board.getCreatedAt()
        );
    }
}