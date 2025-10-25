package api.carpooling.utils;

import api.carpooling.domain.User;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Unit tests for {@link UserTokenService}.
 * <p>
 * These tests verify that the service correctly generates JWT and refresh tokens,
 * sets the token expiration, handles refresh token collisions, and saves the user entity.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserTokenService Test")
@Slf4j
public class UserTokenServiceTest {

    /**
     * Mocked {@link UserRepository} for user persistence operations.
     */
    private static UserRepository userRepository;

    /**
     * Mocked {@link TokenGenerator} for generating JWT and refresh tokens.
     */
    private static TokenGenerator tokenGenerator;

    /**
     * Instance of {@link UserTokenService} under test.
     */
    private static UserTokenService userTokenService;

    /**
     * Displays start message before all tests and sets up mocks.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting UserTokenService tests");
        userRepository = Mockito.mock(UserRepository.class);
        tokenGenerator = Mockito.mock(TokenGenerator.class);
        userTokenService = new UserTokenService(userRepository, tokenGenerator);
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished UserTokenService tests");
    }

    /**
     * Verifies that generateTokens sets JWT, refresh token, expiry date and calls repository save.
     */
    @Test
    @Order(1)
    @DisplayName("Should generate JWT and refresh token and save user")
    void testGenerateTokens() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setRoleUser(RoleUser.ROLE_DRIVER);

        String jwtToken = "jwt-token";
        String refreshToken = "refresh-token";
        LocalDateTime expiry = LocalDateTime.now().plusDays(7);

        when(tokenGenerator.generateJwtToken(userId, RoleUser.ROLE_DRIVER.name())).thenReturn(jwtToken);
        when(tokenGenerator.generateRefreshToken()).thenReturn(refreshToken);
        when(tokenGenerator.getRefreshTokenExpiry()).thenReturn(Date.from(expiry
                .atZone(ZoneId.systemDefault()).toInstant()));
        when(userRepository.existsByRefreshToken(refreshToken)).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userTokenService.generateTokens(user);
        assertAll("User tokens",
                () -> assertEquals(jwtToken, updatedUser.getToken(),
                        "JWT token should be set"),
                () -> assertEquals(refreshToken, updatedUser.getRefreshToken(),
                        "Refresh token should be set"),
                () -> assertEquals(expiry.withNano(0),
                        updatedUser.getTokenExpired().withNano(0),
                        "Token expiry should match")
        );

        verify(userRepository, times(1)).save(user);
        verify(tokenGenerator, times(1)).generateJwtToken(userId,
                RoleUser.ROLE_DRIVER.name());
        verify(tokenGenerator, times(1)).generateRefreshToken();
    }

    /**
     * Verifies that generateTokens regenerates refresh token if already exists.
     */
    @Test
    @Order(2)
    @DisplayName("Should regenerate refresh token if already exists")
    void testGenerateTokensRefreshTokenCollision() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRoleUser(RoleUser.ROLE_PASSENGER);

        String jwtToken = "jwt-token";
        String firstToken = "token-1";
        String secondToken = "token-2";
        LocalDateTime expiry = LocalDateTime.now().plusDays(7);

        when(tokenGenerator.generateJwtToken(any(), any())).thenReturn(jwtToken);
        when(tokenGenerator.generateRefreshToken()).thenReturn(firstToken, secondToken);
        when(tokenGenerator.getRefreshTokenExpiry()).thenReturn(Date.from(expiry
                .atZone(ZoneId.systemDefault()).toInstant()));
        when(userRepository.existsByRefreshToken(firstToken)).thenReturn(true);
        when(userRepository.existsByRefreshToken(secondToken)).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userTokenService.generateTokens(user);
        assertEquals(secondToken, updatedUser.getRefreshToken(),
                "Refresh token should be regenerated if collision occurs");
        verify(userRepository, times(1)).save(user);
    }
}
