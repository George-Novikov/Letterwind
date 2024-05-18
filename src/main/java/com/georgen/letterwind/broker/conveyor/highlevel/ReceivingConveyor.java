package com.georgen.letterwind.broker.conveyor.highlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;

public class ReceivingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
