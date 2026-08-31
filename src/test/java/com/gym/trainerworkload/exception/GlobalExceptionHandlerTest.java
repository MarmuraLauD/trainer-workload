package com.gym.trainerworkload.exception;

import com.gym.trainerworkload.dto.response.ErrorResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationExceptions_buildsErrorResponseWithFieldErrors() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult binding = mock(BindingResult.class);
        FieldError fe = new FieldError("obj", "field", "must not be null");
        when(binding.getAllErrors()).thenReturn(List.of(fe));
        when(ex.getBindingResult()).thenReturn(binding);

        // Act
        ErrorResponse resp = handler.handleValidationExceptions(ex);

        // Assert
        assertEquals(400, resp.status());
        assertEquals("Validation failed", resp.message());
        assertNotNull(resp.errors());
        assertEquals("must not be null", resp.errors().get("field"));
    }

    @Test
    void handleGenericException_returnsGenericErrorResponse() {
        // Arrange
        Exception ex = new IllegalArgumentException("boom");

        // Act
        ErrorResponse resp = handler.handleGenericException(ex);

        // Assert
        assertEquals(400, resp.status());
        assertEquals("An error occurred while processing the request. Please verify your data.", resp.message());
    }

}