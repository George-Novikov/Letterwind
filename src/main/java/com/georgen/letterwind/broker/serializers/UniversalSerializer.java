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
    private Class<T> messageType;
    private ObjectMapper objectMapper;

    public UniversalSerializer(Class<T> messageType){
        this.messageType = messageType;
        initObjectMapper();
    }

    public UniversalSerializer(String messageTypeName) throws LetterwindException {
        initMessageType(messageTypeName);
        initObjectMapper();
    }


    @Override
    public String serialize(@LetterwindMessage T messageObject) throws JsonProcessingException {
        return objectMapper.writeValueAsString(messageObject);
    }

    @Override
    public @LetterwindMessage T deserialize(String serializedMessage) throws JsonProcessingException {
        return objectMapper.readValue(serializedMessage, messageType);
    }

    private void initObjectMapper(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        this.objectMapper.findAndRegisterModules();
    }

    private void initMessageType(String messageTypeName) throws LetterwindException {
        Class<T> messageType = LetterwindControls.getInstance().getMessageTypeBySimpleName(messageTypeName);
        if (messageType == null) throw new LetterwindException(String.format("There is no registered consumer with the %s message type.", messageTypeName));
        this.messageType = messageType;
    }

}
