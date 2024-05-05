package com.georgen.letterwind.broker.conveyor.local;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.broker.conveyor.utility.ConsumerInvokingConveyor;
import com.georgen.letterwind.broker.conveyor.utility.RetrievingConveyor;
import com.georgen.letterwind.model.broker.Envelope;

public class LocalReceivingConveyor<T> extends MessageConveyor<T> {

    private MessageConveyor retrieval = new RetrievingConveyor();
    private MessageConveyor consuming = new ConsumerInvokingConveyor();

    @Override
    public void process(Envelope<T> envelope) throws Exception {
        /** Retrieve -> Give a thread to each of the consumer methods -> Inform MessageFlow */

        this.setConveyor(retrieval);
        retrieval.setConveyor(consuming);

        this.getConveyor().process(envelope);
    }
}
