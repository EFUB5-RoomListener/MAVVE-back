package com.efub.mavve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MavveApplication {

    public static void main(String[] args) {
        SpringApplication.run(MavveApplication.class, args);
    }

}
