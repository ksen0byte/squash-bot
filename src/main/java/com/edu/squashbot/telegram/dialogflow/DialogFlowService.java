package com.edu.squashbot.telegram.dialogflow;

import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2beta1QueryResult;

public interface DialogFlowService {
    GoogleCloudDialogflowV2beta1QueryResult getResponse(String sessionId, String text, String languageCode);
    GoogleCloudDialogflowV2beta1QueryResult getResponse(String sessionId, String text);
}
