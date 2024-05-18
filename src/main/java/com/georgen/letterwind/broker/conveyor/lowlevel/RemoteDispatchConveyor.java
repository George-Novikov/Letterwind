package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.transport.TransportEnvelope;
import com.georgen.letterwind.transport.TransportLayer;

public class RemoteDispatchConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {

        TransportEnvelope transportEnvelope = envelope.toTransportEnvelope();
        TransportLayer.getInstance().send(transportEnvelope);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
