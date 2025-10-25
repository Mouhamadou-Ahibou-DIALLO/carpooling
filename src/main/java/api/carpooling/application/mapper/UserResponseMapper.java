package api.carpooling.application.mapper;

import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.domain.User;
import org.mapstruct.Mapper;

/**
 * Mapper interface to convert between User entity and UserResponse DTO.
 * <p>
 * Uses MapStruct to automatically generate mapping implementations.
 */
@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    /**
     * Converts a User entity to a UserResponse DTO.
     *
     * @param user the User entity
     * @return corresponding UserResponse DTO
     */
    UserResponse toUserResponse(User user);
}
