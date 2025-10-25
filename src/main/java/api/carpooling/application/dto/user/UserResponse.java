package api.carpooling.application.dto.user;

import api.carpooling.domain.enumeration.RoleUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for response and send full user info.
 * @param id           Unique identifier of the user
 * @param email        Email of the user
 * @param username     Username of the user
 * @param phoneNumber phoneNumber of the user
 * @param token        JWT access token
 * @param refreshToken JWT refresh token
 * @param tokenExpired Expiry date of the refresh token
 * @param lastLogin Last login date of the current user
 * @param roleUser  Role of the user
 * @param photoUser Photo of user
 * @param isActive check if user is active
 * @param address Address of User
 * @param isVerified Check if user is verified
 * @param createdAt    Date when the user was created
 * @param updatedAt    Date when the user was last updated
 */
@Builder
@Schema(description = "User data transfert for response when created or update user object ")
public record UserResponse(
        @Schema(description = "Unique identifier of the user",
                example = "e7a1f6f8-1c88-4b5b-a0e7-df30f4bfc5d3")
        UUID id,

        @Schema(description = "Email of the user", example = "john@mail.com")
        String email,

        @Schema(description = "Username of the user", example = "john123")
        String username,

        @Schema(description = "Phone number of the user", example = "+33000000000")
        String phoneNumber,

        @Schema(description = "Photo of the user")
        String photoUser,

        @Schema(description = "Address of the user")
        String address,

        @Schema(description = "Check if user is verified")
        boolean isVerified,

        @Schema(description = "Check if user is active")
        boolean isActive,

        @Schema(description = "JWT access token")
        String token,

        @Schema(description = "JWT refresh token")
        String refreshToken,

        @Schema(description = "Expiry date of the refresh token")
        LocalDateTime tokenExpired,

        @Schema(description = "Last login date of the current user")
        LocalDateTime lastLogin,

        @Schema(description = "Role of the user")
        RoleUser roleUser,

        @Schema(description = "Date when the user was created")
        LocalDateTime createdAt,

        @Schema(description = "Date when the user was last updated")
        LocalDateTime updatedAt
) {
}
