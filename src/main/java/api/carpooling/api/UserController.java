package api.carpooling.api;

import api.carpooling.application.dto.user.CompleteUserRequest;
import api.carpooling.application.dto.user.UpdateRequestUser;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for user management.
 * <p>
 * Provides endpoints for completing user profile, updating user information,
 * and deleting a user account.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User management", description = "API for managing users")
public class UserController {

    /**
     * Service used to manage user operations.
     */
    private final UserService userService;

    /**
     * Completes a user's profile with additional information.
     *
     * @param completeUserRequest payload containing profile information
     * @param token Authorization token of the user
     * @return UserResponse containing updated user information
     */
    @PostMapping
    @Operation(summary = "Complete user profile", description = "Complete a user's profile with additional details")
    @ApiResponse(responseCode = "200", description = "User profile completed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> completeUser(@Valid @RequestBody CompleteUserRequest completeUserRequest,
                                                     @RequestHeader("Authorization") String token) {
        UserResponse userResponse = userService.completeUserProfil(completeUserRequest, token);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Updates a user's information such as username, email, password, etc.
     *
     * @param updateRequestUser payload containing updated user fields
     * @param token Authorization token of the user
     * @return UserResponse containing updated user information
     */
    @PutMapping
    @Operation(summary = "Update user", description = "Update a user's information")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "User already exists")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateRequestUser updateRequestUser,
                                                   @RequestHeader("Authorization") String token) {
        UserResponse userResponse = userService.updateUser(updateRequestUser, token);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Deletes a user account identified by the authorization token.
     *
     * @param token Authorization token of the user
     * @return Map containing a success message and timestamp
     */
    @DeleteMapping
    @Operation(summary = "Delete user", description = "Delete a user account")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestHeader("Authorization") String token) {
        userService.deleteUser(token);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
