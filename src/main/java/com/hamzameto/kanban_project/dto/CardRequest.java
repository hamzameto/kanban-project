package com.hamzameto.kanban_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CardRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Integer position;

    @NotNull(message = "List ID is required")
    private Long listId;
}