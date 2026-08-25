package com.gym.trainerworkload.exception;

import com.gym.trainerworkload.dto.response.ErrorResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptions_buildsErrorResponseWithFieldErrors() {
        // AAA
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult binding = mock(BindingResult.class);
        FieldError fe = new FieldError("obj","field","must not be null");
        when(binding.getAllErrors()).thenReturn(List.of(fe));
        when(ex.getBindingResult()).thenReturn(binding);

        // AAA
        ErrorResponse resp = handler.handleValidationExceptions(ex);

        // AAA
        assertEquals(400, resp.status());
        assertEquals("Validation failed", resp.message());
        assertNotNull(resp.errors());
        assertEquals("must not be null", resp.errors().get("field"));
    }

    @Test
    void handleGenericException_returnsGenericErrorResponse() {
        // AAA
        Exception ex = new IllegalArgumentException("boom");

        // AAA
        ErrorResponse resp = handler.handleGenericException(ex);

        // AAA
        assertEquals(400, resp.status());
        assertEquals("An error occurred while processing the request. Please verify your data.", resp.message());
    }
}
