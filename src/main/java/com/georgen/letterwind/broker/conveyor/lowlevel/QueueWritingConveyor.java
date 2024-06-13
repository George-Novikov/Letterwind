package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.ordering.MessageOrderManager;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.io.FileIOManager;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> The serialized message is persisted in two queues: </p>
 * <p> - the exchange queue, which is later used for direct deserialization </p>
 * <p> - the buffer queue that is cleaned-up after a successful consumer invocation </p>
 * */
public class QueueWritingConveyor<T> extends MessageConveyor<T> {
    private AtomicInteger writeAttemptsCounter;
    private volatile int writeAttemptsLimit;

    public QueueWritingConveyor(){
        super();
        this.writeAttemptsCounter = new AtomicInteger(0);
        this.writeAttemptsLimit = Configuration.getInstance().getIORetriesOnFault();
    }

    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;
        if (!envelope.hasSerializedMessage()) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        String message = envelope.getSerializedMessage();

        String messageExchangePath = PathBuilder.getExchangePath(envelope);
        String messageBufferPath = PathBuilder.getBufferPath(envelope);

        long order = MessageOrderManager.assignOrder(messageExchangePath);
        String messageFileName = String.format("%s-%s", order, envelope.getId());

        String fullExchangePath = getFullPath(messageExchangePath, messageFileName);
        String fullBufferPath = getFullPath(messageBufferPath, messageFileName);

        write(fullExchangePath, message);
        write(fullBufferPath, message);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private String getFullPath(String parentPath, String messageFileName){
        return PathBuilder.concatenate(parentPath, messageFileName);
    }

    private void write(String messagePath, String message) throws LetterwindException {
        try (FileOperation fileOperation = new FileOperation(messagePath, true)){
            File file = fileOperation.getFile();
            FileIOManager.write(file, message);
        } catch (Exception e){
            if (isTooManyAttempts()) throwWriteFailure(messagePath, e);
            write(messagePath, message);
        }
    }

    private boolean isTooManyAttempts(){
        return writeAttemptsCounter.incrementAndGet() >= writeAttemptsLimit;
    }

    private void throwWriteFailure(String messagePath, Exception e) throws LetterwindException {
        String errorMessage = String.format(
                "Failed to write the message to the path %s. \nOriginal cause: %s",
                messagePath, e.getMessage()
        );
        throw new LetterwindException(errorMessage);
    }

}
