package com.georgen.letterwind.api;

import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.broker.ordering.MessageOrderManager;
import com.georgen.letterwind.util.Validator;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    private Map<Class, Set<String>> messageTypeTopicsMap = new ConcurrentHashMap();

    /** To quickly find a message type by its simple class name. */
    private Map<String, Class> messageTypes = new ConcurrentHashMap<>();

    /**
     * A set of global error handlers.
     * Each handler can be ordered via the inherited setOrder() method.
     * Regardless of their order, they have the lowest priority and come after the @LetterwindMessage and LetterwindTopic error handlers.
     * */
    private Set<Class<ErrorHandler>> errorHandlers = new HashSet<>();

    /**
     * A set of global success handlers.
     * Each handler can be ordered via the inherited setOrder() method.
     * Regardless of their order, they have the lowest priority and come after the @LetterwindMessage and LetterwindTopic success handlers.
     * */
    private Set<Class<SuccessHandler>> successHandlers = new HashSet<>();

    private LetterwindControls(){}

    public int getSendersLimit() {
        return sendersLimit;
    }

    public LetterwindControls setSendersLimit(int sendersLimit) {
        this.sendersLimit = sendersLimit;
        return this;
    }

    public int getReceiversLimit() {
        return receiversLimit;
    }

    public LetterwindControls setReceiversLimit(int receiversLimit) {
        this.receiversLimit = receiversLimit;
        return this;
    }

    public int getConsumersLimit() {
        return consumersLimit;
    }

    public LetterwindControls setConsumersLimit(int consumersLimit) {
        this.consumersLimit = consumersLimit;
        return this;
    }

    public boolean isServerActive() {
        return isServerActive;
    }

    public LetterwindControls setServerActive(boolean serverActive) {
        isServerActive = serverActive;
        return this;
    }

    public int getServerPort() {
        return serverPort;
    }

    public LetterwindControls setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public LetterwindControls setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
        return this;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public LetterwindControls setRemotePort(int remotePort) {
        this.remotePort = remotePort;
        return this;
    }

    public Map<String, LetterwindTopic> getTopics() {
        return topics;
    }

    public LetterwindControls setTopics(Map<String, LetterwindTopic> topics) {
        this.topics = topics;
        return this;
    }

    public Map<Class, Set<String>> getMessageTypeTopicsMap() {
        return messageTypeTopicsMap;
    }

    public void setMessageTypeTopicsMap(Map<Class, Set<String>> messageTypeTopicsMap) {
        this.messageTypeTopicsMap = messageTypeTopicsMap;
    }

    public LetterwindControls registerTopic(LetterwindTopic topic) throws Exception {
        topics.put(topic.getName(), topic);
        addToMessageTypes(topic);
        return this;
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
        Set<String> topicNames = messageTypeTopicsMap.get(messageType);
        if (topicNames == null) return null;
        for (String topicName : topicNames){
            LetterwindTopic topic = this.topics.get(topicName);
            if (topic != null) responseTopics.add(topic);
        }
        return responseTopics;
    }

    public Class getMessageTypeBySimpleName(String messageTypeSimpleName){
        if (!Validator.isValid(messageTypeSimpleName)) return null;
        return this.messageTypes.get(messageTypeSimpleName);
    }

    public boolean hasRemoteListener(){
        return Validator.isValid(this.remoteHost) && this.remotePort != 0;
    }

    private void addToMessageTypes(LetterwindTopic topic) throws IOException {
        Set<Class> messageTypes = topic.getConsumerMessageTypes();

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.messageTypeTopicsMap.get(messageType);
            if (topicNames == null) topicNames = new HashSet<>();
            topicNames.add(topic.getName());
            this.messageTypeTopicsMap.put(messageType, topicNames);
            this.messageTypes.put(messageType.getSimpleName(), messageType);
            MessageOrderManager.initForAllTopics(messageType, topicNames);
        }
    }

    private void deleteFromMessageTypes(LetterwindTopic topic){
        Set<Class> messageTypes = topic.getConsumerMessageTypes();

        for (Class messageType : messageTypes){
            Set<String> topicNames = this.messageTypeTopicsMap.get(messageType);
            if (topicNames != null) topicNames.remove(topic.getName());
            // For safety reasons nothing is removed from messageTypes map since @LetterwindMessage can be reused between consumers
        }
    }

    private static class InstanceHolder {
        private static final LetterwindControls INSTANCE = new LetterwindControls();
    }

    public static LetterwindControls getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
