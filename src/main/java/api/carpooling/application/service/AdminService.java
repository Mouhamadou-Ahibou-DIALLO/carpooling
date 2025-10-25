package api.carpooling.application.service;

import api.carpooling.application.dto.admin.UpdateUserByAdmin;
import api.carpooling.application.dto.user.UserResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for administrative operations related to user management.
 * <p>
 * Provides functionalities for updating, deleting, and retrieving users in the system.
 */
public interface AdminService {

    /**
     * Modifies a user's profile information based on the data provided by an administrator.
     *
     * @param updateUserByAdmin the DTO containing updated user role and activity status
     * @param emailUser         the email of the user whose profile is being modified
     * @return a {@link UserResponse} object representing the updated user profile
     */
    UserResponse modifyProfilUser(UpdateUserByAdmin updateUserByAdmin, String emailUser);

    /**
     * Deletes a user from the system based on their email address.
     *
     * @param emailUser  the email address of the user to be deleted
     */
    void deleteUser(String emailUser);

    /**
     * Retrieves a list of all registered users in the system.
     *
     * @param pageable list pagination
     * @return a list of {@link UserResponse} objects representing all users
     */
    Page<UserResponse> getAllUsers(Pageable pageable);

    /**
     * Retrieves a user by their email address.
     *
     * @param emailUser the email address of the user to retrieve
     * @return the {@link UserResponse} DTO associated with the provided email
     */
    UserResponse getUserByEmail(String emailUser);
}
