package com.georgen.letterwind.messaging.validators;

import com.georgen.letterwind.api.annotations.LetterwindMessage;

public interface MessageValidator<T> {
    boolean isValid(@LetterwindMessage T message) throws Exception;
}
