package com.georgen.letterwind.broker.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UniversalSerializer<@LetterwindMessage T> implements MessageSerializer<@LetterwindMessage T>{
    private ObjectMapper objectMapper;

    public UniversalSerializer(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        this.objectMapper.findAndRegisterModules();
    }

    @Override
    public String serialize(@LetterwindMessage T messageObject) throws JsonProcessingException {
        return objectMapper.writeValueAsString(messageObject);
    }

    @Override
    public @LetterwindMessage T deserialize(String serializedMessage) throws JsonProcessingException, LetterwindException {
        LinkedHashMap objectMap = objectMapper.readValue(serializedMessage, LinkedHashMap.class);
        Set<Map.Entry<String, String>> entrySet = objectMap.entrySet();
        if (entrySet.isEmpty()) throw new LetterwindException("An empty message could not be deserialized.");

        String simpleClassName = entrySet.iterator().next().getKey();
        Class<T> messageType = LetterwindControls.getInstance().getMessageTypeBySimpleName(simpleClassName);
        if (messageType == null) throw new LetterwindException(String.format("There is no registered consumer with the %s message type.", simpleClassName));

        return objectMapper.readValue(serializedMessage, messageType);
    }
}
