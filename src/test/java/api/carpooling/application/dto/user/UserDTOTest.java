package api.carpooling.application.dto.user;

import api.carpooling.domain.enumeration.RoleUser;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserDTO}.
 * Verifies builder, equality, hashCode, and toString behaviors.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserDTO Test")
class UserDTOTest {

    /**
     * Runs once before all tests.
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before All");
    }

    /**
     * Runs once after all tests.
     */
    @AfterAll
    static void afterAll() {
        System.out.println("All UserDTO tests completed.");
    }

    /**
     * Tests UserDTO builder with all fields.
     */
    @Test
    @Order(1)
    @DisplayName("Should correctly build a UserDTO with all fields")
    void testUserDTOBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .email("john@mail.com")
                .username("john123")
                .token("accessToken")
                .refreshToken("refreshToken")
                .tokenExpired(now.plusDays(1))
                .roleUser(RoleUser.ROLE_PASSENGER)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        assertAll("UserDTO fields",
                () -> assertEquals(id, userDTO.id(), "ID should match"),
                () -> assertEquals("john@mail.com", userDTO.email(), "Email should match"),
                () -> assertEquals("john123", userDTO.username(), "Username should match"),
                () -> assertEquals("accessToken", userDTO.token(), "Token should match"),
                () -> assertEquals("refreshToken", userDTO.refreshToken(), "Refresh token should match"),
                () -> assertEquals(RoleUser.ROLE_PASSENGER, userDTO.roleUser(), "RoleUser should match"),
                () -> assertNotNull(userDTO.createdAt(), "CreatedAt should not be null"),
                () -> assertNotNull(userDTO.updatedAt(), "UpdatedAt should not be null"),
                () -> assertTrue(userDTO.tokenExpired().isAfter(now), "Token expiry should be in the future")
        );
    }

    /**
     * Tests handling of null or default values.
     */
    @Test
    @Order(2)
    @DisplayName("Should handle null and default values gracefully")
    void testNullValues() {
        UserDTO userDTO = UserDTO.builder().build();

        assertAll("Null fields should be allowed",
                () -> assertNull(userDTO.id(), "ID can be null"),
                () -> assertNull(userDTO.email(), "Email can be null"),
                () -> assertNull(userDTO.username(), "Username can be null"),
                () -> assertNull(userDTO.token(), "Token can be null"),
                () -> assertNull(userDTO.refreshToken(), "Refresh token can be null"),
                () -> assertNull(userDTO.tokenExpired(), "Token expiry can be null"),
                () -> assertNull(userDTO.roleUser(), "Role can be null"),
                () -> assertNull(userDTO.createdAt(), "CreatedAt can be null"),
                () -> assertNull(userDTO.updatedAt(), "UpdatedAt can be null")
        );
    }

    /**
     * Tests equals and hashCode consistency.
     */
    @Test
    @Order(3)
    @DisplayName("Should correctly compare two equal UserDTO instances")
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UserDTO user1 = UserDTO.builder()
                .id(id)
                .email("test@mail.com")
                .username("testUser")
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserDTO user2 = UserDTO.builder()
                .id(id)
                .email("test@mail.com")
                .username("testUser")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(user1, user2, "Two identical UserDTOs should be equal");
        assertEquals(user1.hashCode(), user2.hashCode(), "Hashcodes should match");
    }

    /**
     * Tests toString output.
     */
    @Test
    @Order(4)
    @DisplayName("Should provide correct string representation")
    void testToString() {
        UUID id = UUID.randomUUID();

        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .email("john@mail.com")
                .username("john123")
                .roleUser(RoleUser.ROLE_ADMIN)
                .build();

        String toString = userDTO.toString();
        assertTrue(toString.contains("john@mail.com"), "toString should contain email");
        assertTrue(toString.contains("john123"), "toString should contain username");
        assertTrue(toString.contains("ROLE_ADMIN"), "toString should contain role");
    }
}
