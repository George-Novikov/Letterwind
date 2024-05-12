package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;

public class InformingConveyor<T> extends MessageConveyor<T> {
    private FlowEvent operation;

    public InformingConveyor(FlowEvent operation){
        this.operation = operation;
    }

    @Override
    public void process(Envelope<T> envelope) throws Exception {

        MessageFlow.inform(envelope, operation);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
