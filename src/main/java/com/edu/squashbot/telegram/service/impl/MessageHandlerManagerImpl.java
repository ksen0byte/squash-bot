package com.edu.squashbot.telegram.service.impl;

import com.edu.squashbot.telegram.dialogflow.DialogFlowService;
import com.edu.squashbot.telegram.service.CallbackHandler;
import com.edu.squashbot.telegram.service.MessageHandler;
import com.edu.squashbot.telegram.service.MessageHandlerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service("messageHandlerManager")
public class MessageHandlerManagerImpl implements MessageHandlerManager {
    @Autowired
    private List<MessageHandler> messageHandlers;
    @Autowired
    private List<CallbackHandler> callbackHandlers;
    @Autowired
    private DialogFlowService dialogFlowService;


    @PostConstruct
    public void init() {
        messageHandlers.sort(Comparator.comparing(MessageHandler::getOrder));
        IntStream.range(0, messageHandlers.size() - 1)
                .forEach(i -> messageHandlers.get(i).setNextHandler(messageHandlers.get(i + 1)));
    }

    @Override
    public BotApiMethod<Message> handleMessage(Message message) {
        String sessionId = message.getFrom().getId().toString();
        var dialogFlowResponse = dialogFlowService.getResponse(sessionId, message.getText());
        return messageHandlers.get(0).handleMessage(message, dialogFlowResponse);
    }

    @Override
    public BotApiMethod<Message> handleCallBackQuery(CallbackQuery callbackQuery) {
        return callbackHandlers.get(0).handleCallbackQuery(callbackQuery);
    }
}
