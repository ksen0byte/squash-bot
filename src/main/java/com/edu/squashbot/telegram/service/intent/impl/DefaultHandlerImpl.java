package com.edu.squashbot.telegram.service.intent.impl;

import com.edu.squashbot.telegram.service.intent.BaseMessageHandler;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2beta1QueryResult;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class DefaultHandlerImpl extends BaseMessageHandler {
    @Override
    public BotApiMethod<Message> handleMessage(Message message, GoogleCloudDialogflowV2beta1QueryResult response) {
        return new SendMessage(message.getChatId(), response.getFulfillmentText());
    }

    @Override
    public Integer getOrder() {
        return 20;
    }
}
