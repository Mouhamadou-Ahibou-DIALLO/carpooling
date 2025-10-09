package api.carpooling.domain;

import api.carpooling.domain.enumeration.RoleUser;

import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a user within the carpooling system.
 * This entity is mapped to the "users" table in the database.
 * <p>
 * Lombok annotations (@Builder, @Getter, @Setter, etc.) are used
 * to automatically generate constructors, getters, setters, and builder methods.
 */
@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique identifier for the user, generated automatically as a UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Unique username chosen by the user.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Username is required and unique")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Unique and valid email address of the user.
     * This field is required and must not be blank.
     */
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required and unique")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Password for the user’s account.
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User’s phone number in international format (e.g., +33612345678).
     * Must be valid and unique.
     */
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    @NotBlank(message = "Phone number is required and unique")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    /**
     * URL or path to the user's profile picture.
     */
    @Column(name = "photo_user")
    private String photoUser;

    /**
     * The user’s address or place of residence.
     */
    @Column(name = "address")
    private String address;

    /**
     * Temporary authentication token (e.g., JWT).
     */
    @Column(name = "token")
    private String token;

    /**
     * Refresh token used to generate a new access token.
     */
    @Column(name = "refresh_token")
    private String refreshToken;

    /**
     * Expiration date and time of the authentication token.
     */
    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpired;

    /**
     * Timestamp of the user's last login.
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * Indicates whether the account is active.
     * Default: true.
     */
    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    /**
     * Indicates whether the account is verified (e.g., email confirmation).
     * Default: false.
     */
    @Column(name = "is_verified")
    @Builder.Default
    private boolean isVerified = false;

    /**
     * User role in the system (e.g., ROLE_DRIVER, ROLE_PASSENGER).
     * Default: ROLE_PASSENGER.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Builder.Default
    private RoleUser roleUser = RoleUser.ROLE_PASSENGER;

    /**
     * Timestamp automatically set when the record is created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp automatically updated whenever the record is modified.
     */
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
