package api.carpooling;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test configuration class for defining containerized test dependencies.
 * <p>
 * This setup uses <b>Testcontainers</b> to create and manage Docker containers
 * for PostgreSQL and Redis, ensuring isolated and reproducible test environments.
 */
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    /**
     * Default Redis port used for containerized tests.
     */
    private static final int REDIS_PORT = 6379;

    /**
     * Creates and configures a PostgreSQL container for integration testing.
     * <p>
     * The container automatically starts before tests and stops afterward.
     *
     * @return an instance of {@link PostgreSQLContainer} configured with the latest PostgreSQL image
     */
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    }

    /**
     * Creates and configures a Redis container for integration testing.
     * <p>
     * The container is exposed on the default Redis port and automatically
     * managed by the Spring Boot Testcontainers integration.
     *
     * @return an instance of {@link GenericContainer} configured with the latest Redis image
     */
    @Bean
    @ServiceConnection(name = "redis")
    GenericContainer<?> redisContainer() {
        return new GenericContainer<>(DockerImageName.parse("redis:latest"))
                .withExposedPorts(REDIS_PORT);
    }
}
