package com.georgen.letterwind.broker.conveyor.highlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.*;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;

public class ReprocessingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        MessageConveyor<T> retrieving = new QueueRetrievingConveyor<>();
        MessageConveyor<T> deserialization = new DeserializationConveyor<>();
        MessageConveyor<T> consumerInvocation = new ConsumerInvokingConveyor<>();
        MessageConveyor<T> cleanUp = new CleanUpConveyor<>();
        MessageConveyor<T> informing = new InformingConveyor<>(FlowEvent.SUCCESS);

        this.setConveyor(retrieving);
        retrieving.setConveyor(deserialization);
        deserialization.setConveyor(consumerInvocation);
        consumerInvocation.setConveyor(cleanUp);
        cleanUp.setConveyor(informing);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
