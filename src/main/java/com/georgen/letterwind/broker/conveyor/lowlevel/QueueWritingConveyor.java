package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.ordering.MessageOrderManager;
import com.georgen.letterwind.io.FileIOManager;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;

/**
 * <p> The serialized message is persisted in two queues: </p>
 * <p> - the exchange queue, which is later used for direct deserialization </p>
 * <p> - the buffer queue that is cleaned-up after a successful consumer invocation </p>
 * */
public class QueueWritingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;
        if (!envelope.hasSerializedMessage()) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        String message = envelope.getSerializedMessage();
        LetterwindTopic topic = envelope.getTopic();

        String messageExchangePath = PathBuilder.getExchangePath(topic, envelope);
        String messageBufferPath = PathBuilder.getBufferPath(topic, envelope);

        long order = MessageOrderManager.assign(messageExchangePath);
        String messageFileName = String.format("%s-%s", order, envelope.getId());

        String fullExchangePath = getFullPath(messageExchangePath, messageFileName);
        String fullBufferPath = getFullPath(messageBufferPath, messageFileName);

        persist(fullExchangePath, message);
        persist(fullBufferPath, message);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private String getFullPath(String parentPath, String messageFileName){
        return PathBuilder.concatenate(parentPath, messageFileName);
    }

    private void persist(String messagePath, String message) throws Exception {
        try (FileOperation fileOperation = new FileOperation(messagePath, true)){
            File file = fileOperation.getFile();
            FileIOManager.write(file, message);
        }
    }

}
