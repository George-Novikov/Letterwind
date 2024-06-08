package com.georgen.letterwind.model.consumers;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.util.ResultsStorage;
import org.junit.jupiter.api.Disabled;

@Disabled
public class StringTestConsumer {
    @LetterwindConsumer
    public void receive(String message){
        ResultsStorage.getInstance().add(message);
    }
}
