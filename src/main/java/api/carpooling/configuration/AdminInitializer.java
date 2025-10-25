package api.carpooling.configuration;

import api.carpooling.domain.User;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.repository.UserRepository;
import api.carpooling.utils.EncodedPassword;
import api.carpooling.utils.UserTokenService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Initializes a default ADMIN user if none exists.
 * Sets username, email, password, role, and active/verified flags.
 * Generates JWT, refresh token, and token expiration timestamp.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    /**
     * Utility class for generating and parsing JWT tokens.
     */
    private final UserTokenService userTokenService;

    /**
     * Repository for User entity operations.
     */
    private final UserRepository userRepository;

    /**
     * Creates a default admin account with credentials and tokens.
     * Runs automatically after Spring context initialization.
     */
    @PostConstruct
    public void initAdmin() {
        boolean adminExists = userRepository.findByEmail("admin@carpooling.com").isPresent();
        boolean userExists = userRepository.findByUsername("admin Carpooling").isPresent();
        boolean userNumberExists = userRepository.findByPhoneNumber("+33600000000").isPresent();
        if (adminExists || userExists || userNumberExists) {
            log.info("User already exists");
            return;
        }

        User admin = new User();
        admin.setUsername("admin Carpooling");
        admin.setEmail("admin@carpooling.com");
        admin.setPassword(EncodedPassword.encode("Admin@123"));
        admin.setPhoneNumber("+33611223344");
        admin.setRoleUser(RoleUser.ROLE_ADMIN);
        admin.setActive(true);
        admin.setVerified(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        User tempAdmin = userRepository.saveAndFlush(admin);
        userTokenService.generateTokens(tempAdmin);

        log.info("Admin created:");
        log.info("Email: {}", tempAdmin.getEmail());
        log.info("Password: Admin@123");
        log.info("Token: {}", tempAdmin.getToken());
        log.info("RefreshToken: {}", tempAdmin.getRefreshToken());
        log.info("TokenExpiresAt: {}", tempAdmin.getTokenExpired());
    }
}
