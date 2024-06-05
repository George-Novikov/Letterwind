package com.georgen.letterwind.util;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;

public class AnnotationGuard {
    public static void validateMessageClass(Class messageClass) throws LetterwindException {
        if (!messageClass.isAnnotationPresent(LetterwindMessage.class)){
            throw new LetterwindException("The message class must be marked with the LetterwindMessage annotation.");
        }
    }
}
