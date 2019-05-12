package com.edu.squashbot.telegram.service;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public interface UserService {
    com.edu.squashbot.telegram.entity.User registerNewUser(User user);

    Optional<com.edu.squashbot.telegram.entity.User> getUser(User telegramUser);
}
