package com.georgen.letterwind.messaging.conveyor.remote;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;

public class RemoteSendingConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(T message, LetterwindTopic topic) throws Exception {
        //validate
        //serialize
        //send to the server
    }
}
