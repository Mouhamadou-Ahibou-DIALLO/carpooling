package api.carpooling.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodedPassword {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public EncodedPassword() {}

    public static String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean isRightPassword(String password, String passwordHash) {
        return passwordEncoder.matches(password, passwordHash);
    }
}
