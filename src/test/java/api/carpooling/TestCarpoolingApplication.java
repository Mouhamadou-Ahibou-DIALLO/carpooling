package api.carpooling;

import org.springframework.boot.SpringApplication;

/**
 * Entry point for running the application in a test environment.
 * <p>
 * This class reuses the {@link CarpoolingApplication#main(String[])} method
 * and extends it with {@link TestcontainersConfiguration} to enable
 * containerized test dependencies such as PostgreSQL and Redis.
 */
public class TestCarpoolingApplication {

    /**
     * Bootstraps the application with the {@link TestcontainersConfiguration}
     * for integration and container-based testing.
     *
     * @param args application startup arguments
     */
    public static void main(String[] args) {
        SpringApplication.from(CarpoolingApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
