package com.georgen.letterwind.model;

import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.model.broker.Envelope;

public class SampleErrorHandler extends ErrorHandler<SampleMessage> {
    @Override
    public void process(Envelope<SampleMessage> envelope) throws Exception {
        System.out.println("Error: " + envelope.getException());
    }
}
