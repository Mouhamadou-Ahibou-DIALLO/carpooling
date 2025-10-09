package api.carpooling.security;

import api.carpooling.utils.TokenGenerator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;

/**
 * Unit tests for {@link JwtAuthFilter}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("JwtAuthFilter Test")
@Slf4j
class JwtAuthFilterTest {

    /**
     * Utility component responsible for generating and validating JWT tokens.
     * <p>
     * Used to create access and refresh tokens for authenticated users.
     */
    private TokenGenerator tokenGenerator;

    /**
     * Custom authentication filter that intercepts HTTP requests to validate JWT tokens.
     * <p>
     * Ensures that only authenticated users can access protected endpoints.
     */
    private JwtAuthFilter jwtAuthFilter;

    /**
     * Represents the current HTTP request received by the server.
     * <p>
     * Provides access to headers, parameters, and request body content.
     */
    private HttpServletRequest request;

    /**
     * Represents the HTTP response sent back to the client.
     * <p>
     * Used to modify status codes, headers, or write content to the response body.
     */
    private HttpServletResponse response;

    /**
     * Defines the chain of filters that process the request and response.
     * <p>
     * Allows continuation of the filter execution to the next filter or endpoint.
     */
    private FilterChain filterChain;

    /**
     * Initializes all mocks before each test.
     */
    @BeforeEach
    void setUp() {
        tokenGenerator = mock(TokenGenerator.class);
        jwtAuthFilter = new JwtAuthFilter(tokenGenerator);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void beforeAll() {
        log.info("JwtAuthFilter tests initialized");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void afterAll() {
        log.info("JwtAuthFilter tests completed");
    }

    /**
     * Tests that the filter continues the chain when no Authorization header is present.
     */
    @Test
    @Order(1)
    @DisplayName("Should continue chain when no Authorization header is present")
    void testNoHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(tokenGenerator, never()).parseJwt(anyString());
    }

    /**
     * Tests that the filter sets UNAUTHORIZED status when the token is invalid.
     */
    @Test
    @Order(2)
    @DisplayName("Should set UNAUTHORIZED when token is invalid")
    void testInvalidToken() throws ServletException, IOException {
        String invalidToken = "Bearer invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn(invalidToken);
        when(tokenGenerator.parseJwt(anyString())).thenThrow(new RuntimeException("Invalid token"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    /**
     * Tests that the filter authenticates the user when token is valid.
     */
    @Test
    @Order(3)
    @DisplayName("Should authenticate user when token is valid")
    void testValidToken() throws ServletException, IOException {
        String validToken = "Bearer valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn(validToken);

        Claims claims = mock(Claims.class);
        UUID userId = UUID.randomUUID();
        when(claims.getSubject()).thenReturn(userId.toString());
        when(claims.get("role_user")).thenReturn("ROLE_USER");

        when(tokenGenerator.parseJwt("valid.jwt.token")).thenReturn(claims);
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(userId, auth.getPrincipal());
        Assertions.assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
