package com.hamzameto.kanban_project.repository;

import com.hamzameto.kanban_project.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByListIdOrderByPositionAsc(Long listId);
}