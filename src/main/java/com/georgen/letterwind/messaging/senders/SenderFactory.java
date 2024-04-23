package com.georgen.letterwind.messaging.senders;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.model.exceptions.LetterwindException;

public class SenderFactory {
    private class SenderInstanceHolder{

    }

    public Sender getSender(Class messageClass) throws LetterwindException {
        if (String.class.equals(messageClass)){
            return new StringMessageSender();
        }

        if (!messageClass.isAnnotationPresent(LetterwindMessage.class)){
            throw new LetterwindException("The message cannot be send because its class is not marked with the LetterwindMessage annotation.");
        }

        return new ComplexMessageSender();
    }
}
