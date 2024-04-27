package com.georgen.letterwind.messaging.conveyor.highlevel;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;

import java.util.Set;

public class ServerConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(T message, LetterwindTopic topic) throws Exception {
        //receive from the client
        //queue
        //retrieve
        //give a thread to the final consumer
    }
}
