package com.georgen.letterwind.messaging.conveyor.utility;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.conveyor.MessageFlow;
import com.georgen.letterwind.model.constants.Operation;

public class InformingConveyor extends MessageConveyor<String> {
    private Operation operation;

    public InformingConveyor(Operation operation){
        this.operation = operation;
    }

    @Override
    public void process(String message, LetterwindTopic topic) throws Exception {

        MessageFlow.getInstance().inform(topic.getName(), operation);

        if (hasConveyor()){
            this.getConveyor().process(message, topic);
        }
    }
}
