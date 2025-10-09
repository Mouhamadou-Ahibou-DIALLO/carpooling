package api.carpooling.utils;

import api.carpooling.configuration.JwtProperties;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TokenGenerator}.
 * <p>
 * These tests verify JWT and refresh token generation, parsing, and expiration handling.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TokenGenerator Test")
class TokenGeneratorTest {

    private static TokenGenerator tokenGenerator;

    @BeforeAll
    static void setUpAll() {
        JwtProperties jwtProperties = Mockito.mock(JwtProperties.class);
        when(jwtProperties.getSecret()).thenReturn("MySuperSecretKeyForJWTsMySuperSecretKey");
        when(jwtProperties.getExpirationMs()).thenReturn(3600000L);
        when(jwtProperties.getRefreshExpirationMs()).thenReturn(86400000L);

        tokenGenerator = new TokenGenerator(jwtProperties);
        System.out.println("Starting TokenGenerator tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished TokenGenerator tests");
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
