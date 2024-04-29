package com.georgen.letterwind.messaging.conveyor;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.api.annotations.LetterwindMessage;

import java.util.Set;

public abstract class MessageConveyor<T> {
    private MessageConveyor conveyor;

    public MessageConveyor() {}
    public MessageConveyor(MessageConveyor conveyor) {
        this.conveyor = conveyor;
    }

    public MessageConveyor getConveyor() {
        return conveyor;
    }

    public void setConveyor(MessageConveyor conveyor){
        this.conveyor = conveyor;
    }

    public abstract void process(T message, LetterwindTopic topic) throws Exception;
}
