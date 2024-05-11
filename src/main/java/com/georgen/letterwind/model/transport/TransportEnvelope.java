package com.georgen.letterwind.model.transport;

public class TransportEnvelope {
    private String topicName;
    private String messageType;
    private String serializedMessage;

    public TransportEnvelope() {}

    public TransportEnvelope(String topicName, String messageType, String serializedMessage) {
        this.topicName = topicName;
        this.messageType = messageType;
        this.serializedMessage = serializedMessage;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSerializedMessage() {
        return serializedMessage;
    }

    public void setSerializedMessage(String serializedMessage) {
        this.serializedMessage = serializedMessage;
    }
}
