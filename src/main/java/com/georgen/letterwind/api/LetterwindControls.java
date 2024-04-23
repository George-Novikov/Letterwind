package com.georgen.letterwind.api;

import com.georgen.letterwind.tools.extractors.MessageTypeExtractor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Letterwind main configuration class
 */
public class LetterwindControls {
    /** Regulates whether these controls should be saved and then reloaded on startup
     * If set to false, Letterwind will require reconfiguration every time it starts */
    private boolean isPersistent = true;

    /** Represents the total number @LetterwindConsumer classes allowed to operate simultaneously */
    private Integer concurrencyLimit;

    /** Registered topics. Unregistered ones will not participate in messaging. */
    private Map<String, LetterwindTopic> topics = new ConcurrentHashMap<>();

    /** Message types mapped to the names of all registered topics */
    private Map<Class, Set<String>> messageTypeMap = new ConcurrentHashMap();

    private LetterwindControls(){}

    public void registerTopic(LetterwindTopic topic){
        topics.put(topic.getName(), topic);
        addToMessageTypes(topic);
    }

    public boolean unregisterTopic(String topicName){
        LetterwindTopic topic = topics.get(topicName);
        if (topic != null){
            topics.remove(topicName);
            deleteFromMessageTypes(topic);
            return true;
        } else {
            return false;
        }
    }

    public LetterwindTopic getTopic(String topicName){
        return topics.get(topicName);
    }

    public Set<LetterwindTopic> listTopicsWithMessageType(Class messageType){
        Set<LetterwindTopic> responseTopics = new HashSet<>();
        Set<String> topicNames = messageTypeMap.get(messageType);
        if (topicNames == null) return null;
        for (String topicName : topicNames){
            LetterwindTopic topic = this.topics.get(topicName);
            if (topic != null) responseTopics.add(topic);
        }
        return responseTopics;
    }

    private void addToMessageTypes(LetterwindTopic topic){
        Set<Class> messageTypes = topic.getConsumerMessageTypes();

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.messageTypeMap.get(messageType);
            if (topicNames == null) topicNames = new HashSet<>();
            topicNames.add(topic.getName());
            this.messageTypeMap.put(messageType, topicNames);
        }
    }

    private void deleteFromMessageTypes(LetterwindTopic topic){
        Set<Class> messageTypes = topic.getConsumerMessageTypes();

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.messageTypeMap.get(messageType);
            if (topicNames != null) topicNames.remove(topic.getName());
        }
    }

    private class InstanceHolder {
        private static final LetterwindControls INSTANCE = new LetterwindControls();
    }

    public static LetterwindControls getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
