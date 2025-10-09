package api.carpooling.application.service;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.application.exception.UserExistsAlready;
import api.carpooling.application.exception.PasswordNotMatchException;
import api.carpooling.application.exception.ExpiredRefreshTokenException;
import api.carpooling.application.mapper.UserMapper;
import api.carpooling.application.service.impl.AuthServiceImpl;
import api.carpooling.domain.User;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.repository.UserRepository;
import api.carpooling.utils.EncodedPassword;
import api.carpooling.utils.TokenGenerator;

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
class AuthServiceImplTest {

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
     * Prépare un user et un userDTO réutilisables avant chaque test.
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
        user.setTokenExpired(LocalDateTime.now().plusDays(1));

        LocalDateTime now = LocalDateTime.now();
        userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                "jwt.token",
                "refresh.token",
                user.getTokenExpired(),
                user.getRoleUser(),
                now.minusDays(1),
                now
        );
    }

    // -------------------------
    // REGISTER
    // -------------------------

    /**
     * Vérifie le chemin heureux d'enregistrement d'un utilisateur.
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
        when(userRepository.existsByRefreshToken(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(tokenGenerator.generateJwtToken(any(UUID.class), anyString())).thenReturn("jwt.token");
        when(tokenGenerator.generateRefreshToken()).thenReturn("refresh.token");
        when(tokenGenerator.getRefreshTokenExpiry()).thenReturn(
                Date.from(LocalDateTime.now().plusDays(7)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toInstant())
        );
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        try (MockedStatic<EncodedPassword> encodedStatic = mockStatic(EncodedPassword.class)) {
            encodedStatic.when(() -> EncodedPassword.encode(request.password())).thenReturn("hashedPass");

            UserDTO result = authService.register(request);

            assertNotNull(result);
            assertEquals("TestUser", result.username());
            verify(userRepository, times(1)).saveAndFlush(any(User.class));
            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    /**
     * Vérifie que l'inscription échoue si l'email ou le username existe déjà.
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
     * Vérifie que l'inscription échoue si le mot de passe ne respecte pas la regex.
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
     * Vérifie la connexion (login) réussie.
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
     * Vérifie que le login lève UserNotFoundException si l'email n'existe pas.
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
     * Vérifie que le login lève PasswordNotMatchException si le mot de passe est incorrect.
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
     * Vérifie le rafraîchissement de token dans le cas heureux.
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
     * Vérifie que refreshToken lance ExpiredRefreshTokenException si le token est expiré.
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
     * Vérifie la récupération du profil utilisateur via le token (me).
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
     * Vérifie que me() lève UserNotFoundException si l'utilisateur n'existe pas.
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
}
