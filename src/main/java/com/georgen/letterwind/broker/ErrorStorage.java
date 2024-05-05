package com.georgen.letterwind.broker;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.broker.MessageError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ErrorStorage {
    private static final Map<String, List<MessageError>> ERRORS = new ConcurrentHashMap<>();

    public void register(Envelope envelope, Exception e){
        String topicName = envelope.getTopicName();

        List<MessageError> errors = ERRORS.get(topicName);
        if (errors == null) errors = new ArrayList<>();
        errors.add(new MessageError(envelope, e));

        ERRORS.put(topicName, errors);
    }

    public List<MessageError> getTopicErrors(String topicName){
        return ERRORS.get(topicName);
    }
}
