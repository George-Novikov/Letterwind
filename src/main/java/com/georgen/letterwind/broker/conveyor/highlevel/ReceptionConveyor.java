package com.georgen.letterwind.broker.conveyor.highlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.*;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.MessageFlowEvent;

public class ReceptionConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        if (envelope.isRemote()){
            setRemoteReceptionConveyor();
        } else {
            setLocalReceptionConveyor();
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void setLocalReceptionConveyor(){
        MessageConveyor<T> retrieving = new QueueRetrievingConveyor<>();
        MessageConveyor<T> deserialization = new DeserializationConveyor<>();
        MessageConveyor<T> consumerInvocation = new ConsumerInvokingConveyor<>();
        MessageConveyor<T> cleanUp = new CleanUpConveyor<>();
        MessageConveyor<T> informing = new InformingConveyor<>(MessageFlowEvent.SUCCESS);

        this.setConveyor(retrieving);
        retrieving.setConveyor(deserialization);
        deserialization.setConveyor(consumerInvocation);
        consumerInvocation.setConveyor(cleanUp);
        cleanUp.setConveyor(informing);
    }

    private void setRemoteReceptionConveyor(){
        /** This might be confusing but for remote reception message persistence is important */
        MessageConveyor<T> queueing = new QueueWritingConveyor<>();
        MessageConveyor<T> retrieving = new QueueRetrievingConveyor<>();
        MessageConveyor<T> deserialization = new DeserializationConveyor<>();
        MessageConveyor<T> consumerInvocation = new ConsumerInvokingConveyor<>();
        MessageConveyor<T> cleanUp = new CleanUpConveyor<>();
        MessageConveyor<T> informing = new InformingConveyor<>(MessageFlowEvent.SUCCESS);

        this.setConveyor(queueing);
        queueing.setConveyor(retrieving);
        retrieving.setConveyor(deserialization);
        deserialization.setConveyor(consumerInvocation);
        consumerInvocation.setConveyor(cleanUp);
        cleanUp.setConveyor(informing);
    }
}
