package api.carpooling.application.service.impl;

import api.carpooling.application.dto.admin.UpdateUserByAdmin;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.application.mapper.UserResponseMapper;
import api.carpooling.domain.User;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

/**
 * Unit tests for {@link AdminServiceImpl}.
 * <p>
 * Covers:
 * <ul>
 *     <li>Modification of user profiles by admin</li>
 *     <li>Deletion of users</li>
 *     <li>Retrieving all users (paginated)</li>
 *     <li>Retrieving a user by email</li>
 * </ul>
 * Ensures both success and error paths are properly tested.
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("AdminServiceImpl Tests")
@Slf4j
public class AdminServiceImplTest {

    /** Mocked repository for User entity. */
    @Mock
    private UserRepository userRepository;

    /** Mocked mapper for converting User entity to response DTO. */
    @Mock
    private UserResponseMapper userResponseMapper;

    /** Injected AdminService with mocked dependencies. */
    @InjectMocks
    private AdminServiceImpl adminService;

    /** Sample user used in multiple tests. */
    private User user;

    /** Sample UserResponse used in multiple tests. */
    private UserResponse userResponse;

    /** Executed once before all tests. */
    @BeforeAll
    static void beforeAll() {
        log.info("Starting AdminServiceImpl tests");
    }

    /** Executed once after all tests. */
    @AfterAll
    static void afterAll() {
        log.info("Finished AdminServiceImpl tests");
    }

    /** Initializes mock user and response objects before each test. */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@mail.com");
        user.setUsername("UserTest");
        user.setRoleUser(RoleUser.ROLE_PASSENGER);
        user.setActive(true);

        userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                "+33611111111",
                "photo.png",
                "Paris",
                true,
                true,
                "jwt.token",
                "refresh.token",
                null,
                null,
                RoleUser.ROLE_PASSENGER,
                null,
                null
        );
    }

    // -------------------------
    // MODIFY USER PROFILE
    // -------------------------

    /**
     * Verifies successful modification of a user profile by the admin.
     */
    @Test
    @Order(1)
    @DisplayName("Should modify user profile successfully")
    void testModifyProfilUserSuccess() {
        UpdateUserByAdmin request = new UpdateUserByAdmin(RoleUser.ROLE_DRIVER, false);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userResponseMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        UserResponse result = adminService.modifyProfilUser(request, "user@mail.com");

        assertNotNull(result, "Returned response should not be null");
        verify(userRepository, times(1)).findByEmail("user@mail.com");
        verify(userRepository, times(1)).save(user);
        assertEquals(userResponse, result, "Returned response should match the mapped object");
    }

    /**
     * Ensures {@link UserNotFoundException} is thrown when modifying a non-existing user.
     */
    @Test
    @Order(2)
    @DisplayName("Should throw UserNotFoundException when modifying non-existing user")
    void testModifyProfilUserNotFound() {
        UpdateUserByAdmin request = new UpdateUserByAdmin(RoleUser.ROLE_DRIVER, false);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> adminService.modifyProfilUser(request, "unknown@mail.com"),
                "Should throw UserNotFoundException for unknown user");
    }

    // -------------------------
    // DELETE USER
    // -------------------------

    /**
     * Verifies successful deletion of an existing user by admin.
     */
    @Test
    @Order(3)
    @DisplayName("Should delete user successfully")
    void testDeleteUserSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> adminService.deleteUser("user@mail.com"),
                "Deletion should not throw any exception");

        verify(userRepository, times(1)).delete(user);
    }

    /**
     * Ensures {@link UserNotFoundException} is thrown when deleting a non-existing user.
     */
    @Test
    @Order(4)
    @DisplayName("Should throw UserNotFoundException when deleting non-existing user")
    void testDeleteUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> adminService.deleteUser("unknown@mail.com"),
                "Should throw UserNotFoundException for unknown user");
    }

    // -------------------------
    // GET ALL USERS (PAGINATION)
    // -------------------------

    /**
     * Verifies retrieval of a paginated list of users.
     */
    @Test
    @Order(5)
    @DisplayName("Should return paginated list of users")
    void testGetAllUsersSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userResponseMapper.toUserResponse(user)).thenReturn(userResponse);

        Page<UserResponse> result = adminService.getAllUsers(pageable);

        assertNotNull(result, "Returned page should not be null");
        assertEquals(1, result.getContent().size(), "Page should contain one user");
        assertEquals(userResponse, result.getContent().getFirst(),
                "Returned user should match the expected response");
    }

    // -------------------------
    // GET USER BY EMAIL
    // -------------------------

    /**
     * Verifies successful retrieval of a user by email.
     */
    @Test
    @Order(6)
    @DisplayName("Should return user by email successfully")
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userResponseMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        UserResponse result = adminService.getUserByEmail("user@mail.com");

        assertNotNull(result, "Returned response should not be null");
        assertEquals(userResponse, result, "Returned response should match expected user");
        verify(userRepository, times(1)).findByEmail("user@mail.com");
    }

    /**
     * Ensures {@link UserNotFoundException} is thrown when user not found by email.
     */
    @Test
    @Order(7)
    @DisplayName("Should throw UserNotFoundException when user not found by email")
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> adminService.getUserByEmail("unknown@mail.com"),
                "Should throw UserNotFoundException for missing user");
    }
}