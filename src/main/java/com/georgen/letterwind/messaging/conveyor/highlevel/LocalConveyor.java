package com.georgen.letterwind.messaging.conveyor.highlevel;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.conveyor.lowlevel.*;

public class LocalConveyor<@LetterwindMessage T> extends MessageConveyor<@LetterwindMessage T> {

    private MessageConveyor<@LetterwindMessage T> validation = new ValidationConveyor<>();
    private MessageConveyor<@LetterwindMessage T> serialization = new SerializationConveyor();
    private MessageConveyor<@LetterwindMessage T> queueing = new QueueingConveyor();
    private MessageConveyor<@LetterwindMessage T> retrieval = new RetrievingConveyor();
    private MessageConveyor<@LetterwindMessage T> consuming = new ConsumerInvokingConveyor();

    @Override
    public void process(@LetterwindMessage T message, LetterwindTopic topic) throws Exception {

        /** Validate -> Serialize -> Queue -> Retrieve -> Give a thread to each of the consumer methods */

        this.setConveyor(validation);
        validation.setConveyor(serialization);
        serialization.setConveyor(queueing);
        queueing.setConveyor(retrieval);
        retrieval.setConveyor(consuming);

        this.getConveyor().process(message, topic);
    }
}
