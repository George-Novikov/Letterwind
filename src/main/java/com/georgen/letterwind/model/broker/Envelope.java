package com.georgen.letterwind.model.broker;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.constants.Locality;
import com.georgen.letterwind.model.constants.MessageFlowEvent;
import com.georgen.letterwind.model.transport.TransportEnvelope;
import com.georgen.letterwind.util.Validator;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.georgen.letterwind.model.constants.Locality.*;

public class Envelope<T> {
    private String id;
    private LocalDateTime creationTime;
    private T message;
    private String serializedMessage;
    private String messageTypeName;
    private LetterwindTopic topic;
    private String topicName;
    private String bufferedFileName;
    private Locality locality;
    private Exception exception;

    public Envelope(){}
    public Envelope(T message, LetterwindTopic topic){
        this.id = UUID.randomUUID().toString();
        this.creationTime = LocalDateTime.now();
        this.message = message;
        this.messageTypeName = message.getClass().getSimpleName();
        this.topic = topic;
        this.topicName = topic.getName();
        this.locality = topic.hasRemoteListener() ? REMOTE : LOCAL;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    public LetterwindTopic getTopic() { return topic; }

    public void setTopic(LetterwindTopic topic) {
        this.topic = topic;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getBufferedFileName() {
        return bufferedFileName;
    }

    public void setBufferedFileName(String bufferedFileName) {
        this.bufferedFileName = bufferedFileName;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isLocal(){
        return LOCAL.equals(this.locality);
    }

    public boolean isRemote(){
        return REMOTE.equals(this.locality);
    }

    public boolean hasMessage(){
        return this.message != null;
    }
    public boolean hasTopic(){ return this.topic != null; }
    public boolean hasTopicName(){ return this.topicName != null && !this.topicName.isEmpty(); }
    public boolean hasMessageTypeName(){ return this.messageTypeName != null && !this.messageTypeName.isEmpty(); }
    public boolean hasSerializedMessage(){ return this.serializedMessage != null && !this.serializedMessage.isEmpty(); }
    public boolean hasBufferedResiduals(){ return this.bufferedFileName != null && !this.bufferedFileName.isEmpty(); }

    public boolean isValid(){
        return hasMessage() && this.topic != null && this.topic.isValid() && this.locality != null ;
    }

    public TransportEnvelope toTransportEnvelope(){
        TransportEnvelope envelope = new TransportEnvelope();
        envelope.setId(this.id);
        envelope.setTopicName(this.topicName);
        envelope.setMessageTypeName(this.messageTypeName);
        envelope.setSerializedMessage(this.serializedMessage);
        return envelope;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "id='" + id + '\'' +
                ", creationTime=" + creationTime +
                ", message=" + message +
                ", serializedMessage='" + serializedMessage + '\'' +
                ", messageTypeName='" + messageTypeName + '\'' +
                ", topic=" + topic +
                ", topicName='" + topicName + '\'' +
                ", bufferedFileName='" + bufferedFileName + '\'' +
                ", locality=" + locality +
                ", exception=" + exception +
                '}';
    }
}
