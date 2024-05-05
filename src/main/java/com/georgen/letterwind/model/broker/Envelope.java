package com.georgen.letterwind.model.broker;

import com.georgen.letterwind.api.LetterwindTopic;

import java.time.LocalDateTime;
import java.util.UUID;

public class Envelope<T> {
    private String id;
    private LocalDateTime creationDate;
    private T message;
    private String serializedMessage;
    private Class messageType;
    private LetterwindTopic topic;

    public Envelope(T message, LetterwindTopic topic){
        this.id = UUID.randomUUID().toString();
        this.creationDate = LocalDateTime.now();
        this.message = message;
        this.messageType = message.getClass();
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public String getSerializedMessage() {
        return serializedMessage;
    }

    public void setSerializedMessage(String serializedMessage) {
        this.serializedMessage = serializedMessage;
    }

    public Class getMessageType() {
        return messageType;
    }

    public void setMessageType(Class messageType) {
        this.messageType = messageType;
    }

    public LetterwindTopic getTopic() {
        return topic;
    }

    public void setTopic(LetterwindTopic topic) {
        this.topic = topic;
    }

    public String getTopicName(){
        if (this.topic == null) return null;
        return this.topic.getName();
    }

    public boolean hasMessage(){
        return this.message != null;
    }

    public boolean isSerialized(){
        return this.serializedMessage != null && !this.serializedMessage.isEmpty();
    }
}
