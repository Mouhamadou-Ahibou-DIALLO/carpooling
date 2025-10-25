package api.carpooling.api;

import api.carpooling.application.dto.admin.UpdateUserByAdmin;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for admin operations.
 * Provides endpoints to manage users: list, get by email,
 * update role or active status, and delete users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin API", description = "Admin user management API")
public class AdminController {

    /**
     * Service used to perform admin operations on users.
     */
    private final AdminService adminService;

    /**
     * Retrieves a paginated list of all users.
     *
     * @param pageable pagination settings
     * @return paginated list of UserResponse
     */
    @GetMapping
    @Operation(summary = "List all users", description = "Retrieve all users with pagination")
    @ApiResponse(responseCode = "200", description = "Users listed successfully")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> listAllUser = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(listAllUser);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email email of the user
     * @return UserResponse of the user
     */
    @GetMapping("/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve a user by email")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getUserByEmail(
            @Parameter(description = "User email") @PathVariable String email) {
        UserResponse userResponse = adminService.getUserByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Updates a user's role or active status by admin.
     *
     * @param updateUserByAdmin payload with new role and active status
     * @param email email of the user to update
     * @return updated UserResponse
     */
    @PutMapping("/{email}")
    @Operation(summary = "Update user", description = "Update user's role or active status by admin")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> updateUserByEmail(
            @Valid @RequestBody UpdateUserByAdmin updateUserByAdmin,
            @Parameter(description = "User email") @PathVariable String email) {
        UserResponse userResponse = adminService.modifyProfilUser(updateUserByAdmin, email);
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Deletes a user by admin.
     *
     * @param email email of the user to delete
     * @return response containing message and timestamp
     */
    @DeleteMapping("/{email}")
    @Operation(summary = "Delete user", description = "Delete a user by admin")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Map<String, Object>> deleteUserByEmail(
            @Parameter(description = "User email") @PathVariable String email) {
        adminService.deleteUser(email);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
