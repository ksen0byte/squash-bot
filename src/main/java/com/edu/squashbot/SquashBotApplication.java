package com.edu.squashbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableMongoRepositories
public class SquashBotApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(SquashBotApplication.class, args);
    }

}
