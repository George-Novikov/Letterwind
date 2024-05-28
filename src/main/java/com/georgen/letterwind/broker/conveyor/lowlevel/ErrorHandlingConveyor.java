package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.EventHandler;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.extractors.EventHandlerExtractor;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Set;

public class ErrorHandlingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        Set<Class<ErrorHandler>> topicErrorHandlerClasses = envelope.getTopic().getErrorHandlers();

        System.out.println("ErrorHandlingConveyor: " + envelope.getException());
    }

    public ErrorHandler<T> getMessageErrorHandler(Envelope<T> envelope) throws LetterwindException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class messageErrorHandlerClass = EventHandlerExtractor.extractErrorHandler(envelope.getMessage());
        return (ErrorHandler<T>) messageErrorHandlerClass.getDeclaredConstructor().newInstance();
    }

    private Comparator<EventHandler> getComparator(){
        return Comparator.comparingInt(EventHandler::getOrder);
    }
}
