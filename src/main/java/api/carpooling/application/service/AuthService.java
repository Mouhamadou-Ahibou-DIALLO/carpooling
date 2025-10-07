package api.carpooling.application.service;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;

/**
 * Service interface for user authentication operations.
 */
public interface AuthService {

    /**
     * Registers a new user.
     *
     * @param registerUserRequest registration request payload
     * @return UserDTO of the registered user
     */
    UserDTO register(RegisterUserRequest registerUserRequest);

    /**
     * Authenticates a user with email and password.
     *
     * @param loginUserRequest login request payload
     * @return UserDTO of the authenticated user
     */
    UserDTO login(LoginUserRequest loginUserRequest);

    /**
     * Refreshes JWT access token using a refresh token.
     *
     * @param refreshToken the refresh token
     * @return UserDTO with new JWT token
     */
    UserDTO refreshToken(String refreshToken);

    /**
     * Retrieves the profile of the authenticated user.
     *
     * @param token JWT token from Authorization header
     * @return UserDTO of the authenticated user
     */
    UserDTO me(String token);
}
