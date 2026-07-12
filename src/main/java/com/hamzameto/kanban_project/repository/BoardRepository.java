package com.hamzameto.kanban_project.repository;

import com.hamzameto.kanban_project.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByOwnerUsername(String username);
}