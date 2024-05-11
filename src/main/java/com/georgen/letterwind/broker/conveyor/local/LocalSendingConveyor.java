package com.georgen.letterwind.broker.conveyor.local;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.lowlevel.*;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.ConveyorOperation;

/**
 * Chain of responsibility:
 * Validate -> Serialize -> Queue -> Inform MessageFlow
 */
public class LocalSendingConveyor<T> extends MessageConveyor<T> {

    private MessageConveyor<T> validation = new ValidationConveyor<>();
    private MessageConveyor<T> serialization = new SerializationConveyor();
    private MessageConveyor<String> queueing = new QueueingConveyor();
    private MessageConveyor informing = new InformingConveyor(ConveyorOperation.LOCAL_SEND);


    @Override
    public void process(Envelope<T> envelope) throws Exception {

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(queueing);
        queueing.setConveyor(informing);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
