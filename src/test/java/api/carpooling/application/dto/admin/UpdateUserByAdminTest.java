package api.carpooling.application.dto.admin;

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
 * Unit tests for {@link UpdateUserByAdmin}.
 * <p>
 * These tests verify that the record correctly stores and returns
 * all its fields, and that the auto-generated methods ({@code equals},
 * {@code hashCode}, {@code toString}) behave as expected.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UpdateUserByAdmin Test")
@Slf4j
public class UpdateUserByAdminTest {

    /**
     * Sample role used for testing.
     */
    private static final RoleUser ROLE = RoleUser.ROLE_ADMIN;

    /**
     * Sample active status used for testing.
     */
    private static final boolean ACTIVE = true;

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting UpdateUserByAdmin tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished UpdateUserByAdmin tests");
    }

    /**
     * Verifies that the record correctly stores and returns all field values.
     */
    @Test
    @Order(1)
    @DisplayName("Should correctly store and return field values")
    void testFieldValues() {
        UpdateUserByAdmin request = new UpdateUserByAdmin(ROLE, ACTIVE);

        assertAll("UpdateUserByAdmin fields",
                () -> assertEquals(ROLE, request.roleUser(), "RoleUser should match"),
                () -> assertEquals(ACTIVE, request.isActive(), "isActive should match")
        );
    }

    /**
     * Verifies that {@code equals} and {@code hashCode} work correctly for equal and different objects.
     */
    @Test
    @Order(2)
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        UpdateUserByAdmin request1 = new UpdateUserByAdmin(ROLE, ACTIVE);
        UpdateUserByAdmin request2 = new UpdateUserByAdmin(ROLE, ACTIVE);
        UpdateUserByAdmin request3 = new UpdateUserByAdmin(RoleUser.ROLE_PASSENGER, ACTIVE);

        assertAll("equals and hashCode",
                () -> assertEquals(request1, request2, "Objects with same values should be equal"),
                () -> assertEquals(request1.hashCode(), request2.hashCode(), "Hash codes should match"),
                () -> assertNotEquals(request1, request3, "Different roles should produce different objects")
        );
    }

    /**
     * Verifies that {@code toString} contains the class name and key field values.
     */
    @Test
    @Order(3)
    @DisplayName("Should include field values in toString output")
    void testToString() {
        UpdateUserByAdmin request = new UpdateUserByAdmin(ROLE, ACTIVE);
        String toString = request.toString();

        assertAll("toString output",
                () -> assertTrue(toString.contains("UpdateUserByAdmin"), "Should contain class name"),
                () -> assertTrue(toString.contains(ROLE.toString()), "Should contain role"),
                () -> assertTrue(toString.contains(Boolean.toString(ACTIVE)), "Should contain isActive value")
        );
    }
}
