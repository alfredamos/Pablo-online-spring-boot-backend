package com.alfredamos.springbootbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PabloOnlineSpringBootBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PabloOnlineSpringBootBackendApplication.class, args);
    }

}
