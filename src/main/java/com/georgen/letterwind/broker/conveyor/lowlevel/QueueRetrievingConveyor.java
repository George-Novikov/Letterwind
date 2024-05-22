package com.georgen.letterwind.broker.conveyor.lowlevel;

import com.georgen.letterwind.api.LetterwindControls;
import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.broker.conveyor.MessageConveyor;
import com.georgen.letterwind.config.Configuration;
import com.georgen.letterwind.io.FileIOManager;
import com.georgen.letterwind.io.FileOperation;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.exceptions.LetterwindException;
import com.georgen.letterwind.util.PathBuilder;

import java.io.File;

public class QueueRetrievingConveyor<T> extends MessageConveyor<T> {
    @Override
    public void process(Envelope<T> envelope) throws Exception {
        if (envelope == null) return;

        System.out.println("Message received: " + envelope.getSerializedMessage());

        restoreEnvelopeTopic(envelope);
        String messagePath = getMessagePath(envelope);
        String firstMessage = getFirstMessage(messagePath);
        if (firstMessage == null) return;

        envelope.setSerializedMessage(firstMessage);
        System.out.println("Message retrieved from the : " + envelope.getSerializedMessage());

        if (hasConveyor()){
            this.getConveyor().process(envelope);
        }
    }

    private Class<T> getMessageType(String messageTypeName, LetterwindControls controls) throws LetterwindException {
        Class<T> messageType = controls.getMessageTypeBySimpleName(messageTypeName);
        if (messageType == null) throw new LetterwindException(String.format("There is no registered consumer with the %s message type.", messageTypeName));
        return messageType;
    }

    private void restoreEnvelopeTopic(Envelope<T> envelope) throws LetterwindException {
        String topicName = envelope.getTopicName();
        LetterwindTopic topic = LetterwindControls.getInstance().getTopic(topicName);
        if (topic == null) throw new LetterwindException(String.format("There is no registered topic with the name %s.", topicName));
        envelope.setTopic(topic);
    }

    private String getMessagePath(Envelope<T> envelope){
        String exchangePath = Configuration.getInstance().getExchangePath();
        String topicName = envelope.getTopicName();
        String messageTypeName = envelope.getMessageTypeName();
        return PathBuilder.concatenate(exchangePath, topicName, messageTypeName);
    }

    private String getFirstMessage(String messagePath) throws Exception {
        try (FileOperation operation = new FileOperation(messagePath)){
            File firstMessageFile = operation.getFirstFromDirectory();
            System.out.println(firstMessageFile.getName());
            return firstMessageFile == null ? null : FileIOManager.read(firstMessageFile);
        }
    }
}
