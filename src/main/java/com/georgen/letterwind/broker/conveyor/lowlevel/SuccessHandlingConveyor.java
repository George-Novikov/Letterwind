package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.broker.storages.MessageHandlerStorage;

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
            Class messageType = envelope.getMessage().getClass();
            Class<? extends SuccessHandler> successHandlerClass = MessageHandlerStorage.getInstance().getSuccessHandler(messageType);
            if (!SuccessHandler.isValid(successHandlerClass)) return null;
            return (SuccessHandler<T>) successHandlerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
