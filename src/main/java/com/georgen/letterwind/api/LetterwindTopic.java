package com.georgen.letterwind.api;

import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.broker.TopicBuilder;
import com.georgen.letterwind.model.broker.storages.ConsumerMethodStorage;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.Validator;
import com.georgen.letterwind.util.MessageTypeExtractor;

import java.util.*;
import java.util.stream.Collectors;

public class LetterwindTopic {
    private String name;
    private String remoteHost;
    private int remotePort;
    private Set<Class> consumers = new HashSet<>();

    /**
     * A topic-specific error handler.
     * It has medium priority and comes after the @LetterwindMessage error handler but before the one from the LetterwindControls.
     * */
    private ErrorHandler errorHandler;

    /**
     * A topic-specific success handler.
     * It has medium priority and comes after the @LetterwindMessage success handler but before the one from the LetterwindControls.
     * */
    private SuccessHandler successHandler;

    public LetterwindTopic() {}
    public LetterwindTopic(String name) {
        this.name = name;
    }
    public LetterwindTopic(String name, Class... consumers){
        this(name);
        this.consumers = new HashSet<>(Arrays.asList(consumers));
    }

    public String getName() {
        return name;
    }

    public LetterwindTopic setName(String name) {
        this.name = name;
        return this;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public LetterwindTopic setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
        return this;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public LetterwindTopic setRemotePort(int remotePort) {
        this.remotePort = remotePort;
        return this;
    }

    public Set<Class> getConsumers() {
        return consumers;
    }

    public LetterwindTopic setConsumers(Class... consumers) throws Exception {
        for (Class consumerType : consumers){
            addConsumer(consumerType);
        }
        return this;
    }

    public LetterwindTopic setConsumers(Set<Class> consumers) {
        this.consumers = consumers;
        return this;
    }

    public LetterwindTopic addConsumer(Class consumerClass) throws Exception {
        this.consumers.add(consumerClass);
        ConsumerMethodStorage.getInstance().register(consumerClass);
        return this;
    }

    public boolean removeConsumer(Class consumer){
        return this.consumers.remove(consumer);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public LetterwindTopic setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public SuccessHandler getSuccessHandler() {
        return successHandler;
    }

    public LetterwindTopic setSuccessHandler(SuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public boolean hasName(){
        return Validator.isValid(this.name);
    }

    public boolean hasConsumers(){ return this.consumers != null && !this.consumers.isEmpty(); }

    public boolean hasErrorHandler(){ return this.errorHandler != null; }

    public boolean hasSuccessHandler(){ return this.successHandler != null; }

    public Set<Class> getConsumerMessageTypes(){
        return consumers.stream()
                .map(MessageTypeExtractor::extract)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public LetterwindTopic activate() throws Exception {
        if (!hasName()) throw new LetterwindException("The topic name cannot be null or empty.");
        LetterwindControls.getInstance().addTopic(this);
        return this;
    }

    public LetterwindTopic deactivate() throws Exception {
        if (!hasName()) throw new LetterwindException("The topic name cannot be null or empty.");
        LetterwindControls.getInstance().removeTopic(this.name);
        return this;
    }

    public boolean hasRemoteListener(){
        return Validator.isValid(this.remoteHost) && this.remotePort != 0;
    }

    public boolean isValid(){
        return hasName();
    }

    public boolean hasFinalEventHandlers(){
        return this.errorHandler != null || this.successHandler != null;
    }

    public static LetterwindTopic create(){ return new LetterwindTopic(); }

    public static TopicBuilder builder(){ return new TopicBuilder(); }
}
