package api.carpooling.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for encoding and verifying passwords using BCrypt.
 */
public class EncodedPassword {

    /**
     * BCrypt password encoder instance.
     */
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Encodes a raw password using BCrypt.
     *
     * @param password the raw password to encode
     * @return the hashed password
     */
    public static String encode(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Verifies if a raw password matches the hashed password.
     *
     * @param password the raw password
     * @param passwordHash the hashed password
     * @return true if the password matches, false otherwise
     */
    public static boolean isRightPassword(String password, String passwordHash) {
        return passwordEncoder.matches(password, passwordHash);
    }
}
