package com.edu.squashbot.telegram.service.impl;

import com.edu.squashbot.telegram.config.DialogFlowConfig;
import com.edu.squashbot.telegram.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nServiceImpl implements I18nService {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private DialogFlowConfig config;

    @Override
    public String getMessage(String key, String... args) {
        return messageSource.getMessage(key, args, Locale.forLanguageTag(config.getDefaultLanguage()));
    }
}
