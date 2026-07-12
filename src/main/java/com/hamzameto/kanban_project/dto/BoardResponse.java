package com.hamzameto.kanban_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String ownerUsername;
    private LocalDateTime createdAt;
}