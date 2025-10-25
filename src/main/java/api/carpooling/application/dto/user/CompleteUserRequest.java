package api.carpooling.application.dto.user;

import api.carpooling.domain.enumeration.RoleUser;

/**
 * DTO for completing a user.
 * @param photoUser Photo of user
 * @param address Address of User
 * @param roleUser Role of the user
 */
public record CompleteUserRequest(
        String photoUser,
        String address,
        RoleUser roleUser
) {
}
