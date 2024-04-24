package com.georgen.letterwind.messaging.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UniversalSerializer<@LetterwindMessage T> implements MessageSerializer<@LetterwindMessage T>{
    private ObjectMapper objectMapper;

    public UniversalSerializer(){
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String serialize(@LetterwindMessage T messageObject) throws JsonProcessingException {
        return objectMapper.writeValueAsString(messageObject);
    }

    @Override
    public @LetterwindMessage T deserialize(String serializedMessage) throws JsonProcessingException, ClassNotFoundException, LetterwindException {
        LinkedHashMap objectMap = objectMapper.readValue(serializedMessage, LinkedHashMap.class);
        Set<Map.Entry<String, String>> entrySet = objectMap.entrySet();
        if (entrySet.isEmpty()) throw new LetterwindException("An empty message could not be deserialized.");

        String className = entrySet.iterator().next().getKey();
        Class javaClass = Class.forName(className);
        return objectMapper.readValue(serializedMessage, (Class<T>) javaClass);
    }
}
