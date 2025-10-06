/*package api.carpooling.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import api.carpooling.configuration.JwtProperties;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtProperties jwtProperties;

    public String generateJwtToken(UUID userId, String roleUser) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role_user", roleUser)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(jwtProperties.getExpirationMs())))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Date getRefreshTokenExpiry() {
        return new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMs());
    }

    public Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }
}
*/


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

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtProperties jwtProperties;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

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

    public String generateRefreshToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Date getRefreshTokenExpiry() {
        return new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMs());
    }

    public Claims parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

