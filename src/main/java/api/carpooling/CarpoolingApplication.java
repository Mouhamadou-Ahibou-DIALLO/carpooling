package api.carpooling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Carpooling Spring Boot application.
 */
@SpringBootApplication
public class CarpoolingApplication {

    /**
     * Application launcher.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CarpoolingApplication.class, args);
    }
}
