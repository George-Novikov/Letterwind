package com.georgen.letterwind.api;

import com.georgen.letterwind.tools.extractors.MessageTypeExtractor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private Map<Class, Set<String>> messageTypes = new ConcurrentHashMap();

    private LetterwindControls(){}

    public void registerTopic(LetterwindTopic topic){
        topics.put(topic.getName(), topic);
        addToMessageTypes(topic);
    }

    private void addToMessageTypes(LetterwindTopic topic){
        Set<Class> messageTypes = new HashSet<>();

        for (Object consumer : topic.getConsumers()){
            Set<Class> consumerMessageTypes = MessageTypeExtractor.extract(consumer);
            messageTypes.addAll(consumerMessageTypes);
        }

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.messageTypes.get(messageType);
            if (topicNames == null) topicNames = new HashSet<>();
            topicNames.add(topic.getName());
            this.messageTypes.put(messageType, topicNames);
        }
    }

    public LetterwindTopic getTopic(String topicName){
        return topics.get(topicName);
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

    private void deleteFromMessageTypes(LetterwindTopic topic){
        Set<Class> messageTypes = new HashSet<>();

        for (Object consumer : topic.getConsumers()){

        }
    }

    private class InstanceHolder {
        private static final LetterwindControls INSTANCE = new LetterwindControls();
    }

    public static LetterwindControls getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
