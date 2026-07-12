package com.hamzameto.kanban_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardResponse {
    private Long id;
    private String title;
    private String description;
    private Integer position;
    private Long listId;
}