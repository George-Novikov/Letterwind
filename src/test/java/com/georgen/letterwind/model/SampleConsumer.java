package com.georgen.letterwind.model;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import org.junit.jupiter.api.Disabled;

import java.util.List;

@Disabled
public class SampleConsumer {
    @LetterwindConsumer
    public void receive(SampleMessage message){

    }
}
