package api.carpooling.utils;

import api.carpooling.configuration.JwtProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TokenGenerator}.
 * <p>
 * These tests verify JWT and refresh token generation, parsing, and expiration handling.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TokenGenerator Test")
@Slf4j
public class TokenGeneratorTest {

    /**
     * Utility component responsible for generating and validating JWT tokens.
     * <p>
     * Used to create access and refresh tokens for authenticated users.
     */
    private static TokenGenerator tokenGenerator;

    /**
     * Access token expiration time in milliseconds (1 hour).
     * <p>
     * Used to define the validity period of JWT access tokens during tests.
     */
    private static final long ACCESS_TOKEN_EXPIRATION_MS = 3600000L;

    /**
     * Refresh token expiration time in milliseconds (24 hours).
     * <p>
     * Used to define the validity period of JWT refresh tokens during tests.
     */
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 86400000L;

    /**
     * Initializes all mocks before each test.
     */
    @BeforeAll
    static void setUpAll() {
        JwtProperties jwtProperties = Mockito.mock(JwtProperties.class);
        when(jwtProperties.getSecret()).thenReturn("MySuperSecretKeyForJWTsMySuperSecretKey");
        when(jwtProperties.getExpirationMs()).thenReturn(ACCESS_TOKEN_EXPIRATION_MS);
        when(jwtProperties.getRefreshExpirationMs()).thenReturn(REFRESH_TOKEN_EXPIRATION_MS);

        tokenGenerator = new TokenGenerator(jwtProperties);
        log.info("Starting TokenGenerator tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished TokenGenerator tests");
    }

    /**
     * Verifies that JWT token can be generated and parsed correctly.
     */
    @Test
    @Order(1)
    @DisplayName("Should generate and parse JWT token correctly")
    void testGenerateAndParseJwtToken() {
        UUID userId = UUID.randomUUID();
        String role = "ROLE_USER";

        String jwt = tokenGenerator.generateJwtToken(userId, role);
        assertNotNull(jwt, "JWT token should not be null");

        Claims claims = tokenGenerator.parseJwt(jwt);
        assertEquals(userId.toString(), claims.getSubject(), "JWT subject should match userId");
        assertEquals(role, claims.get("role_user"), "JWT role_user claim should match role");
    }

    /**
     * Verifies that a refresh token is generated and is non-empty.
     */
    @Test
    @Order(2)
    @DisplayName("Should generate non-empty refresh token")
    void testGenerateRefreshToken() {
        String refreshToken = tokenGenerator.generateRefreshToken();
        assertNotNull(refreshToken, "Refresh token should not be null");
        assertFalse(refreshToken.isEmpty(), "Refresh token should not be empty");
        assertFalse(refreshToken.contains("-"), "Refresh token should not contain dashes");
    }

    /**
     * Verifies that refresh token expiry date is in the future.
     */
    @Test
    @Order(3)
    @DisplayName("Should return valid refresh token expiry")
    void testRefreshTokenExpiry() {
        Date expiry = tokenGenerator.getRefreshTokenExpiry();
        assertNotNull(expiry, "Expiry date should not be null");
        assertTrue(expiry.after(new Date()), "Expiry date should be in the future");
    }
}
