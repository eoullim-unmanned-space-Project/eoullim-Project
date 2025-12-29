package org.example.eoullimback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EoullimBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EoullimBackApplication.class, args);
    }

}
