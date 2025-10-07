package api.carpooling.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import api.carpooling.configuration.JwtProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Utility class for generating and parsing JWT and refresh tokens.
 */
@Component
@RequiredArgsConstructor
public class TokenGenerator {

    /**
     * JWT properties from configuration.
     */
    private final JwtProperties jwtProperties;

    /**
     * Returns the signing key used for JWT tokens.
     *
     * @return signing key
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for a user with a role.
     *
     * @param userId the user ID
     * @param roleUser the user role
     * @return JWT token as String
     */
    public String generateJwtToken(UUID userId, String roleUser) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role_user", roleUser)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(jwtProperties.getExpirationMs())))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generates a random refresh token (UUID without dashes).
     *
     * @return refresh token as String
     */
    public String generateRefreshToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Returns the expiration date for the refresh token.
     *
     * @return refresh token expiration date
     */
    public Date getRefreshTokenExpiry() {
        return new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMs());
    }

    /**
     * Parses a JWT token and returns its claims.
     *
     * @param token the JWT token
     * @return claims contained in the token
     */
    public Claims parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
