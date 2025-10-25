package api.carpooling.api;

import api.carpooling.application.dto.admin.UpdateUserByAdmin;
import api.carpooling.application.dto.user.UserResponse;
import api.carpooling.application.service.AdminService;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests for {@link AdminController}.
 * <p>
 * Tests endpoints for listing users, retrieving user by email,
 * updating user by admin, and deleting user by admin.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = AdminController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = api.carpooling.security.JwtAuthFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AdminController REST API Tests")
@Slf4j
public class AdminControllerTest {

    /**
     * MockMvc to simulate HTTP requests without starting a full server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked {@link AdminService} to simulate admin operations.
     */
    @MockitoBean
    private AdminService adminService;

    /**
     * Sample {@link UserResponse} used as expected response in tests.
     */
    private UserResponse userResponse;

    /**
     * Sample {@link Page<UserResponse>} for list users endpoint.
     */
    private Page<UserResponse> userPage;

    /**
     * Initializes all mocks before each test.
     */
    @BeforeAll
    static void beforeAll() {
        log.info("Starting AdminController tests...");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void afterAll() {
        log.info("AdminController tests completed!");
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

        userPage = new PageImpl<>(List.of(userResponse));
    }

    /**
     * Test the GET /api/v1/admin endpoint.
     * <p>
     * Mocks the AdminService.getAllUsers(Pageable) method to return a Page of UserResponse.
     * Verifies that the returned JSON contains the expected username and email fields.
     */
    @Test
    @Order(1)
    @DisplayName("GET /api/v1/admin - should list all users")
    void testGetAllUsers() throws Exception {
        Mockito.when(adminService.getAllUsers(any(Pageable.class)))
                .thenReturn(userPage);

        mockMvc.perform(get("/api/v1/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("UserTest")))
                .andExpect(jsonPath("$.content[0].email", is("user@mail.com")));

        Mockito.verify(adminService, Mockito.times(1)).getAllUsers(any(Pageable.class));
    }

    /**
     * Test the GET /api/v1/admin/{email} endpoint.
     * <p>
     * Mocks AdminService.getUserByEmail(String) to return a UserResponse for a given email.
     * Verifies the response JSON contains the expected username and email.
     */
    @Test
    @Order(2)
    @DisplayName("GET /api/v1/admin/{email} - should get user by email")
    void testGetUserByEmail() throws Exception {
        Mockito.when(adminService.getUserByEmail(anyString()))
                .thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/admin/user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("UserTest")))
                .andExpect(jsonPath("$.email", is("user@mail.com")));

        Mockito.verify(adminService, Mockito.times(1)).getUserByEmail("user@mail.com");
    }

    /**
     * Test the PUT /api/v1/admin/{email} endpoint.
     * <p>
     * Mocks AdminService.modifyProfilUser(UpdateUserByAdmin, String) to return the updated UserResponse.
     * Sends a JSON body representing the role and active status to update.
     * Verifies the returned JSON contains expected username and email fields.
     */
    @Test
    @Order(3)
    @DisplayName("PUT /api/v1/admin/{email} - should update user by admin")
    void testUpdateUserByEmail() throws Exception {
        Mockito.when(adminService.modifyProfilUser(any(UpdateUserByAdmin.class), anyString()))
                .thenReturn(userResponse);

        String jsonRequest = """
            {
                "roleUser": "ROLE_DRIVER",
                "active": true
            }
            """;

        mockMvc.perform(put("/api/v1/admin/user@mail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("UserTest")))
                .andExpect(jsonPath("$.email", is("user@mail.com")));

        Mockito.verify(adminService, Mockito.times(1))
                .modifyProfilUser(any(UpdateUserByAdmin.class), anyString());
    }

    /**
     * Test the DELETE /api/v1/admin/{email} endpoint.
     * <p>
     * Mocks AdminService.deleteUser(String) to do nothing.
     * Verifies that the response JSON contains a success message and a timestamp.
     */
    @Test
    @Order(4)
    @DisplayName("DELETE /api/v1/admin/{email} - should delete user by admin")
    void testDeleteUserByEmail() throws Exception {
        Mockito.doNothing().when(adminService).deleteUser(anyString());

        mockMvc.perform(delete("/api/v1/admin/user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User deleted successfully")))
                .andExpect(jsonPath("$.timestamp").exists());

        Mockito.verify(adminService, Mockito.times(1)).deleteUser("user@mail.com");
    }
}
