package com.georgen.letterwind.broker.conveyor.highlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.InformingConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.QueueingConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.SerializationConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.ValidationConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;

public class SendingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.isValid()) return;

        if (envelope.isRemote()){
            setRemoteConveyor(envelope);
        } else {
            setLocalConveyor(envelope);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    public void setLocalConveyor(Envelope<T> envelope) throws Exception {
        MessageConveyor<T> validation = new ValidationConveyor<>();
        MessageConveyor<T> serialization = new SerializationConveyor();
        MessageConveyor<String> queueing = new QueueingConveyor();
        MessageConveyor informing = new InformingConveyor(FlowEvent.DISPATCH);

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(queueing);
        queueing.setConveyor(informing);
    }

    public void setRemoteConveyor(Envelope<T> envelope){
        MessageConveyor<T> validation = new ValidationConveyor<>();
        MessageConveyor<T> serialization = new SerializationConveyor();

    }
}
