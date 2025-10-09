package api.carpooling.configuration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link FlywayRepairRunner}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("FlywayRepairRunner Test")
class FlywayRepairRunnerTest {

    private static Flyway flyway;
    private static FlywayRepairRunner flywayRepairRunner;

    /**
     * Initializes mock objects before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        System.out.println("FlywayRepairRunner tests initialized");
    }

    /**
     * Re-initializes the mock before each test to avoid call accumulation.
     */
    @BeforeEach
    void setUp() {
        flyway = mock(Flyway.class);
        flywayRepairRunner = new FlywayRepairRunner(flyway);
    }

    /**
     * Cleans up resources after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        System.out.println("FlywayRepairRunner tests completed");
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
        doThrow(new FlywayException("Validation failed")).when(flyway).validate();
        flywayRepairRunner.run();

        verify(flyway, times(1)).validate();
        verify(flyway, times(1)).repair();
    }
}
