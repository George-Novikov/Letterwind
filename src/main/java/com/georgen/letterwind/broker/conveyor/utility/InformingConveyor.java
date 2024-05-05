package com.georgen.letterwind.broker.conveyor.utility;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.ConveyorOperation;

public class InformingConveyor<T> extends MessageConveyor<T> {
    private ConveyorOperation operation;

    public InformingConveyor(ConveyorOperation operation){
        this.operation = operation;
    }

    @Override
    public void process(Envelope<T> envelope) throws Exception {

        LetterwindTopic topic = envelope.getTopic();
        MessageFlow.getInstance().inform(topic.getName(), operation);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
