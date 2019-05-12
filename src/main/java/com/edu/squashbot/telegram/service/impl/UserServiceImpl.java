package com.edu.squashbot.telegram.service.impl;

import com.edu.squashbot.telegram.service.UserService;
import com.edu.squashbot.telegram.entity.User;
import com.edu.squashbot.telegram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public User registerNewUser(org.telegram.telegrambots.meta.api.objects.User userToRegister){
        User user = User.builder()
                .id(userToRegister.getId())
                .isBot(userToRegister.getBot())
                .firstName(userToRegister.getFirstName())
                .lastName(userToRegister.getLastName())
                .userName(userToRegister.getUserName())
                .languageCode(userToRegister.getLanguageCode())
                .build();

        return repository.save(user);
    }

    @Override
    public Optional<User> getUser(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        return repository.findById(telegramUser.getId());
    }
}
