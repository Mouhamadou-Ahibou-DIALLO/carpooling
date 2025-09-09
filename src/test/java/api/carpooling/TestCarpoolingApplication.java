package api.carpooling;

import org.springframework.boot.SpringApplication;

public class TestCarpoolingApplication {

    public static void main(String[] args) {
        SpringApplication.from(CarpoolingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
