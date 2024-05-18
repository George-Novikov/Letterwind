package com.georgen.letterwind.broker.conveyor.highlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.*;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;

public class ReceptionConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.isValid()) return;

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
        MessageConveyor<T> consuming = new ConsumerInvokingConveyor<>();
        MessageConveyor<T> informing = new InformingConveyor<>(FlowEvent.CONSUMPTION);

        this.setConveyor(retrieving);
        retrieving.setConveyor(deserialization);
        deserialization.setConveyor(consuming);
        consuming.setConveyor(informing);
    }

    private void setRemoteReceptionConveyor(){
        MessageConveyor<T> queueing = new QueueDispatchConveyor<>(); // This might be confusing but message persistence is important
        MessageConveyor<T> retrieving = new QueueRetrievingConveyor<>();
        MessageConveyor<T> deserialization = new DeserializationConveyor<>();
        MessageConveyor<T> consuming = new ConsumerInvokingConveyor<>();
        MessageConveyor<T> informing = new InformingConveyor<>(FlowEvent.CONSUMPTION);

        this.setConveyor(queueing);
        queueing.setConveyor(retrieving);
        retrieving.setConveyor(deserialization);
        deserialization.setConveyor(consuming);
        consuming.setConveyor(informing);
    }
}
