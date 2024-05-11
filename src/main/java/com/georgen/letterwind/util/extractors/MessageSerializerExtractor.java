package com.georgen.letterwind.util.extractors;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;

public class MessageSerializerExtractor {
    public static Class extract(@LetterwindMessage Object messageObject) throws LetterwindException {
        Class javaClass = messageObject.getClass();

        if (!javaClass.isAnnotationPresent(LetterwindMessage.class)){
            throw new LetterwindException("Object is not marked with @LetterwindMessage annotation.");
        }

        LetterwindMessage letterwindMessage = (LetterwindMessage) javaClass.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.serializer();
    }
}
