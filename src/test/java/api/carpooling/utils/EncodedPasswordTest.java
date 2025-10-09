package api.carpooling.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link EncodedPassword}.
 * <p>
 * These tests verify password encoding and validation using BCrypt.
 */
@DisplayName("EncodedPassword Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class EncodedPasswordTest {

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        log.info("Starting EncodedPassword tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished EncodedPassword tests");
    }

    /**
     * Verifies that encoding a password returns a non-null, non-empty hash.
     */
    @Test
    @DisplayName("Should encode password successfully")
    @Order(1)
    void testEncodePassword() {
        String rawPassword = "StrongPass1@";
        String hashed = EncodedPassword.encode(rawPassword);

        assertNotNull(hashed, "Encoded password should not be null");
        assertFalse(hashed.isEmpty(), "Encoded password should not be empty");
        assertNotEquals(rawPassword, hashed, "Encoded password should differ from raw password");
    }

    /**
     * Verifies that a correct password matches its hash.
     */
    @Test
    @DisplayName("Should validate correct password")
    @Order(2)
    void testIsRightPasswordCorrect() {
        String rawPassword = "StrongPass1@";
        String hashed = EncodedPassword.encode(rawPassword);

        assertTrue(EncodedPassword.isRightPassword(rawPassword, hashed), "Password should match the hash");
    }

    /**
     * Verifies that an incorrect password does not match the hash.
     */
    @Test
    @DisplayName("Should reject incorrect password")
    @Order(3)
    void testIsRightPasswordIncorrect() {
        String rawPassword = "StrongPass1@";
        String wrongPassword = "WrongPass123!";
        String hashed = EncodedPassword.encode(rawPassword);

        assertFalse(EncodedPassword.isRightPassword(wrongPassword, hashed), "Wrong password should not match the hash");
    }
}
