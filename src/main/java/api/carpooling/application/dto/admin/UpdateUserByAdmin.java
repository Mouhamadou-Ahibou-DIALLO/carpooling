package api.carpooling.application.dto.admin;

import api.carpooling.domain.enumeration.RoleUser;

/**
 * DTO used to update a current user.
 *
 * @param roleUser Role of the user
 * @param isActive Control activity of user
 */
public record UpdateUserByAdmin(
        RoleUser roleUser,
        boolean isActive
) {
}
