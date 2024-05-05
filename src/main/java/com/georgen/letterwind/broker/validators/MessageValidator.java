package com.georgen.letterwind.broker.validators;

import com.georgen.letterwind.api.annotations.LetterwindMessage;

public interface MessageValidator<T> {
    boolean isValid(@LetterwindMessage T message) throws Exception;
}
