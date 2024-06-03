package com.georgen.letterwind.model.broker.storages;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.broker.ordering.MessageOrderManager;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.broker.validators.MessageValidator;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.MessageFlowEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MessageInfoStorage {
    private Map<String, Class> messageTypes;
    private Map<Class, Set<String>> topicsByMessageType = new ConcurrentHashMap();
    private ConcurrentHashMap<Class, MessageSerializer> serializers;
    private ConcurrentHashMap<Class, MessageValidator> validators;
    private ConcurrentHashMap<Class, SuccessHandler> successHandlers;
    private ConcurrentHashMap<Class, ErrorHandler> errorHandlers;

    public boolean hasFinalEventHandlers(Envelope envelope, MessageFlowEvent event){
        Class messageType = messageTypes.get(envelope.getMessageTypeName());
        if (messageType == null) return false;

        switch (event){
            case SUCCESS:
                return successHandlers.get(messageType) != null;
            case ERROR:
                return errorHandlers.get(messageType) != null;
            default:
                return false;
        }
    }

    private MessageInfoStorage(){
        this.messageTypes = new ConcurrentHashMap<>();
        this.serializers = new ConcurrentHashMap<>();
        this.validators = new ConcurrentHashMap<>();
        this.successHandlers = new ConcurrentHashMap<>();
        this.errorHandlers = new ConcurrentHashMap<>();
    }

    public void addToMessageTypes(LetterwindTopic topic) throws IOException {
        Set<Class> messageTypes = topic.getConsumerMessageTypes();

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.topicsByMessageType.get(messageType);
            if (topicNames == null) topicNames = new HashSet<>();
            topicNames.add(topic.getName());
            this.topicsByMessageType.put(messageType, topicNames);
            this.messageTypes.put(messageType.getSimpleName(), messageType);
            MessageOrderManager.initForAllTopics(messageType, topicNames);
        }
    }

    private static class InstanceHolder {
        private static final MessageInfoStorage INSTANCE = new MessageInfoStorage();
    }

    public static MessageInfoStorage getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
