package com.georgen.letterwind.util.extractors;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.AnnotationGuard;

public class EventHandlerExtractor {
    public static Class<? extends ErrorHandler> extractErrorHandler(Object message) throws LetterwindException {
        Class messageClass = message.getClass();
        AnnotationGuard.validateMessageClass(messageClass);
        LetterwindMessage letterwindMessage = (LetterwindMessage) messageClass.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.errorHandler();
    }

    public static Class extractSuccessHandler(Object message) throws LetterwindException {
        Class messageClass = message.getClass();
        AnnotationGuard.validateMessageClass(messageClass);
        LetterwindMessage letterwindMessage = (LetterwindMessage) messageClass.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.successHandler();
    }
}
