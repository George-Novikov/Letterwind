package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;

public class CleanUpConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.hasBufferedResiduals()) return;

        System.out.println("CleanUpConveyor received message: " + envelope.getBufferedFileName());




    }
}
