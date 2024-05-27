package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;

public class SuccessHandlingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        System.out.println("SuccessHandlingConveyor: " + envelope);
    }
}
