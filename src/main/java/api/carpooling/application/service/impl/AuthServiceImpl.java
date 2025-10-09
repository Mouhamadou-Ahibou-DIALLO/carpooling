package api.carpooling.application.service.impl;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.application.exception.ExpiredRefreshTokenException;
import api.carpooling.application.exception.PasswordNotMatchException;
import api.carpooling.application.exception.UserExistsAlready;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.application.mapper.UserMapper;
import api.carpooling.application.service.AuthService;
import api.carpooling.domain.User;
import api.carpooling.repository.UserRepository;
import api.carpooling.utils.EncodedPassword;
import api.carpooling.utils.TokenGenerator;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of AuthService for user registration, login,
 * token refresh, and fetching authenticated user profile.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    /**
     * Repository for User entity operations.
     */
    private final UserRepository userRepository;

    /**
     * Mapper to convert between User entity and UserDTO.
     */
    private final UserMapper userMapper;

    /**
     * Utility class for generating and parsing JWT tokens.
     */
    private final TokenGenerator tokenGenerator;

    @Override
    public UserDTO register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())
                || userRepository.existsByUsername(request.username())) {
            log.error("Username or Email already exists");
            throw new UserExistsAlready("User already exists with email or username");
        }

        String patternRegexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!request.password().matches(patternRegexp)) {
            log.error("Password does not match security requirements");
            throw new PasswordNotMatchException("Password does not meet security requirements!");
        }

        String passwordHash = EncodedPassword.encode(request.password());

        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setUsername(request.username());
        newUser.setPassword(passwordHash);
        newUser.setPhoneNumber(request.phoneNumber());

        log.info("New User temporaire : {}", newUser);
        User tempUser = userRepository.saveAndFlush(newUser);

        String jwtToken = tokenGenerator.generateJwtToken(tempUser.getId(), tempUser.getRoleUser().name());

        String refreshToken;
        do {
            refreshToken = tokenGenerator.generateRefreshToken();
        } while (userRepository.existsByRefreshToken(refreshToken));

        tempUser.setToken(jwtToken);
        tempUser.setRefreshToken(refreshToken);
        tempUser.setTokenExpired(
                tokenGenerator.getRefreshTokenExpiry().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
        );

        User savedUser = userRepository.save(tempUser);
        log.info("New User registered with JWT Token: {}", jwtToken);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO login(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        if (!EncodedPassword.isRightPassword(request.password(), user.getPassword())) {
            log.error("Password does not match for login");
            throw new PasswordNotMatchException("Invalid credentials!");
        }

        String jwt = tokenGenerator.generateJwtToken(user.getId(), user.getRoleUser().name());
        String refresh;
        do {
            refresh = tokenGenerator.generateRefreshToken();
        } while (userRepository.existsByRefreshToken(refresh));

        LocalDateTime expiry = tokenGenerator.getRefreshTokenExpiry().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        user.setToken(jwt);
        user.setRefreshToken(refresh);
        user.setTokenExpired(expiry);
        userRepository.save(user);

        log.info("User login with JWT Token: {}", jwt);
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO refreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UserNotFoundException("Invalid refresh token"));

        if (user.getTokenExpired() != null && user.getTokenExpired().isBefore(LocalDateTime.now())) {
            log.error("Token is expired");
            throw new ExpiredRefreshTokenException("Refresh token expired");
        }

        String newJwt = tokenGenerator.generateJwtToken(user.getId(), user.getRoleUser().name());
        user.setToken(newJwt);
        user.setTokenExpired(
                tokenGenerator.getRefreshTokenExpiry().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
        );

        userRepository.save(user);
        log.info("User refresh token: {}", newJwt);
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO me(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = tokenGenerator.parseJwt(token);
        UUID userId = UUID.fromString(claims.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toDTO(user);
    }
}
