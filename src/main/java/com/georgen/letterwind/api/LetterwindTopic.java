package com.georgen.letterwind.api;

import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.AnnotationGuard;
import com.georgen.letterwind.util.Validator;
import com.georgen.letterwind.util.extractors.MessageTypeExtractor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public LetterwindTopic setConsumers(Class... consumers) {
        if (this.consumers == null) this.consumers = new HashSet<>();
        List<Class> consumersList = Arrays.asList(consumers);
        this.consumers.addAll(consumersList);
        return this;
    }

    public LetterwindTopic setConsumers(Set<Class> consumers) {
        this.consumers = consumers;
        return this;
    }

    public LetterwindTopic addConsumer(Class consumerClass) throws LetterwindException {
        AnnotationGuard.validateConsumer(consumerClass);
        this.consumers.add(consumerClass);
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

    public boolean hasErrorHandler(){ return this.errorHandler != null; }

    public boolean hasSuccessHandler(){ return this.successHandler != null; }

    public Set<Class> getConsumerMessageTypes(){
        Set<Class> messageTypes = new HashSet<>();
        for (Class consumer : consumers){
            Set<Class> consumerMessageTypes = MessageTypeExtractor.extract(consumer);
            if (consumerMessageTypes != null) messageTypes.addAll(consumerMessageTypes);
        }
        return messageTypes;
    }

    public boolean hasRemoteListener(){
        return Validator.isValid(this.remoteHost) && this.remotePort != 0;
    }

    public boolean isValid(){
        return Validator.isValid(this.name) && !this.consumers.isEmpty();
    }

    public static LetterwindTopic build(){ return new LetterwindTopic(); }
}
