package com.georgen.letterwind.api;

import com.georgen.letterwind.transport.TransportLayer;
import com.georgen.letterwind.util.Validator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Letterwind main configuration class
 */
public class LetterwindControls {

    /** Determines how many threads can simultaneously perform the sending process. */
    private int sendersLimit;

    /** Determines how many threads can simultaneously execute the sereceivingnding process. */
    private int receiversLimit;

    /** Determines the total number of @LetterwindConsumer classes allowed to operate simultaneously. */
    private int consumersLimit;

    /**
     * Global settings for listening messages from other Letterwind instances.
     * */
    private boolean isServerActive;
    private int serverPort;

    /**
     * Global remote access config — when the host and port set, all messages will be sent to the remote Letterwind instance.
     * Only individual config within each LetterwindTopic can override this.
     * */
    private String remoteHost;
    private int remotePort;


    /** Registered topics. Unregistered ones will not participate in messaging. */
    private Map<String, LetterwindTopic> topics = new ConcurrentHashMap<>();

    /** Message types mapped to the names of all registered topics */
    private Map<Class, Set<String>> messageTypeMap = new ConcurrentHashMap();

    private LetterwindControls(){}

    public int getSendersLimit() {
        return sendersLimit;
    }

    public void setSendersLimit(int sendersLimit) {
        this.sendersLimit = sendersLimit;
    }

    public int getReceiversLimit() {
        return receiversLimit;
    }

    public void setReceiversLimit(int receiversLimit) {
        this.receiversLimit = receiversLimit;
    }

    public int getConsumersLimit() {
        return consumersLimit;
    }

    public void setConsumersLimit(int consumersLimit) {
        this.consumersLimit = consumersLimit;
    }

    public boolean isServerActive() {
        return isServerActive;
    }

    public void setServerActive(boolean serverActive) {
        isServerActive = serverActive;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public Map<String, LetterwindTopic> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, LetterwindTopic> topics) {
        this.topics = topics;
    }

    public Map<Class, Set<String>> getMessageTypeMap() {
        return messageTypeMap;
    }

    public void setMessageTypeMap(Map<Class, Set<String>> messageTypeMap) {
        this.messageTypeMap = messageTypeMap;
    }

    public void registerTopic(LetterwindTopic topic) throws Exception {
        topics.put(topic.getName(), topic);
        addToMessageTypes(topic);
    }

    public boolean unregisterTopic(String topicName) throws Exception {
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

    public Set<LetterwindTopic> getAllTopicsWithMessageType(Class messageType){
        Set<LetterwindTopic> responseTopics = new HashSet<>();
        Set<String> topicNames = messageTypeMap.get(messageType);
        if (topicNames == null) return null;
        for (String topicName : topicNames){
            LetterwindTopic topic = this.topics.get(topicName);
            if (topic != null) responseTopics.add(topic);
        }
        return responseTopics;
    }

    public Class getMessageTypeBySimpleName(String messageTypeSimpleName){
        if (!Validator.isValid(messageTypeSimpleName)) return null;
        return this.messageTypeMap.keySet()
                .stream()
                .filter(javaClass -> messageTypeSimpleName.equals(javaClass.getSimpleName()))
                .findFirst()
                .orElse(null);
    }

    public boolean hasRemoteListener(){
        return Validator.isValid(this.remoteHost) && this.remotePort != 0;
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

    private static class InstanceHolder {
        private static final LetterwindControls INSTANCE = new LetterwindControls();
    }

    public static LetterwindControls getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
