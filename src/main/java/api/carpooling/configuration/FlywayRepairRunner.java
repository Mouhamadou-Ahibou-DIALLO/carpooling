package api.carpooling.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner that validates Flyway migrations on startup
 * and repairs the database if validation fails.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FlywayRepairRunner implements CommandLineRunner {

    /**
     * Flyway instance used to validate and repair database migrations.
     */
    private final Flyway flyway;

    /**
     * Runs Flyway validation and performs repair if needed.
     *
     * @param args command-line arguments
     */
    @Override
    public void run(String... args) {
        try {
            flyway.validate();
        } catch (FlywayException e) {
            log.error("Problème détecté avec Flyway : {}", e.getMessage());
            log.info("Réparation en cours...");
            flyway.repair();
            log.info("Réparation Flyway terminée !");
        }
    }
}
