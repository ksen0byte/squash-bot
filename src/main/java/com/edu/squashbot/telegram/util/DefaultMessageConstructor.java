package com.edu.squashbot.telegram.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class DefaultMessageConstructor {
    private static Function<Map.Entry<String, String>, InlineKeyboardButton> INLINE_BUTTON_MAPPER = payloadToNameEntry -> new InlineKeyboardButton(payloadToNameEntry.getValue()).setCallbackData(payloadToNameEntry.getKey());
    private static Integer ROW_SIZE = 2;

    public static SendMessage getKeyboardMessage(Message userMessage, String response, Map<String, String> payloadToNameMap) {
        SendMessage sendMessage = createMessage(userMessage.getChatId(), response);

        List<InlineKeyboardButton> buttons = payloadToNameMap.entrySet().stream().map(INLINE_BUTTON_MAPPER).collect(toList());

        int numberOfColumns = Math.floorDiv(buttons.size(), ROW_SIZE) + Math.floorMod(buttons.size(), ROW_SIZE);
        List<List<InlineKeyboardButton>> keyboard = IntStream.range(0, numberOfColumns)
                .mapToObj(i -> buttons.subList(i * ROW_SIZE, Math.min(i * ROW_SIZE + ROW_SIZE, buttons.size())))
                .collect(toList());
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup().setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyMarkup);

        return sendMessage;
    }

    public static SendMessage createMessage(Long chatId, String msg) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(msg);

        return sendMessage;
    }
}
