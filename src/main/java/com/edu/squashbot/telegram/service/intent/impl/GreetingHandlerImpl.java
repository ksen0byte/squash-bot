package com.edu.squashbot.telegram.service.intent.impl;

import com.edu.squashbot.telegram.entity.User;
import com.edu.squashbot.telegram.service.I18nService;
import com.edu.squashbot.telegram.service.UserService;
import com.edu.squashbot.telegram.service.intent.BaseMessageHandler;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2beta1QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Collection;
import java.util.Optional;

import static com.edu.squashbot.telegram.util.DefaultMessageConstructor.createMessage;

@Service
public class GreetingHandlerImpl extends BaseMessageHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private I18nService i18nService;

    @Override
    public BotApiMethod<Message> handleMessage(Message message, GoogleCloudDialogflowV2beta1QueryResult response) {
        if (isInitialMessage(message)) {
            return this.handleGreeting(message);
        } else {
            return this.getNextMessageHandler().handleMessage(message, response);
        }
    }

    private BotApiMethod<Message> handleGreeting(Message message) {
        User user = userService.registerNewUser(message.getFrom());
        String greetingText = i18nService.getMessage("greeting.first", user.getFirstName());
        return createMessage(message.getChatId(), greetingText);
    }

    private boolean isInitialMessage(Message message) {
        boolean isBotCommand = Optional.ofNullable(message.getEntities())
                .map(Collection::stream)
                .map(messageEntityStream -> messageEntityStream
                        .anyMatch(messageEntity -> "bot_command".equals(messageEntity.getType())))
                .orElse(false);
        return "/start".equals(message.getText()) && isBotCommand;
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
