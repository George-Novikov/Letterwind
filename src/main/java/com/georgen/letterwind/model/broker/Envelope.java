package com.georgen.letterwind.model.broker;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.constants.Locality;
import com.georgen.letterwind.model.transport.TransportEnvelope;

import java.time.LocalDateTime;

import static com.georgen.letterwind.model.constants.Locality.*;

public class Envelope<T> {
    private LocalDateTime creationTime;
    private T message;
    private String serializedMessage;
    private String messageTypeName;
    private LetterwindTopic topic;
    private String topicName;
    private Locality locality;

    public Envelope(){}
    public Envelope(T message, LetterwindTopic topic){
        this.creationTime = LocalDateTime.now();
        this.message = message;
        this.messageTypeName = message.getClass().getSimpleName();
        this.topic = topic;
        this.topicName = topic.getName();
        this.locality = topic.hasRemoteListener() ? REMOTE : LOCAL;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
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

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public LetterwindTopic getTopic() {
        return topic;
    }

    public void setTopic(LetterwindTopic topic) {
        this.topic = topic;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public boolean isLocal(){
        return LOCAL.equals(this.locality);
    }

    public boolean isRemote(){
        return REMOTE.equals(this.locality);
    }

    public Class getMessageType() {
        if (this.message == null) return null;
        return message.getClass();
    }

    public boolean hasMessage(){
        return this.message != null;
    }

    public boolean isValid(){
        return hasMessage() && this.topic != null && this.topic.isValid() && this.locality != null ;
    }

    public boolean isSerialized(){
        return this.serializedMessage != null && !this.serializedMessage.isEmpty();
    }

    public TransportEnvelope toTransportEnvelope(){
        TransportEnvelope envelope = new TransportEnvelope();
        envelope.setTopicName(this.topicName);
        envelope.setMessageTypeName(this.messageTypeName);
        envelope.setSerializedMessage(this.serializedMessage);
        return envelope;
    }
}
