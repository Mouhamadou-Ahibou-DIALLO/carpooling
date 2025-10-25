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
 * Unit tests for {@link CompleteUserRequest}.
 * <p>
 * These tests verify that the record correctly stores its properties,
 * and that its automatically generated methods ({@code equals}, {@code hashCode},
 * {@code toString}) behave as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("CompleteUserRequest Test")
@Slf4j
public class CompleteUserRequestTest {

    /**
     * Sample photo URL used in tests.
     */
    private static final String PHOTO = "https://example.com/photo.jpg";

    /**
     * Sample address used in tests.
     */
    private static final String ADDRESS = "123 Main Street, Paris";

    /**
     * Sample role used in tests.
     */
    private static final RoleUser ROLE = RoleUser.ROLE_DRIVER;

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting CompleteUserRequest tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished CompleteUserRequest tests");
    }

    /**
     * Verifies that the record correctly stores and returns all provided values.
     */
    @Test
    @Order(1)
    @DisplayName("Should correctly store and return field values")
    void testFieldValues() {
        CompleteUserRequest request = new CompleteUserRequest(PHOTO, ADDRESS, ROLE);

        assertAll("CompleteUserRequest fields",
                () -> assertEquals(PHOTO, request.photoUser(), "Photo should match"),
                () -> assertEquals(ADDRESS, request.address(), "Address should match"),
                () -> assertEquals(ROLE, request.roleUser(), "RoleUser should match")
        );
    }

    /**
     * Verifies that {@code equals} and {@code hashCode} work correctly for equal and different objects.
     */
    @Test
    @Order(2)
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        CompleteUserRequest request1 = new CompleteUserRequest(PHOTO, ADDRESS, ROLE);
        CompleteUserRequest request2 = new CompleteUserRequest(PHOTO, ADDRESS, ROLE);
        CompleteUserRequest request3 = new CompleteUserRequest("diff-photo", ADDRESS, ROLE);

        assertAll("equals and hashCode",
                () -> assertEquals(request1, request2, "Objects with same values should be equal"),
                () -> assertEquals(request1.hashCode(), request2.hashCode(), "Hash codes should match"),
                () -> assertNotEquals(request1, request3, "Different values should not be equal")
        );
    }

    /**
     * Verifies that {@code toString} contains the class name and field values.
     */
    @Test
    @Order(3)
    @DisplayName("Should include field values in toString output")
    void testToString() {
        CompleteUserRequest request = new CompleteUserRequest(PHOTO, ADDRESS, ROLE);
        String toString = request.toString();

        assertAll("toString output",
                () -> assertTrue(toString.contains("CompleteUserRequest"), "Should contain class name"),
                () -> assertTrue(toString.contains(PHOTO), "Should contain photo value"),
                () -> assertTrue(toString.contains(ADDRESS), "Should contain address value"),
                () -> assertTrue(toString.contains(ROLE.toString()), "Should contain role value")
        );
    }
}
