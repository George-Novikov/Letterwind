package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.handlers.EmptyErrorHandler;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.extractors.EventHandlerExtractor;

import java.lang.reflect.InvocationTargetException;

public class ErrorHandlingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        ErrorHandler<T> messageErrorHandler = getMessageErrorHandler(envelope);
        if (messageErrorHandler != null){
            messageErrorHandler.process(envelope);
            return;
        }

        if (envelope.hasTopic() && envelope.getTopic().hasErrorHandler()){
            ErrorHandler topicErrorHandler = envelope.getTopic().getErrorHandler();
            topicErrorHandler.process(envelope);
            return;
        }

        ErrorHandler globalErrorHandler = LetterwindControls.getInstance().getErrorHandler();
        if (globalErrorHandler != null){
            globalErrorHandler.process(envelope);
            return;
        }
    }

    public ErrorHandler<T> getMessageErrorHandler(Envelope<T> envelope){
        try {
            Class messageErrorHandlerClass = EventHandlerExtractor.extractErrorHandler(envelope.getMessage());
            if (messageErrorHandlerClass == null || EmptyErrorHandler.class.equals(messageErrorHandlerClass)) return null;
            return (ErrorHandler<T>) messageErrorHandlerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
