package com.georgen.letterwind.broker.conveyor.highlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.*;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.MessageFlowEvent;

public class DispatchConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.isValid()) return;

        if (envelope.isRemote() || LetterwindControls.getInstance().hasRemoteListener()){
            setRemoteDispatchConveyor();
        } else {
            setLocalDispatchConveyor();
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void setLocalDispatchConveyor(){
        MessageConveyor<T> validation = new ValidationConveyor<>();
        MessageConveyor<T> serialization = new SerializationConveyor();
        MessageConveyor<T> queueing = new QueueWritingConveyor();
        MessageConveyor<T> informing = new InformingConveyor(MessageFlowEvent.RECEPTION);

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(queueing);
        queueing.setConveyor(informing);
    }

    private void setRemoteDispatchConveyor(){
        MessageConveyor<T> validation = new ValidationConveyor<>();
        MessageConveyor<T> serialization = new SerializationConveyor();
        MessageConveyor<T> dispatch = new RemoteDispatchConveyor<>();

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(dispatch);
    }
}
