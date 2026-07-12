package com.hamzameto.kanban_project.service;

import com.hamzameto.kanban_project.dto.BoardListRequest;
import com.hamzameto.kanban_project.dto.BoardListUpdateRequest;
import com.hamzameto.kanban_project.dto.BoardListResponse;
import com.hamzameto.kanban_project.entity.Board;
import com.hamzameto.kanban_project.entity.BoardList;
import com.hamzameto.kanban_project.repository.BoardListRepository;
import com.hamzameto.kanban_project.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardListService {

    @Autowired
    private BoardListRepository boardListRepository;

    @Autowired
    private BoardRepository boardRepository;

    public BoardListResponse createBoardList(BoardListRequest request, String username) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        checkOwnership(board, username);

        BoardList list = new BoardList();
        list.setTitle(request.getTitle());
        list.setPosition(request.getPosition());
        list.setBoard(board);

        BoardList saved = boardListRepository.save(list);

        return toResponse(saved);
    }

    public List<BoardListResponse> getListsForBoard(Long boardId, String username) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        checkOwnership(board, username);

        return boardListRepository.findByBoardIdOrderByPositionAsc(boardId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BoardListResponse updateBoardList(Long id, BoardListUpdateRequest request, String username) {
        BoardList list = boardListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        checkOwnership(list.getBoard(), username);

        list.setTitle(request.getTitle());
        list.setPosition(request.getPosition());

        BoardList updated = boardListRepository.save(list);

        return toResponse(updated);
    }

    public void deleteBoardList(Long id, String username) {
        BoardList list = boardListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        checkOwnership(list.getBoard(), username);

        boardListRepository.delete(list);
    }

    private void checkOwnership(Board board, String username) {
        if (!board.getOwner().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to access this board");
        }
    }

    private BoardListResponse toResponse(BoardList list) {
        return new BoardListResponse(
                list.getId(),
                list.getTitle(),
                list.getPosition(),
                list.getBoard().getId()
        );
    }
}