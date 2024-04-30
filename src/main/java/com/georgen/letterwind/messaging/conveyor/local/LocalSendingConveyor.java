package com.georgen.letterwind.messaging.conveyor.local;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.conveyor.utility.*;

public class LocalSendingConveyor<@LetterwindMessage T> extends MessageConveyor<@LetterwindMessage T> {

    private MessageConveyor<@LetterwindMessage T> validation = new ValidationConveyor<>();
    private MessageConveyor<@LetterwindMessage T> serialization = new SerializationConveyor();
    private MessageConveyor<String> queueing = new QueueingConveyor();


    @Override
    public void process(@LetterwindMessage T message, LetterwindTopic topic) throws Exception {

        /** Validate -> Serialize -> Queue */

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(queueing);

        if (hasConveyor()){
            this.getConveyor().process(message, topic);
        }
    }
}
