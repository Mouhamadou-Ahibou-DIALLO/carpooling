package api.carpooling.api;

import api.carpooling.application.dto.auth.LoginUserRequest;
import api.carpooling.application.dto.auth.RegisterUserRequest;
import api.carpooling.application.dto.user.UserDTO;
import api.carpooling.application.service.AuthService;
import api.carpooling.domain.enumeration.RoleUser;
import api.carpooling.utils.TokenGenerator;

import org.junit.jupiter.api.*;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link AuthController}.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = api.carpooling.security.JwtAuthFilter.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AuthController REST API Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private TokenGenerator tokenGenerator;

    private UserDTO userDTO;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Starting AuthController tests...");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AuthController tests completed!");
    }

    /**
     * Initialization of a test user before each method.
     */
    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(
                UUID.randomUUID(),
                "test@example.com",
                "TestUser",
                "jwt.token",
                "refresh.token",
                LocalDateTime.now().plusDays(7),
                RoleUser.ROLE_PASSENGER,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
    }

    /**
     * Test the /register route with a valid JSON body.
     */
    @Test
    @Order(1)
    @DisplayName("POST /api/v1/auth/register - should register user successfully")
    void testRegisterSuccess() throws Exception {
        Mockito.when(authService.register(any(RegisterUserRequest.class))).thenReturn(userDTO);

        String jsonRequest = """
            {
                "username": "TestUser",
                "email": "test@example.com",
                "password": "Password@123",
                "phoneNumber": "+33600000000"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("TestUser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        Mockito.verify(authService, Mockito.times(1)).register(any(RegisterUserRequest.class));
    }

    /**
     * Test the /login route with valid credentials.
     */
    @Test
    @Order(2)
    @DisplayName("POST /api/v1/auth/login - should login successfully")
    void testLoginSuccess() throws Exception {
        Mockito.when(authService.login(any(LoginUserRequest.class))).thenReturn(userDTO);

        String jsonRequest = """
            {
                "email": "test@example.com",
                "password": "Password@123"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("TestUser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        Mockito.verify(authService, Mockito.times(1)).login(any(LoginUserRequest.class));
    }

    /**
     * Teste la route / refresh_token avec un token valid.
     */
    @Test
    @Order(3)
    @DisplayName("POST /api/v1/auth/refresh_token - should refresh token successfully")
    void testRefreshTokenSuccess() throws Exception {
        Mockito.when(authService.refreshToken(eq("refresh.token"))).thenReturn(userDTO);

        mockMvc.perform(post("/api/v1/auth/refresh_token")
                        .param("refreshToken", "refresh.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("TestUser")))
                .andExpect(jsonPath("$.token", is("jwt.token")));

        Mockito.verify(authService, Mockito.times(1)).refreshToken(eq("refresh.token"));
    }

    /**
     * Teste la route /me avec un header Authorization valide.
     */
    @Test
    @Order(4)
    @DisplayName("GET /api/v1/auth/me - should return current user profile")
    void testMeSuccess() throws Exception {
        Mockito.when(authService.me(eq("Bearer jwt.token"))).thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer jwt.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("TestUser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        Mockito.verify(authService, Mockito.times(1)).me(eq("Bearer jwt.token"));
    }
}
