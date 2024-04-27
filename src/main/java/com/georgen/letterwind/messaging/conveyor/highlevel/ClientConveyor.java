package com.georgen.letterwind.messaging.conveyor.highlevel;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.messaging.conveyor.MessageConveyor;

import java.util.Set;

public class ClientConveyor<T> extends MessageConveyor<T> {

    @Override
    public void process(T message, LetterwindTopic topic) throws Exception {
        //validate
        //serialize
        //send to the server
    }
}
