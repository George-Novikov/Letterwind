package com.georgen.letterwind.model.consumers;

import com.georgen.letterwind.MixedDispatchTest;
import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.util.ResultsStorage;
import com.georgen.letterwind.model.message.TestMessage;
import org.junit.jupiter.api.Disabled;

@Disabled
public class MixedTypeConsumer {
    @LetterwindConsumer
    public void receive(TestMessage message){
        ResultsStorage.getForClass(message.getSourceClass()).add(message);
    }

    @LetterwindConsumer
    public void receive(String message){
        ResultsStorage.getForClass(MixedDispatchTest.class).add(message);
    }
}
