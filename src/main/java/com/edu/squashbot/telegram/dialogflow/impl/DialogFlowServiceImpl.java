package com.edu.squashbot.telegram.dialogflow.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v2beta1.Dialogflow;
import com.google.api.services.dialogflow.v2beta1.DialogflowScopes;
import com.google.api.services.dialogflow.v2beta1.model.*;
import com.edu.squashbot.telegram.config.DialogFlowConfig;
import com.edu.squashbot.telegram.dialogflow.DialogFlowService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class DialogFlowServiceImpl implements DialogFlowService {
    @Autowired
    private DialogFlowConfig config;

    private Dialogflow dialogflow;

    @SneakyThrows
    @PostConstruct
    public void init() {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = GoogleCredential
                .fromStream(DialogFlowServiceImpl.class.getResourceAsStream(config.getAgentKeyPath()))
                .createScoped(DialogflowScopes.all());

        credential.refreshToken();

        this.dialogflow = new Dialogflow.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(config.getApplicationName())
                .build();
    }

    @Override
    @SneakyThrows
    public GoogleCloudDialogflowV2beta1QueryResult getResponse(String sessionId, String text, String languageCode) {
        GoogleCloudDialogflowV2beta1TextInput textInput = new GoogleCloudDialogflowV2beta1TextInput();
        textInput.setLanguageCode(languageCode);
        textInput.setText(text);

        GoogleCloudDialogflowV2beta1QueryInput queryInput = new GoogleCloudDialogflowV2beta1QueryInput();
        queryInput.setText(textInput);

        GoogleCloudDialogflowV2beta1DetectIntentRequest detectIntentRequest =
                new GoogleCloudDialogflowV2beta1DetectIntentRequest();
        detectIntentRequest.setQueryInput(queryInput);

        String session = String.format(config.getSessionPattern(), config.getAgentId(), sessionId);

        Dialogflow.Projects.Agent.Sessions.DetectIntent detectIntent = dialogflow
                .projects().agent().sessions().detectIntent(session, detectIntentRequest);

        GoogleCloudDialogflowV2beta1DetectIntentResponse detectIntentResponse = detectIntent.execute();
        log.debug(detectIntentResponse.toPrettyString());

        return detectIntentResponse.getQueryResult();
    }

    @Override
    public GoogleCloudDialogflowV2beta1QueryResult getResponse(String sessionId, String text) {
        return getResponse(sessionId, text, config.getDefaultLanguage());
    }

}

