package com.georgen.letterwind.api;

import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.transport.RemoteServerConfig;
import com.georgen.letterwind.util.AnnotationGuard;
import com.georgen.letterwind.util.Validator;
import com.georgen.letterwind.util.extractors.MessageTypeExtractor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LetterwindTopic {
    private String name;
    private Integer concurrencyLimit;
    private RemoteServerConfig remoteConfig;
    private Set<Class> consumers = new HashSet<>();

    public LetterwindTopic() {}
    public LetterwindTopic(String name) {
        this.name = name;
    }
    public LetterwindTopic(String name, Class... consumers){
        this(name);
        this.consumers = new HashSet<>(Arrays.asList(consumers));
    }
    public LetterwindTopic(String name,
                           Integer concurrencyLimit,
                           Class... consumers) {
        this(name, consumers);
        this.concurrencyLimit = concurrencyLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConcurrencyLimit() {
        return concurrencyLimit;
    }

    public void setConcurrencyLimit(Integer concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }

    public RemoteServerConfig getRemoteConfig() {
        return remoteConfig;
    }

    public void setRemoteConfig(RemoteServerConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
    }

    public Set<Class> getConsumers() {
        return consumers;
    }

    public void setConsumers(Set<Class> consumers) {
        this.consumers = consumers;
    }

    public void addConsumer(Class consumerClass) throws LetterwindException {
        AnnotationGuard.validateConsumer(consumerClass);
        this.consumers.add(consumerClass);
    }

    public boolean removeConsumer(Class consumer){
        return this.consumers.remove(consumer);
    }

    public Set<Class> getConsumerMessageTypes(){
        Set<Class> messageTypes = new HashSet<>();
        for (Class consumer : consumers){
            Set<Class> consumerMessageTypes = MessageTypeExtractor.extract(consumer);
            if (consumerMessageTypes != null) messageTypes.addAll(consumerMessageTypes);
        }
        return messageTypes;
    }

    public boolean hasRemoteConfig(){
        return this.remoteConfig != null && this.remoteConfig.isValid();
    }

    public boolean isValid(){
        return Validator.isValid(this.name) && !this.consumers.isEmpty();
    }

    public boolean hasConcurrencyLimit(){
        return this.concurrencyLimit != null && this.concurrencyLimit > 0;
    }
}
