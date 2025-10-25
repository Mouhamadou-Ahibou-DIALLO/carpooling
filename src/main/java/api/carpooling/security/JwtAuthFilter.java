package api.carpooling.security;

import api.carpooling.utils.TokenGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * JWT authentication filter that validates incoming requests.
 * <p>
 * Extracts the JWT token from the Authorization header,
 * validates it, and sets the authentication in the SecurityContext.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * Token generator utility used to parse and validate JWT tokens.
     */
    private final TokenGenerator tokenGenerator;

    /**
     * Name of the HTTP header used for JWT authentication.
     */
    private static final String AUTH_HEADER = "Authorization";

    /**
     * Prefix of the JWT token in the header (e.g., "Bearer ").
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Processes each HTTP request, validates the JWT token,
     * and sets the authentication in the security context if valid.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            String token = header.substring(TOKEN_PREFIX.length());

            try {
                var claims = tokenGenerator.parseJwt(token);
                UUID userId = UUID.fromString(claims.getSubject());
                String role = (String) claims.get("role_user");
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }

                var authorities = List.of(new SimpleGrantedAuthority(role));
                log.info("[JWT AUTH FILTER] User {} authenticated with authorities {}", userId, authorities);

                var auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                log.error("JWT token invalid: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
