package com.georgen.letterwind.model.broker;

import java.time.LocalDateTime;

public class MessageError {
    private String topicName;
    private String messageType;
    private String errorMessage;
    private LocalDateTime time;

    public MessageError(Envelope envelope, Exception exception){
        this.topicName = envelope.getTopic().getName();
        this.messageType = envelope.getMessageType().getName();
        this.errorMessage = exception.getMessage();
        this.time = LocalDateTime.now();
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
