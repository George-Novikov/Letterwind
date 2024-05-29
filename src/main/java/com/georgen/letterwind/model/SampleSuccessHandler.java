package com.georgen.letterwind.model;

import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.broker.Envelope;

public class SampleSuccessHandler extends SuccessHandler<SampleMessage> {
    @Override
    public void process(Envelope<SampleMessage> envelope) throws Exception {
        System.out.println("Success! " + envelope);
    }
}
