package com.georgen.letterwind.model;

import com.georgen.letterwind.api.annotations.ConsumingMethod;
import com.georgen.letterwind.api.annotations.LetterwindConsumer;

@LetterwindConsumer(concurrentInstances = 3)
public class SampleConsumer {
    @ConsumingMethod
    public void receive(SampleMessage message){
        System.out.println("Message received: " + message.getValue());
    }
}
