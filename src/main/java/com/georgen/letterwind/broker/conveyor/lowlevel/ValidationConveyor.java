package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.validators.MessageValidator;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.broker.storages.MessageHandlerStorage;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;

public class ValidationConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (!envelope.hasMessage()) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        T message = envelope.getMessage();
        boolean isString = message instanceof String;

        if (!isString){
            MessageValidator<T> validator = extractValidator(message);
            if (validator == null) throw new LetterwindException("The validator class specified within the message config is faulty.");
            if (!validator.isValid(message)) throw new LetterwindException(BrokerMessage.INVALID_MESSAGE);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private MessageValidator<T> extractValidator(T message){
        try {
            Class<? extends MessageValidator> validatorClass = MessageHandlerStorage.getInstance().getValidator(message.getClass());
            return (MessageValidator<T>) validatorClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
