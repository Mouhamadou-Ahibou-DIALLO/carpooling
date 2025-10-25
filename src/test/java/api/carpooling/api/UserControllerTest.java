package api.carpooling.api;

import api.carpooling.application.dto.user.CompleteUserRequest;
import api.carpooling.application.dto.user.UpdateRequestUser;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.service.UserService;
import api.carpooling.domain.enumeration.RoleUser;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests for {@link UserController}.
 * <p>
 * Tests endpoints for completing user profile, updating user,
 * and deleting user.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = api.carpooling.security.JwtAuthFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserController REST API Tests")
@Slf4j
public class UserControllerTest {

    /**
     * MockMvc to simulate HTTP requests without starting a full server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked {@link UserService} to simulate user operations.
     */
    @MockitoBean
    private UserService userService;

    /**
     * Sample {@link UserResponse} used as expected response in tests.
     */
    private UserResponse userResponse;

    /**
     * Initializes all mocks before each test.
     */
    @BeforeAll
    static void beforeAll() {
        log.info("Starting UserController tests...");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void afterAll() {
        log.info("UserController tests completed!");
    }

    /**
     * Initialization of a test user before each method.
     */
    @BeforeEach
    void setUp() {
        userResponse = new UserResponse(
                UUID.randomUUID(),
                "user@mail.com",
                "UserTest",
                "+33611111111",
                "photo.png",
                "Paris",
                true,
                true,
                "jwt.token",
                "refresh.token",
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now(),
                RoleUser.ROLE_PASSENGER,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
    }

    /**
     * Test the POST /api/v1/user endpoint.
     * <p>
     * Mocks UserService.completeUserProfil(CompleteUserRequest, String) to return a UserResponse.
     * Sends a JSON body representing the user profile to complete.
     * Verifies the returned JSON contains expected username, email, and phoneNumber fields.
     */
    @Test
    @Order(1)
    @DisplayName("POST /api/v1/user - should complete user profile")
    void testCompleteUser() throws Exception {
        Mockito.when(userService.completeUserProfil(any(CompleteUserRequest.class), anyString()))
                .thenReturn(userResponse);

        String jsonRequest = """
            {
                "username": "UserTest",
                "phoneNumber": "+33611111111",
                "address": "Paris",
                "photoUser": "photo.png"
            }
            """;

        mockMvc.perform(post("/api/v1/user")
                        .header("Authorization", "Bearer jwt.token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("UserTest")))
                .andExpect(jsonPath("$.email", is("user@mail.com")))
                .andExpect(jsonPath("$.phoneNumber", is("+33611111111")));

        Mockito.verify(userService, Mockito.times(1))
                .completeUserProfil(any(CompleteUserRequest.class), anyString());
    }

    /**
     * Test the PUT /api/v1/user endpoint.
     * <p>
     * Mocks UserService.updateUser(UpdateRequestUser, String) to return a UserResponse.
     * Sends a JSON body representing the updated user data.
     * Verifies the returned JSON contains expected username and email fields.
     */
    @Test
    @Order(2)
    @DisplayName("PUT /api/v1/user - should update user")
    void testUpdateUser() throws Exception {
        Mockito.when(userService.updateUser(any(UpdateRequestUser.class), anyString()))
                .thenReturn(userResponse);

        String jsonRequest = """
        {
            "username": "UserTestUpdated",
            "email": "user@mail.com",
            "password": "Password@123",
            "phoneNumber": "+33611111122",
            "address": "Paris Updated",
            "photoUser": "photo_updated.png"
        }
        """;

        mockMvc.perform(put("/api/v1/user")
                        .header("Authorization", "Bearer jwt.token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("UserTest")))
                .andExpect(jsonPath("$.email", is("user@mail.com")));

        Mockito.verify(userService, Mockito.times(1))
                .updateUser(any(UpdateRequestUser.class), anyString());
    }

    /**
     * Test the DELETE /api/v1/user endpoint.
     * <p>
     * Mocks UserService.deleteUser(String) to do nothing.
     * Verifies that the response JSON contains a success message and a timestamp.
     */
    @Test
    @Order(3)
    @DisplayName("DELETE /api/v1/user - should delete user")
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/api/v1/user")
                        .header("Authorization", "Bearer jwt.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User deleted successfully")))
                .andExpect(jsonPath("$.timestamp").exists());

        Mockito.verify(userService, Mockito.times(1))
                .deleteUser(anyString());
    }
}
