package com.usermanagement.services;

import org.slf4j.MDC;

import java.util.Arrays;

public class CustomLoggerService {

    public void sendingMailLog(String context, String subject, String sender, String[] receivers, String logLevel) {
        try {
            resetMDCContext();
            MDC.put("context", context);
            MDC.put("subject", subject);
            MDC.put("sender", sender);
            if (receivers.length == 1) {
                MDC.put("receiver", receivers[0]);
            }
            else if (receivers.length > 1) {
                MDC.put("receiver", Arrays.toString(receivers));
            }
            //logBasedOnLevel(logLevel);
            resetMDCContext();
        } catch (Exception e) {
            resetMDCContext();
            casualExceptionLog("customErrorMessage", "mailLog Failed", e);
        }
    }

    private void logBasedOnLevel(String log) {

    }

    public void casualExceptionLog(String logLevelError, String errorMessage, Exception e) {
        resetMDCContext();
        MDC.put("customErrorMessage", errorMessage);
        MDC.put("stackTrace", Arrays.toString(e.getStackTrace()));
        logBasedOnLevel(logLevelError);
        resetMDCContext();
    }
    private void resetMDCContext() {
        MDC.clear();
    }
}
