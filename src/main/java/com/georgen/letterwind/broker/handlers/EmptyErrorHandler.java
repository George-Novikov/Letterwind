package com.georgen.letterwind.broker.handlers;

import com.georgen.letterwind.model.broker.Envelope;

public class EmptyErrorHandler extends ErrorHandler {
    @Override
    public void process(Envelope envelope) throws Exception {}
}
