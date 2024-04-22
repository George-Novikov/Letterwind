package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.tools.AnnotationGuard;
import com.georgen.letterwind.tools.Validator;

import java.util.HashSet;
import java.util.Set;

public class LetterwindTopic {
    private String name;
    private Integer concurrencyLimit;
    private Set<@LetterwindConsumer Class> consumers = new HashSet<>();

    public LetterwindTopic() {}
    public LetterwindTopic(String name) {
        this.name = name;
    }
    public LetterwindTopic(String name,
                           Set<Class> consumers){
        this(name);
        this.consumers = consumers;
    }
    public LetterwindTopic(String name,
                           Integer concurrencyLimit) {
        this(name);
        this.concurrencyLimit = concurrencyLimit;
    }
    public LetterwindTopic(String name,
                           Integer concurrencyLimit,
                           Set<Class> consumers){
        this(name, concurrencyLimit);
        this.consumers = consumers;
    }
    public LetterwindTopic(String name,
                           Integer concurrencyLimit,
                           @LetterwindConsumer Class consumer) {
        this(name, concurrencyLimit);
        this.consumers.add(consumer);
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

    public Set<@LetterwindConsumer Class> getConsumers() {
        return consumers;
    }

    public void setConsumers(Set<@LetterwindConsumer Class> consumers) {
        this.consumers = consumers;
    }

    public void addConsumer(@LetterwindConsumer Class consumerClass) throws LetterwindException {
        AnnotationGuard.validateConsumer(consumerClass);
        this.consumers.add(consumerClass);
    }

    public boolean removeConsumer(@LetterwindConsumer Class consumer){
        return this.consumers.remove(consumer);
    }

    public boolean isValid(){
        return Validator.isValid(this.name) && !this.consumers.isEmpty();
    }

    public boolean hasConcurrencyLimit(){
        return this.concurrencyLimit != null && this.concurrencyLimit > 0;
    }
}
