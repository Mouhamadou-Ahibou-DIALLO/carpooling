package api.carpooling.domain;

import api.carpooling.domain.enumeration.RoleUser;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Username is required and unique")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email(message = "Email must be valide")
    @NotBlank(message = "Email is required and unique")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    @NotBlank(message = "Phone number is required and unique")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "photo_user")
    private String photoUser;

    @Column(name = "address")
    private String address;

    @Column(name = "token")
    private String token;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpired;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "is_verified")
    @Builder.Default
    private boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Builder.Default
    private RoleUser roleUser = RoleUser.ROLE_PASSENGER;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
