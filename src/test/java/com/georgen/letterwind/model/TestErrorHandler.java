package com.georgen.letterwind.model;

import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.message.TestMessage;

public class TestErrorHandler extends ErrorHandler<TestMessage> {
    @Override
    public void process(Envelope<TestMessage> envelope) throws Exception {

    }
}
