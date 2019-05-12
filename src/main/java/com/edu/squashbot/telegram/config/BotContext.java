package com.edu.squashbot.telegram.config;

import com.edu.squashbot.telegram.SquashBot;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import javax.annotation.PostConstruct;

@Component
public class BotContext {
    private final SquashBot squashBot;

    public BotContext(SquashBot squashBot) {
        this.squashBot = squashBot;
    }

    @PostConstruct
    @SneakyThrows
    public void init() {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(this.squashBot);
    }

}
