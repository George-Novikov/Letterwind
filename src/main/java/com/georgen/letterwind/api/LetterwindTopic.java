package com.georgen.letterwind.api;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;

import java.util.ArrayList;
import java.util.List;

public class LetterwindTopic {
    private String name;
    private List<@LetterwindConsumer Object> consumers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Object> consumers) {
        this.consumers = consumers;
    }
}
