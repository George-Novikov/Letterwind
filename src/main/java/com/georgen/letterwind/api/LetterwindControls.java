package com.georgen.letterwind.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.letterwind.io.FileIOManager;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.network.RemoteConfig;
import com.georgen.letterwind.settings.Configuration;
import com.georgen.letterwind.tools.Serializer;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Letterwind main configuration class
 */
public class LetterwindControls {
    private File controlFile;
    /** Regulates whether these controls should be saved and then reloaded on startup
     * If set to false, Letterwind will require reconfiguration every time it starts */
    private boolean isPersistent = true;

    /** Represents the total number of @LetterwindConsumer classes allowed to operate simultaneously */
    private Integer concurrencyLimit;

    private RemoteConfig remoteConfig;

    /** Registered topics. Unregistered ones will not participate in messaging. */
    private Map<String, LetterwindTopic> topics = new ConcurrentHashMap<>();

    /** Message types mapped to the names of all registered topics */
    private Map<Class, Set<String>> messageTypeMap = new ConcurrentHashMap();

    private LetterwindControls(){}

    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean persistent) {
        isPersistent = persistent;
    }

    public Integer getConcurrencyLimit() {
        return concurrencyLimit;
    }

    public void setConcurrencyLimit(Integer concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }

    public RemoteConfig getRemoteConfig() {
        return remoteConfig;
    }

    public void setRemoteConfig(RemoteConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
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
        if (isPersistent) save();
    }

    public boolean unregisterTopic(String topicName) throws Exception {
        LetterwindTopic topic = topics.get(topicName);
        if (topic != null){
            topics.remove(topicName);
            deleteFromMessageTypes(topic);
            if (isPersistent) save();
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

    public void save() throws Exception {
        if (this.controlFile == null) this.controlFile = Configuration.getInstance().getControlFile();
        if (this.controlFile == null) throw new LetterwindException("The control file is null.");

        synchronized (this){
            String controlsJson = Serializer.toJson(this);
            FileIOManager.write(this.controlFile, controlsJson);
        }
    }

    public boolean load() throws Exception {
        if (this.controlFile == null) this.controlFile = Configuration.getInstance().getControlFile();
        if (this.controlFile == null) throw new LetterwindException("The control file is null.");

        synchronized (this){
            try {
                String controlsJson = FileIOManager.read(this.controlFile);
                LetterwindControls loadedControls = Serializer.deserialize(controlsJson, LetterwindControls.class);
                this.isPersistent = loadedControls.isPersistent();
                this.concurrencyLimit = loadedControls.getConcurrencyLimit();
                this.remoteConfig = loadedControls.getRemoteConfig();
                this.topics = loadedControls.getTopics();
                this.messageTypeMap = loadedControls.getMessageTypeMap();
                return true;
            } catch (Exception e){
                throw new LetterwindException("The contents of the control file might be corrupted.");
            }
        }
    }

    public boolean hasRemoteConfig(){
        return this.remoteConfig != null && this.remoteConfig.isValid();
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
