package com.hamzameto.kanban_project.service;

import com.hamzameto.kanban_project.dto.LoginRequest;
import com.hamzameto.kanban_project.security.JwtUtil;
import java.util.Map;
import com.hamzameto.kanban_project.dto.RegisterRequest;
import com.hamzameto.kanban_project.entity.User;
import com.hamzameto.kanban_project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldThrowWhenUsernameTaken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void registerShouldSucceedWhenValid() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedpassword");

        authService.register(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void loginShouldSucceedWhenCredentialsValid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedpassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn("fake-jwt-token");

        Map<String, String> result = authService.login(request);

        assertEquals("fake-jwt-token", result.get("token"));
        assertEquals("testuser", result.get("username"));
    }

    @Test
    void loginShouldThrowWhenPasswordWrong() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashedpassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}