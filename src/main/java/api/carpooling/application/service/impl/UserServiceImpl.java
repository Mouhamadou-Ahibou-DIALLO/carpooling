package api.carpooling.application.service.impl;

import api.carpooling.application.dto.user.CompleteUserRequest;
import api.carpooling.application.dto.user.UpdateRequestUser;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.exception.RoleAssignmentNotAllowedException;
import api.carpooling.application.exception.UserExistsAlready;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.application.mapper.UserResponseMapper;
import api.carpooling.application.service.UserService;
import api.carpooling.domain.User;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.repository.UserRepository;
import api.carpooling.utils.TokenGenerator;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of UserService for user created,
 * updated, and deleted.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * Repository for User entity operations.
     */
    private final UserRepository userRepository;

    /**
     * Utility class for generating and parsing JWT tokens.
     */
    private final TokenGenerator tokenGenerator;

    /**
     * Mapper to convert between User entity and User Response DTO.
     */
    private final UserResponseMapper userResponseMapper;

    @Override
    public UserResponse completeUserProfil(CompleteUserRequest completeUserRequest, String token) {
        User existingUser = getUserByToken(token);
        existingUser.setPhotoUser(completeUserRequest.photoUser());
        existingUser.setAddress(completeUserRequest.address());
        existingUser.setVerified(true);

        RoleUser requestRoleUser = completeUserRequest.roleUser();
        if (requestRoleUser == RoleUser.ROLE_ADMIN) {
            throw new RoleAssignmentNotAllowedException("[USER SERVICE] "
                    + "You are not allowed to assign ADMIN role");
        }

        if (requestRoleUser == RoleUser.ROLE_PASSENGER
                || requestRoleUser == RoleUser.ROLE_DRIVER) {
            existingUser.setRoleUser(requestRoleUser);
        }

        User savedUser = userRepository.save(existingUser);
        savedUser.setVerified(true);
        UserResponse userResponse = userResponseMapper.toUserResponse(savedUser);

        log.info("[USER SERVICE] User saved successfully {}", userResponse.toString());
        return userResponse;
    }

    @Override
    public UserResponse updateUser(UpdateRequestUser updateUserRequest, String token) {
        User existingUser = getUserByToken(token);
        if (hasNoChanges(existingUser, updateUserRequest)) {
            throw new UserExistsAlready("No changes detected — your profile is already up to date.");
        }

        BeanUtils.copyProperties(updateUserRequest, existingUser, getNullPropertyNames(updateUserRequest));

        User savedUser = userRepository.save(existingUser);
        UserResponse userResponse = userResponseMapper.toUserResponse(savedUser);

        log.info("[USER SERVICE] User updated successfully {}", userResponse.toString());
        return userResponse;
    }

    @Override
    public void deleteUser(String token) {
        User user = getUserByToken(token);
        log.warn("[USER SERVICE] Delete user {}", user);
        userRepository.delete(user);
    }

    /**
     * Checks if any unique field (email, username, phone) of a user
     * matches the given update request.
     *
     * @param existingUser the existing user
     * @param request the update request
     * @return true if email, username, or phone number matches
     */
    private boolean hasNoChanges(User existingUser, UpdateRequestUser request) {
        return (request.email() != null && request.email().equals(existingUser.getEmail()))
                && (request.username() != null && request.username().equals(existingUser.getUsername()))
                && (request.phoneNumber() != null
                && request.phoneNumber().equals(existingUser.getPhoneNumber()));
    }

    /**
     * Retrieves a user based on a JWT token.
     *
     * @param token the JWT or Bearer token
     * @return the corresponding user
     * @throws UserNotFoundException if user not found
     */
    private User getUserByToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = tokenGenerator.parseJwt(token);
        UUID userId = UUID.fromString(claims.getSubject());

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("[USER SERVICE] User not found"));
    }

    /**
     * Returns names of properties that are null in the given object.
     *
     * @param source the object to inspect
     * @return array of null property names
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }
}
