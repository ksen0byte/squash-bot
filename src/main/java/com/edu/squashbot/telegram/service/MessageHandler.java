package com.edu.squashbot.telegram.service;

import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2beta1QueryResult;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {
    Integer getOrder();
    void setNextHandler(MessageHandler nextHandler);
    BotApiMethod<Message> handleMessage(Message message, GoogleCloudDialogflowV2beta1QueryResult response);
}
