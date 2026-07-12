package com.hamzameto.kanban_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardListResponse {
    private Long id;
    private String title;
    private Integer position;
    private Long boardId;
}