package api.carpooling.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties class that maps JWT configuration from application properties.
 * <p>
 * Includes secret key, access token expiration, and refresh token expiration.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {

    /**
     * Secret key used to sign JWT tokens.
     */
    private String secret;

    /**
     * Access token expiration time in milliseconds.
     */
    private long expirationMs;

    /**
     * Refresh token expiration time in milliseconds.
     */
    private long refreshExpirationMs;
}
