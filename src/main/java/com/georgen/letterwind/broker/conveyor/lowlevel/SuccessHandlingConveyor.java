package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.handlers.EmptySuccessHandler;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.extractors.EventHandlerExtractor;

import java.lang.reflect.InvocationTargetException;

public class SuccessHandlingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        SuccessHandler<T> messageSuccessHandler = getMessageSuccessHandler(envelope);
        if (messageSuccessHandler != null){
            messageSuccessHandler.process(envelope);
            return;
        }

        if (envelope.hasTopic() && envelope.getTopic().hasSuccessHandler()){
            SuccessHandler topicSuccessHandler = envelope.getTopic().getSuccessHandler();
            topicSuccessHandler.process(envelope);
            return;
        }

        SuccessHandler globalSuccessHandler = LetterwindControls.getInstance().getSuccessHandler();
        if (globalSuccessHandler != null){
            globalSuccessHandler.process(envelope);
            return;
        }
    }

    public SuccessHandler<T> getMessageSuccessHandler(Envelope<T> envelope) {
        try {
            Class messageSuccessHandlerClass = EventHandlerExtractor.extractSuccessHandler(envelope.getMessage());
            if (messageSuccessHandlerClass == null || EmptySuccessHandler.class.equals(messageSuccessHandlerClass)) return null;
            return (SuccessHandler<T>) messageSuccessHandlerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
