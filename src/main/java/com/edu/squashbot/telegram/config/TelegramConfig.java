package com.edu.squashbot.telegram.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegram")
@PropertySource("classpath:telegram.properties")
public class TelegramConfig {
    private String botToken;
    private String botName;
}
