package com.georgen.letterwind.messaging.conveyor.local;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.conveyor.utility.ConsumerInvokingConveyor;
import com.georgen.letterwind.messaging.conveyor.utility.RetrievingConveyor;

public class LocalReceivingConveyor<T> extends MessageConveyor<T> {

    private MessageConveyor<@LetterwindMessage T> retrieval = new RetrievingConveyor();
    private MessageConveyor<@LetterwindMessage T> consuming = new ConsumerInvokingConveyor();

    @Override
    public void process(T message, LetterwindTopic topic) throws Exception {
        /** Retrieve -> Give a thread to each of the consumer methods */

        this.setConveyor(retrieval);
        retrieval.setConveyor(consuming);

        this.getConveyor().process(message, topic);
    }
}
