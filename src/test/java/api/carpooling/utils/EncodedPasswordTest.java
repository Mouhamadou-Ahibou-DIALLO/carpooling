package api.carpooling.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EncodedPassword}.
 * <p>
 * These tests verify password encoding and validation using BCrypt.
 */
@DisplayName("EncodedPassword Test")
class EncodedPasswordTest {

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting EncodedPassword tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished EncodedPassword tests");
    }

    /**
     * Verifies that encoding a password returns a non-null, non-empty hash.
     */
    @Test
    @DisplayName("Should encode password successfully")
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
    void testIsRightPasswordIncorrect() {
        String rawPassword = "StrongPass1@";
        String wrongPassword = "WrongPass123!";
        String hashed = EncodedPassword.encode(rawPassword);

        assertFalse(EncodedPassword.isRightPassword(wrongPassword, hashed), "Wrong password should not match the hash");
    }
}
