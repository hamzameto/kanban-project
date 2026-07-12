package com.hamzameto.kanban_project.controller;

import com.hamzameto.kanban_project.dto.BoardRequest;
import com.hamzameto.kanban_project.dto.BoardResponse;
import com.hamzameto.kanban_project.security.JwtUtil;
import com.hamzameto.kanban_project.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoardService boardService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void createBoardShouldReturn200WhenValid() throws Exception {
        BoardRequest request = new BoardRequest();
        request.setTitle("My Board");

        BoardResponse response = new BoardResponse(1L, "My Board", "testuser", LocalDateTime.now());

        when(boardService.createBoard(any(BoardRequest.class), eq("testuser"))).thenReturn(response);

        mockMvc.perform(post("/api/boards")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My Board"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void createBoardShouldReturn400WhenTitleBlank() throws Exception {
        BoardRequest request = new BoardRequest();
        request.setTitle("");

        mockMvc.perform(post("/api/boards")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}