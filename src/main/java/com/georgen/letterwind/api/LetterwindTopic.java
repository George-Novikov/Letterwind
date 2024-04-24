package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.tools.AnnotationGuard;
import com.georgen.letterwind.tools.Validator;
import com.georgen.letterwind.tools.extractors.MessageTypeExtractor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LetterwindTopic {
    private String name;
    private Integer concurrencyLimit;
    private Set<@LetterwindConsumer Class> consumers = new HashSet<>();

    public LetterwindTopic() {}
    public LetterwindTopic(String name) {
        this.name = name;
    }
    public LetterwindTopic(String name, Class... consumers){
        this(name);
        this.consumers = new HashSet<>(List.of(consumers));
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


    public void addConsumer(@LetterwindConsumer Class consumerClass) throws LetterwindException {
        AnnotationGuard.validateConsumer(consumerClass);
        this.consumers.add(consumerClass);
    }

    public boolean removeConsumer(@LetterwindConsumer Class consumer){
        return this.consumers.remove(consumer);
    }

    public Set<Class> getConsumerMessageTypes(){
        Set<Class> messageTypes = new HashSet<>();
        for (Object consumer : consumers){
            Set<Class> consumerMessageTypes = MessageTypeExtractor.extract(consumer);
            if (consumerMessageTypes != null) messageTypes.addAll(consumerMessageTypes);
        }
        return messageTypes;
    }

    public boolean isValid(){
        return Validator.isValid(this.name) && !this.consumers.isEmpty();
    }

    public boolean hasConcurrencyLimit(){
        return this.concurrencyLimit != null && this.concurrencyLimit > 0;
    }
}
