package com.georgen.letterwind.messaging.conveyor.local;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.conveyor.utility.*;
import com.georgen.letterwind.model.constants.Operation;

/**
 * Chain of responsibility:
 * Validate -> Serialize -> Queue -> Inform MessageFlow
 */
public class LocalSendingConveyor<T> extends MessageConveyor<T> {

    private MessageConveyor<T> validation = new ValidationConveyor<>();
    private MessageConveyor<T> serialization = new SerializationConveyor();
    private MessageConveyor<String> queueing = new QueueingConveyor();
    private MessageConveyor informing = new InformingConveyor(Operation.SEND);


    @Override
    public void process(T message, LetterwindTopic topic) throws Exception {

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(queueing);
        queueing.setConveyor(informing);

        if (hasConveyor()){
            this.getConveyor().process(message, topic);
        }
    }
}
