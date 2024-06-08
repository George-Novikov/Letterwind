package com.georgen.letterwind.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.message.TestMessage;
import org.junit.jupiter.api.Disabled;

@Disabled
public class TestMessageSerializer implements MessageSerializer<TestMessage> {
    private ObjectMapper mapper;

    public TestMessageSerializer(){
        this.mapper = new ObjectMapper();
        this.mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        this.mapper.findAndRegisterModules();
    }

    @Override
    public String serialize(TestMessage messageObject) throws JsonProcessingException {
        return mapper.writeValueAsString(messageObject);
    }

    @Override
    public @LetterwindMessage TestMessage deserialize(String serializedMessage) throws JsonProcessingException, ClassNotFoundException, LetterwindException {
        return mapper.readValue(serializedMessage, TestMessage.class);
    }
}
