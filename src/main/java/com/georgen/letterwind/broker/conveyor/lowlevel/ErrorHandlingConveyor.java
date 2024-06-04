package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.broker.storages.MessageHandlerStorage;

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
            Class messageType = LetterwindControls.get().messageType(envelope.getMessageTypeName());
            Class<? extends ErrorHandler> errorHandlerClass = MessageHandlerStorage.getInstance().getErrorHandler(messageType);
            if (!ErrorHandler.isValid(errorHandlerClass)) return null;
            return (ErrorHandler<T>) errorHandlerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
