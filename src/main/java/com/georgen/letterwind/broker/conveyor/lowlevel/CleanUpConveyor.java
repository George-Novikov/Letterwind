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

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void cleanUpBuffer(Envelope<T> envelope) {

        String bufferPath = PathBuilder.getBufferPath(envelope);
        String bufferedMessagePath = PathBuilder.concatenate(bufferPath, envelope.getBufferedFileName());

        try (FileOperation operation = new FileOperation(bufferedMessagePath, false)){
            System.out.println("Deleting file: " + bufferedMessagePath);
            if (operation.isExistingFile()){
                operation.delete();
                System.out.println("File is deleted: " + bufferedMessagePath);
            }
        } catch (Exception e){
            MessageFlow.push(envelope, MessageFlowEvent.CLEANING);
        }

        cleanUpFirstBuffered(envelope, bufferPath);
    }

    private void cleanUpFirstBuffered(Envelope<T> envelope, String bufferPath) {
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
            MessageFlow.push(envelope, MessageFlowEvent.CLEANING);
        }
    }

    private boolean isPresentInDirectory(String directoryPath, String fileName) throws Exception {
        String filePath = PathBuilder.concatenate(directoryPath, fileName);

        try (FileOperation operation = new FileOperation(filePath, false)){
            return operation.isExistingFile();
        }
    }
}
