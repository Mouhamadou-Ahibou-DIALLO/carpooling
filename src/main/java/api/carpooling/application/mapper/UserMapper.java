package api.carpooling.application.mapper;

import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface to convert between User entity and UserDTO.
 * <p>
 * Uses MapStruct to automatically generate mapping implementations.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the User entity
     * @return corresponding UserDTO
     */
    UserDTO toDTO(User user);

    /**
     * Converts a UserDTO to a User entity.
     * <p>
     * Some sensitive fields like password and tokens are ignored.
     *
     * @param dto the UserDTO
     * @return corresponding User entity
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "photoUser", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    User toEntity(UserDTO dto);
}
