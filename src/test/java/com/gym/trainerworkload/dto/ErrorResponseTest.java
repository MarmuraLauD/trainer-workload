package com.gym.trainerworkload.dto;

import com.gym.trainerworkload.dto.response.ErrorResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void twoArgConstructor_setsTimestampAndNullErrors() {
        // AAA
        ErrorResponse r = new ErrorResponse(400, "x");

        // AAA
        assertEquals(400, r.status());
        assertEquals("x", r.message());
        assertNotNull(r.timestamp());
        assertNull(r.errors());
    }

    @Test
    void fullConstructor_setsErrors() {
        // AAA
        ErrorResponse r = new ErrorResponse(422, "bad", Map.of("f","m"));

        // AAA
        assertEquals(422, r.status());
        assertEquals("bad", r.message());
        assertNotNull(r.timestamp());
        assertEquals("m", r.errors().get("f"));
    }
}
