package com.edu.squashbot.telegram.service.intent;

import com.edu.squashbot.telegram.service.MessageHandler;
import lombok.Getter;

@Getter
public abstract class BaseMessageHandler implements MessageHandler {
    private MessageHandler nextMessageHandler;

    @Override
    public void setNextHandler(MessageHandler messageHandler) {
        this.nextMessageHandler = messageHandler;
    }

}
