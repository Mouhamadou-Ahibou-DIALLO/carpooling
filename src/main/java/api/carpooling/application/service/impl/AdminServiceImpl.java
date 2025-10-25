package api.carpooling.application.service.impl;

import api.carpooling.application.dto.admin.UpdateUserByAdmin;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.exception.UserNotFoundException;
import api.carpooling.application.mapper.UserResponseMapper;
import api.carpooling.application.service.AdminService;
import api.carpooling.domain.User;
import api.carpooling.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Implementation of AdminService for user modifyProfilUser,
 * delete, show all users and retrieves user by email.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminServiceImpl implements AdminService {

    /**
     * Repository for User entity operations.
     */
    private final UserRepository userRepository;

    /**
     * Mapper to convert between User entity and User Response DTO.
     */
    private final UserResponseMapper userResponseMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse modifyProfilUser(UpdateUserByAdmin updateUserByAdmin, String emailUser) {
        User user = findUserByEmail(emailUser);
        user.setRoleUser(updateUserByAdmin.roleUser());
        user.setActive(updateUserByAdmin.isActive());

        User savedUser = userRepository.save(user);
        UserResponse userResponse = userResponseMapper.toUserResponse(savedUser);

        log.info("[ADMIN SERVICE] Updated user '{}' with role {} and active={}",
                user.getEmail(), user.getRoleUser(), user.isActive());
        return userResponse;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String emailUser) {
        User user = findUserByEmail(emailUser);
        log.info("[ADMIN SERVICE] Delete user with email {}", emailUser);
        userRepository.delete(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("[ADMIN SERVICE] Get all users");
        return userRepository.findAll(pageable)
                .map(userResponseMapper::toUserResponse);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String emailUser) {
        User user = findUserByEmail(emailUser);
        log.info("[ADMIN SERVICE] Get user with email {}", emailUser);
        return userResponseMapper.toUserResponse(user);
    }

    /**
     * Retrieves a user based on email.
     *
     * @param emailUser email of User
     * @return the corresponding user
     * @throws UserNotFoundException if user not found
     */
    private User findUserByEmail(String emailUser) {
        return  userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new UserNotFoundException("[ADMIN SERVICE] "
                        + "User with email " + emailUser + " not found"));
    }
}
