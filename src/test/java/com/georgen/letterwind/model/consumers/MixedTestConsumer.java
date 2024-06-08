package com.georgen.letterwind.model.consumers;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.util.ResultsStorage;
import com.georgen.letterwind.model.message.TestMessage;
import org.junit.jupiter.api.Disabled;

@Disabled
public class MixedTestConsumer {
    @LetterwindConsumer
    public void receive(TestMessage message){
        ResultsStorage.getInstance().add(message);
    }

    @LetterwindConsumer
    public void receive(String message){
        ResultsStorage.getInstance().add(message);
    }
}
