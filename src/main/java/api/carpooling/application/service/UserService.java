package api.carpooling.application.service;

import api.carpooling.application.dto.user.CompleteUserRequest;
import api.carpooling.application.dto.user.UpdateRequestUser;
import api.carpooling.application.dto.user.UserResponse;

/**
 * Service interface for standard user operations.
 * <p>
 * Provides methods for creating, updating, and deleting user accounts.
 */
public interface UserService {

    /**
     * Complete a new user based on the provided registration data.
     *
     * @param completeUserRequest the DTO containing new user information
     * @param token             the authentication or invitation token used during creation
     * @return a {@link UserResponse} representing the newly created user
     */
    UserResponse completeUserProfil(CompleteUserRequest completeUserRequest, String token);

    /**
     * Updates the profile information of an existing user.
     *
     * @param updateUserRequest the DTO containing updated user information
     * @param token             the JWT token of the user performing the update
     * @return a {@link UserResponse} representing the updated user data
     */
    UserResponse updateUser(UpdateRequestUser updateUserRequest, String token);

    /**
     * Deletes the authenticated user from the system.
     *
     * @param token the JWT token identifying the user to be deleted
     */
    void deleteUser(String token);
}
