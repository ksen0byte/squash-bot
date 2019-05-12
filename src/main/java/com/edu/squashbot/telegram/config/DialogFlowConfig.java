package com.edu.squashbot.telegram.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:dialogFlow.properties")
public class DialogFlowConfig {
    @Value("${dialogFlow.agent.key.path}")
    private String agentKeyPath;
    @Value("${dialogFlow.application.name}")
    private String applicationName;
    @Value("${dialogFlow.agent.id}")
    private String agentId;
    @Value("${dialogFlow.session.pattern}")
    private String sessionPattern;
    @Value("${dialogFlow.language.default}")
    private String defaultLanguage;
}
