package com.georgen.letterwind.model;

import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.model.broker.Envelope;

public class SampleErrorHandler extends ErrorHandler<SampleMessage> {
    @Override
    public void process(Envelope<SampleMessage> envelope) throws Exception {
        Exception exception = envelope.getException();

        System.out.println("Error: " + exception);
        for (StackTraceElement traceElement : exception.getStackTrace()){
            System.out.println(traceElement.getClassName() + " " + traceElement.getMethodName());
        }
    }
}
