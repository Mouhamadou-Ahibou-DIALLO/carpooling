package api.carpooling.configuration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link FlywayRepairRunner}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("FlywayRepairRunner Test")
@Slf4j
public class FlywayRepairRunnerTest {

    /** Flyway instance used to manage database migrations. */
    private static Flyway flyway;

    /** FlywayRepairRunner instance to repair Flyway metadata if needed. */
    private static FlywayRepairRunner flywayRepairRunner;

    /**
     * Initializes mocks before all tests to avoid modifying static fields from an instance method.
     */
    @BeforeAll
    static void setUpAll() {
        flyway = mock(Flyway.class);
        flywayRepairRunner = new FlywayRepairRunner(flyway);
        log.info("FlywayRepairRunner tests initialized");
    }

    /** Cleans up resources after all tests. */
    @AfterAll
    static void tearDownAll() {
        log.info("FlywayRepairRunner tests completed");
    }

    /**
     * Verifies that Flyway validation runs successfully when no exception is thrown.
     */
    @Test
    @Order(1)
    @DisplayName("Should validate Flyway successfully without repair")
    void testFlywayValidateSuccess() {
        Assertions.assertDoesNotThrow(() -> flywayRepairRunner.run());
        verify(flyway, times(1)).validate();
        verify(flyway, never()).repair();
    }

    /**
     * Verifies that Flyway repair is called when validation throws an exception.
     */
    @Test
    @Order(2)
    @DisplayName("Should repair Flyway when validation fails")
    void testFlywayRepairOnException() {
        doThrow(new FlywayException("Validation failed"))
                .when(flyway).validate();

        flywayRepairRunner.run();

        verify(flyway, times(2)).validate();
        verify(flyway, times(1)).repair();
    }
}
