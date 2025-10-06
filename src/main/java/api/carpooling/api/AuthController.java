package api.carpooling.api;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.application.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth user", description = "Auth user management API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Create a user, generate a token immediately."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password or user already exists")
    })
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        UserDTO userDTO = authService.register(registerUserRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticate user with email & password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        UserDTO userDTO = authService.login(loginUserRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/refresh_token")
    @Operation(
            summary = "Refresh access token",
            description = "Generate a new JWT token using a refresh token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Expired refresh token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> refreshToken(@RequestParam String refreshToken) {
        UserDTO userDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get logged user profile",
            description = "Return the profile of the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> me(@RequestHeader("Authorization") String authHeader) {
        UserDTO userDTO = authService.me(authHeader);
        return ResponseEntity.ok(userDTO);
    }
}
