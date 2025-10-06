package api.carpooling.application.dto.user;

import api.carpooling.domain.enumeration.RoleUser;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserDTO(
        UUID id,
        String email,
        String username,
        String token,
        String refreshToken,
        LocalDateTime tokenExpired,
        RoleUser roleUser,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

