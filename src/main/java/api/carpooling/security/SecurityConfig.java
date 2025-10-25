package api.carpooling.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for Spring Security.
 * <p>
 * Configures JWT authentication, password encoding, and access rules
 * for different endpoints based on roles.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * JWT authentication filter used to validate tokens on each request.
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Sets up the HTTP security filter chain for the application.
     * <p>
     * Configures access rules for endpoints, disables CSRF and CORS protections,
     * and registers the JWT authentication filter before the standard
     * UsernamePasswordAuthenticationFilter.
     *
     * @param http the HttpSecurity configuration object
     * @return the fully configured SecurityFilterChain
     */
    @SuppressWarnings({"PMD.SignatureDeclareThrowsException", "java:S4502"})
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())

                // CSRF is disabled intentionally because the app uses JWT authentication (stateless).
                // No session or cookie-based authentication => not vulnerable to CSRF.
                // SonarCloud warning acknowledged and safely ignored.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login",
                                "/api/v1/auth/refresh_token").permitAll()
                        .requestMatchers("/api/v1/auth/me", "/api/v1/auth/logout").authenticated()
                        .requestMatchers("/api/v1/user/**").hasAnyRole("ADMIN",
                                "PASSENGER", "DRIVER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/trips/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/actuator/**",
                                "/"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Provides the AuthenticationManager bean used for authentication.
     *
     * @param authConfig the AuthenticationConfiguration
     * @return the AuthenticationManager
     * @throws Exception in case of configuration errors
     */
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder bean using BCrypt hashing algorithm.
     *
     * @return a PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
