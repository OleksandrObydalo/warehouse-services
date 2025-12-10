package org.example.consoleclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ConsoleClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsoleClientApplication.class, args);
    }
}

