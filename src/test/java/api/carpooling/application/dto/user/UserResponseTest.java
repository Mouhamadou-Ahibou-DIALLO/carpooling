package api.carpooling.application.dto.user;

import api.carpooling.domain.enumeration.RoleUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link UserResponse}.
 * <p>
 * These tests verify that the record correctly stores and returns
 * all its fields, and that the auto-generated methods ({@code equals},
 * {@code hashCode}, {@code toString}) behave as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserResponse Test")
@Slf4j
public class UserResponseTest {

    /**
     * Sample user ID used for testing.
     */
    private static final UUID ID = UUID.randomUUID();

    /**
     * Sample email used for testing.
     */
    private static final String EMAIL = "john.doe@example.com";

    /**
     * Sample username used for testing.
     */
    private static final String USERNAME = "john123";

    /**
     * Sample phone number used for testing.
     */
    private static final String PHONE = "+33000000000";

    /**
     * Sample photo URL used for testing.
     */
    private static final String PHOTO = "https://example.com/photo.jpg";

    /**
     * Sample address used for testing.
     */
    private static final String ADDRESS = "123 Main Street, Paris";

    /**
     * Sample verification status used for testing.
     */
    private static final boolean VERIFIED = true;

    /**
     * Sample active status used for testing.
     */
    private static final boolean ACTIVE = true;

    /**
     * Sample JWT access token used for testing.
     */
    private static final String TOKEN = "access-token";

    /**
     * Sample JWT refresh token used for testing.
     */
    private static final String REFRESH_TOKEN = "refresh-token";

    /**
     * Sample expiration date for token used for testing.
     */
    private static final LocalDateTime TOKEN_EXPIRED = LocalDateTime.now().plusDays(1);

    /**
     * Sample last login date used for testing.
     */
    private static final LocalDateTime LAST_LOGIN = LocalDateTime.now().minusDays(1);

    /**
     * Sample role used for testing.
     */
    private static final RoleUser ROLE = RoleUser.ROLE_PASSENGER;

    /**
     * Sample creation date used for testing.
     */
    private static final LocalDateTime CREATED_AT = LocalDateTime.now().minusDays(10);

    /**
     * Sample update date used for testing.
     */
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now();

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting UserResponse tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished UserResponse tests");
    }

    /**
     * Verifies that all field values are correctly stored and returned by the record.
     */
    @Test
    @Order(1)
    @DisplayName("Should correctly store and return all field values")
    void testFieldValues() {
        UserResponse response = new UserResponse(
                ID, EMAIL, USERNAME, PHONE, PHOTO, ADDRESS, VERIFIED, ACTIVE,
                TOKEN, REFRESH_TOKEN, TOKEN_EXPIRED, LAST_LOGIN, ROLE, CREATED_AT, UPDATED_AT
        );

        assertAll("UserResponse fields",
                () -> assertEquals(ID, response.id(), "ID should match"),
                () -> assertEquals(EMAIL, response.email(), "Email should match"),
                () -> assertEquals(USERNAME, response.username(), "Username should match"),
                () -> assertEquals(PHONE, response.phoneNumber(), "Phone number should match"),
                () -> assertEquals(PHOTO, response.photoUser(), "Photo should match"),
                () -> assertEquals(ADDRESS, response.address(), "Address should match"),
                () -> assertEquals(VERIFIED, response.isVerified(), "isVerified should match"),
                () -> assertEquals(ACTIVE, response.isActive(), "isActive should match"),
                () -> assertEquals(TOKEN, response.token(), "Token should match"),
                () -> assertEquals(REFRESH_TOKEN, response.refreshToken(), "Refresh token should match"),
                () -> assertEquals(TOKEN_EXPIRED, response.tokenExpired(), "Token expiration date should match"),
                () -> assertEquals(LAST_LOGIN, response.lastLogin(), "Last login should match"),
                () -> assertEquals(ROLE, response.roleUser(), "RoleUser should match"),
                () -> assertEquals(CREATED_AT, response.createdAt(), "CreatedAt should match"),
                () -> assertEquals(UPDATED_AT, response.updatedAt(), "UpdatedAt should match")
        );
    }

    /**
     * Verifies that {@code equals} and {@code hashCode} are consistent and behave correctly.
     */
    @Test
    @Order(2)
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        UserResponse response1 = new UserResponse(
                ID, EMAIL, USERNAME, PHONE, PHOTO, ADDRESS, VERIFIED, ACTIVE,
                TOKEN, REFRESH_TOKEN, TOKEN_EXPIRED, LAST_LOGIN, ROLE, CREATED_AT, UPDATED_AT
        );

        UserResponse response2 = new UserResponse(
                ID, EMAIL, USERNAME, PHONE, PHOTO, ADDRESS, VERIFIED, ACTIVE,
                TOKEN, REFRESH_TOKEN, TOKEN_EXPIRED, LAST_LOGIN, ROLE, CREATED_AT, UPDATED_AT
        );

        UserResponse response3 = new UserResponse(
                UUID.randomUUID(), EMAIL, USERNAME, PHONE, PHOTO, ADDRESS, VERIFIED, ACTIVE,
                TOKEN, REFRESH_TOKEN, TOKEN_EXPIRED, LAST_LOGIN, ROLE, CREATED_AT, UPDATED_AT
        );

        assertAll("equals and hashCode",
                () -> assertEquals(response1, response2, "Objects with same values should be equal"),
                () -> assertEquals(response1.hashCode(), response2.hashCode(), "Hash codes should match"),
                () -> assertNotEquals(response1, response3, "Different IDs should produce different objects")
        );
    }

    /**
     * Verifies that {@code toString} contains the class name and key field values.
     */
    @Test
    @Order(3)
    @DisplayName("Should include key field values in toString output")
    void testToString() {
        UserResponse response = new UserResponse(
                ID, EMAIL, USERNAME, PHONE, PHOTO, ADDRESS, VERIFIED, ACTIVE,
                TOKEN, REFRESH_TOKEN, TOKEN_EXPIRED, LAST_LOGIN, ROLE, CREATED_AT, UPDATED_AT
        );

        String toString = response.toString();

        assertAll("toString output",
                () -> assertTrue(toString.contains("UserResponse"), "Should contain class name"),
                () -> assertTrue(toString.contains(EMAIL), "Should contain email"),
                () -> assertTrue(toString.contains(USERNAME), "Should contain username"),
                () -> assertTrue(toString.contains(ROLE.toString()), "Should contain role"),
                () -> assertTrue(toString.contains(ID.toString()), "Should contain ID")
        );
    }
}
