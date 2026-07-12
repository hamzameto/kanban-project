package com.hamzameto.kanban_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardUpdateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Integer position;
}