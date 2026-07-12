package com.hamzameto.kanban_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoardListRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private Integer position;

    @NotNull(message = "Board ID is required")
    private Long boardId;
}