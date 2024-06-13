package com.georgen.letterwind.model.transport;

import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.Locality;

import java.time.LocalDateTime;

public class TransportEnvelope {
    private String id;
    private String topicName;
    private String messageTypeName;
    private String serializedMessage;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public String getSerializedMessage() {
        return serializedMessage;
    }

    public void setSerializedMessage(String serializedMessage) {
        this.serializedMessage = serializedMessage;
    }

    public boolean isValid(){
        return this.topicName != null && this.messageTypeName != null && this.serializedMessage != null;
    }

    public Envelope toRegularEnvelope(Locality locality){
        Envelope envelope = new Envelope();
        envelope.setId(this.id);
        envelope.setCreationTime(LocalDateTime.now());
        envelope.setTopicName(this.topicName);
        envelope.setMessageTypeName(this.messageTypeName);
        envelope.setSerializedMessage(this.serializedMessage);
        envelope.setLocality(locality);
        return envelope;
    }

    @Override
    public String toString() {
        return "TransportEnvelope{" +
                "id='" + id + '\'' +
                ", topicName='" + topicName + '\'' +
                ", messageTypeName='" + messageTypeName + '\'' +
                ", serializedMessage='" + serializedMessage + '\'' +
                '}';
    }
}
