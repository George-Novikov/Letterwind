package com.georgen.letterwind.model;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;


public class SampleConsumer {
    @LetterwindConsumer(concurrentInstances = 3)
    public void receive(SampleMessage message){
        System.out.println("Message received: " + message.getValue());
    }

    @LetterwindConsumer(concurrentInstances = 3)
    public void receiveString(String message){
        System.out.println("Message received: " + message);
    }
}
