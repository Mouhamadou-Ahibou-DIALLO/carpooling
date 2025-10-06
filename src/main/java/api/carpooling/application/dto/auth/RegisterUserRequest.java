package api.carpooling.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest (
        @NotBlank(message = "Username is required and unique")
        String username,

        @Email(message = "Email must be valide")
        @NotBlank(message = "Email is required and unique")
        String email,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Le mot de passe doit contenir au minimum 8 caractères, avec une majuscule, une minuscule, un chiffre et un caractère spécial."
        )
        @NotBlank(message = "Password is required")
        String password,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
        @NotBlank(message = "Phone number is required and unique")
        String phoneNumber
) {}
