package com.georgen.letterwind.tools;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.tools.extractors.ConsumerExtractor;

public class AnnotationGuard {
    public static void validateConsumer(Class consumerClass) throws LetterwindException {
        if (!ConsumerExtractor.hasConsumingMethods(consumerClass)){
            throw new LetterwindException(
                    String.format(
                            "To be added to a topic, the %s class must have methods marked with the @LetterwindConsumer annotation",
                            consumerClass.getSimpleName()
                    )
            );
        }
    }

    public static void validateMessage(Class messageClass) throws LetterwindException {
        if (!messageClass.isAnnotationPresent(LetterwindMessage.class)){
            throw new LetterwindException("The message class must be marked with the LetterwindMessage annotation.");
        }
    }
}
