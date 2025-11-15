package api.carpooling.api;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.application.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for user authentication.
 * <p>
 * Provides endpoints for registration, login, logout, token refresh, and
 * fetching the current authenticated user's profile.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth user", description = "Auth user management API")
public class AuthController {

    /**
     * Service used to perform authentication operations.
     */
    private final AuthService authService;

    /**
     * Registers a new user and generates a JWT token.
     *
     * @param registerUserRequest registration request payload
     * @return UserDTO of the registered user
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a user, generate a token immediately.")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid password or user already exists")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        UserDTO userDTO = authService.register(registerUserRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param loginUserRequest login request payload
     * @return UserDTO of the authenticated user
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with email & password")
    @ApiResponse(responseCode = "200", description = "User logged successfully")
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "401", description = "Inactive account")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        UserDTO userDTO = authService.login(loginUserRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * logout user with authenticated token
     *
     * @param token JWT token from Authorization header
     * user logout
     * @return map content : message : User logout successfully and timestamp
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidate the current user's token and refresh token")
    @ApiResponse(responseCode = "200", description = "User logout successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User logout successfully");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes an access JWT token using a refresh token.
     *
     * @param refreshToken the refresh token
     * @return UserDTO with new JWT token
     */
    @PostMapping("/refresh_token")
    @Operation(summary = "Refresh access token", description = "Generate a new JWT token using a refresh token.")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @ApiResponse(responseCode = "401", description = "Expired refresh token")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserDTO> refreshToken(@RequestParam String refreshToken) {
        UserDTO userDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @param authHeader the Authorization header containing the JWT token
     * @return UserDTO of the authenticated user
     */
    @GetMapping("/me")
    @Operation(summary = "Get logged user profile", description = "Return the profile of the authenticated user")
    @ApiResponse(responseCode = "200", description = "User profile retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserDTO> me(@RequestHeader("Authorization") String authHeader) {
        UserDTO userDTO = authService.me(authHeader);
        return ResponseEntity.ok(userDTO);
    }
}
