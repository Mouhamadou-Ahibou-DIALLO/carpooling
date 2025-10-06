package api.carpooling.application.service;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;

public interface AuthService {

    public UserDTO register(RegisterUserRequest registerUserRequest);

    public UserDTO login(LoginUserRequest loginUserRequest);

    UserDTO refreshToken(String refreshToken);

    UserDTO me(String token);
}
