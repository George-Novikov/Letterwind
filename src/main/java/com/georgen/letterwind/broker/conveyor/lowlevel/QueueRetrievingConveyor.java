package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;
import java.nio.file.Path;

public class QueueRetrievingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        restoreEnvelopeTopic(envelope);
        retrieveMessage(envelope);
        if (!envelope.hasSerializedMessage()) return;

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void restoreEnvelopeTopic(Envelope<T> envelope) throws LetterwindException {
        String topicName = envelope.getTopicName();
        LetterwindTopic topic = LetterwindControls.getInstance().getTopic(topicName);
        if (topic == null) throw new LetterwindException(String.format("There is no registered topic with the name %s.", topicName));
        envelope.setTopic(topic);
    }

    /**
     * <p> The following solution might seem counterintuitive, but here's the gist: </p>
     * <p> - The received message was sent asynchronously in a multithreaded environment. </p>
     * <p> - Before entering the reception conveyor it was assigned an order number and stored in a queue file. </p>
     * <p> - So if an asynchronously received message was processed immediately, it could negate the sequencing process. </p>
     * <p> - Thus, a received message serves as an event to trigger the retrieving process of the next message in its order. </p>
     * */
    private void retrieveMessage(Envelope<T> envelope) throws Exception {
        String messageExchangePath = PathBuilder.getExchangePath(envelope);

        try (FileOperation operation = new FileOperation(messageExchangePath)){
            File firstMessageFile = operation.getFirstFromDirectory();
            if (firstMessageFile == null) return;

            String serializedMessage = operation.read(firstMessageFile);
            String bufferedFileName = firstMessageFile.getName();
            operation.delete(firstMessageFile);

            envelope.setSerializedMessage(serializedMessage);
            envelope.setBufferedFileName(bufferedFileName);
        }
    }
}
