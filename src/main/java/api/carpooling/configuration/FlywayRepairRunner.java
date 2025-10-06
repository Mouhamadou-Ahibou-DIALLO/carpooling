package api.carpooling.configuration;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlywayRepairRunner implements CommandLineRunner {

    private final Flyway flyway;

    @Override
    public void run(String... args) {
        try {
            flyway.validate();
        } catch (FlywayException e) {
            System.out.println("Problème détecté avec Flyway : " + e.getMessage());
            System.out.println("Réparation en cours...");
            flyway.repair();
            System.out.println("Réparation Flyway terminée !");
        }
    }
}

