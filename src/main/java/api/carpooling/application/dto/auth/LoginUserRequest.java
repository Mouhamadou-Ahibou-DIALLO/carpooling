package api.carpooling.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO used to log in user.
 *
 * @param email the email address
 * @param password the password
 */
public record LoginUserRequest(
        @Email(message = "Email must be valide")
        @NotBlank(message = "Email is required and unique")
        String email,

        @NotBlank(message = "Password is required")
        String password
) { }
