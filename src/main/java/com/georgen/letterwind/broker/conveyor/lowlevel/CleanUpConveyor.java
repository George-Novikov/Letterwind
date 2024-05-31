package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.FlowEvent;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;

public class CleanUpConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null || !envelope.hasBufferedResiduals()) return;

        cleanUpBuffer(envelope);
        //cleanUpExchange(envelope);

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void cleanUpBuffer(Envelope<T> envelope) throws Exception {

        String bufferPath = PathBuilder.getBufferPath(envelope);
        String bufferedMessagePath = PathBuilder.concatenate(bufferPath, envelope.getBufferedFileName());

        try (FileOperation operation = new FileOperation(bufferedMessagePath, false)){
            System.out.println("Deleting file: " + bufferedMessagePath);
            File bufferedMessage = operation.getFile();
            if (bufferedMessage.exists()){
                operation.delete();
                System.out.println("File is deleted: " + bufferedMessagePath);
            }
        }

        cleanUpFirstBuffered(envelope, bufferPath);
    }

    private void cleanUpFirstBuffered(Envelope<T> envelope, String bufferPath) throws Exception {
        String exchangePath = PathBuilder.getExchangePath(envelope);

        try (FileOperation operation = new FileOperation(bufferPath, false)){
            File firstBuffered = operation.getFirstFromDirectory();
            if (firstBuffered == null) return;

            boolean isPresentInExchange = isPresentInExchange(exchangePath, firstBuffered.getName());

            if (isPresentInExchange) {
                MessageFlow.push(envelope, FlowEvent.REPROCESSING);
            } else {
                operation.delete(firstBuffered);
            }
        }
    }

    private boolean isPresentInExchange(String exchangePath, String fileName) throws Exception {
        String exchangeMessagePath = PathBuilder.concatenate(exchangePath, fileName);

        try (FileOperation operation = new FileOperation(exchangeMessagePath, false)){
            File exchangeFile = operation.getFile();
            if (exchangeFile == null) return false;
            return exchangeFile.exists();
        }
    }

    private void cleanUpExchange(Envelope<T> envelope) throws Exception {
        String exchangePath = PathBuilder.getExchangePath(envelope);
        try (FileOperation operation = new FileOperation(exchangePath, false)){
            if (operation.hasDirectoryContents()){
                /**
                 * This ensures that there are no residual messages left in the queue due to intense concurrency or read failures.
                 * If there are no messages in the queue, the QueueRetrievingConveyor.retrieveMessage() method stops
                 * the process, preventing an eternal loop.
                 * */
                MessageFlow.push(envelope, FlowEvent.REPROCESSING);
            }
        }
    }
}
