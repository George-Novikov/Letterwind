package com.georgen.letterwind.messaging.conveyor.utility;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.validators.MessageValidator;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.tools.extractors.MessageValidatorExtractor;

public class ValidationConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(T message, LetterwindTopic topic) throws Exception {
        if (message == null) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);
        boolean isString = message instanceof String;

        if (!isString){
            MessageValidator<T> validator = extractValidator(message);
            if (validator == null) throw new LetterwindException("The validator class specified within the message config is faulty.");
            if (!validator.isValid(message)) throw new LetterwindException(BrokerMessage.INVALID_MESSAGE);
        }

        if (hasConveyor()){
            this.getConveyor().process(message, topic);
        }
    }

    private MessageValidator<T> extractValidator(T message){
        try {
            Class validatorClass = MessageValidatorExtractor.extract(message);
            return (MessageValidator<T>) validatorClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
