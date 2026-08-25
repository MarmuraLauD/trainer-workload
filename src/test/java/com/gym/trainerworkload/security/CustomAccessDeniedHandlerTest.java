package com.gym.trainerworkload.security;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomAccessDeniedHandlerTest {

    private CustomAccessDeniedHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CustomAccessDeniedHandler();
    }

    @Test
    void handle_writesForbiddenErrorJson() throws Exception {
        // Arrange
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(resp.getWriter()).thenReturn(pw);

        // Act
        handler.handle(req, resp, new org.springframework.security.access.AccessDeniedException("nope"));
        pw.flush();
        String out = sw.toString();

        // Assert
        assertTrue(out.contains("Access denied"));
        assertTrue(out.contains(String.valueOf(HttpStatus.FORBIDDEN.value())));
    }

}
