package com.georgen.letterwind.api.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;

public interface MessageSerializer<@LetterwindMessage T> {
    String serialize(@LetterwindMessage T messageObject) throws JsonProcessingException;
    @LetterwindMessage T deserialize(String serializedMessage) throws JsonProcessingException, ClassNotFoundException, LetterwindException;
}
