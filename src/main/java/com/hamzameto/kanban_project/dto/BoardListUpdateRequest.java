package com.hamzameto.kanban_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardListUpdateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private Integer position;
}