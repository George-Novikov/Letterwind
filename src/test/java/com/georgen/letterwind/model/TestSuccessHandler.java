package com.georgen.letterwind.model;

import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.message.TestMessage;

public class TestSuccessHandler extends SuccessHandler<TestMessage> {
    @Override
    public void process(Envelope<TestMessage> envelope) throws Exception {

    }
}
