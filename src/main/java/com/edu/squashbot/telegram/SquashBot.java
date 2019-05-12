package com.edu.squashbot.telegram;

import com.edu.squashbot.telegram.config.TelegramConfig;
import com.edu.squashbot.telegram.service.MessageHandlerManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class SquashBot extends TelegramLongPollingBot {
    @Autowired
    private MessageHandlerManager messageHandlerManager;
    @Autowired
    private TelegramConfig telegramConfig;

    @Override
    public String getBotToken() {
        return telegramConfig.getBotToken();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            BotApiMethod<Message> botApiMethod = messageHandlerManager.handleMessage(message);
            execute(botApiMethod);
        } else if (update.hasCallbackQuery()) {
            BotApiMethod<Message> botApiMethod = messageHandlerManager.handleCallBackQuery(update.getCallbackQuery());
            execute(botApiMethod);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }
}
