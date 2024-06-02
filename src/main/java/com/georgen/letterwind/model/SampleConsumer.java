package com.georgen.letterwind.model;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;


public class SampleConsumer {
    @LetterwindConsumer
    public void receive(SampleMessage message){
        System.out.println("SampleConsumer.receive(): " + message);
    }

    @LetterwindConsumer
    public void receiveString(String message){
        System.out.println("SampleConsumer.receiveString(): " + message);
    }
}
