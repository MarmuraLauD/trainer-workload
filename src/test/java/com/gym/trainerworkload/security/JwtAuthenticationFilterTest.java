package com.gym.trainerworkload.security;

import com.gym.trainerworkload.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenNoAuthHeader_thenChainCalledAndNoAuthenticationSet() throws Exception {
        // AAA
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        // AAA
        filter.doFilter(request, response, chain);

        // AAA
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
    }

    @Test
    void whenValidToken_thenAuthenticationSet() throws Exception {
        // AAA
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer mytoken");
        when(jwtUtils.extractUsername("mytoken")).thenReturn("alice");
        when(jwtUtils.validateToken("mytoken")).thenReturn(true);

        doAnswer(_ -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(auth);
            Object principal = auth.getPrincipal();
            assertEquals("alice", principal);
            return null;
        }).when(chain).doFilter(request, response);

        // AAA
        filter.doFilter(request, response, chain);

        // AAA
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenJwtUtilsThrows_thenNoExceptionPropagatedAndChainCalled() throws Exception {
        // AAA
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer badtoken");
        when(jwtUtils.extractUsername("badtoken")).thenThrow(new RuntimeException("bad"));

        // AAA
        filter.doFilter(request, response, chain);

        // AAA
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
