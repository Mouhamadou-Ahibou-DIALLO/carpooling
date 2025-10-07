package api.carpooling.repository;

import api.carpooling.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing User entities.
 * Provides methods to query users by email, username, tokens, and refresh tokens.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by their email.
     *
     * @param email the user's email
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by email.
     *
     * @param email the user's email
     * @return true if the user exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user exists by username.
     *
     * @param username the user's username
     * @return true if the user exists
     */
    boolean existsByUsername(String username);

    /**
     * Find a user by their authentication token.
     *
     * @param token the authentication token
     * @return an Optional containing the user if found
     */
    @Query("SELECT u FROM User u WHERE u.token = :token")
    Optional<User> findByToken(@Param("token") String token);

    /**
     * Find a user by their refresh token.
     *
     * @param refreshToken the refresh token
     * @return an Optional containing the user if found
     */
    Optional<User> findByRefreshToken(String refreshToken);

    /**
     * Check if a refresh token exists in the database.
     *
     * @param refreshToken the refresh token
     * @return true if the refresh token exists
     */
    boolean existsByRefreshToken(String refreshToken);
}
