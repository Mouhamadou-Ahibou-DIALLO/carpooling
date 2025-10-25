package api.carpooling.utils;

import api.carpooling.domain.User;
import api.carpooling.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating and parsing JWT and refresh tokens using tokenGenerator class.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserTokenService {

    /**
     * Repository for User entity operations.
     */
    private final UserRepository userRepository;

    /**
     * Utility class for generating and parsing JWT tokens.
     */
    private final TokenGenerator tokenGenerator;

    /**
     * Generates JWT and refresh token for a user, sets expiration, and saves the user.
     *
     * @param user the user entity to update
     * @return updated user with tokens
     */
    public User generateTokens(User user) {
        String jwtToken = tokenGenerator.generateJwtToken(user.getId(), user.getRoleUser().name());
        String refreshToken;
        do {
            refreshToken = tokenGenerator.generateRefreshToken();
        } while (userRepository.existsByRefreshToken(refreshToken));

        user.setToken(jwtToken);
        user.setRefreshToken(refreshToken);
        user.setTokenExpired(
                tokenGenerator.getRefreshTokenExpiry().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
        );

        log.info("New User registered with JWT Token: {}", jwtToken);
        return userRepository.save(user);
    }
}
