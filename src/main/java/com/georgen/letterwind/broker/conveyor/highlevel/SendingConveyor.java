package com.georgen.letterwind.broker.conveyor.highlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.*;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;

public class SendingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.isValid()) return;

        MessageConveyor<T> validation = new ValidationConveyor<>();
        MessageConveyor<T> serialization = new SerializationConveyor();
        MessageConveyor<T> dispatch = envelope.isRemote() ? new QueueDispatchConveyor() : new RemoteDispatchConveyor<>();;
        MessageConveyor<T> informing = new InformingConveyor(FlowEvent.RECEPTION);

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(dispatch);
        dispatch.setConveyor(informing);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
