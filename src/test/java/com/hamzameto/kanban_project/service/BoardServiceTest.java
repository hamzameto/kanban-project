package com.hamzameto.kanban_project.service;

import com.hamzameto.kanban_project.dto.BoardRequest;
import com.hamzameto.kanban_project.dto.BoardResponse;
import com.hamzameto.kanban_project.entity.Board;
import com.hamzameto.kanban_project.entity.User;
import com.hamzameto.kanban_project.repository.BoardRepository;
import com.hamzameto.kanban_project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    void createBoardShouldSucceedWhenUserExists() {
        BoardRequest request = new BoardRequest();
        request.setTitle("My Board");

        User owner = new User();
        owner.setUsername("testuser");

        Board savedBoard = new Board();
        savedBoard.setId(1L);
        savedBoard.setTitle("My Board");
        savedBoard.setOwner(owner);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

        BoardResponse response = boardService.createBoard(request, "testuser");

        assertEquals("My Board", response.getTitle());
        assertEquals("testuser", response.getOwnerUsername());
    }

    @Test
    void getBoardByIdShouldThrowSecurityExceptionWhenNotOwner() {
        User actualOwner = new User();
        actualOwner.setUsername("realowner");

        Board board = new Board();
        board.setId(1L);
        board.setTitle("Someone Else's Board");
        board.setOwner(actualOwner);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        assertThrows(SecurityException.class, () ->
                boardService.getBoardById(1L, "differentuser")
        );
    }

    @Test
    void getBoardByIdShouldThrowWhenBoardNotFound() {
        when(boardRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                boardService.getBoardById(999L, "testuser")
        );
    }
}