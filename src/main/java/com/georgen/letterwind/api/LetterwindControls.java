package com.georgen.letterwind.api;

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
    private Integer concurrencyCapacity;

    /** Registered topics. Unregistered ones will not participate in messaging. */
    private Map<String, LetterwindTopic> topics = new ConcurrentHashMap<>();

    /** Message types mapped to the names of all registered topics */
    private Map<Class, Set<String>> messageTypes = new ConcurrentHashMap();

    private LetterwindControls(){}

    public boolean registerTopic(LetterwindTopic topic){

    }

    private void registerTopicTypes(){

    }

    public boolean registerType(){

    }

    public LetterwindTopic getTopic(String topicName){
        return topics.get(topicName);
    }

    public boolean deleteTopic(){

    }

    private class InstanceHolder {
        private static final LetterwindControls INSTANCE = new LetterwindControls();
    }

    public static LetterwindControls getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
