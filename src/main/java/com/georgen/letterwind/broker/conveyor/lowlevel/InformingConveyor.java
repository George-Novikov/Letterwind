package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.MessageFlowEvent;

public class InformingConveyor<T> extends MessageConveyor<T> {
    private MessageFlowEvent operation;

    public InformingConveyor(MessageFlowEvent operation){
        this.operation = operation;
    }

    @Override
    public void process(Envelope<T> envelope) throws Exception {

        MessageFlow.push(envelope, operation);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
