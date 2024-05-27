package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;

public class CleanUpConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.hasBufferedResiduals()) return;

        String bufferPath = PathBuilder.getBufferPath(envelope);
        String bufferedMessagePath = PathBuilder.concatenate(bufferPath, envelope.getBufferedFileName());

        try (FileOperation operation = new FileOperation(bufferedMessagePath, false)){
            File bufferedMessage = operation.getFile();
            if (!bufferedMessage.exists()) return;
            operation.delete();
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }
}
