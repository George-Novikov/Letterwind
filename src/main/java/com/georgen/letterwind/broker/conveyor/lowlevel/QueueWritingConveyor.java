package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.ordering.MessageOrderManager;
import com.georgen.letterwind.io.FileIOManager;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.model.messages.BrokerMessage;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;

public class QueueWritingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {


        String message = envelope.getSerializedMessage();
        LetterwindTopic topic = envelope.getTopic();

        if (message == null) throw new LetterwindException(BrokerMessage.NULL_MESSAGE);

        String messageID = envelope.getId();
        String messageTypePath = PathBuilder.concatenate(
                Configuration.getInstance().getExchangePath(),
                topic.getName(),
                envelope.getMessageTypeName()
        );

        long order = MessageOrderManager.assign(messageTypePath);
        String messageFileName = String.format("%s-%s", order, messageID);
        String messagePath = PathBuilder.concatenate(messageTypePath, messageFileName);

        try (FileOperation fileOperation = new FileOperation(messagePath, true)){
            File file = fileOperation.getFile();
            FileIOManager.write(file, message);
        }

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private void throwException(String messageID, String messageTypePath) throws LetterwindException {
        throw new LetterwindException(
                String.format(
                        "Failed to write the message with id %s to the queue with the path: %s",
                        messageID, messageTypePath
                )
        );
    }
}
