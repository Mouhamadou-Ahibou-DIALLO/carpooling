package api.carpooling;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * Integration test that verifies if the Spring Boot application context
 * loads correctly with the {@link TestcontainersConfiguration}.
 * <p>
 * This test ensures that all required beans and dependencies are
 * properly initialized before running other tests.
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CarpoolingApplicationTests {

    /**
     * Validates that the Spring application context starts successfully.
     * <p>
     * If this test fails, it indicates a configuration issue in the application
     * (e.g., missing beans, broken context, or invalid properties).
     */
    @Test
    void contextLoads() {
        // Voluntarily empty test: simply checks that the Spring Boot context starts without error.
        // NOSONAR
    }
}
