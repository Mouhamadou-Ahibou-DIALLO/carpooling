package api.carpooling.domain;

import api.carpooling.domain.enumeration.RoleUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterEach;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link User} entity.
 * <p>
 * This test class ensures that the User entity behaves correctly
 * in terms of its attributes, builder pattern, and default values.
 */
@DisplayName("User Entity Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class UserTest {

    /**
     * Represents the user associated with this entity
     */
    private User user;

    /**
     * This method runs before each test case.
     * It initializes a fresh User instance to ensure test isolation.
     */
    @BeforeEach
    @DisplayName("Should run before each test case")
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("john_doe")
                .email("john@example.com")
                .password("securePass123")
                .phoneNumber("+33612345678")
                .address("123 Rue de Paris")
                .photoUser("photo.jpg")
                .token("token123")
                .refreshToken("refreshToken123")
                .tokenExpired(LocalDateTime.now().plusHours(1))
                .lastLogin(LocalDateTime.now().minusDays(1))
                .isActive(true)
                .isVerified(false)
                .roleUser(RoleUser.ROLE_DRIVER)
                .build();
        log.info("User instance initialized before test");
    }

    /**
     * This method runs after each test case.
     * It clears the user object to free resources and avoid data contamination.
     */
    @AfterEach
    @DisplayName("Should run after each test")
    void tearDown() {
        user = null;
        log.info("User instance cleared after test");
    }

    /**
     * Verifies that the builder correctly initializes all fields.
     */
    @Test
    @Order(1)
    @DisplayName("Should verify that the builder correctly initializes all fields")
    void testUserBuilderInitialization() {
        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("+33612345678", user.getPhoneNumber());
        assertEquals(RoleUser.ROLE_DRIVER, user.getRoleUser());
    }

    /**
     * Ensures that default values are correctly set when not specified.
     */
    @Test
    @Order(2)
    @DisplayName("Should ensure that default values are correctly set when not specified")
    void testDefaultValues() {
        User defaultUser = User.builder()
                .username("jane_doe")
                .email("jane@example.com")
                .password("12345")
                .phoneNumber("+33687654321")
                .build();

        assertTrue(defaultUser.isActive(), "User should be active by default");
        assertFalse(defaultUser.isVerified(), "User should not be verified by default");
        assertEquals(RoleUser.ROLE_PASSENGER, defaultUser.getRoleUser(), "Default role should be PASSENGER");
    }

    /**
     * Validates that setters correctly update field values.
     */
    @Test
    @Order(3)
    @DisplayName("Should validate that setters correctly update field values")
    void testSetters() {
        user.setUsername("new_name");
        user.setVerified(true);
        user.setRoleUser(RoleUser.ROLE_PASSENGER);

        assertEquals("new_name", user.getUsername());
        assertTrue(user.isVerified());
        assertEquals(RoleUser.ROLE_PASSENGER, user.getRoleUser());
    }

    /**
     * Tests that timestamps are correctly handled.
     */
    @Test
    @Order(4)
    @DisplayName("Should test that timestamps are correctly handled")
    void testTimestamps() {
        assertNull(user.getCreatedAt(), "CreatedAt should be null before persistence");
        assertNull(user.getUpdatedAt(), "UpdatedAt should be null before persistence");
    }
}
