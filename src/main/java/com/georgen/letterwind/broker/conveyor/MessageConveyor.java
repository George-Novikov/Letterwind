package com.georgen.letterwind.broker.conveyor;

import com.georgen.letterwind.model.broker.Envelope;

/** Chain of responsibility */
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

    public boolean hasConveyor(){
        return this.conveyor != null;
    }

    public abstract void process(Envelope<T> envelope) throws Exception;
}
