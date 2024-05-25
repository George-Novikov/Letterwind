package com.georgen.letterwind.util.extractors;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.AnnotationGuard;

public class MessageSerializerExtractor {
    public static Class extract(@LetterwindMessage Object messageObject) throws LetterwindException {
        Class javaClass = messageObject.getClass();
        AnnotationGuard.validateMessageClass(javaClass);
        LetterwindMessage letterwindMessage = (LetterwindMessage) javaClass.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.serializer();
    }

    public static Class extract(Class messageType) throws LetterwindException {
        AnnotationGuard.validateMessageClass(messageType);
        LetterwindMessage letterwindMessage = (LetterwindMessage) messageType.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.serializer();
    }
}
