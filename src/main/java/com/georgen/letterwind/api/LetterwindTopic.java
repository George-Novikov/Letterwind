package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;

import java.util.ArrayList;
import java.util.List;

public class LetterwindTopic {
    private String name;
    private int capacity;
    private List<@LetterwindConsumer Object> consumers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Object> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Object> consumers) {
        this.consumers = consumers;
    }
}
