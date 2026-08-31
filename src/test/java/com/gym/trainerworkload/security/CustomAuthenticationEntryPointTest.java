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

class CustomAuthenticationEntryPointTest {

    private CustomAuthenticationEntryPoint entry;

    @BeforeEach
    void setUp() {
        entry = new CustomAuthenticationEntryPoint();
    }

    @Test
    void commence_writesUnauthorizedErrorJson() throws Exception {
        // Arrange
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(resp.getWriter()).thenReturn(pw);

        // Act
        entry.commence(req, resp, new org.springframework.security.core.AuthenticationException("bad"){});
        pw.flush();
        String out = sw.toString();

        // Assert
        assertTrue(out.contains("Unauthorized"));
        assertTrue(out.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value())));
    }

}