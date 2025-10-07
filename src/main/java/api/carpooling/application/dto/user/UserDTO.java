package api.carpooling.application.dto.user;

import api.carpooling.domain.enumeration.RoleUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for User entity.
 * <p>
 * Used to transfer user information safely outside the application layer.
 */
@Schema(description = "User data transfer object")
@Builder
public record UserDTO(
        @Schema(description = "Unique identifier of the user", example = "e7a1f6f8-1c88-4b5b-a0e7-df30f4bfc5d3")
        UUID id,

        @Schema(description = "Email of the user", example = "john@mail.com")
        String email,

        @Schema(description = "Username of the user", example = "john123")
        String username,

        @Schema(description = "JWT access token")
        String token,

        @Schema(description = "JWT refresh token")
        String refreshToken,

        @Schema(description = "Expiry date of the refresh token")
        LocalDateTime tokenExpired,

        @Schema(description = "Role of the user")
        RoleUser roleUser,

        @Schema(description = "Date when the user was created")
        LocalDateTime createdAt,

        @Schema(description = "Date when the user was last updated")
        LocalDateTime updatedAt
) {}

