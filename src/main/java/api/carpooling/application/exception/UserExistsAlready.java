package api.carpooling.application.exception;

public class UserExistsAlready extends RuntimeException {
    public UserExistsAlready(String message) {
        super(message);
    }
}
