package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.ErrorStorage;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;

public class ErrorHandlingConveyor<T> extends MessageConveyor<T> {
    private static final ErrorStorage ERROR_STORAGE = new ErrorStorage();
    @Override
    public void process(Envelope<T> envelope) throws Exception {

    }
}
