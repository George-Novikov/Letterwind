package com.georgen.letterwind.model.consumers;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.model.message.DefaultAnnotationMessage;
import com.georgen.letterwind.util.ResultsStorage;

public class DefaultAnnotationConsumer {
    @LetterwindConsumer
    public void receive(DefaultAnnotationMessage message){
        ResultsStorage.getForClass(message.getSourceClass()).add(message);
    }
}
