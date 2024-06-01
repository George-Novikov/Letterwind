package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.MessageFlowEvent;
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
            if (operation.isExistingFile()){
                operation.delete();
                System.out.println("File is deleted: " + bufferedMessagePath);
            }
        } catch (Exception e){
            System.out.println("Clean-up exception: " + e.getMessage());
        }

        cleanUpFirstBuffered(envelope, bufferPath);
    }

    private void cleanUpFirstBuffered(Envelope<T> envelope, String bufferPath) throws Exception {
        String exchangePath = PathBuilder.getExchangePath(envelope);

        try (FileOperation operation = new FileOperation(bufferPath, false)){
            File firstBuffered = operation.getFirstFromDirectory();
            if (firstBuffered == null) return;

            boolean isPresentInExchange = isPresentInDirectory(exchangePath, firstBuffered.getName());

            if (isPresentInExchange) {
                MessageFlow.push(envelope, MessageFlowEvent.REPROCESSING);
            } else {
                operation.delete(firstBuffered);
            }
        } catch (Exception e){
            System.out.println("Clean-up exception: " + e.getMessage());
        }
    }

    private boolean isPresentInDirectory(String directoryPath, String fileName) throws Exception {
        String filePath = PathBuilder.concatenate(directoryPath, fileName);

        try (FileOperation operation = new FileOperation(filePath, false)){
            return operation.isExistingFile();
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
                MessageFlow.push(envelope, MessageFlowEvent.REPROCESSING);
            }
        }
    }
}
