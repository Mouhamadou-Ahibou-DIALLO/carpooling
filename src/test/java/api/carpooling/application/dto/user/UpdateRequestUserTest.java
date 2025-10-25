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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link UpdateRequestUser}.
 * <p>
 * These tests verify that the record correctly stores and returns
 * all its fields, and that the auto-generated methods ({@code equals},
 * {@code hashCode}, {@code toString}) behave as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UpdateRequestUser Test")
@Slf4j
public class UpdateRequestUserTest {

    /**
     * Sample username used for testing.
     */
    private static final String USERNAME = "johnDoe";

    /**
     * Sample email used for testing.
     */
    private static final String EMAIL = "john.doe@example.com";

    /**
     * Sample password used for testing.
     */
    private static final String PASSWORD = "Password@123";

    /**
     * Sample phone number used for testing.
     */
    private static final String PHONE = "+33612345678";

    /**
     * Sample role used for testing.
     */
    private static final RoleUser ROLE = RoleUser.ROLE_DRIVER;

    /**
     * Sample photo URL used for testing.
     */
    private static final String PHOTO = "https://example.com/photo.jpg";

    /**
     * Sample address used for testing.
     */
    private static final String ADDRESS = "10 Rue de Paris, Lyon";

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting UpdateRequestUser tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished UpdateRequestUser tests");
    }

    /**
     * Verifies that the record correctly stores and returns all field values.
     */
    @Test
    @Order(1)
    @DisplayName("Should correctly store and return field values")
    void testFieldValues() {
        UpdateRequestUser request = new UpdateRequestUser(
                USERNAME, EMAIL, PASSWORD, PHONE, ROLE, PHOTO, ADDRESS
        );

        assertAll("UpdateRequestUser fields",
                () -> assertEquals(USERNAME, request.username(), "Username should match"),
                () -> assertEquals(EMAIL, request.email(), "Email should match"),
                () -> assertEquals(PASSWORD, request.password(), "Password should match"),
                () -> assertEquals(PHONE, request.phoneNumber(), "Phone number should match"),
                () -> assertEquals(ROLE, request.roleUser(), "RoleUser should match"),
                () -> assertEquals(PHOTO, request.photoUser(), "PhotoUser should match"),
                () -> assertEquals(ADDRESS, request.address(), "Address should match")
        );
    }

    /**
     * Verifies that {@code equals} and {@code hashCode} work correctly for equal and different objects.
     */
    @Test
    @Order(2)
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        UpdateRequestUser request1 = new UpdateRequestUser(
                USERNAME, EMAIL, PASSWORD, PHONE, ROLE, PHOTO, ADDRESS
        );

        UpdateRequestUser request2 = new UpdateRequestUser(
                USERNAME, EMAIL, PASSWORD, PHONE, ROLE, PHOTO, ADDRESS
        );

        UpdateRequestUser request3 = new UpdateRequestUser(
                "differentUser", EMAIL, PASSWORD, PHONE, ROLE, PHOTO, ADDRESS
        );

        assertAll("equals and hashCode",
                () -> assertEquals(request1, request2, "Objects with same values should be equal"),
                () -> assertEquals(request1.hashCode(), request2.hashCode(), "Hash codes should match"),
                () -> assertNotEquals(request1, request3, "Different usernames should produce different objects")
        );
    }

    /**
     * Verifies that {@code toString} contains the class name and main field values.
     */
    @Test
    @Order(3)
    @DisplayName("Should include field values in toString output")
    void testToString() {
        UpdateRequestUser request = new UpdateRequestUser(
                USERNAME, EMAIL, PASSWORD, PHONE, ROLE, PHOTO, ADDRESS
        );

        String toString = request.toString();

        assertAll("toString output",
                () -> assertTrue(toString.contains("UpdateRequestUser"), "Should contain class name"),
                () -> assertTrue(toString.contains(USERNAME), "Should contain username"),
                () -> assertTrue(toString.contains(EMAIL), "Should contain email"),
                () -> assertTrue(toString.contains(PHONE), "Should contain phone number"),
                () -> assertTrue(toString.contains(ROLE.toString()), "Should contain role")
        );
    }
}
