package api.carpooling.application.service.impl;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.application.exception.UserExistsAlready;
import api.carpooling.application.exception.PasswordNotMatchException;
import api.carpooling.application.exception.ExpiredRefreshTokenException;
import api.carpooling.application.mapper.UserMapper;
import api.carpooling.domain.User;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.repository.UserRepository;
import api.carpooling.utils.EncodedPassword;
import api.carpooling.utils.TokenGenerator;
import api.carpooling.utils.UserTokenService;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link AuthServiceImpl}.
 * <p>
 * Tests cover registration, login, refresh token and "me" functionality,
 * including success paths and expected exceptions.
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AuthServiceImpl Tests")
@Slf4j
public class AuthServiceImplTest {

    /**
     * Mocked instance of {@link UserRepository} used to simulate database operations.
     * <p>
     * Prevents real database access during tests and allows control over returned data.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked instance of {@link UserMapper} used to convert entities to DTOs and vice versa.
     * <p>
     * Helps test the service logic without invoking the real mapping logic.
     */
    @Mock
    private UserMapper userMapper;

    /**
     * Mocked instance of {@link TokenGenerator} used to simulate JWT token creation.
     * <p>
     * Avoids generating real tokens during service tests.
     */
    @Mock
    private TokenGenerator tokenGenerator;

    /**
     * Injects the mocked dependencies into an instance of {@link AuthServiceImpl}.
     * <p>
     * This ensures that when methods of {@code AuthServiceImpl} are called,
     * they use the mocked components instead of real implementations.
     */
    @InjectMocks
    private AuthServiceImpl authService;

    /**
     * Mocked instance of {@link UserTokenService} used to simulate token operations.
     * <p>
     * Generating and parsing JWT and refresh tokens using tokenGenerator class
     */
    @Mock
    private UserTokenService userTokenService;

    /**
     * Represents a mock {@link User} entity used as test data.
     * <p>
     * Serves as a sample object for testing authentication and user-related logic.
     */
    private User user;

    /**
     * Represents a mock {@link UserDTO} used as test data.
     * <p>
     * Simulates the user data transfer object typically returned by the service layer.
     */
    private UserDTO userDTO;

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void beforeAll() {
        log.info("AuthServiceImpl tests initialized");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void afterAll() {
        log.info("AuthServiceImpl tests completed");
    }

    /**
     * Prepare a reusable user and userDTO before each test.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setUsername("TestUser");
        user.setPassword("EncodedPass123!");
        user.setPhoneNumber("+33600000000");
        user.setRoleUser(RoleUser.ROLE_PASSENGER);
        user.setLastLogin(LocalDateTime.now());
        user.setTokenExpired(LocalDateTime.now().plusDays(1));

        LocalDateTime now = LocalDateTime.now();
        userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                "jwt.token",
                "refresh.token",
                user.getTokenExpired(),
                user.getLastLogin(),
                user.getRoleUser(),
                now.minusDays(1),
                now
        );
    }

    // -------------------------
    // REGISTER
    // -------------------------

    /**
     * Checks the happy path of a user’s registration.
     */
    @Test
    @Order(1)
    @DisplayName("Should register user successfully")
    void testRegisterSuccess() {
        // order of RegisterUserRequest: username, email, password, phoneNumber
        RegisterUserRequest request = new RegisterUserRequest(
                "TestUser",
                "test@example.com",
                "Password@123",
                "+33600000000"
        );

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        when(userTokenService.generateTokens(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setToken("jwt.token");
            u.setRefreshToken("refresh.token");
            return u;
        });

        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        try (MockedStatic<EncodedPassword> encodedStatic = mockStatic(EncodedPassword.class)) {
            encodedStatic.when(() -> EncodedPassword.encode(request.password())).thenReturn("hashedPass");

            UserDTO result = authService.register(request);

            assertNotNull(result);
            assertEquals("TestUser", result.username());
            verify(userRepository, times(1)).saveAndFlush(any(User.class));
        }
    }

    /**
     * Checks that the registration fails if the email or username already exists.
     */
    @Test
    @Order(2)
    @DisplayName("Should throw UserExistsAlready when email or username exists")
    void testRegisterUserAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest(
                "TestUser",
                "test@example.com",
                "Password@123",
                "+33600000000"
        );

        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(UserExistsAlready.class, () -> authService.register(request));
    }

    /**
     * Checks that the registration fails if the password does not respect the regex.
     */
    @Test
    @Order(3)
    @DisplayName("Should throw PasswordNotMatchException when password is weak")
    void testRegisterWeakPassword() {
        RegisterUserRequest request = new RegisterUserRequest(
                "TestUser",
                "test@example.com",
                "weakpass",
                "+33600000000"
        );

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> authService.register(request));
    }

    // -------------------------
    // LOGIN
    // -------------------------

    /**
     * checks the successful connection (login).
     */
    @Test
    @Order(4)
    @DisplayName("Should login successfully")
    void testLoginSuccess() {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "Password@123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(tokenGenerator.generateJwtToken(any(UUID.class), anyString())).thenReturn("jwt.token");
        when(tokenGenerator.generateRefreshToken()).thenReturn("refresh.token");
        when(tokenGenerator.getRefreshTokenExpiry()).thenReturn(
                Date.from(LocalDateTime.now().plusDays(7)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant())
        );
        when(userRepository.existsByRefreshToken(anyString())).thenReturn(false);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        try (MockedStatic<EncodedPassword> enc = mockStatic(EncodedPassword.class)) {
            enc.when(() -> EncodedPassword.isRightPassword(request.password(), user.getPassword()))
                    .thenReturn(true);

            UserDTO result = authService.login(request);

            assertNotNull(result);
            assertEquals("TestUser", result.username());
            verify(userRepository, times(1)).findByEmail(anyString());
            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    /**
     * Checks that the login raises UserNotFoundException if the email does not exist.
     */
    @Test
    @Order(5)
    @DisplayName("Should throw UserNotFoundException when user not found at login")
    void testLoginUserNotFound() {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "Password@123");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(request));
    }

    /**
     * Check that the login is PasswordNotMatchException if the password is incorrect.
     */
    @Test
    @Order(6)
    @DisplayName("Should throw PasswordNotMatchException when password incorrect")
    void testLoginPasswordIncorrect() {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "Password@123");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        try (MockedStatic<EncodedPassword> enc = mockStatic(EncodedPassword.class)) {
            enc.when(() -> EncodedPassword.isRightPassword(request.password(), user.getPassword()))
                    .thenReturn(false);

            assertThrows(PasswordNotMatchException.class, () -> authService.login(request));
        }
    }

    // -------------------------
    // REFRESH TOKEN
    // -------------------------

    /**
     * Check the token refresh in happy case.
     */
    @Test
    @Order(7)
    @DisplayName("Should refresh token successfully")
    void testRefreshTokenSuccess() {
        when(userRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(user));
        when(tokenGenerator.generateJwtToken(any(UUID.class), anyString())).thenReturn("new.jwt.token");
        when(tokenGenerator.getRefreshTokenExpiry()).thenReturn(
                Date.from(LocalDateTime.now().plusDays(7)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant())
        );
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = authService.refreshToken("validRefreshToken");
        assertNotNull(result);
        verify(userRepository, times(1)).findByRefreshToken(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Checks that refreshToken launches ExpiredRefreshTokenException if the token is expired.
     */
    @Test
    @Order(8)
    @DisplayName("Should throw ExpiredRefreshTokenException when token expired")
    void testRefreshTokenExpired() {
        user.setTokenExpired(LocalDateTime.now().minusDays(1));
        when(userRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(user));

        assertThrows(ExpiredRefreshTokenException.class, () -> authService.refreshToken("expiredToken"));
    }

    // -------------------------
    // ME
    // -------------------------

    /**
     * Verifies the retrieval of the user profile via the token (me).
     */
    @Test
    @Order(9)
    @DisplayName("Should return user profile for valid JWT")
    void testMeSuccess() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = authService.me("Bearer valid.jwt.token");
        assertEquals("TestUser", result.username());
        verify(tokenGenerator, times(1)).parseJwt(anyString());
    }

    /**
     * Checks that me() raises UserNotFoundException if the user does not exist.
     */
    @Test
    @Order(10)
    @DisplayName("Should throw UserNotFoundException when user not found in me()")
    void testMeUserNotFound() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(UUID.randomUUID().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.me("Bearer valid.jwt.token"));
    }

    // -------------------------
    // LOGOUT
    // -------------------------

    /**
     * Verifies that logout() correctly invalidates the user's tokens.
     */
    @Test
    @Order(11)
    @DisplayName("Should logout user successfully")
    void testLogoutSuccess() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        authService.logout("Bearer valid.jwt.token");

        verify(userRepository, times(1)).save(user);
        assertNull(user.getToken());
        assertNull(user.getRefreshToken());
        assertNotNull(user.getLastLogin());
    }

    /**
     * Ensures logout() throws UserNotFoundException when no user is found.
     */
    @Test
    @Order(12)
    @DisplayName("Should throw UserNotFoundException when user not found in logout()")
    void testLogoutUserNotFound() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(UUID.randomUUID().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.logout("Bearer valid.jwt.token"));
    }

    /**
     * Ensures logout() throws an exception if the token is malformed or invalid.
     */
    @Test
    @Order(13)
    @DisplayName("Should throw IllegalArgumentException for invalid token in logout()")
    void testLogoutInvalidToken() {
        when(tokenGenerator.parseJwt(anyString())).thenThrow(new IllegalArgumentException("Invalid JWT token"));
        assertThrows(IllegalArgumentException.class, () -> authService.logout("Bearer invalid.jwt.token"));
    }

}
