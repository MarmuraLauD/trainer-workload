package com.gym.trainerworkload.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class TransactionFilterTest {

    private final TransactionFilter filter = new TransactionFilter();

    @AfterEach
    void tearDown() {
        MDC.remove("transactionId");
    }

    @Test
    void whenHeaderPresent_thenMdcContainsSameValueDuringFilter() throws Exception {
        // AAA
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("X-Transaction-Id")).thenReturn("tx-123");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");
        when(response.getStatus()).thenReturn(200);

        doAnswer(_ -> {
            assertEquals("tx-123", MDC.get("transactionId"));
            return null;
        }).when(chain).doFilter(request, response);

        // AAA
        filter.doFilter(request, response, chain);

        // AAA
        assertNull(MDC.get("transactionId"));
    }

    @Test
    void whenHeaderMissing_thenMdcHasGeneratedValueDuringFilter() throws Exception {
        // AAA
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("X-Transaction-Id")).thenReturn(null);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/test2");
        when(response.getStatus()).thenReturn(201);

        doAnswer(_ -> {
            String v = MDC.get("transactionId");
            assertNotNull(v);
            assertFalse(v.isEmpty());
            return null;
        }).when(chain).doFilter(request, response);

        // AAA
        filter.doFilter(request, response, chain);

        // AAA
        assertNull(MDC.get("transactionId"));
    }
}
