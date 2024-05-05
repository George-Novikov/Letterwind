package com.georgen.letterwind.broker.conveyor.remote;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;

public class RemoteReceivingConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(Envelope<T> envelope) throws Exception {
        //receive from the client
        //queue
        //retrieve
        //give a thread to the final consumer
    }
}
