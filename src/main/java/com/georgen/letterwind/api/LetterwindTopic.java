package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;

import java.util.ArrayList;
import java.util.List;

public class LetterwindTopic {
    private String name;
    private Integer concurrencyCapacity;
    private List<@LetterwindConsumer Object> consumers = new ArrayList<>();

    public LetterwindTopic() {}
    public LetterwindTopic(String name) {
        this.name = name;
    }
    public LetterwindTopic(String name, Integer concurrencyCapacity) {
        this.name = name;
        this.concurrencyCapacity = concurrencyCapacity;
    }
    public LetterwindTopic(String name, Integer concurrencyCapacity, @LetterwindConsumer Object consumer) {
        this.name = name;
        this.concurrencyCapacity = concurrencyCapacity;
        this.consumers.add(consumer);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConcurrencyCapacity() {
        return concurrencyCapacity;
    }

    public void setConcurrencyCapacity(Integer concurrencyCapacity) {
        this.concurrencyCapacity = concurrencyCapacity;
    }

    public List<Object> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Object> consumers) {
        this.consumers = consumers;
    }
}
