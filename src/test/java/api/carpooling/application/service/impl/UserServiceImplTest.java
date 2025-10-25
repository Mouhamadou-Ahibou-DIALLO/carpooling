package api.carpooling.application.service.impl;

import api.carpooling.application.dto.user.CompleteUserRequest;
import api.carpooling.application.dto.user.UpdateRequestUser;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.exception.RoleAssignmentNotAllowedException;
import api.carpooling.application.exception.UserExistsAlready;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.application.mapper.UserResponseMapper;
import api.carpooling.domain.User;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.repository.UserRepository;
import api.carpooling.utils.TokenGenerator;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserServiceImpl}.
 * <p>
 * Covers profile completion, user update, and deletion scenarios.
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserServiceImpl Tests")
@Slf4j
public class UserServiceImplTest {

    /** Mocked repository for User entity. */
    @Mock
    private UserRepository userRepository;

    /** Mocked JWT token utility. */
    @Mock
    private TokenGenerator tokenGenerator;

    /** Mocked mapper for converting User entity to response DTO. */
    @Mock
    private UserResponseMapper userResponseMapper;

    /** Injected service under test with mocked dependencies. */
    @InjectMocks
    private UserServiceImpl userService;

    /** Sample user used across tests. */
    private User user;

    /** Sample user response DTO used across tests. */
    private UserResponse userResponse;

    /** Executed once before all tests. */
    @BeforeAll
    static void beforeAll() {
        log.info("Starting UserServiceImpl tests");
    }

    /** Executed once after all tests. */
    @AfterAll
    static void afterAll() {
        log.info("Finished UserServiceImpl tests");
    }

    /** Prepare mock user and response before each test. */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@mail.com");
        user.setUsername("UserTest");
        user.setPhoneNumber("+33611111111");
        user.setRoleUser(RoleUser.ROLE_PASSENGER);
        user.setVerified(false);

        userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPhoneNumber(),
                "photo.png",
                "Paris",
                true,
                true,
                "jwt.token",
                "refresh.token",
                null,
                null,
                user.getRoleUser(),
                null,
                null
        );
    }

    // -------------------------
    // COMPLETE USER PROFILE
    // -------------------------

    /**
     * Verifies successful completion of user profile with ROLE_PASSENGER.
     */
    @Test
    @Order(1)
    @DisplayName("Should complete user profile successfully with ROLE_PASSENGER")
    void testCompleteUserProfileSuccessPassenger() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userResponseMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        CompleteUserRequest request = new CompleteUserRequest("photo.png", "Paris", RoleUser.ROLE_PASSENGER);
        UserResponse result = userService.completeUserProfil(request, "Bearer token");

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Verifies successful completion of user profile with ROLE_DRIVER.
     */
    @Test
    @Order(2)
    @DisplayName("Should complete user profile successfully with ROLE_DRIVER")
    void testCompleteUserProfileSuccessDriver() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userResponseMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        CompleteUserRequest request = new CompleteUserRequest("photo.png", "Paris", RoleUser.ROLE_DRIVER);
        UserResponse result = userService.completeUserProfil(request, "Bearer token");

        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Ensures RoleAssignmentNotAllowedException is thrown for ROLE_ADMIN.
     */
    @Test
    @Order(3)
    @DisplayName("Should throw RoleAssignmentNotAllowedException for ROLE_ADMIN")
    void testCompleteUserProfileRoleAdminForbidden() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        CompleteUserRequest request = new CompleteUserRequest("photo.png", "Paris", RoleUser.ROLE_ADMIN);

        assertThrows(RoleAssignmentNotAllowedException.class,
                () -> userService.completeUserProfil(request, "Bearer token"));
    }

    // -------------------------
    // UPDATE USER
    // -------------------------

    /**
     * Verifies successful update of user data.
     */
    @Test
    @Order(4)
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccess() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userResponseMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        UpdateRequestUser request = new UpdateRequestUser(
                "NewUser",
                "new@mail.com",
                "Password@123",
                "+33622222222",
                RoleUser.ROLE_PASSENGER,
                "newPhoto.png",
                "Lyon"
        );

        UserResponse result = userService.updateUser(request, "Bearer token");

        assertNotNull(result);
        assertEquals("NewUser", user.getUsername(),
                "Username should be updated to the new value");
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Ensures UserExistsAlready is thrown when no changes detected.
     */
    @Test
    @Order(5)
    @DisplayName("Should throw UserExistsAlready when no changes detected")
    void testUpdateUserNoChanges() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        UpdateRequestUser request = new UpdateRequestUser(
                user.getUsername(),
                user.getEmail(),
                "Password@123",
                user.getPhoneNumber(),
                RoleUser.ROLE_PASSENGER,
                "photo.png",
                "Paris"
        );

        assertThrows(UserExistsAlready.class, () -> userService.updateUser(request, "Bearer token"));
    }

    // -------------------------
    // DELETE USER
    // -------------------------

    /**
     * Verifies successful deletion of user.
     */
    @Test
    @Order(6)
    @DisplayName("Should delete user successfully")
    void testDeleteUserSuccess() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(user.getId().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        userService.deleteUser("Bearer token");

        verify(userRepository, times(1)).delete(user);
    }

    /**
     * Ensures UserNotFoundException is thrown when user not found during deletion.
     */
    @Test
    @Order(7)
    @DisplayName("Should throw UserNotFoundException when deleting non-existing user")
    void testDeleteUserNotFound() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(UUID.randomUUID().toString());
        when(tokenGenerator.parseJwt(anyString())).thenReturn(claims);
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("Bearer token"));
    }
}

