package com.gym.trainerworkload.security;

import com.gym.trainerworkload.security.util.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private static String toBase64Key(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Test
    void extractUsernameAndValidateToken_validToken() throws Exception {
        // Arrange
        SecretKey key = Jwts.SIG.HS256.key().build();
        String base64 = toBase64Key(key.getEncoded());

        JwtUtils utils = new JwtUtils();
        Field f = JwtUtils.class.getDeclaredField("jwtSecret");
        f.setAccessible(true);
        f.set(utils, base64);

        String token = Jwts.builder()
                .subject("bob")
                .expiration(Date.from(Instant.now().plus(10, TimeUnit.SECONDS.toChronoUnit())))
                .signWith(key)
                .compact();

        // Act
        String username = utils.extractUsername(token);
        Boolean isValid = utils.validateToken(token);

        // Assert
        assertEquals("bob", username);
        assertTrue(isValid);
    }

    @Test
    void validateToken_expiredToken_returnsFalse() throws Exception {
        // Arrange
        SecretKey key = Jwts.SIG.HS256.key().build();
        String base64 = toBase64Key(key.getEncoded());

        JwtUtils utils = new JwtUtils();
        Field f = JwtUtils.class.getDeclaredField("jwtSecret");
        f.setAccessible(true);
        f.set(utils, base64);

        String token = Jwts.builder()
                .subject("eve")
                .expiration(Date.from(Instant.now().minus(10, TimeUnit.SECONDS.toChronoUnit())))
                .signWith(key)
                .compact();

        // Act
        Boolean isValid = utils.validateToken(token);

        // Assert
        assertFalse(isValid);
    }
}