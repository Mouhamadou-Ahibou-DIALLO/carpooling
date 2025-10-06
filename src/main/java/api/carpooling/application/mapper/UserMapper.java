package api.carpooling.application.mapper;

import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "photoUser", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    User toEntity(UserDTO dto);
}


