package api.carpooling.security;

import api.carpooling.utils.TokenGenerator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JwtAuthFilter}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("JwtAuthFilter Test")
class JwtAuthFilterTest {

    private TokenGenerator tokenGenerator;
    private JwtAuthFilter jwtAuthFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
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
        System.out.println("JwtAuthFilter tests initialized");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void afterAll() {
        System.out.println("JwtAuthFilter tests completed");
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
