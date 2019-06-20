package com.edu.squashbot.telegram.service.intent.impl;

import com.edu.squashbot.telegram.config.DialogFlowConfig;
import com.edu.squashbot.telegram.dialogflow.DialogFlowService;
import com.edu.squashbot.telegram.dialogflow.IntentName;
import com.edu.squashbot.telegram.entity.CourtBooking;
import com.edu.squashbot.telegram.service.BookingService;
import com.edu.squashbot.telegram.service.CallbackHandler;
import com.edu.squashbot.telegram.service.I18nService;
import com.edu.squashbot.telegram.service.UserService;
import com.edu.squashbot.telegram.service.intent.BaseMessageHandler;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2beta1QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.edu.squashbot.telegram.util.DefaultMessageConstructor.createMessage;
import static com.edu.squashbot.telegram.util.DefaultMessageConstructor.getKeyboardMessage;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class BookCourtHandlerImpl extends BaseMessageHandler implements CallbackHandler {
    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    private DialogFlowService dialogFlowService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private DialogFlowConfig config;
    @Autowired
    private I18nService i18nService;

    @PostConstruct
    public void init() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm, MMM dd")
                .localizedBy(Locale.forLanguageTag(config.getDefaultLanguage()));
    }

    @Override
    public BotApiMethod<Message> handleMessage(Message message, GoogleCloudDialogflowV2beta1QueryResult response) {
        if (IntentName.COURT_BOOKING.equals(response.getIntent().getDisplayName())) {
            String responseText = handleCourtBooking(response, message.getFrom());
            return createMessage(message.getChatId(), responseText);
        } else if (IntentName.CANCEL_COURT_BOOKING.equals(response.getIntent().getDisplayName())) {
            return handleCancelCourtBooking(message, response);
        }
        return this.getNextMessageHandler().handleMessage(message, response);
    }

    private String handleCourtBooking(GoogleCloudDialogflowV2beta1QueryResult response, User telegramUser) {
        Map<String, Object> parameters = response.getParameters();
        String timeParameter = (String) parameters.get("time");
        String dateParameter = (String) parameters.get("date");
        // prompts from DialogFlow
        if (StringUtils.isEmpty(timeParameter) || StringUtils.isEmpty(dateParameter)) {
            return response.getFulfillmentText();
        }

        // parse time
        ZonedDateTime time = ZonedDateTime.parse(timeParameter);
        ZonedDateTime date = ZonedDateTime.parse(dateParameter);
        LocalDateTime actualDateTime = LocalDateTime.of(date.toLocalDate(), time.toLocalTime());

        return userService.getUser(telegramUser)
                .flatMap(user -> bookingService.makeBooking(user, actualDateTime))
                .map(courtBooking -> this.getSuccessBookingResponse(response.getFulfillmentText(), courtBooking, actualDateTime))
                .orElseGet(() -> {
                    var courtIsNotAvailableResponse = dialogFlowService.getResponse(telegramUser.getId().toString(),
                            i18nService.getMessage("court.notAvailable", telegramUser.getFirstName()));
                    return courtIsNotAvailableResponse.getFulfillmentText();
                });
    }

    private String getSuccessBookingResponse(String dialogFlowResponse, CourtBooking courtBooking, LocalDateTime actualDateTime) {
        return dialogFlowResponse
                .replace("_dateTime_", actualDateTime.format(dateTimeFormatter))
                .replace("_courtName_", courtBooking.getCourt().getName());
    }

    private BotApiMethod<Message> handleCancelCourtBooking(Message message, GoogleCloudDialogflowV2beta1QueryResult response) {
        // check if user is ok
        User telegramUser = message.getFrom();
        Optional<com.edu.squashbot.telegram.entity.User> userOpt = userService.getUser(telegramUser);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("wtf, no user");
        }

        // get booking dates
        var user = userOpt.get();
        Map<String, String> idToBookingDateMap = bookingService.getBookingsForUser(user).stream()
                .collect(toMap(
                        courtBooking -> "CourtBookingId:" + courtBooking.getId(),
                        courtBooking -> courtBooking.getCourt().getName() + ", " + courtBooking.getStart().format(dateTimeFormatter)));

        if (!idToBookingDateMap.isEmpty()) {
            return getKeyboardMessage(message, response.getFulfillmentText(), idToBookingDateMap);
        } else {
            return createMessage(message.getChatId(), i18nService.getMessage("court.noBookings"));
        }
    }

    @Override
    public BotApiMethod<Message> handleCallbackQuery(CallbackQuery callbackQuery) {
        String payload = callbackQuery.getData();
        if (payload.startsWith("CourtBookingId:")) {
            var courtBookingId = payload.replace("CourtBookingId:", "");
            bookingService.removeBooking(courtBookingId);
            return createMessage(callbackQuery.getMessage().getChatId(),
                    i18nService.getMessage("court.booking.success"));
        }
        throw new RuntimeException("Wtf button");
    }

    @Override
    public Integer getOrder() {
        return 10;
    }
}
