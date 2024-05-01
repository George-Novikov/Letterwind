package com.georgen.letterwind.messaging.conveyor.local;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;
import com.georgen.letterwind.messaging.conveyor.utility.ConsumerInvokingConveyor;
import com.georgen.letterwind.messaging.conveyor.utility.RetrievingConveyor;

public class LocalReceivingConveyor<T> extends MessageConveyor<T> {

    private MessageConveyor retrieval = new RetrievingConveyor();
    private MessageConveyor consuming = new ConsumerInvokingConveyor();

    @Override
    public void process(T message, LetterwindTopic topic) throws Exception {
        /** Retrieve -> Give a thread to each of the consumer methods -> Inform MessageFlow */

        this.setConveyor(retrieval);
        retrieval.setConveyor(consuming);

        this.getConveyor().process(message, topic);
    }
}
