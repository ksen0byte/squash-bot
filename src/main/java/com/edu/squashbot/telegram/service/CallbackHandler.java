package com.edu.squashbot.telegram.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CallbackHandler {
    BotApiMethod<Message> handleCallbackQuery(CallbackQuery callbackQuery);
}
